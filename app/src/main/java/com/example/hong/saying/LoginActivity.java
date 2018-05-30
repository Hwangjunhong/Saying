package com.example.hong.saying;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hong.saying.DataBase.DataCallback;
import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.UserModel;
import com.example.hong.saying.KakaoPackage.SessionCallback;
import com.example.hong.saying.Util.LoadingProgress;
import com.example.hong.saying.Util.SharedPreference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.Query;

import javax.microedition.khronos.opengles.GL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>, DataCallback {

    TextView logoText;
    RelativeLayout kakaoLogin, facebookLogin, emailLogin;
    SessionCallback sessionCallback;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CallbackManager callbackManager;
    LoginButton facebookBt;
    SharedPreference sharedPreference = new SharedPreference();

    EditText editEmail, editPw;
    TextView signUpBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        initView();

    }


//    public static String getKeyHash(final Context context) {
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.w("asdasd", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

    private void initView() {
        logoText = findViewById(R.id.logoText);
        kakaoLogin = findViewById(R.id.kakao);

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
        kakaoLogin.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        facebookBt.registerCallback(callbackManager, this);

        signUpBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kakao:
//                kakaoLogin();
                break;
            case R.id.facebook:
//                LoadingProgress.showDialog(this, true);
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    handleFacebookAccessToken(accessToken);
                } else {
                    facebookBt.performClick();
                }
                break;

            case R.id.email:
                LoadingProgress.showDialog(this, true);
                emailSignIn(editEmail.getText().toString(), editPw.getText().toString());

                break;

            case R.id.sign_up_bt:
                LoadingProgress.showDialog(this, true);
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


//    private void kakaoLogin() {
//        Session.getCurrentSession().addCallback(sessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();
//        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
//    }

//    @Override
//    public void kakaoResult(String result) {
//        switch (result) {
//            case "OK":
//                requestAccessTokenInfo();
//                break;
//            case "FAIL":
//                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            return;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Session.getCurrentSession().removeCallback(sessionCallback);
//    }

//    private void requestAccessTokenInfo() {
//        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//
//            }
//
//            @Override
//            public void onNotSignedUp() {
//                // not happened
//            }
//
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e("failed to get access token info. msg=" + errorResult);
//            }
//
//            @Override
//            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
//                long userId = accessTokenInfoResponse.getUserId();
//                Logger.d("aaa", "this access token is for userId=" + userId);
//
//                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
//                Logger.d("bbb", "this access token expires after " + expiresInMilis + " milliseconds.");
//
//
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//
////                String customToken = getFirebaseToken(userId);
////                signInFirebaseToken(customToken);
//
//
//            }
//        });
//    }
//
//    private void signInFirebaseToken(String token) {
//        mAuth.signInWithCustomToken(token)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        FirebaseUser user = mAuth.getCurrentUser();
//
//                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//
//                        }
//                    }
//                });
//
//    }
//
//    private String getFirebaseToken(long userId) {
//        String key = getString(R.string.jwt_key);
//        long iat = System.currentTimeMillis();
//        Map<String, Object> claim = new HashMap<>();
//        claim.put("alg", "RS256");
//        claim.put("iss", "ghkdwnsghd1@gmail.com");
//        claim.put("sub", "ghkdwnsghd1@gmail.com");
//        claim.put("aud", "\"https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit\"");
//        claim.put("iat", iat);
//        claim.put("exp", iat + 3600000);
//        claim.put("uid", userId);
//        String token = Jwts.builder().addClaims(claim).signWith(SignatureAlgorithm.RS256, key).compact();
//        return token;
//
//    }


//    public void facebookLoginOnClick() {
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
//                Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(final LoginResult result) {
//
//                GraphRequest request;
//                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//
//                    @Override
//                    public void onCompleted(JSONObject user, GraphResponse response) {
//                        if (response.getError() == null) {
//                            Log.i("TAG", "user: " + user.toString());
//                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
//                            try {
//                                String id = user.getString("id");
//                                String name = user.getString("name");
//                                String email = user.getString("email");
//                                AccessToken accessToken = result.getAccessToken();
//                                handleFacebookAccessToken(accessToken);
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.e("test", "Error: " + error);
//                //finish();
//            }
//
//            @Override
//            public void onCancel() {
//                //finish();
//            }
//        });
//    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseData firebaseData = new FirebaseData();
                            firebaseData.setDataCallback(LoginActivity.this);

                            String uid = mAuth.getCurrentUser().getUid();

                            Profile profile = Profile.getCurrentProfile();
                            UserModel userModel = new UserModel();
                            userModel.setName(profile.getName());
                            userModel.setProfileUrl(profile.getProfilePictureUri(300,300).toString());
                            firebaseData.userDataUpload(uid, userModel);


                            SharedPreference sharedPreference = new SharedPreference();
                            sharedPreference.put(LoginActivity.this, "userName", userModel.getName());
                            sharedPreference.put(LoginActivity.this, "profileUrl", userModel.getProfileUrl());
//                            firebaseData.userDataUpload(uid, userModel);


                        } else {

                        }

                    }
                });
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
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


                            UserModel userModel = new UserModel();
                            SharedPreference sharedPreference = new SharedPreference();
                            sharedPreference.put(LoginActivity.this, "name", userModel.getName());

                            FirebaseData firebaseData = new FirebaseData();
//                            firebaseData.setDataCallback(LoginActivity.this);

                            String uid = mAuth.getCurrentUser().getUid();
                            userModel.setName(userModel.getName());
                            firebaseData.userDataUpload(uid, userModel);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                        }

                        // ...
                    }
                });

    }


}
