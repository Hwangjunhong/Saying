package com.project.hong.saying.Upload.SettingPackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.hong.saying.R;

/**
 * Created by hong on 2018-04-19.
 */

public class ImageFragment extends Fragment implements View.OnClickListener {

    ButtonClick buttonClick;
    RelativeLayout cancel, complete;
    LinearLayout search, gallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_image, container, false);

        initView(view);

        return view;
    }

    private void initView(View view){

        cancel = view.findViewById(R.id.cancel);
        complete = view.findViewById(R.id.complete);
        search = view.findViewById(R.id.search_image);
        gallery = view.findViewById(R.id.select_gallery);

        cancel.setOnClickListener(this);
        complete.setOnClickListener(this);
        search.setOnClickListener(this);
        gallery.setOnClickListener(this);

    }
    public void setButtonClick(com.project.hong.saying.Upload.SettingPackage.ButtonClick buttonClick) {
        this.buttonClick = buttonClick;
    }

    @Override
    public void onClick(View v) {
        if(buttonClick !=null){
            buttonClick.onClickEvent(v);
        }
    }
}
