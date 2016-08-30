package com.matcha.jjbros.matchaapp.user;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.common.DBControl;
import com.matcha.jjbros.matchaapp.common.Values;
import com.matcha.jjbros.matchaapp.entity.GenUser;
import com.matcha.jjbros.matchaapp.entity.RealtimeLocationOwner;
import com.matcha.jjbros.matchaapp.geofencing.GeofenceErrorMessages;
import com.matcha.jjbros.matchaapp.geofencing.GeofenceTransitionsIntentService;
import com.matcha.jjbros.matchaapp.owner.LocationService;

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jylee on 2016-08-24.
 */
public class UserLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    private GenUser user;

    protected static final String TAG = "UserLocationService";
    // Google Play services 진입점을 제공한다.
    protected GoogleApiClient mGoogleApiClient;
    // 실시간 푸드트럭 위치 지오펜스 리스트.
    protected ArrayList<Geofence> mGeofenceList;
    // 지오펜스가 추가 되었는지 판별하는 변수
    private boolean mGeofencesAdded;
    // 지오펜스를 추가하거나 제거하는 요청할 때 사용
    private PendingIntent mGeofencePendingIntent;
    // 지오펜스가 추가되었을 때 어플리케이션 상태를 유지하기 위해 사용
    private SharedPreferences mSharedPreferences;

    private LocationManager locationManager;
    private LocationListener locationListener;

    AsyncTask loadOwnerRealtimeLocation;

    public UserLocationService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Values.MY_LOCATION = new LatLng(location.getLatitude(), location.getLatitude());
                loadOwnerRealtimeLocation = new LoadOwnerRealtimeLocation().execute(1);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Register the listener with the Location Manager to receive location updates
        // 300초마다 갱신
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Values.MY_LOCATION = new LatLng(lat, lng);
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }

        mGeofenceList = new ArrayList<>();

        // PendingIntent가 addGeofences()와 removeGeofences()에서 사용될 때, null로 세팅한다.
        mGeofencePendingIntent = null;
        // SharedPreferences 객체 인스턴스를 가져옴
        mSharedPreferences = getSharedPreferences(Values.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Values.GEOFENCES_ADDED_KEY, false);

        // GoogleApiClient 빌드 시작
        buildGoogleApiClient();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        removeGeofences();
        mGoogleApiClient.disconnect();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        Toast.makeText(UserLocationService.this, "실시간 위치 알림이 중지되었습니다.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    public void populateGeofenceList() {
        mGeofenceList.clear();
        for (Map.Entry<String, RealtimeLocationOwner> entry : Values.FOOD_TRUCK_CURRENT_LOCATION.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().getLat(),
                            entry.getValue().getLng(),
                            Values.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Values.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }
    }

    public class LoadOwnerRealtimeLocation extends AsyncTask<Integer, Integer, HashMap<String, RealtimeLocationOwner>> {
        @Override
        protected HashMap<String, RealtimeLocationOwner> doInBackground(Integer... command) {
            Connection conn = null;
            int result = 0;
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                String url = new DBControl().url;
                Properties props = new Properties();
                props.setProperty("user", "postgres");
                props.setProperty("password", "admin123");

                Log.d("url", url);
                conn = DriverManager.getConnection(url, props);
                if (conn == null) // couldn't connect to server
                {
                    Log.d("connection : ", "null");
                    return null;
                }
            } catch (Exception e) {
                Log.d("PPJY", e.getLocalizedMessage());
                return null;
            }

            Statement stmt = null;
            ResultSet rs = null;
            int res = 0;

            HashMap<String, RealtimeLocationOwner> locationOwnerHashMap = new HashMap<String, RealtimeLocationOwner>();

            String sql = "SELECT \"REALTIME_LOCATION\".\"location\", \"REALTIME_LOCATION\".\"OWNER_ID\", \"OWNER\".\"NAME\" "
                    + "FROM \"REALTIME_LOCATION\" INNER JOIN \"OWNER\" ON \"REALTIME_LOCATION\".\"OWNER_ID\"=\"OWNER\".\"ID\" "
                    + "WHERE \"REALTIME_LOCATION\".\"STATUS\"=TRUE;";
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    RealtimeLocationOwner rlo = new RealtimeLocationOwner();
                    PGpoint pGpoint = (PGpoint) rs.getObject(1);
                    rlo.setLat(pGpoint.x);
                    rlo.setLng(pGpoint.y);
                    rlo.setOwner_id(rs.getInt(2));
                    rlo.setTruck_name(rs.getString(3));

                    locationOwnerHashMap.put(rlo.getTruck_name(), rlo);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return locationOwnerHashMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, RealtimeLocationOwner> locationOwnerHashMap) {
            super.onPostExecute(locationOwnerHashMap);
            if (locationOwnerHashMap != null) {
                removeGeofences();
                Values.FOOD_TRUCK_CURRENT_LOCATION = locationOwnerHashMap;
                populateGeofenceList();
                Log.d("푸드트럭: ","지오펜싱 리스트가 생성 되었습니다.");
                addGeofences();
            } else {
                Log.d("푸드트럭: ","실시간 위치 알림 상태의 푸드트럭이 없습니다.");
            }
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }


    // GoogleApiClient 빌드. LocationServices API를 요청하기 위해 {@code #addApi} method를 사용
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Runs when a GoogleApiClient object가 성공적으로 연결 되었을 때 실행
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // 구글플레이 서비스 연결을 잃어버렸을 때
        Log.i(TAG, "Connection suspended");

        // 서비스가 재연결 됐을 때 자동으로 onConnected()를 다시 호출
    }

    // GeofencingRequest를 빌드하고 반환. 모니터링할 지오펜스 리스트를 명시함.
    // 또한 지오펜스 알림이 초기에 어떻게 작동되는지 명시함.
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag는 지오펜스가 추가되고, 만약 기기가 이미 지오펜스 안에 있을 때
        // 지오펜싱 서비스가 GEOFENCE_TRANSITION_ENTER 알림을 동작해야 함을 알려준다.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // 지오펜싱 서비스에 의해 모니터링 되는 지오펜스를 추가한다.
        builder.addGeofences(mGeofenceList);

        // GeofencingRequest를 반환한다.
        return builder.build();
    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Values.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            Log.d("지오펜스가 ",mGeofencesAdded ? "지오펜스가 추가 되었습니다." :
                            "지오펜스가 제거 되었습니다.");
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }


    // 지오펜스를 추가/제거하기 위한 요청을 보낼 PendingIntent를 얻는다.
    // 현재 지오펜스 목록에 대해 지오펜스 트랜지션(이행)이 발생할 때 마다 Location Services는 해당 PendingIntent 내에 인텐트를 발행한다.
    // @return 지오펜스 트랜지션을 처리하는 IntentService에 대한 PendingIntent
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        intent.putExtra("user",user);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        user = (GenUser) intent.getParcelableExtra("user");
        return null;
    }


    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }
}
