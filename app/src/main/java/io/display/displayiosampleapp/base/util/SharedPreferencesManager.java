package io.display.displayiosampleapp.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREFERENCES_FILE_NAME = "io.display.preferences";
    private static final String USER_DEFINED_PLACEMENTS_KEY = "user_defined_placements";
    private static final String APP_ID = "appId";
    private static final String PLACEMENT_ID = "id";
    private static final String NAME = "name";
    private static final String TYPE = "type";

    private static SharedPreferencesManager manager;

    private SharedPreferences sharedPreferences;

    public static SharedPreferencesManager getInstance(Context applicationContext) {
        if (manager == null) {
            manager = new SharedPreferencesManager(applicationContext);
        }
        return manager;
    }

    private SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public List<PlacementWrapper> getUserDefinedPlacements() {
        try {
            List<PlacementWrapper> placementWrappers = new ArrayList<>();
            JSONArray placementsJsonArray = new JSONArray(sharedPreferences.getString(USER_DEFINED_PLACEMENTS_KEY, "[]"));

            for (int i = 0; i < placementsJsonArray.length(); i++) {
                JSONObject placementJson = placementsJsonArray.getJSONObject(i);

                String appId = placementJson.getString("appId");
                String placementId = placementJson.getString("id");
                String placementName = placementJson.getString("name");
                String placementType = placementJson.getString("type");

                placementWrappers.add(new PlacementWrapper(appId, placementId, placementName, placementType));
            }
            return placementWrappers;

        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    public void addNewPlacement(PlacementWrapper placementWrapper) {
        try {
            JSONArray placementsJsonArray = new JSONArray(sharedPreferences.getString(USER_DEFINED_PLACEMENTS_KEY, "[]"));
            JSONObject placementJson = new JSONObject();
            placementJson.put(APP_ID, placementWrapper.getAppId())
                    .put(PLACEMENT_ID, placementWrapper.getId())
                    .put(NAME, placementWrapper.getName())
                    .put(TYPE, placementWrapper.getType());
            placementsJsonArray.put(placementJson);

            sharedPreferences.edit()
                    .putString(USER_DEFINED_PLACEMENTS_KEY, placementsJsonArray.toString())
                    .apply();
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    public void removePlacement(int position) {
        try {
            JSONArray placementsJsonArray = new JSONArray(sharedPreferences.getString(USER_DEFINED_PLACEMENTS_KEY, "[]"));
            if (placementsJsonArray.length() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    placementsJsonArray.remove(position);
                } else {
                    final JSONArray copy = new JSONArray();
                    for (int i = 0, count = placementsJsonArray.length(); i < count; i++) {
                        if (i != position) {
                            copy.put(placementsJsonArray.get(i));
                        }
                    }
                    placementsJsonArray = copy;
                }
                sharedPreferences.edit()
                        .putString(USER_DEFINED_PLACEMENTS_KEY, placementsJsonArray.toString())
                        .apply();
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }
}
