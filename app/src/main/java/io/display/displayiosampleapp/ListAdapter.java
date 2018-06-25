package io.display.displayiosampleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.display.sdk.Controller;
import io.display.sdk.ads.InfeedAdContainer;
import io.display.sdk.ads.supers.NativeAd;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_VIEW_TYPE = 1;
    private static final int DEFAULT_VIEW_TYPE = 0;

    private String placementId;
    private Context context;
    private List<String> adPosition;
    private int[] imagesIds;
    private SparseArray<InfeedAdContainer> loadedAds;
    private boolean displayed;
    private boolean isNativeAd;

    public ListAdapter(int[] imagesIds, String[] adPosition, String placementId, boolean isNative) {
        this.placementId = placementId;
        this.adPosition = Arrays.asList(adPosition);
        this.imagesIds = imagesIds;
        isNativeAd = isNative;
        displayed = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case ADD_VIEW_TYPE:
                if (isNativeAd) {
                    return new NativeAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_nativead, parent, false));
                } else {
                    return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ad, parent, false));
                }

            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (adPosition.contains(String.valueOf(position))) {
            return ADD_VIEW_TYPE;
        } else {
            return DEFAULT_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ADD_VIEW_TYPE) {
            if (isNativeAd) {
                NativeAd nativeAd = Controller.getInstance().getNativeAd(placementId);

                NativeAdViewHolder nativeAddViewHolder = (NativeAdViewHolder) holder;
                nativeAddViewHolder.appIcon.setImageBitmap(nativeAd.getIconBitmap(NativeAd.ICON_SIZE_200));
                nativeAddViewHolder.appName.setText(nativeAd.getAppName());
                nativeAddViewHolder.appRatingBar.setRating((float) nativeAd.getRating());
                nativeAddViewHolder.ctaButton.setText(nativeAd.getCallToAction());

                nativeAddViewHolder.ctaButton.setOnClickListener(view -> nativeAd.sendClick(context));
            } else {
                InfeedAdContainer infeedAdContainer = Controller.getInstance().getInfeedAdContainer(context, this.placementId);
                infeedAdContainer.bindTo((FrameLayout) ((AdViewHolder) holder).itemView);
            }
        } else {
            ((ViewHolder) holder).itemImageView.setImageResource(imagesIds[holder.getAdapterPosition()]);
        }
    }

    private void checkandAttachAdToRow(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return imagesIds.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageView;

        ViewHolder(View itemView) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.image_view_item);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout imageFrame;

        AdViewHolder(View itemView) {
            super(itemView);

            imageFrame = new RelativeLayout(itemView.getContext());
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageFrame.setLayoutParams(rlParams);
        }
    }

    class NativeAdViewHolder extends RecyclerView.ViewHolder {

        ImageView appIcon;
        TextView appName;
        RatingBar appRatingBar;
        Button ctaButton;

        NativeAdViewHolder(View itemView) {
            super(itemView);

            appIcon = itemView.findViewById(R.id.imageView_app_icon);
            appName = itemView.findViewById(R.id.textView_app_name);
            appRatingBar = itemView.findViewById(R.id.ratingBar);
            ctaButton = itemView.findViewById(R.id.button_cta);
        }
    }
}

