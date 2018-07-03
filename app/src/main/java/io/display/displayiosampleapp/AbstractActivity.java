package io.display.displayiosampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;
import io.display.sdk.ads.supers.RewardedVideoAd;

public abstract class AbstractActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final String APP_ID = "12345";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Controller.getInstance().init(this, APP_ID, false);
        Controller.getInstance().setEventListener(new EventListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "Controller initialized");
            }

            @Override
            public void onInitError(String msg) {
                Log.e(TAG, msg);
            }

            @Override
            public void onAdShown(String placementId) {
                Log.i(TAG, "Ad was shown for placement " + placementId);
            }

            @Override
            public void onAdFailedToShow(String placementId) {
                Log.e(TAG, "Ad is failed to show for placement " + placementId);
            }

            @Override
            public void onNoAds(String placementId) {
                Log.e(TAG, "No ads for placement " + placementId);
            }

            @Override
            public void onAdCompleted(String placementId) {
                Log.i(TAG, "Ad is completed for placement " + placementId);
            }

            @Override
            public void onAdClose(String placementId) {
                Log.i(TAG, "Ad is closed for placement " + placementId);
            }

            @Override
            public void onAdClick(String placementId) {
                Log.i(TAG, "Ad is clicked for placement " + placementId);
            }

            @Override
            public void onAdReady(String placementId) {
                Log.i(TAG, "Ad is ready for placement " + placementId);
            }

            @Override
            public void onRewardedVideoCompleted(String placementId, RewardedVideoAd.Reward reward) {
                Log.i(TAG, "Rewarded video is completed for placement " + placementId);
            }

            @Override
            public void inactivate() {
                Log.i(TAG, "Inactivating event listener");
                super.inactivate();
            }

            @Override
            public void activate() {
                Log.i(TAG, "Activating event listener");
                super.activate();
            }

            @Override
            public boolean isActive() {
                return super.isActive();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
    }

    public void showAd(String placementId) {
        Placement placement = Controller.getInstance().placements.get(placementId);

        if (placement == null) {
            Log.e(TAG, "Can not show ad. Placement is null");
            Controller.getInstance().getEventListener().onAdFailedToShow(placementId);
            return;
        }
        if (!placement.hasAd()) {
            Log.e(TAG, "Can not show ad. Placement has no ad");
            Controller.getInstance().getEventListener().onNoAds(placementId);
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
            Log.e(TAG, "Parsing data error", e);
            Controller.getInstance().getEventListener().onAdFailedToShow(placementId);
        }
    }
}
