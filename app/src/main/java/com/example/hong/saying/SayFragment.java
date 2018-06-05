package com.example.hong.saying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hong.saying.DataBase.FeedDataCallback;
import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.Upload.UploadActivity;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

/**
 * Created by hong on 2018-04-03.
 */

public class SayFragment extends Fragment implements View.OnClickListener, FeedDataCallback,
        ScrollToTopClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<FeedModel> feedModels = new ArrayList<>();
    private FeedAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseData firebaseData = new FirebaseData();
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<String> keyList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_say, container, false);

        initView(view);
        setRecyclerView();
        getData();

        recyclerView.setOnClickListener(this);

        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
    }

    private void getData() {
        if (feedModels != null) {
            feedModels.clear();
        }
        firebaseData.removeCallback();
        firebaseData.setFeedDataCallback(this);
        firebaseData.getFeedData();
    }


    private void setRecyclerView() {
        adapter = new FeedAdapter(getActivity(), feedModels, keyList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
                break;


        }

    }

    @Override
    public void getFeedData(FeedModel feedModel, String key) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        feedModels.add(0, feedModel);
        keyList.add(0, key);
        adapter.notifyItemInserted(0);
    }

    @Override
    public void scrollClick() {
        if (feedModels.size() > 4) {
            recyclerView.scrollToPosition(1);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        getData();
    }

}
