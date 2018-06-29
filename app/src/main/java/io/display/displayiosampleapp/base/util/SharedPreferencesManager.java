package io.display.displayiosampleapp.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.display.sdk.Placement;

public class SharedPreferencesManager {

    private static final String PREFERENCES_FILE_NAME = "io.display.preferences";

    private static final String USER_DEFINED_PLACEMENTS_KEY = "user_defined_placements";

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

    public String getUserDefinedPlacements() {
        return sharedPreferences.getString(USER_DEFINED_PLACEMENTS_KEY, "{}");
    }

    public void addNewPlacement(Placement placement) {
        String placementsString = getUserDefinedPlacements();
        try {
            JSONObject placementsJson = new JSONObject(placementsString);
            placementsJson.put(placement.getId(), placement.getData());
            placementsString = placementsJson.toString();
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
        sharedPreferences.edit()
                .putString(USER_DEFINED_PLACEMENTS_KEY, placementsString)
                .apply();
    }

    public void removePlacement(Placement placement) {
        String placementsString = getUserDefinedPlacements();
        try {
            JSONObject placementsJson = new JSONObject(placementsString);
            placementsJson.remove(placement.getId());
            placementsString = placementsJson.toString();
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
        sharedPreferences.edit()
                .putString(USER_DEFINED_PLACEMENTS_KEY, placementsString)
                .apply();
    }
}
