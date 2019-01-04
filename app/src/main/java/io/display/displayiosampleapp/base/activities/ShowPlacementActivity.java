package io.display.displayiosampleapp.base.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import io.display.displayiosampleapp.AbstractActivity;
import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.util.PlacementWrapper;
import io.display.displayiosampleapp.base.util.SharedPreferencesManager;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.listeners.SdkInitListener;

public class ShowPlacementActivity extends AbstractActivity {

    private static final String TAG = "ShowPlacementActivity";

    private TextView showAdTextView;
    private FrameLayout showAdTextViewContainer;
    private ProgressBar progressBar;
    private Toast toast;

    private PlacementWrapper placementWrapper;
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
            int position = getIntent().getIntExtra(StaticValues.POSITION, -1);
            if (position == -1) {
                placementWrapper = new PlacementWrapper("", "", "", "");
                showToastNotification(getString(R.string.notification_error_placement_is_undefined), Toast.LENGTH_SHORT, true);
            }
            placementWrapper = isPredefined ? StaticValues.PREDEFINED_PLACEMENTS.get(position) :
                    SharedPreferencesManager.getInstance(this).getUserDefinedPlacements().get(position);
            setupTextViews();
        }

        setupButtons();
        setupProgressBar();
        setupBackImageView();
        setupSdkVersion();
    }

    private void initController() {
        progressBar.setVisibility(View.VISIBLE);
        Controller.getInstance().setSdkInitListener(new SdkInitListener() {
            @Override
            public void onInit() {
                requestAd();
            }

            @Override
            public void onInitError(String s) {
                progressBar.setVisibility(View.GONE);
                adIsLoading = false;
                adIsLoaded = false;
                showToastNotification(getString(R.string.notification_error_init_error), Toast.LENGTH_SHORT, true);
            }
        });

        try {
            Class[] paramTypes = new Class[]{Context.class, String.class};
            Method a = Controller.getInstance().getClass().getDeclaredMethod("a", paramTypes);
            a.setAccessible(true);
            Object[] args = new Object[]{this, placementWrapper.getAppId()};
            a.invoke(Controller.getInstance(), args);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }

    private void setupTextViews() {
        if (placementWrapper == null) {
            return;
        }

        TextView placementNameTextView = findViewById(R.id.text_view_show_placement_name);
        placementNameTextView.setText(placementWrapper.getName());

        TextView placementTypeTextView = findViewById(R.id.text_view_show_placement_type);
        placementTypeTextView.setText(placementWrapper.getType());
    }

    private void setupButtons() {
        TextView loadAdTextView = findViewById(R.id.text_view_load_ad);
        loadAdTextView.setOnClickListener(view -> {

            if (!adIsLoaded && !adIsLoading) {
                initController();
                adIsLoading = true;
            } else if (adIsLoading && !adIsLoaded) {
                showToastNotification(getString(R.string.notification_add_is_loading), Toast.LENGTH_SHORT, false);
            } else {
                showToastNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
            }
        });

        showAdTextView = findViewById(R.id.text_view_show_ad);
        showAdTextViewContainer = findViewById(R.id.frame_layout_show_add);
        showAdTextView.setOnClickListener(view -> {
            showAd();
            adShown();
        });
        showAdTextView.setEnabled(false);
    }

    @Override
    public void adLoaded(String placementId) {
        Log.i(getClass().getSimpleName(), "Ad is ready for placement " + placementId);
        showToastNotification(getString(R.string.notification_success_add_was_loaded), Toast.LENGTH_SHORT, false);
        showAdTextViewContainer.setBackgroundResource(R.color.colorPrimaryDark);
        showAdTextView.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        adIsLoading = false;
        adIsLoaded = true;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void adFailedToLoad() {
        adIsLoading = false;
        adIsLoaded = false;
        progressBar.setVisibility(View.GONE);
        showToastNotification(getString(R.string.notification_error_ad_failed_to_load), Toast.LENGTH_SHORT, true);
    }

    @Override
    public void noFill() {
        adIsLoading = false;
        adIsLoaded = false;
        progressBar.setVisibility(View.GONE);
        showToastNotification(getString(R.string.notification_error_no_fill), Toast.LENGTH_SHORT, true);
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

    private void adShown() {
        adIsLoading = false;
        adIsLoaded = false;
        showAdTextView.setEnabled(false);
        showAdTextViewContainer.setBackgroundResource(R.color.colorDarkGrey);
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
