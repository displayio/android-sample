package io.display.displayiosampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.display.displayiosampleapp.util.StaticValues;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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
        recyclerView = findViewById(R.id.recycler_view_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ListAdapter(15, new String[]{"6", "12"}, placementId, isNativeAdd));
    }
}
