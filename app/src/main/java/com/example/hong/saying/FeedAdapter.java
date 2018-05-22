package com.example.hong.saying;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.hong.saying.DataModel.FeedModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-04-03.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    Context context;
    ArrayList<FeedModel> feedModels = new ArrayList<>();
    FeedModel item;
    RequestManager requestManager;
    static final int VIEW_LEFT = 0;
    static final int VIEW_RIGHT = 1;

    RequestOptions options = new RequestOptions();

    Typeface typeface;

    public FeedAdapter(Context context, ArrayList<FeedModel> feedModels) {
        this.context = context;
        this.feedModels = feedModels;
        requestManager = Glide.with(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "조선일보명조.ttf");
        options.error(R.drawable.user);
        options.placeholder(R.drawable.user);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.feed_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.feed_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView feedImage;
        TextView contents;
        View itemView;
        CircleImageView profileImage;
        TextView userName;


        public ViewHolder(View itemView) {
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

            Intent intent = new Intent(context, SayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", model);
            intent.putExtras(bundle);
            context.startActivity(intent);


        }
    }
}
