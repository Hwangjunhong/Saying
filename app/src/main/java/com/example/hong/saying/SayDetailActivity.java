package com.example.hong.saying;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.Util.LayoutToImage;
import com.example.hong.saying.Util.LoadingProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SayDetailActivity extends AppCompatActivity implements View.OnClickListener, LayoutToImage.SaveImageCallback {

    private FeedModel feedModel;
    private ImageView imageView, shareBt, openCloseArrow, shadow, likeBt, scrapBt;
    private CircleImageView profileImage;
    private TextView userName, say;
    private RelativeLayout openClose, bottomSheet, backBt;
    private RequestOptions options = new RequestOptions();
    private BottomSheetBehavior bottomSheetBehavior;
    private CardView cardView;
    private String key;
    private boolean buttonClick = false;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_detail);

        getData();
        initView();
        setData();

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
        feedModel = (FeedModel) getIntent().getExtras().
                get("feedData");

        key = getIntent().getStringExtra("feedKey");
        reference = database.getReference().child("feed").child(key);

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


    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                FeedModel feedModel = mutableData.getValue(FeedModel.class);
                if (feedModel.getScrap() != null) {
                    if (feedModel.getScrap().contains(firebaseAuth.getCurrentUser().getUid())) {
                        int position = feedModel.getScrap().indexOf(firebaseAuth.getCurrentUser().getUid());
                        feedModel.getScrap().remove(position);

                    } else {
                        feedModel.getScrap().add(firebaseAuth.getCurrentUser().getUid());

                    }

                } else {
                    feedModel.setScrap(new ArrayList<String>());
                    feedModel.getScrap().add(firebaseAuth.getCurrentUser().getUid());
                }
                mutableData.setValue(feedModel);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d("asdasd", databaseError.toString() + "");
                }
            }
        });
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
                onStarClicked(reference);
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


}
