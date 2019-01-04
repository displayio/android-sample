package io.display.displayiosampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.AdProvider;
import io.display.sdk.AdRequest;
import io.display.sdk.Controller;
import io.display.sdk.Placement;
import io.display.sdk.ads.Ad;
import io.display.sdk.exceptions.DioSdkException;
import io.display.sdk.listeners.AdEventListener;
import io.display.sdk.listeners.AdLoadListener;
import io.display.sdk.listeners.AdRequestListener;
import io.display.sdk.listeners.SdkInitListener;

public abstract class AbstractActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private String appId;
    private String placementId;
    private AdRequest adRequest;
    private AdProvider adProvider;
    private Ad ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            appId = getIntent().getStringExtra(StaticValues.APP_ID);
            placementId = getIntent().getStringExtra(StaticValues.PLACEMENT_ID);
        }
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
    }

    public void initAdsController() {
        Controller.getInstance().init(this, appId, new SdkInitListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "Controller initialized");
            }

            @Override
            public void onInitError(String msg) {
                Log.e(TAG, msg);
            }
        });
    }

    public void requestAd() {
        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
            return;
        }

        if (placement.hasPendingAdRequests()) {
            try {
                adRequest = placement.getLastAdRequest();
            } catch (DioSdkException e) {
                adRequest = placement.newAdRequest();
            }
        } else {
            adRequest = placement.newAdRequest();
        }

        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(AdProvider returnedAdProvider) {

                adProvider = returnedAdProvider;
                adProvider.setAdLoadListener(new AdLoadListener() {
                    @Override
                    public void onLoaded(Ad adUnit) {
                        ad = adUnit;
                        adLoaded(placementId);
                    }

                    @Override
                    public void onFailedToLoad() {
                        adFailedToLoad();
                    }
                });

                loadAd();
            }

            @Override
            public void onNoAds() {
                noFill();
            }

        });

        adRequest.requestAd();
    }

    public void loadAd() {
        try {
            adProvider.loadAd();
        } catch (DioSdkException e) {
            Log.e(getClass().getSimpleName(), "Loading Exception: " + e.getLocalizedMessage());
        }
    }

    public void showAd() {
        if (ad != null) {
            if (ad.isInterstitial()) {
                ad.setEventListener(new AdEventListener() {
                    @Override
                    public void onShown(Ad Ad) {
                        //
                    }

                    @Override
                    public void onFailedToShow(Ad ad) {
                        //
                    }

                    @Override
                    public void onClicked(Ad ad) {
                        //
                    }

                    @Override
                    public void onClosed(Ad ad) {
                        //
                    }

                    @Override
                    public void onAdCompleted(Ad ad) {
                        //
                    }
                });

                ad.showAd(this);
            } else {
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("placementId", placementId);
                intent.putExtra("requestId", adRequest.getId());
                startActivity(intent);
            }
        }
    }

    public void adLoaded(String placementId) {
        //
    }

    public void adFailedToLoad() {
        //
    }

    public void noFill() {
        //
    }
}
