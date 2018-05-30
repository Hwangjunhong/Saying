package com.example.hong.saying.AccountPackage;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.MyPostAdapter;
import com.example.hong.saying.R;

import java.util.ArrayList;

/**
 * Created by hong on 2018-05-24.
 */

public class MyPostFragment extends Fragment{

    ArrayList<FeedModel> feedModels = new ArrayList<>();
    RecyclerView recyclerView;
    MyPostAdapter adapter;
    TextView empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_mypost, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        empty = view.findViewById(R.id.empty);

        setRecyclerView();

        return view;
    }

    private void setRecyclerView(){
        adapter = new MyPostAdapter(feedModels, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        if(feedModels != null && feedModels.size() > 0){
            feedModels.clear();
        }


    }

    public void addFeedModel(FeedModel feedModel) {
        feedModels.add(feedModel);
        adapter.notifyItemChanged(feedModels.size() - 1);

    }
}
