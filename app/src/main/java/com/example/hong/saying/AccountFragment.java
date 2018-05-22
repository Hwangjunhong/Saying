package com.example.hong.saying;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hong.saying.DataBase.FeedDataCallback;
import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.FeedModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by hong on 2018-04-03.
 */

public class AccountFragment extends Fragment implements FeedDataCallback {

    FirebaseData firebaseData = new FirebaseData();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String uid = user.getUid();
    ArrayList<FeedModel> feedModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_account, container, false);

        getMyFeedData();

        return view;
    }

    private void getMyFeedData(){
        firebaseData.setFeedDataCallback(this);
        firebaseData.getMyFeedData(uid);

    }

    @Override
    public void getFeedData(FeedModel feedModel) {
        feedModels.add(feedModel);
        Log.d("asdasd", feedModel.getUserKey());
    }
}
