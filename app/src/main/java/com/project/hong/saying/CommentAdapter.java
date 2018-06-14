package com.project.hong.saying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.project.hong.saying.DataModel.CommentModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-06-11.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentModel> commentModels = new ArrayList<>();
    private CommentModel item;
    private Context context;
    private RequestOptions options = new RequestOptions();
    private RequestManager requestManager;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentModels) {
        this.commentModels = commentModels;
        this.context = context;
        requestManager = Glide.with(context);
        options.error(R.drawable.user1);
        options.placeholder(R.drawable.user1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item = commentModels.get(position);
        requestManager.load(item.getProfileUrl()).apply(options).into(holder.profileImage);
        holder.commentText.setText(item.getComment());
        holder.userName.setText(item.getName());
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView commentText, userName;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.user_profile);
            commentText = itemView.findViewById(R.id.user_comment);
            userName = itemView.findViewById(R.id.user_name);
        }
    }

    public void addItem(CommentModel items){
        commentModels.add(items);
        notifyDataSetChanged();
    }
}
