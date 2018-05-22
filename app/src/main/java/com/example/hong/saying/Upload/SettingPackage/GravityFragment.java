package com.example.hong.saying.Upload.SettingPackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hong.saying.R;

/**
 * Created by hong on 2018-04-19.
 */

public class GravityFragment extends Fragment implements View.OnClickListener {
    ButtonClick buttonClick;
    RelativeLayout cancel, complete;
    LinearLayout gravityRight, gravityLeft, gravityCenter, gravityRightBottom, getGravityLeftBottom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gravity, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        cancel = view.findViewById(R.id.cancel);
        complete = view.findViewById(R.id.complete);
        gravityCenter = view.findViewById(R.id.gravity_center);
        gravityLeft = view.findViewById(R.id.gravity_left);
        gravityRight = view.findViewById(R.id.gravity_right);
        gravityRightBottom = view.findViewById(R.id.gravity_right_bottom);
        getGravityLeftBottom = view.findViewById(R.id.gravity_left_bottom);

        cancel.setOnClickListener(this);
        complete.setOnClickListener(this);
        gravityCenter.setOnClickListener(this);
        gravityLeft.setOnClickListener(this);
        gravityRight.setOnClickListener(this);
        gravityRightBottom.setOnClickListener(this);
        getGravityLeftBottom.setOnClickListener(this);

    }


    public void setOnButtonClick(ButtonClick buttonClick) {
        this.buttonClick = buttonClick;
    }

    @Override
    public void onClick(View v) {
        if(buttonClick != null){
            buttonClick.onClickEvent(v);
        }
    }
}
