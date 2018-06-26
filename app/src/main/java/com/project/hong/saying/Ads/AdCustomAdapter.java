package com.project.hong.saying.Ads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.edge.fbadhelper.FBAdapterSetting;
import com.edge.fbadhelper.FBCustomAdapter;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.MainActivity;
import com.project.hong.saying.R;
import com.project.hong.saying.SayDetailActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-06-20.
 */

public class AdCustomAdapter extends FBCustomAdapter<AdCustomAdapter.MyHolder, AdCustomAdapter.AdHolder> {
    private Context context;
    private ArrayList<FeedModel> feedModels = new ArrayList<>();
    private FeedModel item;
    private RequestManager requestManager;
    private static final int VIEW_LEFT = 0;
    private static final int VIEW_RIGHT = 1;

    private RequestOptions options = new RequestOptions();

    private Typeface typeface;
    private ArrayList<String> keyList = new ArrayList<>();

    public AdCustomAdapter(Context context, ArrayList<FeedModel> feedModels, ArrayList<String> keyList, FBAdapterSetting setting) {
        super(context, feedModels, setting);
        this.context = context;
        this.feedModels = feedModels;
        this.keyList = keyList;
        requestManager = Glide.with(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "조선일보명조.ttf");
        options.error(R.drawable.user1);
        options.placeholder(R.drawable.user1);
        
    }


    @Override
    public RecyclerView.ViewHolder onCreateAllViewHolder(ViewGroup parent, int viewType) {
        View myView, adView;
        switch (viewType) {
            case FBCustomAdapter.AD_TYPE:
                if (viewType == VIEW_RIGHT) {
                    adView = LayoutInflater.from(context).inflate(R.layout.custom_layout, parent, false);
                } else {
                    adView = LayoutInflater.from(context).inflate(R.layout.custom_layout, parent, false);
                }
                return new AdHolder(adView);
            case FBCustomAdapter.POST_TYPE:
                if (viewType == VIEW_RIGHT) {
                    myView = LayoutInflater.from(context).inflate(R.layout.feed_item_right, parent, false);
                } else {
                    myView = LayoutInflater.from(context).inflate(R.layout.feed_item_left, parent, false);
                }
                return new MyHolder(myView);
            default:
                return null;
        }
    }

    @Override
    public void onBindMyViewHolder(MyHolder holder, int position) {
        item = feedModels.get(position);
        requestManager.load(item.getImageUrl()).transition(GenericTransitionOptions.with(R.anim.alpha_anim)).into(holder.feedImage);
        requestManager.load(item.getProfileUrl()).apply(options).into(holder.profileImage);
        holder.contents.setText(item.getContents());
        holder.contents.setTextColor(Color.parseColor("#" + item.getTextColor()));
        holder.contents.setGravity(item.getGravity());
        holder.userName.setText(item.getUserName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 != 0) {
            return VIEW_RIGHT;
        } else {
            return VIEW_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    @Override
    public void onBindFBViewHolder(AdHolder holder, int position, NativeAd nativeAd) {
        holder.bindView(nativeAd);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView feedImage;
        TextView contents;
        View itemView;
        CircleImageView profileImage;
        TextView userName;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            feedImage = itemView.findViewById(R.id.feed_image);
            contents = itemView.findViewById(R.id.contents);
            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            userName.setTypeface(typeface);
            contents.setTypeface(typeface);

            setOnClick();

        }

        private void setOnClick() {
            feedImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) itemView.getTag();
            FeedModel model = feedModels.get(position);
            String key = keyList.get(position);

            Intent intent = new Intent(context, SayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", model);
            intent.putExtras(bundle);
            intent.putExtra("position" , position);
            intent.putExtra("feedKey", key);
            if(context instanceof MainActivity){
                ((MainActivity)context).startActivityForResult(intent, 100);
            }
        }
    }

    class AdHolder extends RecyclerView.ViewHolder {
        private MediaView mAdMedia;
        private ImageView mAdIcon;
        private TextView mAdTitle;
        private TextView mAdBody;
        private TextView mAdSocialContext;
        private Button mAdCallToAction;

        public AdHolder(View view) {
            super(view);

            mAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
            mAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            mAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            mAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            mAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
            mAdIcon = (ImageView) view.findViewById(R.id.native_ad_icon);

        }

        public void bindView(NativeAd ad) {
            if (ad == null) {
                mAdTitle.setText("No Ad");
                mAdBody.setText("Ad is not loaded.");
            } else {
                mAdTitle.setText(ad.getAdvertiserName());
                mAdBody.setText(ad.getAdBodyText());
                mAdSocialContext.setText(ad.getAdSocialContext());
                mAdCallToAction.setText(ad.getAdCallToAction());
                ad.unregisterView();


                ad.downloadMedia();

                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(mAdTitle);
                clickableViews.add(mAdCallToAction);

                // Register the Title and CTA button to listen for clicks.
                ad.registerViewForInteraction(
                        mAdCallToAction,
                        (MediaView) clickableViews);
            }
        }


    }
}