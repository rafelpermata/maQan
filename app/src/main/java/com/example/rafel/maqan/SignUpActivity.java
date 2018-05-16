package com.example.rafel.maqan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword, edtName;
    private Button btnSignUp, btnToLogin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = (Button)findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(this);
        btnToLogin = (Button)findViewById(R.id.btn_to_login);
        btnToLogin.setOnClickListener(this);

        edtName = (EditText)findViewById(R.id.edt_name);
        edtUsername = (EditText)findViewById(R.id.edt_username2);
        edtPassword = (EditText)findViewById(R.id.edt_password2);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup :
                String name = edtName.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                signUp(username,password, name);
                break;
            case R.id.btn_to_login :
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void signUp(final String username, final String password, final String name) {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter e-mail address!", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this,"E-mail is not valid",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        FirebaseUser user= firebaseAuth.getCurrentUser();
                        String userId = user.getUid();
                        DatabaseReference ref = database.getReference().child("users").child(userId);
                        ref.child("Name").setValue(name);
                        ref.child("E-mail").setValue(username);

                        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
