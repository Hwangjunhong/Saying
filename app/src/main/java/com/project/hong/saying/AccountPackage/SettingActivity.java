package com.project.hong.saying.AccountPackage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.project.hong.saying.AccountPackage.EditPackage.ProfileEditActivity;
import com.project.hong.saying.AccountPackage.EditPackage.PwEditActivity;
import com.project.hong.saying.AccountPackage.EditPackage.SuggestActivity;
import com.project.hong.saying.LoginActivity;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.SharedPreference;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout backBt;
    private RelativeLayout logoutBt, licenseBt, profileBt, suggestBt, editPwBt, inviteBt;
    private SharedPreference sharedPreference;
    private int userInfo = 0;

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
        editPwBt = findViewById(R.id.edit_pw_bt);
        inviteBt = findViewById(R.id.invite_bt);

        logoutBt.setOnClickListener(this);
        backBt.setOnClickListener(this);
        licenseBt.setOnClickListener(this);
        profileBt.setOnClickListener(this);
        suggestBt.setOnClickListener(this);
        editPwBt.setOnClickListener(this);
        inviteBt.setOnClickListener(this);

        sharedPreference = new SharedPreference();
        userInfo = sharedPreference.getValue(this, "loginUserInfo", 0);
        if (userInfo == 201) {
            editPwBt.setVisibility(View.VISIBLE);
        } else {
            editPwBt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_bt:
                logoutDialog();

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
                SuggestActivity dialog = new SuggestActivity(this);
                dialog.show();
                break;

            case R.id.edit_pw_bt:
                intentActivity(PwEditActivity.class);
                break;

            case R.id.invite_bt:
                sendInvitation();
                break;

        }
    }

    private void sendInvitation() {
//        TextTemplate params = TextTemplate.newBuilder(
//                "Text", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
//                        .setMobileWebUrl("https://developers.kakao.com")
//                        .build())
//                        .setButtonTitle("Saying으로 초대합니다")
//                        .build();
//
//        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(KakaoLinkResponse result) {
//            }
//        });

        FeedTemplate params = FeedTemplate.newBuilder(ContentObject.newBuilder("Saying",
                "https://firebasestorage.googleapis.com/v0/b/saying-f8ffd.appspot.com/o/appIcon%2Fsaying.jpg?alt=media&token=fe986f3d-97b4-4aea-94c4-6177603eb93b",
                LinkObject.newBuilder().build())
                .setDescrption("모든 것들을 공유하고 공감해 보아요")
                .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setAndroidExecutionParams("key1=value1")
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {

            }
        });


    }


    private <T> void intentActivity(Class<T> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


    private void logoutDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int checkLogin = 0;
                        SharedPreference sharedPreference = new SharedPreference();
                        sharedPreference.put(SettingActivity.this, "checklogin", checkLogin);
                        sharedPreference.put(SettingActivity.this, "loginUserInfo", 0);

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
