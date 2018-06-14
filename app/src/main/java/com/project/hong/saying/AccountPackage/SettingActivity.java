package com.project.hong.saying.AccountPackage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.project.hong.saying.AccountPackage.ProfileEditPackage.ProfileEditActivity;
import com.project.hong.saying.LoginActivity;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.CustomDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout backBt;
    RelativeLayout logoutBt, licenseBt, profileBt, suggestBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

    }

    private void initView() {
        logoutBt = findViewById(R.id.logout_bt);
        backBt = findViewById(R.id.back_bt);
        licenseBt = findViewById(R.id.license_bt);
        profileBt = findViewById(R.id.profile_edit_bt);
        suggestBt = findViewById(R.id.suggestion_bt);

        logoutBt.setOnClickListener(this);
        backBt.setOnClickListener(this);
        licenseBt.setOnClickListener(this);
        profileBt.setOnClickListener(this);
        suggestBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_bt:
                loginDialog();

                break;
            case R.id.back_bt:
                finish();
                break;

            case R.id.license_bt:
                intentActivity(LicenseActivity.class);
                break;

            case R.id.profile_edit_bt:
                intentActivity(ProfileEditActivity.class);
                break;

            case R.id.suggestion_bt:
                CustomDialog dialog = new CustomDialog(this);
                dialog.show();
                break;

        }
    }

    private <T> void intentActivity(Class<T> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


    private void loginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        intentActivity(LoginActivity.class);
                        ActivityCompat.finishAffinity(SettingActivity.this);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }

}
