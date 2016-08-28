package com.matcha.jjbros.matchaapp.geofencing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.matcha.jjbros.matchaapp.R;
import com.matcha.jjbros.matchaapp.truck.NotifiedFoodTruckMapActivity;
import com.matcha.jjbros.matchaapp.truck.UserNearFoodtruckActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jylee on 2016-08-28.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    protected static final String TAG = "GeofenceTransitionsIS";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public GeofenceTransitionsIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 들어오는 인텐트를 조작한다.
    // @param intent Location Service에 의해 전송됨. 이 인텐트는 addGeofences()가 호출될 때,
    //               (PendingIntent 내의)Location Service에서 제공받는다.
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // the transition type을 받음
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // 작동된 지오펜스들을 받는다. 한 이벤트가 여러 지오펜스들을 작동시킬 수 있다.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return triggeringGeofencesIdsString;
    }

    // 트랜지션이 감지될 때, 알림을 띄운다.
    // 만약 유저가 알림을 클릭하면, 컨트롤이 푸드트럭 지도로 이동하여, 푸드트럭 위치를 보여준다.
    private void sendNotification(String notificationDetails) {
        // NotifiedFoodTruckMapActivity를 시작할 명시적 content Intent를 생성한다.
        Intent notificationIntent = new Intent(getApplicationContext(), NotifiedFoodTruckMapActivity.class);
        notificationIntent.putExtra("truckName", notificationDetails);

        // task stack을 만든다.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // parent로서의 작업 스택에 NotifiedFoodTruckMapActivity를 추가한다.
        stackBuilder.addParentStack(NotifiedFoodTruckMapActivity.class);

        // content Intent를 스텍으로 밀어넣는다.
        stackBuilder.addNextIntent(notificationIntent);

        // 전체 back stack을 가진 PendingIntent
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // 버전이 4 이상의 버전과 호환되는 a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // 알림 설정을 정의
        builder.setSmallIcon(R.drawable.ic_stat_name)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_stat_name))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("푸드트럭 위치를 확인하려면 클릭하세요.")
                .setContentIntent(notificationPendingIntent);

        // 사용자가 알림을 터치할 때, 알림을 제거한다.
        builder.setAutoCancel(true);

        // Notification manager 인스턴스
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 알림을 발행한다.
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Endtered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited";
            default:
                return "Unknown Transition";
        }
    }
}
