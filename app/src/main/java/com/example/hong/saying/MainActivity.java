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
    Menu menu;
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
        setSupportActionBar(toolbar);

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
        menu = toolbar.getMenu();
        MenuItem item = menu.findItem(R.id.setting_btn);

        if(position == 2){
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        menu.findItem(R.id.setting_btn).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_btn:
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

}

