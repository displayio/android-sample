package io.display.displayiosampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Controller;
import io.display.sdk.exceptions.DioSdkException;

public class ListActivity extends AppCompatActivity {

    private String placementId;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        placementId = getIntent().getStringExtra(StaticValues.PLACEMENT_ID);
        requestId = getIntent().getStringExtra(StaticValues.REQUEST_ID);

        setupRecyclerView();
        setupBackButton();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<Integer> imagesIds = Arrays.asList(
                R.drawable.image_0,
                R.drawable.image_1,
                R.drawable.image_2,
                R.drawable.image_3,
                R.drawable.image_4,
                R.drawable.image_5,
                R.drawable.image_6,
                R.drawable.image_7);
        recyclerView.setAdapter(new ListAdapter(imagesIds, new int[]{1}, placementId, requestId));
    }

    private void setupBackButton() {
        ImageView backImageView = findViewById(R.id.image_view_list_back);
        backImageView.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        try {
            Controller.getInstance()
                    .getPlacement(placementId)
                    .getAdRequestById(requestId)
                    .getAdProvider().getAd().close();
        } catch (DioSdkException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
        }
        super.onDestroy();
    }
}
