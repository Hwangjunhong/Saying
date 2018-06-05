package com.example.hong.saying;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.hong.saying.DataModel.FeedModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-06-02.
 */

public class FeedSearchAdapter extends RecyclerView.Adapter<FeedSearchAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FeedModel> feedModels = new ArrayList<>();
    private FeedModel item;
    private RequestManager requestManager;
    private RequestOptions options = new RequestOptions();

    private Typeface typeface;
    private ArrayList<String> keyList = new ArrayList<>();

    public FeedSearchAdapter(Context context, ArrayList<FeedModel> feedModels, ArrayList<String> keyList) {
        this.context = context;
        this.feedModels = feedModels;
        this.keyList = keyList;
        requestManager = Glide.with(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "조선일보명조.ttf");
        options.error(R.drawable.user);
        options.placeholder(R.drawable.user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.feed_item_search, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item = feedModels.get(position);
        requestManager.load(item.getImageUrl()).into(holder.feedImage);
        requestManager.load(item.getProfileUrl()).apply(options).into(holder.profileImage);
        holder.contents.setText(item.getContents());
        holder.contents.setTextColor(Color.parseColor("#" + item.getTextColor()));
        holder.userName.setText(item.getUserName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            String key = keyList.get(position);

            Intent intent = new Intent(context, SayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", model);
            intent.putExtras(bundle);
            intent.putExtra("feedKey", key);
            context.startActivity(intent);
        }
    }
}
