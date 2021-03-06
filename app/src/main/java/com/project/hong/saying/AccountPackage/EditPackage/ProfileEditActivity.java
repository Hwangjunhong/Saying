package com.project.hong.saying.AccountPackage.EditPackage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hong.saying.DataBase.FileCallback;
import com.project.hong.saying.DataBase.FireBaseFile;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.LoadingProgress;
import com.project.hong.saying.Util.RealPathUtil;
import com.project.hong.saying.Util.SharedPreference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, FileCallback, OnCompleteListener<Void> {


    CircleImageView profileImage;
    EditText userNameEdit;
    TextView completeBt;
    RelativeLayout backBt;
    FireBaseFile fireBaseFile = new FireBaseFile(this);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("user");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid, userName, profileUrl;
    Uri uri;
    boolean isProfileChange = false;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getMyInfo();
        initView();
    }

    private void initView() {
        profileImage = findViewById(R.id.profile_image);
        backBt = findViewById(R.id.back_bt);
        userNameEdit = findViewById(R.id.user_name);
        completeBt = findViewById(R.id.complete_bt);

        userNameEdit.addTextChangedListener(this);
        completeBt.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        backBt.setOnClickListener(this);

        userNameEdit.setText(userName);
        Glide.with(this).load(profileUrl).apply(new RequestOptions().placeholder(R.drawable.user1).error(R.drawable.user1)).into(profileImage);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 1) {
            completeBt.setEnabled(true);
        } else {
            completeBt.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                if (checkPermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 200);

                }
                break;

            case R.id.complete_bt:
                LoadingProgress.showDialog(this, true);
                userName = userNameEdit.getText().toString().replace(" ", "");
                if (!isProfileChange) {
                    dataUpload(userName, profileUrl);
                } else {
                    startUpload();
                }
                break;

            case R.id.back_bt:
                finish();
                break;

        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 900);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
            Glide.with(this).load(uri).into(profileImage);
            isProfileChange = true;
        }

    }

    private void getMyInfo() {
        userName = sharedPreference.getValue(this, "userName", "");
        profileUrl = sharedPreference.getValue(this, "profileUrl", "");

    }

    private void startUpload() {
        fireBaseFile.fileUpload("profileImage", RealPathUtil.getRealPath(this, uri), this);
    }

    private void dataUpload(final String name, final String profileUrl) {
        uid = auth.getCurrentUser().getUid();
        Map<String, Object> child = new HashMap<>();
        child.put("name", name);
        if (!TextUtils.isEmpty(profileUrl)) {
            child.put("profileUrl", profileUrl);
        }
        reference.child(uid).updateChildren(child).addOnCompleteListener(this);
    }

    @Override
    public void completeFileUpload(String url) {
        profileUrl = url;
        dataUpload(userName, url);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        LoadingProgress.dismissDialog();
        if (task.isSuccessful()) {
            sharedPreference.put(this, "userName", userName);
            sharedPreference.put(this, "profileUrl", profileUrl);
            finish();
        } else {
            Toast.makeText(this, "회원 정보 수정 실패", Toast.LENGTH_SHORT).show();
            isProfileChange = false;
        }
    }


}
