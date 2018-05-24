package com.example.hong.saying;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    FragAdapter adapter;
    Menu btMenu;
    Toolbar toolbar;

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
        toolbar = findViewById(R.id.ToolBar);

    }

    private void setViewPager() {
        adapter = new FragAdapter(getSupportFragmentManager());
        adapter.addFragment(new SayFragment());
        adapter.addFragment(new ScarbFragment());
        adapter.addFragment(new AccountFragment());
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
                if(viewPager.getCurrentItem() == 0){
                    ((SayFragment)adapter.getItem(0)).scrollClick();
                } else {
                    viewPager.setCurrentItem(0);
                }
                break;

            case R.id.bt_nav_search:
                viewPager.setCurrentItem(1);
                break;

            case R.id.bt_nav_account:
                viewPager.setCurrentItem(2);
                break;
        }
        return false;
    }

}

