package com.matcha.jjbros.matchaapp.common;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.matcha.jjbros.matchaapp.entity.RealtimeLocationOwner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jylee on 2016-08-16.
 */
public class Values {
    public static int USER = 1;
    public static int OWNER = 0;
    public static int VIEW = 1;
    public static int HIDE = 0;
    public static int ADD = 1;
    public static int UPDATE = 0;
    public static int NEW_SCHEDULE = 1;
    public static int UPDATED_SCHEDULE = 2;
    public static int DELETED_SCHEDULE = 3;
    public static int NONE_SCHEDULE = 0;

    public static boolean USER_BOOKMARK_TRUCK_ALERT = false;
    public static boolean USER_NEAR_TRUCK_ALERT = false;
    public static boolean USER_EVENT_PUSH_ALERT = false;

    public static LatLng MY_LOCATION = new LatLng(0,0);

    // 실시간 푸드트럭 위치 지오펜스 리스트.
    public static ArrayList<Geofence> mGeofenceList;

    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static HashMap<String, RealtimeLocationOwner> FOOD_TRUCK_CURRENT_LOCATION = new HashMap<String, RealtimeLocationOwner>();

    public static ArrayList<Geofence> GEOFENCE_LIST;

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 5000; // 5 km

}
