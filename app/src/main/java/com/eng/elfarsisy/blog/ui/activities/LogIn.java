package com.eng.elfarsisy.blog.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.elfarsisy.blog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogIn extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.logInEmail)
    EditText logInEmail;
    @BindView(R.id.logInPassword)
    EditText logInPassword;
    @BindView(R.id.logInButton)
    Button logInButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        logInButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.imageView, R.id.logInButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                startActivity(new Intent(LogIn.this, MainActivity.class));
                break;
            case R.id.logInButton:
                logInButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String email = logInEmail.getText().toString().trim();
                String password = logInPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    signInUser(email, password);

                }

                break;
        }
    }

    private void signInUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logInButton.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    //go to mainactivity
                    startActivity(new Intent(LogIn.this, HomeHomeActivity.class));
                } else {
                    Toast.makeText(LogIn.this,
                            "fail"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            logInButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
