package com.project.hong.saying;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity implements Runnable {

    TextView logoText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoText = findViewById(R.id.logoText);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Walkway SemiBold.ttf");
        logoText.setTypeface(typeface);
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
        Pair<View, String> pair = Pair.create((View) logoText, "logo_transition");
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, optionsCompat.toBundle());
        finish();
    }
}
