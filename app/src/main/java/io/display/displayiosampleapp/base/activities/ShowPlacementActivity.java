package io.display.displayiosampleapp.base.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.display.displayiosampleapp.AbstractActivity;
import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;
import io.display.sdk.ServiceClient;

public class ShowPlacementActivity extends AbstractActivity {

    private TextView showAdTextView;
    private FrameLayout showAdTextViewContainer;
    private ProgressBar progressBar;

    private Placement placement;
    private String appId;
    private boolean isPredefined;
    private boolean adIsLoaded;
    private boolean adIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_placement);

        if (getIntent() != null) {
            isPredefined = getIntent().getBooleanExtra(StaticValues.IS_PREDEFINED, false);
            setupPlacement(getIntent().getStringExtra(StaticValues.PLACEMENT_ID), getIntent().getStringExtra(StaticValues.APP_ID));
            setupTextViews();
        }

        setupAdsController();
        setupButtons();
        setupProgressBar();
        setupBackImageView();
        setupSdkVersion();
    }

    private void setupPlacement(String placementId, String appId) {
        placement = Controller.getInstance().placements.get(placementId);
        this.appId = appId;
    }

    private void setupAdsController() {
        Controller controller = Controller.getInstance();
        String placementId = placement.getId();
        controller.setNativeAdCaching(placementId, true);

        try {
            Field field = controller.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(controller, appId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        controller.setEventListener(new EventListener() {

            @Override
            public void onAdReady(String placementId) {
                onAdReadyToShow(placementId);
            }
        });
    }

    private void setupTextViews() {
        if (placement == null) {
            return;
        }

        TextView placementNameTextView = findViewById(R.id.text_view_show_placement_name);
        placementNameTextView.setText(placement.getName());

        TextView placementTypeTextView = findViewById(R.id.text_view_show_placement_type);
        try {
            placementTypeTextView.setText(StaticValues.AD_TYPES.get(((JSONObject) placement.getData().getJSONArray("ads").get(0)).getJSONObject("ad").getString("type")));
        } catch (JSONException e) {
            placementTypeTextView.setText(getString(R.string.notification_error_no_fill));
        }
    }

    private void setupButtons() {
        TextView loadAdTextView = findViewById(R.id.text_view_load_ad);
        loadAdTextView.setOnClickListener(view -> {
            if (placement.hasAd()) {
                if (!adIsLoaded && !adIsLoading) {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        Method loadAd = placement.getClass().getDeclaredMethod("loadAd");
                        loadAd.setAccessible(true);
                        loadAd.invoke(placement);
                        adIsLoading = true;
                    } catch (Throwable e) {
                        Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
                    }
                } else if (adIsLoading && !adIsLoaded) {
                    showToastNotification(getString(R.string.notification_add_is_loading), Toast.LENGTH_SHORT, false);
                } else {
                    showToastNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
                }
            } else if (!placement.isOperative()) {
                showToastNotification(getString(R.string.notification_error_placements_is_inactive), Toast.LENGTH_SHORT, true);
            } else {
                showToastNotification(getString(R.string.notification_error_no_fill), Toast.LENGTH_SHORT, true);
            }
        });

        showAdTextView = findViewById(R.id.text_view_show_ad);
        showAdTextViewContainer = findViewById(R.id.frame_layout_show_add);
        showAdTextView.setOnClickListener(view -> showAd(placement.getId()));
        showAdTextView.setEnabled(false);
    }

    private void onAdReadyToShow(String placementId) {
        Log.i(getClass().getSimpleName(), "Ad is ready for placement " + placementId);
        showToastNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
        showAdTextViewContainer.setBackgroundResource(R.color.colorPrimaryDark);
        showAdTextView.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        adIsLoaded = true;
    }

    private void setupBackImageView() {
        ImageView backImageView = findViewById(R.id.image_view_show_placement_back);
        backImageView.setOnClickListener(view -> onBackPressed());
    }

    private void setupProgressBar() {
        progressBar = findViewById(R.id.progress_bar_show_placement);
    }

    private void setupSdkVersion() {
        TextView sdkVersionTextView = findViewById(R.id.text_view_show_placement_sdk_version);
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), Controller.getInstance().getVer()));
    }

    private void showToastNotification(String message, int length, boolean error) {
        Toast toast = Toast.makeText(this, message, length);
        toast.getView().setBackgroundResource(error ? R.drawable.bg_red_toast : R.drawable.bg_green_toast);
        toast.show();
    }
}
