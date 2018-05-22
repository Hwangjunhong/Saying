package com.example.hong.saying;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hong.saying.Util.ApiService;
import com.example.hong.saying.Util.GridSpacingItemDecoration;
import com.example.hong.saying.Util.Hit;
import com.example.hong.saying.Util.PaginationScrollListener;
import com.example.hong.saying.Util.PixabayImage;
import com.example.hong.saying.Util.RetrofitCall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Callback<PixabayImage>, View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

    RelativeLayout backBt, resetBt;
    EditText searchEdit;
    RecyclerView recyclerView;
    SearchAdapter adapter;
    List<Hit> images = new ArrayList<>();
    int maxPageCount = 0;
    int currentPage = 1;
    boolean isLoading = false;
    String keyword;

    Call<PixabayImage> getFirstImage;
    Call<PixabayImage> getMoreImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

    }

    private void initView() {
        backBt = findViewById(R.id.back_bt);
        resetBt = findViewById(R.id.reset_bt);
        searchEdit = findViewById(R.id.search_edit);
        recyclerView = findViewById(R.id.recycler);

        backBt.setOnClickListener(this);
        resetBt.setOnClickListener(this);
        searchEdit.addTextChangedListener(this);
        searchEdit.setOnEditorActionListener(this);
        setRecyclerView();

    }

    private void setRecyclerView() {
        adapter = new SearchAdapter(images, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 1, false));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                    isLoading = true;
                    currentPage++;
                    getPixabayMoreImage(keyword, currentPage);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return maxPageCount == currentPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });
    }

    private void getPixabayImage(String keyword) {

        getFirstImage = RetrofitCall.retrofit()
                .getRandomImage(ApiService.APP_KEY, keyword, "popular", "photo");

        getFirstImage.enqueue(this);

    }

    private void getPixabayMoreImage(String keyword, int currentPage) {

        getMoreImage = RetrofitCall.retrofit()
                .getMoreImage(ApiService.APP_KEY, keyword, "popular", "photo", currentPage);

        getMoreImage.enqueue(this);

    }


    @Override
    public void onResponse(Call<PixabayImage> call, Response<PixabayImage> response) {
        if (response.isSuccessful()) {
            PixabayImage pixabayImage = response.body();
                if (pixabayImage.getHits() != null && pixabayImage.getHits().size() > 0) {
                    maxPageCount = pixabayImage.getTotalHits() / pixabayImage.getHits().size();
                } else {
                    Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            isLoading = false;
            if (maxPageCount == 0) {
                maxPageCount = 1;
            }

            if (pixabayImage != null) {
                notifyImage(pixabayImage);
            }
        }
    }


    @Override
    public void onFailure(Call<PixabayImage> call, Throwable t) {
    }

    private void notifyImage(PixabayImage image) {
        if (currentPage == 1) {
            images.clear();
        }
        images.addAll(image.getHits());
        adapter.notifyItemRangeChanged(0, images.size());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_bt:
                finish();
                break;

            case R.id.reset_bt:
                searchEdit.setText("");
                resetBt.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            resetBt.setVisibility(View.VISIBLE);
        } else {
            resetBt.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("ServiceCast")
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                keyword = searchEdit.getText().toString().trim();
                currentPage = 1;
                if (!isLoading) {
                    getPixabayImage(keyword);
                }
                ((InputMethodManager) getApplicationContext()
                        .getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
                break;

        }

        return false;
    }
}
