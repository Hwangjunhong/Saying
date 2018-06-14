package com.project.hong.saying;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.project.hong.saying.Util.ProgressGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText editEmail, editPw, editName;
    ActionProcessButton signUpBt;
    TextInputLayout edit1, edit2, edit3;
    ProgressGenerator progressGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();


    }

    private void initView() {
        editEmail = findViewById(R.id.edit_email);
        editPw = findViewById(R.id.edit_pw);
        signUpBt = findViewById(R.id.sign_up_bt);

        signUpBt.setOnClickListener(this);
        progressGenerator = new ProgressGenerator(this);
    }


    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


//                            UserModel userModel = new UserModel();
//                            SharedPreference sharedPreference = new SharedPreference();

//                            String name = editName.getText().toString();
//                            sharedPreference.getValue(SignUpActivity.this, "name", name);

//                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();


                        } else {

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_up_bt:

                Bundle extras = getIntent().getExtras();
                if (extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
                    signUpBt.setMode(ActionProcessButton.Mode.ENDLESS);
                } else {
                    signUpBt.setMode(ActionProcessButton.Mode.PROGRESS);
                }
                createUser(editEmail.getText().toString(), editPw.getText().toString());
                progressGenerator.start(signUpBt);
                signUpBt.setEnabled(false);
                editEmail.setEnabled(false);
                editPw.setEnabled(false);

                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "회원가입이 완료되었습니다 ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
