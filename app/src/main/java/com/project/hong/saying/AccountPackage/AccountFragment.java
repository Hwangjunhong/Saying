package com.project.hong.saying.AccountPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.hong.saying.DataBase.FeedDataCallback;
import com.project.hong.saying.DataBase.FirebaseData;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.FragAdapter;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hong on 2018-04-03.
 */

public class AccountFragment extends Fragment implements FeedDataCallback, View.OnClickListener {

    private FirebaseData firebaseData = new FirebaseData();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private String uid = user.getUid();

    private RelativeLayout settingBt;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CircleImageView profileImage;
    private TextView userName;
    private SharedPreference sharedPreference = new SharedPreference();
    private String myProfileUrl, myName;
    private FragAdapter fragAdapter;
    private MyPostFragment myPostFragment = new MyPostFragment();
    private ScrapFragment scrapFragment = new ScrapFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_account, container, false);

        initView(view);
        setViewPager();
        getMyFeedData();

        return view;
    }

    private void initView(View view) {
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        settingBt = view.findViewById(R.id.setting_bt);

        settingBt.setOnClickListener(this);



    }

    private void setData(){
        Glide.with(getActivity()).load(myProfileUrl).apply(new RequestOptions().error(R.drawable.user).placeholder(R.drawable.user)).into(profileImage);
        userName.setText(myName);
    }

    private void setViewPager() {
        fragAdapter = new FragAdapter(getChildFragmentManager());
        fragAdapter.addFragment(scrapFragment);
        fragAdapter.addFragment(myPostFragment);
        fragAdapter.addTitle("스크랩");
        fragAdapter.addTitle("내 포스트");
        viewPager.setAdapter(fragAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getMyInfo() {
        myName = sharedPreference.getValue(getActivity(), "userName", "");
        myProfileUrl = sharedPreference.getValue(getActivity(), "profileUrl", "");

    }

    private void getMyFeedData() {
        firebaseData.setFeedDataCallback(this);
        firebaseData.getMyFeedData(uid);

    }

    @Override
    public void onResume() {
        super.onResume();
        getMyInfo();
        setData();
    }

    @Override
    public void getFeedData(FeedModel feedModel, String key) {
        myPostFragment.addFeedModel(feedModel, key);

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseData.removeCallback();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_bt) {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
//            getActivity().finish();
        }
    }

}
