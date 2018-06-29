package io.display.displayiosampleapp.base.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.display.displayiosampleapp.AbstractActivity;
import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.adapter.PlacementsAdapter;
import io.display.displayiosampleapp.base.listeners.OnRecyclerViewItemClickListener;
import io.display.displayiosampleapp.base.util.SharedPreferencesManager;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.BuildConfig;
import io.display.sdk.Controller;
import io.display.sdk.DioSdkException;
import io.display.sdk.Placement;

public class MainActivity extends AbstractActivity implements OnRecyclerViewItemClickListener {

    private PlacementsAdapter predefinedPlacementsAdapter;
    private PlacementsAdapter userDefinedPlacementsAdapter;

    private List<Placement> predefinedPlacements = new ArrayList<>();
    private List<Placement> userDefinedPlacements = new ArrayList<>();

    private long backPressedTimeOut = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSdkVersion();
        setupPlacementsList();
        setupAddPlacementTextView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Controller controller = Controller.getInstance();
        try {
            Method method = controller.getClass().getDeclaredMethod("f");
            method.setAccessible(true);
            method.invoke(controller);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        getPlacements(false);
        predefinedPlacementsAdapter.setPlacements(predefinedPlacements);
        predefinedPlacementsAdapter.notifyDataSetChanged();

        getPlacements(true);
        userDefinedPlacementsAdapter.setPlacements(userDefinedPlacements);
        userDefinedPlacementsAdapter.notifyDataSetChanged();
    }

    private void setupSdkVersion() {
        TextView sdkVersionTextView = findViewById(R.id.text_view_main_sdk_version);
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), BuildConfig.VERSION_NAME));
    }

    private void setupPlacementsList() {
        setupPredefinedRecyclerView();
        setupUserDefinedRecyclerView();
    }

    private void setupPredefinedRecyclerView() {
        RecyclerView predefinedPlacementsRecyclerView = findViewById(R.id.recycler_view_main_placements_predefined);

        predefinedPlacementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        predefinedPlacementsAdapter = new PlacementsAdapter(this, false);
        predefinedPlacementsAdapter.setPlacements(predefinedPlacements);
        predefinedPlacementsRecyclerView.setAdapter(predefinedPlacementsAdapter);
        predefinedPlacementsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupUserDefinedRecyclerView() {
        RecyclerView userPlacementsRecyclerView = findViewById(R.id.recycler_view_main_placements_user);

        userPlacementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userDefinedPlacementsAdapter = new PlacementsAdapter(this, true);
        userDefinedPlacementsAdapter.setPlacements(userDefinedPlacements);
        userPlacementsRecyclerView.setAdapter(userDefinedPlacementsAdapter);
        userPlacementsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void getPlacements(boolean removable) {
        if (removable) {
            userDefinedPlacements = parsePlacements(SharedPreferencesManager.getInstance(this.getApplicationContext()).getUserDefinedPlacements());
        } else {
            predefinedPlacements = parsePlacements(StaticValues.PREDEFINED_PLACEMENTS);
        }
    }

    private void setupAddPlacementTextView() {
        TextView addPlacementTextView = findViewById(R.id.text_view_add_placement);

        addPlacementTextView.setOnClickListener(view -> startActivity(new Intent(this, AddPlacementActivity.class)));
    }

    private void removePlacement(int position) {
        SharedPreferencesManager.getInstance(this.getApplicationContext()).removePlacement(userDefinedPlacements.remove(position));
        userDefinedPlacementsAdapter.setPlacements(userDefinedPlacements);
        userDefinedPlacementsAdapter.notifyItemRemoved(position);
    }

    private List<Placement> parsePlacements(String placementsString) {
        List<Placement> placements = new ArrayList<>();
        try {
            Class paramType = JSONObject.class;
            JSONObject placementsJson = new JSONObject(placementsString);
            Iterator keys = placementsJson.keys();

            while (keys.hasNext()) {
                String placementId = (String) keys.next();

                JSONObject plcData = placementsJson.getJSONObject(placementId);
                Placement placement = new Placement(placementId, false);

                Method setup = placement.getClass().getDeclaredMethod("setup", paramType);
                setup.setAccessible(true);
                Object[] args = new Object[]{plcData};
                setup.invoke(placement, args);

                placements.add(placement);
                Controller.getInstance().placements.put(placementId, placement);
            }

        } catch (DioSdkException | JSONException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return placements;
    }

    @Override
    public void onBackPressed() {
        if (backPressedTimeOut + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.on_back_pressed, Toast.LENGTH_SHORT).show();
            backPressedTimeOut = System.currentTimeMillis();
        }
    }

    @Override
    public void onItemClick(int position, int section) {
        startActivity(new Intent(this, ShowPlacementActivity.class)
                .putExtra(StaticValues.PLACEMENT_ID, section == 0 ? predefinedPlacements.get(position).getId() : userDefinedPlacements.get(position).getId()));
    }

    @Override
    public void onItemButtonClicked(int position) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Remove placement");
        alert.setMessage("Are you sure you want to remove '" + String.format(getString(R.string.placeholder_placement_label_my), userDefinedPlacements.get(position).getName()) + "' placement?");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> removePlacement(position));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.dismiss());
        alert.show();
    }
}
