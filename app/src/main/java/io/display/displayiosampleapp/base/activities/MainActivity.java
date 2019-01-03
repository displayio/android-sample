package io.display.displayiosampleapp.base.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.adapter.PlacementsAdapter;
import io.display.displayiosampleapp.base.listeners.OnRecyclerViewItemClickListener;
import io.display.displayiosampleapp.base.util.PlacementWrapper;
import io.display.displayiosampleapp.base.util.SharedPreferencesManager;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private PlacementsAdapter predefinedPlacementsAdapter;
    private PlacementsAdapter userDefinedPlacementsAdapter;

    private List<PlacementWrapper> predefinedPlacements = new ArrayList<>();
    private List<PlacementWrapper> userDefinedPlacements = new ArrayList<>();

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

        Controller.getInstance().onDestroy();

        getPlacements(false);
        predefinedPlacementsAdapter.setPlacements(predefinedPlacements);
        predefinedPlacementsAdapter.notifyDataSetChanged();

        getPlacements(true);
        userDefinedPlacementsAdapter.setPlacements(userDefinedPlacements);
        userDefinedPlacementsAdapter.notifyDataSetChanged();
    }

    private void setupSdkVersion() {
        TextView sdkVersionTextView = findViewById(R.id.text_view_main_sdk_version);
        sdkVersionTextView.setText(String.format(getString(R.string.placeholder_sdk_version), Controller.getInstance().getVer()));
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
            userDefinedPlacements = SharedPreferencesManager.getInstance(this).getUserDefinedPlacements();
        } else {
            predefinedPlacements = StaticValues.PREDEFINED_PLACEMENTS;
        }
    }

    private void setupAddPlacementTextView() {
        TextView addPlacementTextView = findViewById(R.id.text_view_add_placement);

        addPlacementTextView.setOnClickListener(view -> startActivity(new Intent(this, AddPlacementActivity.class)));
    }

    private void removePlacement(int position) {
        if (userDefinedPlacements.isEmpty()) {
            return;
        }

        if (position < 0) {
            return;
        }

        SharedPreferencesManager.getInstance(this.getApplicationContext()).removePlacement(position);
        userDefinedPlacements.remove(position);
        userDefinedPlacementsAdapter.setPlacements(userDefinedPlacements);
        userDefinedPlacementsAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().onDestroy();
        super.onDestroy();
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
        PlacementWrapper placementWrapper = section == 0 ? predefinedPlacements.get(position) : userDefinedPlacements.get(position);
        String placementId = placementWrapper.getId();
        String appId = placementWrapper.getAppId();
        startActivity(new Intent(this, ShowPlacementActivity.class)
                .putExtra(StaticValues.IS_PREDEFINED, section == 0)
                .putExtra(StaticValues.POSITION, position)
                .putExtra(StaticValues.APP_ID, appId)
                .putExtra(StaticValues.PLACEMENT_ID, placementId));
    }

    @Override
    public void onItemButtonClicked(int position) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.dialog_title_remove_placement));
        alert.setMessage(String.format(getString(R.string.dialog_message_remove_placement), userDefinedPlacements.get(position).getName()));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.text_yes), (dialog, which) -> removePlacement(position));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.text_no), (dialog, which) -> dialog.dismiss());
        alert.show();
    }
}
