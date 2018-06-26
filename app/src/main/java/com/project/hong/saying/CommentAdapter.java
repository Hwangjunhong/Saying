package com.project.hong.saying;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.project.hong.saying.DataModel.CommentModel;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.Util.SharedPreference;

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

    private DatabaseReference reference;

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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView profileImage;
        TextView commentText, userName;
        ImageView likeBt;
        LinearLayout commentItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.user_profile);
            commentText = itemView.findViewById(R.id.user_comment);
            userName = itemView.findViewById(R.id.user_name);
            likeBt = itemView.findViewById(R.id.like_bt);
            commentItemLayout = itemView.findViewById(R.id.commentItem_layout);


            reference = FirebaseDatabase.getInstance().getReference().child("feed").child("comment");
            likeBt.setOnClickListener(this);
            commentItemLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHeartClicked(reference);

        }

        //TODO 자기 자신의 아이템만 삭제할수있게..
        @Override
        public boolean onLongClick(View v) {

            Log.d("asdasdasd", "asdasdasdasd");
            commentModels.remove(getAdapterPosition());
            notifyDataSetChanged();


            return true;
        }














        private void onHeartClicked(DatabaseReference postRef) {
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    CommentModel item = mutableData.getValue(CommentModel.class);
                    if (item.getLike() != null) {
                        if (item.getLike().contains(firebaseAuth.getCurrentUser().getUid())) {
                            int position = item.getLike().indexOf(firebaseAuth.getCurrentUser().getUid());
                            item.getLike().remove(position);
                            likeBt.setSelected(false);

                        } else {
                            item.getLike().add(firebaseAuth.getCurrentUser().getUid());
                            likeBt.setSelected(true);

                        }

                    } else {
                        item.setLike(new ArrayList<String>());
                        item.getLike().add(firebaseAuth.getCurrentUser().getUid());
                        likeBt.setSelected(true);
                    }

                    mutableData.setValue(item);

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        Log.d("asdasd", databaseError.toString() + "");
                    }
                }
            });
        }


    }


}
