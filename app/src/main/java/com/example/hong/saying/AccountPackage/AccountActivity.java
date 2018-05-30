package com.example.hong.saying.AccountPackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hong.saying.LoginActivity;
import com.example.hong.saying.MainActivity;
import com.example.hong.saying.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout backBt;
    TextView logoutBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();

    }

    private void initView(){
        logoutBt = findViewById(R.id.logout_bt);
        backBt = findViewById(R.id.back_bt);

        logoutBt.setOnClickListener(this);
        backBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_bt:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back_bt:

                finish();
                break;
        }
    }

}
