package com.example.hong.saying;

import android.app.Application;

import com.example.hong.saying.KakaoPackage.KakaoSDKAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by hong on 2018-05-10.
 */

public class SayApplication extends Application {

    static SayApplication application;

    public static SayApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

}
