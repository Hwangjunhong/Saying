package com.example.hong.saying;

import android.content.Context;
import android.content.Intent;
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
import com.example.hong.saying.DataModel.FeedModel;

import java.util.ArrayList;

/**
 * Created by hong on 2018-05-24.
 */

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder>{

    ArrayList<FeedModel> feedModels;
    Context context;
    LayoutInflater inflater;
    FeedModel item;
    RequestManager requestManager;

    public MyPostAdapter(ArrayList<FeedModel> feedModels, Context context) {
        this.feedModels = feedModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
        requestManager = Glide.with(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mypost_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item = feedModels.get(position);
        holder.itemView.setTag(position);
        requestManager.load(item.getImageUrl()).into(holder.image);
        holder.contents.setText(item.getContents());
        holder.writerName.setText(item.getUserName());

    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView contents, scrapCount, writerName;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView(itemView);
        }


        private void initView(View view) {
            image = itemView.findViewById(R.id.image);
            contents = itemView.findViewById(R.id.contents);
            scrapCount = itemView.findViewById(R.id.scrap_count);
            writerName = itemView.findViewById(R.id.writer_name);
            contents.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) itemView.getTag();
            FeedModel feedModel = feedModels.get(position);
            switch (v.getId()) {
                case R.id.contents:
                    goDetailIntent(feedModel);
                    break;
                case R.id.image:
                    goDetailIntent(feedModel);
                    break;
            }

        }
    }

    private void goDetailIntent(FeedModel feedModel){
        Intent intent = new Intent(context, SayDetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("feedData", feedModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
