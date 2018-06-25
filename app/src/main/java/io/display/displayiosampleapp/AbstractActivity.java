package io.display.displayiosampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.display.displayiosampleapp.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.Placement;

public abstract class AbstractActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Controller.getInstance().init(this, StaticValues.APP_ID, false);
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
    }

    public void showAd(String placementId) {
        Placement placement = Controller.getInstance().placements.get(placementId);

        if (placement == null) {
            Log.e(TAG,"Can not show ad. Placement is null");
            return;
        }
        if (!placement.hasAd()) {
            Log.e(TAG,"Can not show ad. Placement has no ad");
            return;
        }

        try {
            switch (((JSONObject) placement.getData().getJSONArray("ads").get(0)).getJSONObject("ad").getString("type")) {
                case Controller.AD_INFEED:
                    startActivity(new Intent(this, ListActivity.class)
                            .putExtra(StaticValues.PLACEMENT_ID, placementId));
                    break;
                case Controller.AD_NATIVE:
                    startActivity(new Intent(this, ListActivity.class)
                            .putExtra(StaticValues.PLACEMENT_ID, placementId)
                            .putExtra(StaticValues.IS_NATIVE_ADD, true));
                    break;
                default:
                    JSONObject adParams = new JSONObject();
                    adParams.put("rewardName", "credit")
                            .put("rewardAmount", 15);
                    Controller.getInstance().showAd(this, placementId, adParams);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showNotification(String message, int length, boolean error) {
        Toast toast = Toast.makeText(this, message, length);
        toast.getView().setBackgroundResource(error ? R.drawable.bg_red_toast : R.drawable.bg_green_toast);
        toast.show();
    }
}
