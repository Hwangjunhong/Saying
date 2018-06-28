package com.project.hong.saying.Ads;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edge.fbadhelper.FBAdManager;
import com.edge.fbadhelper.FBAdapterSetting;
import com.edge.fbadhelper.FBLoadListener;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.project.hong.saying.DataBase.FeedDataCallback;
import com.project.hong.saying.DataBase.FirebaseData;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.DataModel.SayFeedModel;
import com.project.hong.saying.R;
import com.project.hong.saying.ScrollToTopClickListener;
import com.project.hong.saying.Upload.UploadActivity;

import java.util.ArrayList;

/**
 * Created by hong on 2018-06-20.
 */

public class AdsSayFragment extends Fragment implements View.OnClickListener, FeedDataCallback,
        ScrollToTopClickListener, SwipeRefreshLayout.OnRefreshListener, FBLoadListener {

    private RecyclerView recyclerView;
    private ArrayList<SayFeedModel> feedModels = new ArrayList<>();
    private AdCustomAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseData firebaseData = new FirebaseData();
    private SwipeRefreshLayout refreshLayout;

    FBAdManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_say, container, false);

        initView(view);
        setManager();

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


//    private void setRecyclerView() {
//        adapter = new FeedAdapter(getActivity(), feedModels, keyList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(layoutManager);
//
//
//    }

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
        SayFeedModel sayFeedModel = new SayFeedModel();
        sayFeedModel.setFeedModel(feedModel);
        sayFeedModel.setKey(key);

        Log.d("asdasdasd", "asdasdasd");
        adapter.addData(0, sayFeedModel);

    }

    @Override
    public void scrollClick() {
        if (feedModels.size() > 4) {
            recyclerView.scrollToPosition(0);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        getData();
    }


    private void setRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        getData();

    }

    private void setAdapter(NativeAdsManager nativeAdsManager) {
        FBAdapterSetting setting = new FBAdapterSetting.Builder()
                .setAdInterval(10)
                .setAdsManager(nativeAdsManager)
                .build();
        adapter = new AdCustomAdapter(getContext(), feedModels, setting);

        setRecyclerView();
    }


    private void setManager() {
        manager = new FBAdManager.Builder("241595176615214_241599689948096", getContext())
                .setAdLoadCount(20)
                .setListener(this)
                .isCaching(true)
                .build();
    }


    @Override
    public void onLoadSuccess(NativeAdsManager nativeAdsManager) {
        setAdapter(nativeAdsManager);

    }

    @Override
    public void onLoadFail(AdError adError) {
        setAdapter(null);
    }

}
