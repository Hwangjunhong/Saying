package com.project.hong.saying.Ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.project.hong.saying.R;

public class AdsFull {

    private static InterstitialAd adFull;

    private static AdsFull instance = null;
    private static Context context;

    public AdsFull(Context context) {
        MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
        this.context = context;
    }

    public static AdsFull getInstance(Context context) {


        if (instance == null) {
            instance = new AdsFull(context);
            adFull = new InterstitialAd(context);
            setAds();
        }

        return instance;
    }


    private static void setAds() {

        AdRequest adRequest = new AdRequest.Builder().build(); //새 광고요청
        adFull = new InterstitialAd(context);
        adFull.setAdUnitId(context.getResources().getString(R.string.ad_unit_id));
        adFull.loadAd(adRequest); //요청한 광고를 load 합니다.

        adFull.setAdListener(new AdListener() { //전면 광고의 상태를 확인하는 리스너 등록

            @Override
            public void onAdClosed() { //전면 광고가 열린 뒤에 닫혔을 때
                AdRequest adRequest = new AdRequest.Builder().build(); //새 광고요청
                adFull.loadAd(adRequest); //요청한 광고를 load 합니다.
            }
        });
    }




}

