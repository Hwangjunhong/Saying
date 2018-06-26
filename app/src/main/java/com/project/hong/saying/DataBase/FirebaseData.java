package com.project.hong.saying.DataBase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.DataModel.UserModel;

/**
 * Created by hong on 2018-05-08.
 */

public class FirebaseData implements OnCompleteListener<Void>, ChildEventListener {

    FeedDataCallback feedDataCallback;
    DataCallback dataCallback;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FeedModel feedModel;

    public void setDataCallback(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    public void FeedDataUpload(FeedModel feedModel) {
        reference = database.getReference("feed");
        reference.push().setValue(feedModel)
                .addOnCompleteListener(this);

    }

    public void userDataUpload(final String uid, final UserModel userModel){
        reference = database.getReference("user");
        reference.orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    reference.child(uid).setValue(userModel).addOnCompleteListener(FirebaseData.this);
                } else {
                    dataCallback.completeUpload(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setFeedDataCallback(FeedDataCallback feedDataCallback) {
        this.feedDataCallback = feedDataCallback;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            if (dataCallback != null) {
                dataCallback.completeUpload(true);
            }

        } else {
            if (dataCallback != null) {
                dataCallback.completeUpload(false);
            }

        }
    }


    public void getFeedData() {
        reference = database.getReference("feed");
        reference.orderByChild("time").addChildEventListener(this);
    }

    public void getMyFeedData(String userKey) {
        reference = database.getReference("feed");
        reference.orderByChild("userKey").equalTo(userKey).addChildEventListener(this);
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        FeedModel feedModel = dataSnapshot.getValue(FeedModel.class);
        if (feedDataCallback != null) {
            feedDataCallback.getFeedData(feedModel, dataSnapshot.getKey());
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    public void removeCallback(){
        if(reference != null) {
            reference.removeEventListener(this);
        }
    }

}
