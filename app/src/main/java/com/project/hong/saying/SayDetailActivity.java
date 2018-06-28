package com.project.hong.saying;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.project.hong.saying.DataModel.CommentModel;
import com.project.hong.saying.DataModel.FeedModel;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.Util.LayoutToImage;
import com.project.hong.saying.Util.LoadingProgress;
import com.project.hong.saying.Util.SharedPreference;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SayDetailActivity extends AppCompatActivity implements View.OnClickListener, LayoutToImage.SaveImageCallback,
        ChildEventListener {

    private FeedModel feedModel;
    private ImageView imageView, shareBt, openCloseArrow, shadow, scrapBt, chatBt;
    private CircleImageView profileImage;
    private TextView userName, say, replyCount, time;
    private RelativeLayout openClose, bottomSheet, backBt;
    private RequestOptions options = new RequestOptions();
    private BottomSheetBehavior bottomSheetBehavior;
    private CardView cardView;
    private String key, uid, userNameSt, profileUrl;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference, myDatabase, commentData;
    private int position;
    private Toolbar toolbar;

    private RelativeLayout commentSendBt;
    private RecyclerView commentRecycler;
    private CommentAdapter adapter;
    private EditText commentEdit;
    private ArrayList<CommentModel> commentModels = new ArrayList<>();
    private CommentModel commentModel = new CommentModel();
    private ArrayList<String> keyList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_detail);

        getData();
        initView();
        setScrapBt();
        setData();
        setCommentRecycler();

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
        scrapBt = findViewById(R.id.scrap_bt);
        replyCount = findViewById(R.id.reply_count);
        chatBt = findViewById(R.id.reply_bt);
        time = findViewById(R.id.time);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        scrapBt.setOnClickListener(this);
        chatBt.setOnClickListener(this);

        commentSendBt = findViewById(R.id.send_comment);
        commentEdit = findViewById(R.id.comment_edit);
        commentRecycler = findViewById(R.id.comment_recycler);
        commentSendBt.setOnClickListener(this);

    }


    private void setCommentRecycler() {
        adapter = new CommentAdapter(this, commentModels, keyList, key);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRecycler.setLayoutManager(layoutManager);
        commentRecycler.setAdapter(adapter);

    }

    private void getData() {

        feedModel = (FeedModel) getIntent().getExtras().get("feedData");
        position = getIntent().getIntExtra("position", 0);
        key = getIntent().getStringExtra("feedKey");
        reference = database.getReference().child("feed").child(key);
        uid = firebaseAuth.getCurrentUser().getUid();
        myDatabase = database.getReference().child("user").child(uid);
        commentData = database.getReference().child("feed").child(key);
        commentData.child("comment").orderByKey().addChildEventListener(this);

        SharedPreference sharedPreference = new SharedPreference();
        userNameSt = sharedPreference.getValue(this, "userName", "");
        profileUrl = sharedPreference.getValue(this, "profileUrl", "");
        sharedPreference.put(this, "removeKey", key);


    }


    private void uploadComment(String comment) {
//        SharedPreference sharedPreference = new SharedPreference();
//        String commentKey = sharedPreference.getValue(this, "commentKey", "");
//        reference.child("comment").child(commentKey).push().setValue(commentModel);

        CommentModel commentModel = new CommentModel(profileUrl, userNameSt, comment, uid, System.currentTimeMillis());
        reference.child("comment").push().setValue(commentModel);

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

        Date date = new Date();
        date.setTime(feedModel.getTime());
        time.setText(formatTimeString(date));


    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int DAY = 30;
        public static final int HOUR = 24;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }


    private void setScrapBt() {
        if (feedModel.getScrap() != null) {
            boolean isScraped = feedModel.getScrap().contains(uid);
            scrapBt.setSelected(isScraped);

        }

    }

    private void saveMyScrap(final DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if (userModel.getScrapList() != null) {
                    if (userModel.getScrapList().contains(key)) {
                        int position = userModel.getScrapList().indexOf(key);
                        userModel.getScrapList().remove(position);

                    } else {
                        userModel.getScrapList().add(key);
                    }

                } else {
                    userModel.setScrapList(new ArrayList<String>());
                    userModel.getScrapList().add(key);
                }

                reference.setValue(userModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                feedModel = mutableData.getValue(FeedModel.class);
                if (!feedModel.getUserKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                    if (feedModel.getScrap() != null) {

                        if (feedModel.getScrap().contains(firebaseAuth.getCurrentUser().getUid())) {
                            int position = feedModel.getScrap().indexOf(firebaseAuth.getCurrentUser().getUid());
                            feedModel.getScrap().remove(position);
                            scrapBt.setSelected(false);

                        } else {
                            feedModel.getScrap().add(firebaseAuth.getCurrentUser().getUid());
                            scrapBt.setSelected(true);

                        }

                    } else {
                        feedModel.setScrap(new ArrayList<String>());
                        feedModel.getScrap().add(firebaseAuth.getCurrentUser().getUid());
                        scrapBt.setSelected(true);
                    }

                    saveMyScrap(myDatabase);
                    mutableData.setValue(feedModel);

                }
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
            case R.id.reply_bt:
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                }
                break;

            case R.id.back_bt:
                onBackPressed();
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

            case R.id.scrap_bt:
                onStarClicked(reference);
                break;


            case R.id.send_comment:
                String comments = commentEdit.getText().toString();
                if (!comments.trim().isEmpty()) {
                    uploadComment(comments);
                } else {
                    Toast.makeText(this, "메시지를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }

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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedData", feedModel);
                intent.putExtra("position", position);
                intent.putExtras(bundle);
                setResult(100, intent);

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


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        CommentModel commentModel = dataSnapshot.getValue(CommentModel.class);
        commentModels.add(commentModel);
        keyList.add(dataSnapshot.getKey());

        adapter.notifyItemInserted(commentModels.size() - 1);
        replyCount.setText("(" + commentModels.size() + ")");

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentData.removeEventListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (feedModel.getUserKey().equals(firebaseAuth.getCurrentUser().getUid())) {
            getMenuInflater().inflate(R.menu.my_feed_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.other_feed_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_remove:
                if (feedModel.getUserKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                    reference.removeValue();

                    Intent intent = new Intent(SayDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                break;
            case R.id.bt_report:
                break;

        }
        return true;
    }
}
