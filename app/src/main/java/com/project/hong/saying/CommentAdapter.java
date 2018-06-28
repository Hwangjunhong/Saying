package com.project.hong.saying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hong.saying.DataModel.CommentModel;

import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<String> keyList = new ArrayList<>();
    private String feedKey;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentModels, ArrayList<String> keyList, String feedKey) {
        this.commentModels = commentModels;
        this.context = context;
        this.keyList = keyList;
        this.feedKey = feedKey;
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

        Date date = new Date();
        date.setTime(item.getTime());
        holder.time.setText(formatTimeString(date));

    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, OnSuccessListener<Void> {
        CircleImageView profileImage;
        TextView commentText, userName, time;
        LinearLayout commentItemLayout;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            time = itemView.findViewById(R.id.time);
            profileImage = itemView.findViewById(R.id.user_profile);
            commentText = itemView.findViewById(R.id.user_comment);
            userName = itemView.findViewById(R.id.user_name);
            commentItemLayout = itemView.findViewById(R.id.commentItem_layout);

            commentItemLayout.setOnLongClickListener(this);

        }


        @Override
        public boolean onLongClick(View v) {

            int position = (int) itemView.getTag();
            String commentKey = keyList.get(position);
            DatabaseReference commentData = FirebaseDatabase.getInstance().getReference().child("feed").child(feedKey).child("comment").child(commentKey);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(item.getUserKey().equals(user.getUid())){
                commentModels.remove(position);
                commentData.removeValue().addOnSuccessListener(this);

            }

            return true;
        }


        @Override
        public void onSuccess(Void aVoid) {
            notifyDataSetChanged();
        }
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int DAY = 30;
        public static final int HOUR = 24;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }


}
