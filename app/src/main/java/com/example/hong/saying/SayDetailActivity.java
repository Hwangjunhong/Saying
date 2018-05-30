package com.example.hong.saying;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.Util.LayoutToImage;
import com.example.hong.saying.Util.LoadingProgress;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SayDetailActivity extends AppCompatActivity implements View.OnClickListener, LayoutToImage.SaveImageCallback {

    FeedModel feedModel;
    ImageView imageView, shareBt, openCloseArrow, shadow, likeBt, scrapBt;
    CircleImageView profileImage;
    TextView userName, say, text_hashTag;
    RelativeLayout openClose, bottomSheet, backBt;
    RequestOptions options = new RequestOptions();
    BottomSheetBehavior bottomSheetBehavior;
    CardView cardView;

    ArrayList<String> hashTag = new ArrayList<>();

    private HashTagHelper mTextHashTagHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_detail);

        getData();
        initView();
        setData();
        HashTagClicked();

    }

    private void initView() {
        imageView = findViewById(R.id.image);
        backBt = findViewById(R.id.back_bt);
        shareBt = findViewById(R.id.share_bt);
        openCloseArrow = findViewById(R.id.open_close_arrow);
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        openClose = findViewById(R.id.open_close);
        bottomSheet = findViewById(R.id.bottom_sheet);
        shadow = findViewById(R.id.shadow);
        say = findViewById(R.id.say);
        cardView = findViewById(R.id.card_view);
        likeBt = findViewById(R.id.like_bt);
        scrapBt = findViewById(R.id.scrap_bt);
        text_hashTag = findViewById(R.id.text_hashTag);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                openCloseArrow.setRotation(180 - (180 * slideOffset));
                if (slideOffset > 0.9) {
                    shadow.setVisibility(View.GONE);
                } else {
                    shadow.setVisibility(View.VISIBLE);
                }
            }
        });

        backBt.setOnClickListener(this);
        openClose.setOnClickListener(this);
        shareBt.setOnClickListener(this);
        likeBt.setOnClickListener(this);
        scrapBt.setOnClickListener(this);
    }

    private void getData() {
        feedModel = (FeedModel) getIntent().getExtras().get("feedData");
    }

    private void setData() {
        Glide.with(this).load(feedModel.getImageUrl())
                .transition(GenericTransitionOptions.with(R.anim.alpha_anim))
                .into(imageView);


        Glide.with(this).load(feedModel.getProfileUrl())
                .apply(options.error(R.drawable.user).placeholder(R.drawable.user))
                .into(profileImage);

        userName.setText(feedModel.getUserName());
        say.setText(feedModel.getContents());
        say.setTextColor(Color.parseColor("#" + feedModel.getTextColor()));
        say.setGravity(feedModel.getGravity());
        text_hashTag.setText(feedModel.getHashTag());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_bt:
                finish();
                break;
            case R.id.open_close:
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                }
                break;

            case R.id.share_bt:
                if (checkPermassion()) {
                    makeImageCard();
                }
                break;

            case R.id.like_bt:

                break;
            case R.id.scrap_bt:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (bottomSheetBehavior.getState()) {
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case BottomSheetBehavior.STATE_COLLAPSED:
                finish();
                break;


        }
    }

    private void makeImageCard() {
        LoadingProgress.showDialog(this, true);
        LayoutToImage layoutToImage = new LayoutToImage();
        layoutToImage.setSaveImageCallback(this);
        layoutToImage.saveBitMap(this, cardView);
    }

    @Override
    public void imageResult(int status, Uri uri) {
        LoadingProgress.dismissDialog();
        switch (status) {
            case 200:
                shareIamge(uri);
                break;
            case 400:
                break;

        }
    }

    private boolean checkPermassion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            makeImageCard();
        }
    }

    private void shareIamge(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(intent.createChooser(intent, "Share Image"));

    }


    private void HashTagClicked() {
        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {


                hashTag = text_hashTag.getText().toString();
//                chooseWord(hashTag.length() + 1);

                Toast.makeText(SayDetailActivity.this, hashTag, Toast.LENGTH_SHORT).show();
            }
        });

        mTextHashTagHelper.handle(text_hashTag);
    }


//    public String chooseWord(int offset) {
//        String strWord = "";
//
//        for (int i = offset - 1; ; i--) {     //선택한 글자(알파벳하나)가 포함된 단어의 앞부분 받아오기
//            String a = String.valueOf(text_hashTag.getText().charAt(i)-1);
//
//            if (!a.equals(" ") && !a.equals(",") && !a.equals("]") && !a.equals(")") && !a.equals("`") && !a.equals("?") && !a.equals(".") && !a.equals("/") && !a.equals(";") && !a.equals(":")) {
//                strWord = a + strWord;
//                Log.d("offset", "offset " + offset);
//                Log.d("offset", "word " + strWord);
//            } else
//                break;
//        }
//
//        for (int i = offset; ; i++) {   //선택한 글자(알파벳하나)가 포함된 단어의 뒷부분 받아오기
//
//            String a = String.valueOf(text_hashTag.getText().charAt(i)-1);
//
//            if (!a.equals(" ") && !a.equals(",") && !a.equals("]") && !a.equals(")") && !a.equals("`") && !a.equals("?") && !a.equals(".") && !a.equals("/") && !a.equals(";") && !a.equals(":")) {
//                strWord = strWord + a;
//                Log.d("offset", "offset " + offset);
//                Log.d("offset", "word " + strWord);
//            } else
//                break;
//        }
//        return strWord;
//
//
//    }
//
//
//    public int getIndex(TextView view, MotionEvent event) {
//
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        x -= view.getTotalPaddingLeft();
//        y -= view.getTotalPaddingTop();
//        x += view.getScrollX();
//        y += view.getScrollY();
//        Layout layout = view.getLayout();
//        int line = layout.getLineForVertical(y);
//        int offset = layout.getOffsetForHorizontal(line, x);
//        Toast.makeText(this, "line:" + line + ",index:" + offset, Toast.LENGTH_LONG).show();
//        return offset;
//
//    }
//
//    GestureDetector.OnGestureListener mTouchListener = new GestureDetector.OnGestureListener() {
//
//        public boolean onDown(MotionEvent event) {
//            return false;
//        }
//
//        public void onShowPress(MotionEvent event) {
//            int offset = getIndex(text_hashTag, event);
//
//        }
//
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            return false;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            return false;
//        }
//
//        @Override
//        public void onLongPress(MotionEvent e) {
//
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            return false;
//        }
//    };


}
