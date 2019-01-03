package io.display.displayiosampleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import io.display.sdk.BannerPlacement;
import io.display.sdk.Controller;
import io.display.sdk.ads.BannerAdContainer;
import io.display.sdk.exceptions.DioSdkException;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final byte AD_VIEW_TYPE = 0;
    private static final byte SIMPLE_VIEW_TYPE = 1;

    private static final String TAG = "ListAdapter";

    private String placementId;
    private String requestId;
    private Context context;
    private List<Integer> imagesIds;

    public ListAdapter(List<Integer> imagesIds, int[] adPosition, String placementId, String requestId) {
        this.placementId = placementId;
        this.requestId = requestId;
        this.imagesIds = new ArrayList<>();
        this.imagesIds.addAll(imagesIds);
        for (int position : adPosition) {
            this.imagesIds.add(position, null);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext().getApplicationContext();
        switch (viewType) {
            case AD_VIEW_TYPE:
                return new AdViewHolder(BannerAdContainer.getAdView(context));
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (imagesIds.get(position) == null) {
            return AD_VIEW_TYPE;
        } else {
            return SIMPLE_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == AD_VIEW_TYPE && holder instanceof AdViewHolder) {
            try {
                BannerAdContainer bannerContainer = ((BannerPlacement) Controller.getInstance().getPlacement(placementId))
                        .getBannerContainer(context, requestId);
                bannerContainer.bindTo((RelativeLayout) holder.itemView);
            } catch (DioSdkException e) {
                Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
            }
        } else {
            ((ItemViewHolder) holder).itemImageView.setImageResource(imagesIds.get(holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return imagesIds.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.image_view_item);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View itemView) {
            super(itemView);
        }
    }
}

