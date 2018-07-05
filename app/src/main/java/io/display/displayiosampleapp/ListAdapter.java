package io.display.displayiosampleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.display.sdk.Controller;
import io.display.sdk.ads.InfeedAdContainer;
import io.display.sdk.ads.supers.NativeAd;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_VIEW_TYPE = 1;
    private static final int DEFAULT_VIEW_TYPE = 0;

    private String placementId;
    private Context context;
    private List<Integer> imagesIds;
    private SparseArray<InfeedAdContainer> loadedAds;
    private boolean displayed;
    private boolean isNativeAd;

    public ListAdapter(List<Integer> imagesIds, int[] adPosition, String placementId, boolean isNative) {
        this.placementId = placementId;
        this.imagesIds = new ArrayList<>();
        this.imagesIds.addAll(imagesIds);
        for (int position : adPosition) {
            this.imagesIds.add(position, null);
        }
        isNativeAd = isNative;
        displayed = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext().getApplicationContext();
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
        if (imagesIds.get(position) == null) {
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

                int width = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
                float aspectRatio = (float) nativeAd.getCreativeHeight() / (float) nativeAd.getCreativeWidth();

                nativeAddViewHolder.appCreative.getLayoutParams().width = width;
                nativeAddViewHolder.appCreative.getLayoutParams().height = (int) (width * aspectRatio);
                nativeAddViewHolder.appCreative.setImageBitmap(nativeAd.getCreativeBitmap());

                nativeAddViewHolder.appName.setText(nativeAd.getAppName());
                nativeAddViewHolder.appDescription.setText(nativeAd.getDescription());

                nativeAddViewHolder.ctaText.setText(nativeAd.getCallToAction());
                nativeAddViewHolder.ctaFrame.setOnClickListener(view -> nativeAd.sendClick(context));
            }
        } else {
            ((ViewHolder) holder).itemImageView.setImageResource(imagesIds.get(holder.getAdapterPosition()));
        }
    }

    private void checkandAttachAdToRow(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imagesIds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageView;

        ViewHolder(View itemView) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.image_view_item);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == ADD_VIEW_TYPE && !isNativeAd) {
            InfeedAdContainer infeedAdContainer = Controller.getInstance().getInfeedAdContainer(context, this.placementId);
            infeedAdContainer.bindTo((FrameLayout) ((AdViewHolder) holder).itemView);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == ADD_VIEW_TYPE && !isNativeAd) {
            InfeedAdContainer infeedAdContainer = Controller.getInstance().getInfeedAdContainer(context, this.placementId);
            infeedAdContainer.unbindFrom((FrameLayout) ((AdViewHolder) holder).itemView);
        }

        super.onViewDetachedFromWindow(holder);
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View itemView) {
            super(itemView);
        }
    }

    class NativeAdViewHolder extends RecyclerView.ViewHolder {

        ImageView appIcon;
        TextView appName;
        TextView appDescription;
        ImageView appCreative;
        RelativeLayout ctaFrame;
        TextView ctaText;

        NativeAdViewHolder(View itemView) {
            super(itemView);

            appIcon = itemView.findViewById(R.id.image_view_app_icon);
            appName = itemView.findViewById(R.id.text_view_native_ad_app_name);
            appDescription = itemView.findViewById(R.id.text_view_native_ad_app_description);
            appCreative = itemView.findViewById(R.id.image_view_creative);
            ctaFrame = itemView.findViewById(R.id.relative_layout_cta);
            ctaText = itemView.findViewById(R.id.text_view_cta);
        }
    }
}

