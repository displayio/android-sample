package io.display.displayiosampleapp.activities;

import android.content.Intent;
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

import java.lang.reflect.Method;

import io.display.displayiosampleapp.AbstractActivity;
import io.display.displayiosampleapp.ListActivity;
import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.util.StaticValues;
import io.display.sdk.BuildConfig;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;

public class ShowPlacementActivity extends AbstractActivity {

    private TextView showAdTextView;
    private FrameLayout showAdTextViewContainer;
    private ProgressBar progressBar;

    private Controller adsController;
    private Placement placement;
    private boolean adIsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_placement);

        setupAdsController();
        setupButtons();
        setupProgressBar();
        setupBackImageView();
        setupSdkVersion();

        if (getIntent() != null) {
            setupPlacement(getIntent().getStringExtra(StaticValues.PLACEMENT_ID));
            setupTextViews();
        }
    }

    private void setupPlacement(String placementId) {
        placement = Controller.getInstance().placements.get(placementId);
    }

    private void setupAdsController() {
        adsController = Controller.getInstance();
        adsController.setEventListener(new EventListener() {

            @Override
            public void onAdReady(String placementId) {
                Log.i(getClass().getSimpleName(), "Ad is ready for placement " + placementId);
                showNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
                showAdTextViewContainer.setBackgroundResource(R.color.colorPrimaryDark);
                showAdTextView.setEnabled(true);
                progressBar.setVisibility(View.GONE);
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
        placementTypeTextView.setText(String.format(getString(R.string.placeholder_placement_name), placement.getName()));
    }

    private void setupButtons() {
        TextView loadAdTextView = findViewById(R.id.text_view_load_ad);
        loadAdTextView.setOnClickListener(view -> {
            try {
                if (placement.hasAd()) {
                    if (!adIsLoaded) {
                        progressBar.setVisibility(View.VISIBLE);
                        Method loadAd = placement.getClass().getDeclaredMethod("loadAd");
                        loadAd.setAccessible(true);
                        loadAd.invoke(placement);
                        adIsLoaded = true;
                    } else {
                        showNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
                    }
                } else if (!placement.isOperative()) {
                    showNotification(getString(R.string.notification_error_placements_is_inactive), Toast.LENGTH_SHORT, true);
                } else {
                    showNotification(getString(R.string.notification_error_no_fill), Toast.LENGTH_SHORT, true);
                }
            } catch (Throwable e) {
                Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
            }
        });

        showAdTextView = findViewById(R.id.text_view_show_ad);
        showAdTextViewContainer = findViewById(R.id.frame_layout_show_add);
        showAdTextView.setOnClickListener(view -> showAd(placement.getId()));
        showAdTextView.setEnabled(false);
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
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), BuildConfig.VERSION_NAME));
    }
}
