package com.project.hong.saying;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.project.hong.saying.Util.SharedPreference;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private TextView logoText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoText = findViewById(R.id.logoText);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Walkway SemiBold.ttf");
        logoText.setTypeface(typeface);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        checkLogin();
    }


    private void checkLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            handleFacebookAccessToken(accessToken);
        } else {
            intentLogin();
        }
    }

    private void intentLogin() {
        Handler handler = new Handler();
        handler.postDelayed(this, 1000);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            intentLogin();
                        }

                    }
                });
    }

    @Override
    public void run() {
        Intent intent;
        SharedPreference sharedPreference = new SharedPreference();
        int LoginCheck = sharedPreference.getValue(SplashActivity.this, "checklogin", 0);

        Pair<View, String> pair = Pair.create((View) logoText, "logo_transition");
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);
        if (LoginCheck == 1) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);

        }
        startActivity(intent, optionsCompat.toBundle());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }

    }
}
