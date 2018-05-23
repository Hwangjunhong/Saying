package com.example.hong.saying.Upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.hong.saying.DataBase.DataCallback;
import com.example.hong.saying.DataBase.FileCallback;
import com.example.hong.saying.DataBase.FireBaseFile;
import com.example.hong.saying.DataBase.FirebaseData;
import com.example.hong.saying.DataModel.FeedModel;
import com.example.hong.saying.R;
import com.example.hong.saying.SearchActivity;
import com.example.hong.saying.Upload.SettingPackage.ButtonClick;
import com.example.hong.saying.Upload.SettingPackage.ColorCallback;
import com.example.hong.saying.Upload.SettingPackage.ColorPickerFragment;
import com.example.hong.saying.Upload.SettingPackage.GravityFragment;
import com.example.hong.saying.Upload.SettingPackage.ImageFragment;
import com.example.hong.saying.Util.ApiService;
import com.example.hong.saying.Util.ColorPicker;
import com.example.hong.saying.Util.Hit;
import com.example.hong.saying.Util.LoadingProgress;
import com.example.hong.saying.Util.PixabayImage;
import com.example.hong.saying.Util.RealPathUtil;
import com.example.hong.saying.Util.RetrofitCall;
import com.example.hong.saying.Util.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener, ColorCallback, ButtonClick, Callback<PixabayImage>, RequestListener<Bitmap>, DataCallback, FileCallback {

    ColorPickerFragment pickerFragment = new ColorPickerFragment();
    ImageFragment imageFragment = new ImageFragment();
    GravityFragment gravityFragment = new GravityFragment();

    FrameLayout textColor, searchImage, textLocation;
    LinearLayout bottomSheet;
    RelativeLayout backBtn;

    TextView complete;
    ImageView image;
    EditText write;

    FragmentManager fragmentManager;
    BottomSheetBehavior behavior;

    int firstGravity = Gravity.CENTER;
    int secondGravity = Gravity.CENTER;
    int firstColor = Color.WHITE;
    int secondColor;
    boolean isOpen = false;
    boolean isGallery = false;
    String[] keywordArr;
    List<Hit> hitList = new ArrayList<>();
    int position = 0;
    String galleryUri;
    String imageUrl;

    File pixabayFile;

    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        keywordArr = getResources().getStringArray(R.array.pixabay_keyword);
        getPixabayImage(keywordArr[randomNumber(keywordArr.length)]);

        fragmentManager = getSupportFragmentManager();

        initView();
        pickerFragment.setColorCallback(this);
        pickerFragment.setButtonClick(this);
        imageFragment.setButtonClick(this);
        gravityFragment.setOnButtonClick(this);

    }

    private void initView() {
        bottomSheet = findViewById(R.id.bottom_sheet);
        textColor = findViewById(R.id.text_color);
        searchImage = findViewById(R.id.search_image);
        textLocation = findViewById(R.id.text_location);
        backBtn = findViewById(R.id.back_bt);
        complete = findViewById(R.id.complete);
        image = findViewById(R.id.image);
        write = findViewById(R.id.write);

        textColor.setOnClickListener(this);
        searchImage.setOnClickListener(this);
        textLocation.setOnClickListener(this);
        write.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        complete.setOnClickListener(this);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);


    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        isOpen = true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_color:
                position = 0;
                openFragment(pickerFragment);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.search_image:
                position = 1;
                openFragment(imageFragment);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.text_location:
                openFragment(gravityFragment);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                position = 2;
                break;
            case R.id.back_bt:
                finish();
                break;
            case R.id.complete:
                LoadingProgress.showDialog(this, true);
                uploadFile();
                break;

        }
    }

    private void uploadData() {
        SharedPreference sharedPreference = new SharedPreference();
        String userName = sharedPreference.getValue(this, "userName", "");
        String profileUrl = sharedPreference.getValue(this, "profileUrl", "");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userKey = firebaseAuth.getCurrentUser().getUid();


        String color = Integer.toHexString(firstColor);
        FeedModel feedModel = new FeedModel(imageUrl, userName, profileUrl, firstGravity, color, write.getText().toString(), System.currentTimeMillis(), userKey);
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.setDataCallback(this);
        firebaseData.FeedDataUpload(feedModel);

    }

    private void uploadFile() {
        FireBaseFile fireBaseFile = new FireBaseFile(this);
        if (!isGallery) {
            fireBaseFile.fileUpload(pixabayFile);
        } else {
            File file = new File(galleryUri);
            fireBaseFile.fileUpload(file);
        }


    }

    @Override
    public void getTextColor(int color) {
        secondColor = color;
        write.setTextColor(secondColor);
        write.setHintTextColor(secondColor);
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (position == 0) {
                    write.setTextColor(firstColor);
                    write.setHintTextColor(firstColor);
                } else if (position == 2) {
                    setGravity(firstGravity);
                }
                onBackPressed();
                break;

            case R.id.complete:
                if (position == 0) {
                    firstColor = secondColor;
                } else if (position == 2) {
                    firstGravity = secondGravity;
                }
                onBackPressed();
                break;

            case R.id.search_image:
                isGallery = false;
                Intent intent2 = new Intent(this, SearchActivity.class);
                startActivityForResult(intent2, 200);
                break;

            case R.id.select_gallery:
                isGallery = true;
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);

                break;

            case R.id.gravity_center:
                setGravity(Gravity.CENTER);
                break;

            case R.id.gravity_left:
                setGravity(Gravity.START);
                break;

            case R.id.gravity_right:
                setGravity(Gravity.END);
                break;

            case R.id.gravity_left_bottom:
                setGravity(Gravity.START | Gravity.BOTTOM);
                break;

            case R.id.gravity_right_bottom:
                setGravity(Gravity.END | Gravity.BOTTOM);
                break;


        }
    }

    private void setGravity(int gravity) {
        secondGravity = gravity;

        switch (gravity) {
            case Gravity.CENTER:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                write.setGravity(gravity);
                write.setLayoutParams(layoutParams);
                break;

            case Gravity.START:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                write.setGravity(gravity);
                write.setLayoutParams(layoutParams);
                break;

            case Gravity.END:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                write.setGravity(gravity);
                write.setLayoutParams(layoutParams);
                break;

            case Gravity.START | Gravity.BOTTOM:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                write.setGravity(gravity);
                write.setLayoutParams(layoutParams);
                break;

            case Gravity.END | Gravity.BOTTOM:
                layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                write.setGravity(gravity);
                write.setLayoutParams(layoutParams);
                break;


        }
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            isOpen = false;
        } else {
            super.onBackPressed();

        }
    }

    private void getPixabayImage(String keyword) {
        Call<PixabayImage> getImage = RetrofitCall.retrofit()
                .getRandomImage(ApiService.APP_KEY, keyword, "popular", "photo");

        getImage.enqueue(this);

    }

    public int randomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    @Override
    public void onResponse(Call<PixabayImage> call, Response<PixabayImage> response) {
        if (response.isSuccessful()) {
            PixabayImage pixabayImage = response.body();
            hitList = pixabayImage.getHits();
            Hit hit = hitList.get(randomNumber(hitList.size()));
            imageUrl = hit.getWebformatURL();
            setBackgroundImage(hit.getWebformatURL(), null, false);

        }
    }

    @Override
    public void onFailure(Call<PixabayImage> call, Throwable t) {

    }


    private void setBackgroundImage(String path, Uri uri, boolean isUri) {
        if (!this.isDestroyed()) {
            RequestManager requestManager = Glide.with(this);

            if (!isUri) {
                requestManager.asBitmap().load(path)
                        .thumbnail(0.1f).listener(this).transition(GenericTransitionOptions.with(R.anim.alpha_anim)).into(image);
                getPixabayFile(path);
            } else {
                requestManager.asBitmap().load(uri)
                        .thumbnail(0.1f).listener(this).transition(GenericTransitionOptions.with(R.anim.alpha_anim)).into(image);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null) {
                    isGallery = true;
                    Uri uri = data.getData();
                    galleryUri = RealPathUtil.getRealPath(this, uri);
                    setBackgroundImage("", uri, true);
                    onBackPressed();
                }
                break;

            case 200:
                if (data != null) {
                    isGallery = false;
                    String path = data.getStringExtra("path");
                    imageUrl = path;
                    Glide.with(this).asBitmap()
                            .load(path)
                            .thumbnail(0.1f)
                            .listener(this).transition(GenericTransitionOptions.with(R.anim.alpha_anim))
                            .into(image);

                    getPixabayFile(path);

                    onBackPressed();
                }
                break;

        }

    }

    private void getPixabayFile(String url) {
        Glide.with(this).downloadOnly().load(url)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        pixabayFile = resource;
                    }
                });

    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        boolean isDark = ColorPicker.isDark(resource);
        if (isDark) {
            firstColor = Color.BLACK;
        }
        write.setTextColor(firstColor);
        write.setHintTextColor(firstColor);
        return false;
    }


    @Override
    public void completeUpload(Boolean isSuccess) {
        if (isSuccess) {
            LoadingProgress.dismissDialog();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "데이터 업로드 실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void completeFileUpload(String url) {
        if (!TextUtils.isEmpty(url)) {
//            imageUrl = url;
            uploadData();
        } else {
            Toast.makeText(this, "데이터 업로드 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
