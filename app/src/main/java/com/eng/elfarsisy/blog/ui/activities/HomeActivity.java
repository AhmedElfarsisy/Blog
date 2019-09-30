package com.eng.elfarsisy.blog.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.eng.elfarsisy.blog.R;
import com.eng.elfarsisy.blog.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    ImageView popProfileImage;
    EditText titletxt;
    EditText descriptiontxt;
    ImageView popPhoto;
    ImageView popAddbtn;
    ProgressBar popProgressBar;
    private AppBarConfiguration mAppBarConfiguration;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private Dialog popAddPost;


    static int perReqCode = 2;
    static int REQCode = 2;
    Uri pickedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        initialPop();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNavBar();


    }


    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(HomeActivity.this, "Agree to the permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, perReqCode);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, REQCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQCode && data != null) {

            pickedImageUri = data.getData();
            popPhoto.setImageURI(pickedImageUri);
        }


    }

    private void initialPop() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.pop_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
        popAddbtn = popAddPost.findViewById(R.id.popAddbtn);
        popProfileImage = popAddPost.findViewById(R.id.pop_profile_image);
        popPhoto = popAddPost.findViewById(R.id.pop_photo);
        titletxt = popAddPost.findViewById(R.id.titletxt);
        descriptiontxt = popAddPost.findViewById(R.id.descriptiontxt);
        popProgressBar = popAddPost.findViewById(R.id.pop_progressBar);
        popPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        checkAndRequestForPermission();

            }
        });
        Glide.with(HomeActivity.this).load(currentUser.getPhotoUrl()).into(popProfileImage);

        popAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddbtn.setVisibility(View.INVISIBLE);
                popProgressBar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(titletxt.getText())
                        && !TextUtils.isEmpty(descriptiontxt.getText())
                        && pickedImageUri != null) {
                    popAddbtn.setVisibility(View.INVISIBLE);
                    popProgressBar.setVisibility(View.VISIBLE);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog-Image");
                    StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageDownloadLink = uri.toString();
                                            Post post = new Post(titletxt.getText().toString(), descriptiontxt.getText().toString()
                                                    , imageDownloadLink, currentUser.getUid()
                                                    , currentUser.getPhotoUrl().toString());
//                                          add post to database
                                            addPost(post);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            popAddbtn.setVisibility(View.VISIBLE);
                                            popProgressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });


                } else {
                    popAddbtn.setVisibility(View.VISIBLE);
                    popProgressBar.setVisibility(View.INVISIBLE);
                }


            }
        });

    }

    private void addPost(Post post) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("posts").push();
////        get post uniqe id & update post key
        String key = myReference.getKey();
        post.setPostKey(key);
        myReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                popAddbtn.setVisibility(View.VISIBLE);
                popProgressBar.setVisibility(View.INVISIBLE);
                popAddPost.dismiss();

            }
        });

    }

    private void updateNavBar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navView = navigationView.getHeaderView(0);
        ImageView profileImage = navView.findViewById(R.id.nav_imageView);
        TextView username = navView.findViewById(R.id.nav_username);
        TextView email = navView.findViewById(R.id.nav_email);
        username.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profileImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void LogOut(MenuItem item) {
        firebaseAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LogIn.class));
        finish();


    }


}
