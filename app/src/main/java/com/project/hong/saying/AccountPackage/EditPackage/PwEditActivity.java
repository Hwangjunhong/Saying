package com.project.hong.saying.AccountPackage.EditPackage;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.R;
import com.project.hong.saying.Util.LoadingProgress;
import com.project.hong.saying.Util.SharedPreference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class PwEditActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<Void> {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("user");
    private String uid, pwd, originPwd, changePwd, changeOkPwd, originCheckPwd;
    private EditText originPw, changePw, changeOkPw;
    private TextView completeBt;
    private SharedPreference sharedPreference = new SharedPreference();
    private UserModel userModel = new UserModel();
    private RelativeLayout backBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_edit);

        initView();
    }

    private void initView() {
        originPw = findViewById(R.id.edit_origin_pw);
        changePw = findViewById(R.id.edit_change_pw);
        changeOkPw = findViewById(R.id.edit_change_pw_ok);
        completeBt = findViewById(R.id.complete_bt);
        backBt = findViewById(R.id.back_bt);

        completeBt.setOnClickListener(this);
        backBt.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_bt:

                originPwd = sharedPreference.getValue(this, "userPwd", "");
                originCheckPwd = originPw.getText().toString();
                changePwd = changePw.getText().toString();
                changeOkPwd = changeOkPw.getText().toString();

                if (!originCheckPwd.trim().isEmpty() && !changePwd.trim().isEmpty() && !changeOkPwd.trim().isEmpty()) {

                    if (originCheckPwd.equals(originPwd)) {
                        if (changePwd.equals(changeOkPwd)) {
                            LoadingProgress.showDialog(this, true);
                            changePassword();
                        } else {
                            Toast.makeText(this, "변경할 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "기존의 비밀번호와 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.back_bt:
                finish();
                break;
        }

    }


    private void changePassword() {
        dataUpload(changePwd);
    }

    private void dataUpload(final String password) {
        uid = auth.getCurrentUser().getUid();
        Map<String, Object> child = new HashMap<>();
        if (!TextUtils.isEmpty(password)) {
            child.put("password", password);
        }

        reference.child(uid).updateChildren(child).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        LoadingProgress.dismissDialog();
        if (task.isSuccessful()) {
            sharedPreference.put(this, "userPwd", changePwd);
            firebaseUpdatePassword(changePwd);

        } else {
            Toast.makeText(this, "회원 정보 수정 실패", Toast.LENGTH_SHORT).show();
        }
    }


    private void firebaseUpdatePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PwEditActivity.this, "비밀번호가 성공적으로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });
    }


}

