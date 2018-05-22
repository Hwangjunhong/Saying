package com.example.hong.saying.KakaoPackage;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {

    KakaoCallback callback;

    public SessionCallback(KakaoCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSessionOpened() {
        callback.kakaoResult("OK");
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        callback.kakaoResult("FAIL");
    }
}