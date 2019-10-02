package com.eng.elfarsisy.blog.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eng.elfarsisy.blog.R;
import com.eng.elfarsisy.blog.adapters.CommentAdapter;
import com.eng.elfarsisy.blog.data.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    @BindView(R.id.post_detail_commenttxt)
    EditText post_detail_commenttxt;
    @BindView(R.id.post_detail_add)
    Button postDetailAdd;
    @BindView(R.id.Post_detail_publisherphoto)
    ImageView PostDetailPublisherphoto;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    Uri currentphoto;
    FirebaseDatabase firebaseDatabase;
    String postkey;
    RecyclerView commentrecycler;
    CommentAdapter commentAdapter;
    List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        commentrecycler = findViewById(R.id.commntRecycler);

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
        currentphoto = currentUser.getPhotoUrl();
        postkey = getIntent().getExtras().getString("postKey");

        Glide.with(this).load(currentphoto).into(postDetailProfilePhoto);

        initalcomment();


    }

    private void initalcomment() {

        commentrecycler.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentReference = firebaseDatabase.getReference("comment").child(postkey);
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();

                for (DataSnapshot Snap : dataSnapshot.getChildren()) {
                    Comment comment = Snap.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
                commentrecycler.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private String parseTimeStamp(long postDate) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(postDate);
        String date = DateFormat.format("dd-MM-yyy", calendar).toString();


        return date;
    }

    @OnClick(R.id.post_detail_add)
    public void onViewClicked() {
        postDetailAdd.setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("comment").child(postkey).push();
        String commentTxt = post_detail_commenttxt.getText().toString();
        String userId = currentUser.getUid();
        String userImage = currentUser.getPhotoUrl().toString();
        String username = currentUser.getDisplayName();

        Comment comment = new Comment(commentTxt, userId, username, userImage);
        databaseReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                postDetailAdd.setVisibility(View.VISIBLE);
                post_detail_commenttxt.setText("");
                Toast.makeText(PostDetailActivity.this, "comment added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postDetailAdd.setVisibility(View.VISIBLE);
                Toast.makeText(PostDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
