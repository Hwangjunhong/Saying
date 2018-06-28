package com.project.hong.saying;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hong.saying.DataBase.DataCallback;
import com.project.hong.saying.DataBase.FirebaseData;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.KakaoPackage.SessionCallback;
import com.project.hong.saying.Util.LoadingProgress;
import com.project.hong.saying.Util.SharedPreference;
import com.rengwuxian.materialedittext.MaterialEditText;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>, DataCallback {

    private TextView logoText;
    private RelativeLayout facebookLogin, emailLogin, signUpBt;
    private SessionCallback sessionCallback;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CallbackManager callbackManager;
    private LoginButton facebookBt;
    private SharedPreference sharedPreference = new SharedPreference();

    private MaterialEditText editEmail, editPw;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        initView();

    }


    private void initView() {
        logoText = findViewById(R.id.logoText);
        facebookLogin = findViewById(R.id.facebook);
        facebookBt = findViewById(R.id.login_button);
        facebookBt.setReadPermissions("email");

        emailLogin = findViewById(R.id.email);
        editEmail = findViewById(R.id.edit_email);
        editPw = findViewById(R.id.edit_pw);
        signUpBt = findViewById(R.id.sign_up_bt);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Walkway SemiBold.ttf");
        logoText.setTypeface(typeface);
        emailLogin.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        facebookBt.registerCallback(callbackManager, this);

        signUpBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook:
//                LoadingProgress.showDialog(this, true);
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    handleFacebookAccessToken(accessToken, true);
                } else {
                    facebookBt.performClick();
                }
                break;

            case R.id.email:

//                SharedPreference sharedPreference = new SharedPreference();
//                String email = sharedPreference.getValue(this, "userEmail", "");
//                String pwd = sharedPreference.getValue(this, "userPwd", "");


                if (!editEmail.getText().toString().trim().isEmpty() && !editPw.getText().toString().trim().isEmpty()) {
                    if (!TextUtils.isEmpty(editEmail.getText().toString()) && !TextUtils.isEmpty(editPw.getText().toString())) {

                        LoadingProgress.showDialog(this, true);
                        performLoginOrAccountCreation(editEmail.getText().toString(), editPw.getText().toString());
                    } else {
                        Toast.makeText(this, "정보를 확인해 주세요", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    LoadingProgress.dismissDialog();
                    Toast.makeText(this, "정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.sign_up_bt:
                LoadingProgress.showDialog(this, true);
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    private void handleFacebookAccessToken(AccessToken token, final boolean isMember) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isMember) {
                                FirebaseData firebaseData = new FirebaseData();
                                firebaseData.setDataCallback(LoginActivity.this);

                                String uid = mAuth.getCurrentUser().getUid();

                                Profile profile = Profile.getCurrentProfile();
                                UserModel userModel = new UserModel();

                                userModel.setName(profile.getName());
                                userModel.setProfileUrl(profile.getProfilePictureUri(300, 300).toString());
                                firebaseData.userDataUpload(uid, userModel);


                                SharedPreference sharedPreference = new SharedPreference();
                                sharedPreference.put(LoginActivity.this, "userName", userModel.getName());
                                sharedPreference.put(LoginActivity.this, "profileUrl", userModel.getProfileUrl());
                                sharedPreference.put(LoginActivity.this, "loginUserInfo", 0);

                            } else {

                                LoadingProgress.dismissDialog();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {

                        }

                    }
                });
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken(), false);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void completeUpload(Boolean isSuccess) {
        if (isSuccess) {
            LoadingProgress.dismissDialog();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void emailSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            LoadingProgress.dismissDialog();

                            int checkLogin = 1;
                            sharedPreference.put(LoginActivity.this, "checklogin", checkLogin);
                            sharedPreference.put(LoginActivity.this, "loginUserInfo", 201);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            LoadingProgress.dismissDialog();
                            Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    private void performLoginOrAccountCreation(final String email, final String password) {
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(
                this, new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Tag", "checking to see if user exists in firebase or not");
                            ProviderQueryResult result = task.getResult();

                            if (result != null && result.getProviders() != null
                                    && result.getProviders().size() > 0) {

                                emailSignIn(email, password);

                            } else {
                                LoadingProgress.dismissDialog();
                                Toast.makeText(LoginActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            LoadingProgress.dismissDialog();
                            Toast.makeText(LoginActivity.this, "오류 발생 다시 시도해 주세요", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
