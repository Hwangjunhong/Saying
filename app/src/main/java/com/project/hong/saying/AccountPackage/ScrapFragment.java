package com.project.hong.saying.AccountPackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.hong.saying.DataBase.FirebaseData;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.MyPostAdapter;
import com.project.hong.saying.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hong on 2018-05-24.
 */

public class ScrapFragment extends Fragment implements ValueEventListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private DatabaseReference feedReference;

    private String uid;
    private UserModel userModel;
    private ArrayList<FeedModel> feedModels = new ArrayList<>();
    private ArrayList<String> keyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyPostAdapter adapter;
    private TextView empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_scrab, container, false);

        getData();
        initView(view);
        setRecyclerView();

        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler);
        empty = view.findViewById(R.id.empty);

    }

    private void setRecyclerView() {
        adapter = new MyPostAdapter(feedModels, getActivity(), keyList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        if (feedModels != null && feedModels.size() > 0) {
            feedModels.clear();
        }

        if (keyList != null && keyList.size() > 0) {
            feedModels.clear();
        }

    }

    private void getData() {
        uid = firebaseAuth.getCurrentUser().getUid();
        reference = database.getReference().child("user").child(uid);
        reference.addListenerForSingleValueEvent(this);

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        userModel = dataSnapshot.getValue(UserModel.class);
        if (userModel != null) {
            if (userModel.getScrapList() != null) {
                empty.setVisibility(View.GONE);
                Collections.reverse(userModel.getScrapList());
                keyList.addAll(userModel.getScrapList());
                getFeedData();
            }
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void getFeedData() {
        for (String key : userModel.getScrapList()) {
            feedReference = database.getReference().child("feed").child(key);
            feedReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FeedModel feedModel = dataSnapshot.getValue(FeedModel.class);
                    feedModels.add(feedModel);
                    adapter.notifyItemInserted(feedModels.size() - 1);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
