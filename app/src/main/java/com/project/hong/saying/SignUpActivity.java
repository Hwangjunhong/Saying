package com.project.hong.saying;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.hong.saying.DataBase.DataCallback;
import com.project.hong.saying.DataBase.FirebaseData;
import com.project.hong.saying.DataModel.UserModel;
import com.project.hong.saying.Util.LoadingProgress;
import com.project.hong.saying.Util.SharedPreference;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DataCallback {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MaterialEditText editEmail, editPw, editName, editPwOk;
    Button signUpBt;
    RelativeLayout BackBt;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();


    }

    private void initView() {
        editEmail = findViewById(R.id.edit_email);
        editPw = findViewById(R.id.edit_pw);
        editPwOk = findViewById(R.id.edit_pw_ok);
        editName = findViewById(R.id.edit_name);
        signUpBt = findViewById(R.id.sign_up_bt);
        BackBt = findViewById(R.id.back_bt);

        signUpBt.setOnClickListener(this);
        BackBt.setOnClickListener(this);

    }


    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            UserModel userModel = new UserModel();
                            FirebaseData firebaseData = new FirebaseData();
                            userModel.setName(editName.getText().toString());
                            userModel.setPassword(editPw.getText().toString());

                            firebaseData.setDataCallback(SignUpActivity.this);
                            firebaseData.userDataUpload(mAuth.getCurrentUser().getUid(), userModel);

                            sharedPreference.put(SignUpActivity.this, "userName", editName.getText().toString());

//                            sharedPreference.put(SignUpActivity.this, "userEmail", editEmail.getText().toString());
//                            sharedPreference.put(SignUpActivity.this, "userPwd", editPw.getText().toString());


                        } else {
                            LoadingProgress.dismissDialog();
                            Toast.makeText(SignUpActivity.this, "중복 이메일이 존재합니다", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_up_bt:

                if (!editEmail.getText().toString().trim().isEmpty() && !editPw.getText().toString().trim().isEmpty()
                        && !editName.getText().toString().trim().isEmpty() && !editPwOk.getText().toString().trim().isEmpty()) {

                    if(editPw.getText().length() > 7 && editPwOk.getText().length() > 7){
                        if (editPw.getText().toString().equals(editPwOk.getText().toString())) {
                            LoadingProgress.showDialog(this);
                            createUser(editEmail.getText().toString(), editPw.getText().toString());

                        } else {
                            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "비밀번호는 8자 이상 입력해 주세요", Toast.LENGTH_SHORT).show();
                    }


                } else if (editEmail.getText().toString().trim().isEmpty() || editName.getText().toString().trim().isEmpty()
                        || editPw.getText().toString().trim().isEmpty() || editPwOk.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "정보를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }

                break;


            case R.id.back_bt:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }


    }


    @Override
    public void completeUpload(Boolean isSuccess) {
        if (isSuccess) {
            LoadingProgress.dismissDialog();
            Toast.makeText(this, "회원가입이 완료되었습니다 ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
