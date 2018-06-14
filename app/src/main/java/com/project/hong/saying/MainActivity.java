package com.project.hong.saying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.project.hong.saying.AccountPackage.AccountFragment;
import com.project.hong.saying.DataModel.FeedModel;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    FragAdapter adapter;
    Menu btMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();
        setViewPager();
        setBottomNavigationView();

    }


    private void initView() {
        viewPager = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.bt_nav);

    }

    private void setViewPager() {
        adapter = new FragAdapter(getSupportFragmentManager());
        adapter.addFragment(new SayFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new AccountFragment());
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void setBottomNavigationView() {
        btMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btMenu.getItem(position).setChecked(true);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_nav_say:
                if (viewPager.getCurrentItem() == 0) {
                    ((SayFragment) adapter.getItem(0)).scrollClick();

                } else {
                    viewPager.setCurrentItem(0);
                }
                break;

            case R.id.bt_nav_search:
                if (viewPager.getCurrentItem() == 1) {
                    ((SearchFragment) adapter.getItem(1)).scrollClick();
                } else {
                    viewPager.setCurrentItem(1);
                }

                break;

            case R.id.bt_nav_account:
                viewPager.setCurrentItem(2);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            FeedModel feedModel = (FeedModel) data.getExtras().getSerializable("feedData");
            int position =  data.getIntExtra("position", 0);
            ((SayFragment)adapter.getItem(0)).refreshFeedData(position, feedModel);

        }
    }
}

