package com.eng.elfarsisy.blog.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eng.elfarsisy.blog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {
    FirebaseAuth firebaseAuth;


    static int perReqCode = 1;
    static int REQCode = 1;
    Uri pickedImageUri;


    @BindView(R.id.addphoto_imageV)
    ImageView addphotoImageV;
    @BindView(R.id.regName)
    EditText regName;
    @BindView(R.id.regEmail)
    EditText regEmail;
    @BindView(R.id.regPassword1)
    EditText regPassword1;
    @BindView(R.id.regPassword2)
    EditText regPassword2;
    @BindView(R.id.regButton)
    Button regButton;
    @BindView(R.id.regprogressBar)
    ProgressBar regprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        regprogressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.addphoto_imageV, R.id.regButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addphoto_imageV:
// on click image view
                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }

                break;
            case R.id.regButton:
// on click  register button
                regButton.setVisibility(View.INVISIBLE);
                regprogressBar.setVisibility(View.VISIBLE);
                String name = regName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String password1 = regPassword1.getText().toString();
                String password2 = regPassword2.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password1) && pickedImageUri!= null&& TextUtils.equals(password1, password2)) {
                    createNewUser(name, email, password1);

                } else {

                    regButton.setVisibility(View.VISIBLE);
                   regprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, "Verfiy your info", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void createNewUser(String name, String email, String password1) {
        firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    updateUserInfo(name, pickedImageUri, firebaseAuth.getCurrentUser());

                } else {
                    Toast.makeText(Register.this, "faild:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    regprogressBar.setVisibility(View.INVISIBLE);
                    regButton.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void updateUserInfo(String name, Uri pickedImageUri, FirebaseUser currentUser) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users_photos");
        StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "thanks its complet", Toast.LENGTH_SHORT).show();
                                    updatUI();
                                }
                            }
                        });

                    }
                });
            }
        });

    }

    private void updatUI() {
        startActivity(new Intent(Register.this, HomeActivity.class));
        finish();

    }

    private void openGallery() {
        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, REQCode);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Register.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Register.this, "Agree to the permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Register.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, perReqCode);
            }
        } else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQCode && data != null) {

            pickedImageUri = data.getData();
            addphotoImageV.setImageURI(pickedImageUri);
        }


    }
}
