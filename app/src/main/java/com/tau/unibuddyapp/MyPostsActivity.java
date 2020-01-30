package com.tau.unibuddyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView myPostsList;

    private FirebaseAuth mAuth;
    private DatabaseReference PostsRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        mToolbar = (Toolbar) findViewById(R.id.my_posts_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Posts");

        myPostsList = (RecyclerView) findViewById(R.id.my_all_posts_list);
        myPostsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostsList.setLayoutManager(linearLayoutManager);

        DisplayMyAllPosts();

    }



    private void DisplayMyAllPosts() {

        Query MyPostsQuery = PostsRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID + "\uf8ff");


        FirebaseRecyclerAdapter<Posts, MyPostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, MyPostsViewHolder>
                (
                        Posts.class,
                        R.layout.all_posts_layout,
                        MyPostsViewHolder.class,
                        MyPostsQuery
                )
        {
            @Override
            protected void populateViewHolder(MyPostsViewHolder viewHolder, Posts model, int position) {

                final String PostKey = getRef(position).getKey();
                viewHolder.setFullname(model.getFullname());
                viewHolder.setTime(model.getTime());
                viewHolder.setDate(model.getDate());
                viewHolder.setDescription(model.getDescription());
                /*viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());*/

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent = new Intent(MyPostsActivity.this, ClickPostActivity.class);
                        clickPostIntent.putExtra("PostKey", PostKey);
                        startActivity(clickPostIntent);
                    }
                });

                viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent commentsIntent = new Intent(MyPostsActivity.this, CommentsActivity.class);
                        commentsIntent.putExtra("PostKey", PostKey);
                        startActivity(commentsIntent);

                    }
                });



            }
        };

        myPostsList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MyPostsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton CommentPostButton;

        String currentUserId;


        public MyPostsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        /*public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.with(ctx).load(profileimage).into(image);
        }*/
        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }
    }
}
