package com.eng.elfarsisy.blog.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.eng.elfarsisy.blog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.post_detail_photo)
    ImageView postDetailPhoto;
    @BindView(R.id.post_detail_title)
    TextView postDetailTitle;
    @BindView(R.id.post_detail_date)
    TextView postDetailDate;
    @BindView(R.id.post_detail_description)
    TextView postDetailDescription;
    @BindView(R.id.post_detail_profile_photo)
    ImageView postDetailProfilePhoto;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.post_detail_add)
    Button postDetailAdd;
    @BindView(R.id.Post_detail_publisherphoto)
    ImageView PostDetailPublisherphoto;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    Uri currentphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        String postphto = getIntent().getExtras().getString("postphoto");
        Glide.with(this).load(postphto).into(postDetailPhoto);

        String publisherPhoto = getIntent().getExtras().getString("publisherPhoto");

        Glide.with(this).load(publisherPhoto).into(PostDetailPublisherphoto);

        String title = getIntent().getExtras().getString("title");
        postDetailTitle.setText(title);
        String description = getIntent().getExtras().getString("description");
        postDetailDescription.setText(description);
        String date = parseTimeStamp(getIntent().getExtras().getLong("postDate"));
        postDetailDate.setText(date);
        currentphoto = currentUser.getPhotoUrl() ;

        Glide.with(this).load(currentphoto).into(postDetailProfilePhoto);


    }


    private String parseTimeStamp(long postDate) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(postDate);
        String date = DateFormat.format("dd-MM-yyy", calendar).toString();
        return date;
    }

    @OnClick(R.id.post_detail_add)
    public void onViewClicked() {
    }


}
