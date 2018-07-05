package io.display.displayiosampleapp.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.display.displayiosampleapp.R;
import io.display.displayiosampleapp.base.listeners.OnRecyclerViewItemClickListener;
import io.display.displayiosampleapp.base.util.StaticValues;
import io.display.sdk.Placement;

public class PlacementsAdapter extends RecyclerView.Adapter<PlacementsAdapter.PlacementsViewHolder> {

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private List<Placement> placements = new ArrayList<>();
    private boolean removable;

    public PlacementsAdapter(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener, boolean removable) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        this.removable = removable;
    }

    public void setPlacements(@NonNull List<Placement> placements) {
        this.placements = placements;
    }

    @NonNull
    @Override
    public PlacementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlacementsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placement_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PlacementsViewHolder holder, int position) {
        Placement placementModel = placements.get(holder.getAdapterPosition());
        holder.bindData(placementModel);
    }

    @Override
    public int getItemCount() {
        return placements.size();
    }

    class PlacementsViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemContainer;
        private TextView placementLabelTextView;
        private TextView placementNameTextView;
        private ImageView removeImageView;

        PlacementsViewHolder(View itemView) {
            super(itemView);

            itemContainer = itemView.findViewById(R.id.relative_layout_placement_container);
            placementLabelTextView = itemView.findViewById(R.id.text_view_placement_label);
            placementNameTextView = itemView.findViewById(R.id.text_view_placement_name);
            removeImageView = itemView.findViewById(R.id.image_view_remove);
        }

        void bindData(Placement placement) {
            placementLabelTextView.setText(placement.getName());
            try {
                placementNameTextView.setText(StaticValues.AD_TYPES.get(((JSONObject) placement.getData().getJSONArray("ads").get(0)).getJSONObject("ad").getString("type")));
            } catch (JSONException e) {
                placementNameTextView.setText(itemView.getContext().getString(R.string.placeholder_placement_type));
            }
            removeImageView.setVisibility(removable ? View.VISIBLE : View.GONE);
            itemContainer.setOnClickListener(view -> onRecyclerViewItemClickListener.onItemClick(getAdapterPosition(), removable ? 1 : 0));
            removeImageView.setOnClickListener(view -> onRecyclerViewItemClickListener.onItemButtonClicked(getAdapterPosition()));
            itemView.setBackgroundResource(removable ? R.color.colorDarkGrey : R.color.colorLightGrey);
        }
    }
}
