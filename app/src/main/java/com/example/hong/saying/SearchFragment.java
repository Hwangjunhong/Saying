package com.example.hong.saying;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.Util.PaginationScrollListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by hong on 2018-04-03.
 */

public class SearchFragment extends Fragment implements ValueEventListener, TextView.OnEditorActionListener, ScrollToTopClickListener{

    private FirebaseData firebaseData = new FirebaseData();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("feed");

    private String query;
    private RecyclerView recycler;
    private ArrayList<FeedModel> feedModels = new ArrayList<>();
    private FeedSearchAdapter adapter;


    private String lastKey;
    private boolean isLoading = false;
    private ArrayList<String> keyList = new ArrayList<>();

    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_search, container, false);


        initView(view);



        return view;

    }

    private void initView(View view) {
        recycler = view.findViewById(R.id.recycler);
        editText = view.findViewById(R.id.edit_search);
        editText.setOnEditorActionListener(this);

    }


    private void setRecyclerView() {
        adapter = new FeedSearchAdapter(getActivity(), feedModels, keyList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);


        recycler.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                loadMoreFeed(lastKey);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });

    }

    private void loadMoreFeed(String lastKey) {
        reference.orderByKey().startAt(lastKey).limitToFirst(100).addListenerForSingleValueEvent(this);
        isLoading = true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            FeedModel feedModel = snapshot.getValue(FeedModel.class);
            if (feedModel.getContents().contains(query) || feedModel.getUserName().contains(query)) {
                if (lastKey != null) {
                    if (!snapshot.getKey().equals(lastKey)) {
                        feedModels.add(feedModel);
                        keyList.add(snapshot.getKey());
                    }
                }
            }

            lastKey = snapshot.getKey();
        }

        if (adapter == null) {
            setRecyclerView();
        } else {
            adapter.notifyDataSetChanged();
        }
        isLoading = false;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void searchFeed() {
        reference.orderByKey().limitToFirst(100).addListenerForSingleValueEvent(this);
    }


    @SuppressLint("ServiceCast")
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                feedModels.clear();

                query = editText.getText().toString().trim();
                editText.setText("");

                ((InputMethodManager) getActivity()
                        .getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editText.getWindowToken(), 0);

                if (!isLoading) {
                    searchFeed();
                }
                break;

        }

        return false;
    }

    @Override
    public void scrollClick() {
        if (feedModels.size() > 4) {
            recycler.scrollToPosition(1);
        }
        recycler.smoothScrollToPosition(0);
    }


}
