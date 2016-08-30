package com.matcha.jjbros.matchaapp.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
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

import org.postgresql.geometric.PGpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jylee on 2016-08-24.
 */
public class UserCouponService extends Service {
    private GenUser user;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public UserCouponService() {
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
                new UpdateUserLocation().execute(location);
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
        // 60초마다 갱신
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, locationListener);

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new UpdateCouponStatus().execute(true);
        Toast.makeText(UserCouponService.this, "쿠폰 알림 서비스가 시작 되었습니다.", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        new UpdateCouponStatus().execute(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        Toast.makeText(UserCouponService.this, "쿠폰 알림 서비스가 중지 되었습니다.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class UpdateUserLocation extends AsyncTask<Location, Integer, Integer> {
        @Override
        protected Integer doInBackground(Location... locations) {
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

            int res = 0;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM \"USER_LOCATION\" WHERE \"USER_ID\"=?";
            int check = 0;
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, Values.GENUSER.getId());

                rs = pstm.executeQuery();
                if (rs.next()) {
                    rs.close();
                } else {
                    pstm.close();
                    rs.close();
                    sql = "INSERT INTO \"USER_LOCATION\"(\"USER_ID\") VALUES(?);";
                    pstm = conn.prepareStatement(sql);
                    pstm.setInt(1, Values.GENUSER.getId());

                    pstm.executeUpdate();
                }
                pstm.close();

                sql = "update \"USER_LOCATION\" SET \"location\"=point(?,?) where \"USER_ID\"=?";
                pstm = conn.prepareStatement(sql);
                pstm.setDouble(1, locations[0].getLatitude());
                pstm.setDouble(2, locations[0].getLongitude());
                pstm.setInt(3, Values.GENUSER.getId());

                res = pstm.executeUpdate();
                if (res > 0) {
                    result += 1;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                Log.d("SQLException: ", e.getLocalizedMessage());
                return 0;
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.d("Update SQLException", e.getLocalizedMessage());
                    return 0;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result > 0) {
                Log.d("db update", "success");
            } else {
                Log.d("db update", "failed");
            }
        }
    }

    public class UpdateCouponStatus extends AsyncTask<Boolean, Integer, Integer> {
        @Override
        protected Integer doInBackground(Boolean... booleen) {
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

            int res = 0;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            String sql = "update \"USER_LOCATION\" SET \"COUPON_STATUS\"=? where \"USER_ID\"=?";
            int check = 0;
            try {
                pstm = conn.prepareStatement(sql);
                pstm.setBoolean(1, booleen[0]);
                pstm.setInt(2, Values.GENUSER.getId());

                res = pstm.executeUpdate();
                if (res > 0) {
                    result += 1;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                Log.d("SQLException: ", e.getLocalizedMessage());
                return 0;
            } finally {
                try {
                    pstm.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.d("Update SQLException", e.getLocalizedMessage());
                    return 0;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result > 0) {
                Log.d("db update", "success");
            } else {
                Log.d("db update", "failed");
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        user = (GenUser) intent.getParcelableExtra("user");
        return null;
    }

}