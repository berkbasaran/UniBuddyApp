package com.tau.unibuddyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private Button UpdatePostButton;
    private EditText PostDescription;

    private String Description;

    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, current_user_id;
    private long countPosts = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        PostDescription =(EditText) findViewById(R.id.post_description);
        loadingBar = new ProgressDialog(this);


        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidatePostInfo();
            }
        });
    }



    private void ValidatePostInfo()
    {
        Description = PostDescription.getText().toString();

        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "You need to write information about you Study Partner search!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Adding New Post");
            loadingBar.setMessage("Please wait, while we are updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            SavingPostInformationToDatabase();
        }
    }


    private void SavingPostInformationToDatabase()
    {

        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    countPosts = dataSnapshot.getChildrenCount();
                }
                else {
                    countPosts = 0;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    final String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    /*final String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();*/

                    HashMap postsMap = new HashMap();
                        postsMap.put("uid", current_user_id);
                        postsMap.put("date", saveCurrentDate);
                        postsMap.put("time", saveCurrentTime);
                        postsMap.put("description", Description);
                        /*postsMap.put("profileimage", userProfileImage);*/
                        postsMap.put("fullname", userFullName);
                        postsMap.put("counter", countPosts);
                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        SendUserToMainActivity();
                                        Toast.makeText(PostActivity.this, "Your new post is updated successfully!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(PostActivity.this, "Error occured while updating your post!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            SendUserToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }



    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}