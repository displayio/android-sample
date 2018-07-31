package io.display.displayiosampleapp.base.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import io.display.displayiosampleapp.base.util.SharedPreferencesManager;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;
import io.display.sdk.ServiceClient;

public class ShowPlacementActivity extends AbstractActivity {

    private TextView showAdTextView;
    private FrameLayout showAdTextViewContainer;
    private ProgressBar progressBar;
    private Toast toast;

    private Placement placement;
    private String appId;
    private String message;
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
        controller.setNativeAdCaching(placement.getId(), true);

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

            if (!adIsLoaded && !adIsLoading) {
                try {
                    Class[] paramTypes = new Class[]{String.class, String.class, ServiceClient.ServiceResponseListener.class};
                    ServiceClient client = new ServiceClient(Controller.getInstance());
                    Method method = client.getClass().getDeclaredMethod("a", paramTypes);
                    method.setAccessible(true);
                    Object[] args = new Object[]{appId, placement.getId(), new ServiceClient.ServiceResponseListener() {

                        public void onErrorResponse(String msg, JSONObject data) {
                            Log.e(getClass().getSimpleName(), msg);
                            showToastNotification(getString(R.string.notification_error_app_is_inactive), Toast.LENGTH_SHORT, true);
                        }

                        public void onSuccessResponse(JSONObject resp) {
                            try {
                                if (placement != null) {
                                    progressBar.setVisibility(View.VISIBLE);

                                    Method method = placement.getClass().getDeclaredMethod("setup", JSONObject.class);
                                    method.setAccessible(true);
                                    method.invoke(placement, resp);

                                    if (!isPredefined) {
                                        SharedPreferencesManager.getInstance(ShowPlacementActivity.this.getApplicationContext()).addNewPlacement(placement, appId);
                                        setupTextViews();
                                    }

                                    if (!placement.isOperative()) {
                                        progressBar.setVisibility(View.GONE);
                                        showToastNotification(getString(R.string.notification_error_placements_is_inactive), Toast.LENGTH_SHORT, true);
                                        return;
                                    }

                                    if (!placement.hasAd()) {
                                        progressBar.setVisibility(View.GONE);
                                        showToastNotification(getString(R.string.notification_error_no_fill), Toast.LENGTH_SHORT, true);
                                        return;
                                    }

                                    Method loadAd = placement.getClass().getDeclaredMethod("loadAd");
                                    loadAd.setAccessible(true);
                                    loadAd.invoke(placement);
                                    adIsLoading = true;
                                }
                            } catch (Exception e) {
                                Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
                            }
                        }

                        public void onError(String error, JSONObject resp) {
                            Log.e(getClass().getSimpleName(), error, new RuntimeException(resp.toString()));
                        }
                    }};
                    method.invoke(client, args);

                } catch (Throwable e) {
                    Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
                }

            } else if (adIsLoading && !adIsLoaded) {
                showToastNotification(getString(R.string.notification_add_is_loading), Toast.LENGTH_SHORT, false);
            } else {
                showToastNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
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

    private void showToastNotification(@NonNull String message, int length, boolean error) {
        if (toast != null) {
            if (message.equals(this.message) && toast.getView().isShown()) {
                return;
            } else {
                toast.cancel();
            }
        }
        toast = Toast.makeText(this, message, length);
        toast.getView().setBackgroundResource(error ? R.drawable.bg_red_toast : R.drawable.bg_green_toast);
        toast.show();
        this.message = message;
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
    }
}
