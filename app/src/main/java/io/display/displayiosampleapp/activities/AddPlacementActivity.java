package io.display.displayiosampleapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import io.display.displayiosampleapp.AbstractActivity;
import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.adapter.PlacementsAdapter;
import io.display.displayiosampleapp.listeners.OnRecyclerViewItemClickListener;
import io.display.displayiosampleapp.util.SharedPreferencesManager;
import io.display.sdk.BuildConfig;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;

public class AddPlacementActivity extends AbstractActivity implements OnRecyclerViewItemClickListener {

    private EditText appIdEditText;
    private ProgressBar addPlacementsProgressBar;
    private PlacementsAdapter addPlacementsAdapter;
    private ArrayList<Placement> placements;

    private Controller adsController;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_placement);

        setupBackImageView();
        setupGetPlacementsTextView();
        setupAppIdEditText();
        setupSdkVersion();
        setupAddPlacementsRecyclerView();
        setupProgressBar();

        setupAdsController();
    }

    private void setupAdsController() {
        adsController = Controller.getInstance();
        adsController.setEventListener(new EventListener() {
            @Override
            public void onInit() {
                if (adsController.placements.isEmpty()) {
                    return;
                }

                placements = new ArrayList<>();
                for (Map.Entry<String, Placement> entry : adsController.placements.entrySet()) {
                    placements.add(entry.getValue());
                }

                addPlacementsAdapter.setPlacements(placements);
                addPlacementsAdapter.notifyDataSetChanged();
                addPlacementsProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onInitError(String msg) {
                addPlacementsProgressBar.setVisibility(View.GONE);
                try {
                    String error = new JSONObject(msg.substring(msg.indexOf("{"))).getString("errMsg");
                    switch (error) {
                        case "app inactive":
                            showNotification(getString(R.string.notification_error_app_is_inactive), Toast.LENGTH_SHORT, true);
                            break;
                        default:
                            showNotification(getString(R.string.notification_error_no_app_for_the_id), Toast.LENGTH_SHORT, true);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupAddPlacementsRecyclerView() {
        RecyclerView addPlacementsRecyclerView = findViewById(R.id.recycler_view_add_placements);

        addPlacementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addPlacementsAdapter = new PlacementsAdapter(this, false);
        addPlacementsAdapter.setPlacements(new ArrayList<>());
        addPlacementsRecyclerView.setAdapter(addPlacementsAdapter);
        addPlacementsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupProgressBar() {
        addPlacementsProgressBar = findViewById(R.id.progress_bar_add_placements);
    }

    private void setupBackImageView() {
        ImageView backImageView = findViewById(R.id.image_view_add_placement_back);
        backImageView.setOnClickListener(view -> onBackPressed());
    }

    private void setupGetPlacementsTextView() {
        TextView getPlacementsTextView = findViewById(R.id.text_view_get_placement);
        getPlacementsTextView.setOnClickListener(view -> {
            refreshController(this, appIdEditText.getText().toString(), false);
            addPlacementsAdapter.setPlacements(new ArrayList<>());
            addPlacementsAdapter.notifyDataSetChanged();
            addPlacementsProgressBar.setVisibility(View.VISIBLE);
            hideKeyBoard();
        });
    }

    private void setupAppIdEditText() {
        appIdEditText = findViewById(R.id.edit_text_app_id);
    }

    private void setupSdkVersion() {
        TextView sdkVersionTextView = findViewById(R.id.text_view_add_placement_sdk_version);
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), BuildConfig.VERSION_NAME));
    }

    private void refreshController(Context context, String appId, boolean preloadAd) {
        try {
            Class[] paramTypes = new Class[]{Context.class, String.class, boolean.class};
            Method method = adsController.getClass().getDeclaredMethod("a", paramTypes);
            method.setAccessible(true);
            Object[] args = new Object[]{context, appId, preloadAd};
            method.invoke(adsController, args);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position, int section) {
        SharedPreferencesManager.getInstance(this.getApplicationContext()).addNewPlacement(placements.get(position));
        showNotification(getString(R.string.notification_success_placement_was_loaded), Toast.LENGTH_LONG, false);
        finish();
    }

    @Override
    public void onItemButtonClicked(int position) {

    }
}
