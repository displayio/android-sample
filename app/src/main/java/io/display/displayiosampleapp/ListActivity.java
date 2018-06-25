package io.display.displayiosampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.display.displayiosampleapp.util.StaticValues;

public class ListActivity extends AppCompatActivity {

    private String placementId;
    private boolean isNativeAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        placementId = getIntent().getStringExtra(StaticValues.PLACEMENT_ID);
        isNativeAdd = getIntent().getBooleanExtra(StaticValues.IS_NATIVE_ADD, false);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        int[] imagesIds = new int[]{R.drawable.image_0, R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5, R.drawable.image_6, R.drawable.image_7};
        recyclerView.setAdapter(new ListAdapter(imagesIds, new String[]{"4"}, placementId, isNativeAdd));
    }
}
