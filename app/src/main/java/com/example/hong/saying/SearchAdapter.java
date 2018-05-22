package com.example.hong.saying;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.hong.saying.Util.Hit;
import com.example.hong.saying.Util.SquareImageView;

import java.util.List;

/**
 * Created by hong on 2018-05-02.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    List<Hit> images;
    Context context;
    RequestManager requestManager;

    public SearchAdapter(List<Hit> images, Context context) {
        this.images = images;
        this.context = context;
        requestManager = Glide.with(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        requestManager.load(images.get(position).getPreviewURL()).into(holder.imageView);
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SquareImageView imageView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = itemView.findViewById(R.id.image);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = (int) view.getTag();
            if (context instanceof SearchActivity) {
                Intent intent = new Intent();
                intent.putExtra("path", images.get(position).getWebformatURL());
                ((SearchActivity) context).setResult(100, intent);
                ((SearchActivity) context).finish();
            }


        }
    }
}
