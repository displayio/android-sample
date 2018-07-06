package io.display.displayiosampleapp.base.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.adapter.PlacementsAdapter;
import io.display.displayiosampleapp.base.listeners.OnRecyclerViewItemClickListener;
import io.display.displayiosampleapp.base.util.SharedPreferencesManager;
import io.display.sdk.BuildConfig;
import io.display.sdk.Controller;
import io.display.sdk.EventListener;
import io.display.sdk.Placement;

public class AddPlacementActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private EditText appIdEditText;
    private ProgressBar addPlacementsProgressBar;
    private PlacementsAdapter addPlacementsAdapter;
    private ArrayList<Placement> placements;
    private String appId;

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
                showToastNotification(getString(R.string.notification_error_no_app_for_the_id), Toast.LENGTH_SHORT, true);
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
            appId = appIdEditText.getText().toString();
            refreshController(this, appId);
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
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), Controller.getInstance().getVer()));
    }

    private void refreshController(Context context, String appId) {
        try {
            Class[] paramTypes = new Class[]{Context.class, String.class, boolean.class};
            Method a = adsController.getClass().getDeclaredMethod("a", paramTypes);
            a.setAccessible(true);
            Object[] args = new Object[]{context, appId, false};
            a.invoke(adsController, args);

            Method f = adsController.getClass().getDeclaredMethod("f");
            f.setAccessible(true);
            f.invoke(adsController);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showToastNotification(String message, int length, boolean error) {
        Toast toast = Toast.makeText(this, message, length);
        toast.getView().setBackgroundResource(error ? R.drawable.bg_red_toast : R.drawable.bg_green_toast);
        toast.show();
    }

    @Override
    public void onItemClick(int position, int section) {
        SharedPreferencesManager.getInstance(this.getApplicationContext()).addNewPlacement(placements.get(position), appId);
        showToastNotification(getString(R.string.notification_success_placement_was_loaded), Toast.LENGTH_LONG, false);
        finish();
    }

    @Override
    public void onItemButtonClicked(int position) {

    }
}
