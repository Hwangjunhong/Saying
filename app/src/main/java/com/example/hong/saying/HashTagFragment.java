package com.example.hong.saying;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.hong.saying.DataBase.FeedDataCallback;
import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.FeedModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hong on 2018-04-03.
 */

public class HashTagFragment extends Fragment implements View.OnClickListener {

    private FirebaseData firebaseData = new FirebaseData();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

//    private HashTagHelper mTextHashTagHelper;
//    List<String> allHashTags = mTextHashTagHelper.getAllHashTags();

    private RelativeLayout searchBt;
    private EditText editText;
    private String hashTag;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_hashtag, container, false);


        initView(view);
        hashTag = editText.getText().toString();


        return view;

    }

    private void initView(View view){
        searchBt = view.findViewById(R.id.search_bt);
        editText = view.findViewById(R.id.search_hash_edit);

        searchBt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_bt:
//                getHashTagData(hashTag);
                break;
        }
    }
//
//    private void getHashTagData(String hashTag){
//        firebaseData.setFeedDataCallback(this);
//        firebaseData.getHashTagData(hashTag);
//    }
//
//
//    @Override
//    public void getFeedData(FeedModel feedModel) {
//
//            reference.child("feed").getDatabase();
//
//    }


}
