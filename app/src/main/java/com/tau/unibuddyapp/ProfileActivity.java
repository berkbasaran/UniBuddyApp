package com.tau.unibuddyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userProfName, userStatus, userCity, userPhoneNumber, userUniversity,userUniversityProgram, userUniversityYear, userAdress;
    /*private CircleImageView userProfileImage;*/

    private DatabaseReference profileUserRef, PostsRef;
    private FirebaseAuth mAuth;
    private Button MyPosts;

    private String currentUserId;
    private int countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        userProfName = (TextView) findViewById(R.id.my_profile_full_name);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userCity = (TextView) findViewById(R.id.my_profile_city);
        userPhoneNumber = (TextView) findViewById(R.id.my_profile_phone_number);
        userUniversity = (TextView) findViewById(R.id.my_profile_university);
        userUniversityProgram = (TextView) findViewById(R.id.my_profile_uniprogram);
        userUniversityYear = (TextView) findViewById(R.id.my_profile_uniyear);
        userAdress = (TextView) findViewById(R.id.my_profile_adress);
        /*userProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);*/
        MyPosts = (Button) findViewById(R.id.my_post_button);


        MyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMyPostsActivity();
            }
        });


        PostsRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    countPosts = (int) dataSnapshot.getChildrenCount();
                    MyPosts.setText(Integer.toString(countPosts) + "  Posts");

                }
                else {
                    MyPosts.setText("0 Posts");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    /*String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();*/
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myCity = dataSnapshot.child("city").getValue().toString();
                    String myPhoneNumber = dataSnapshot.child("phonenumber").getValue().toString();
                    String myUniversity = dataSnapshot.child("university").getValue().toString();
                    String myUniProgram = dataSnapshot.child("universityprogram").getValue().toString();
                    String myUniYear = dataSnapshot.child("year").getValue().toString();
                    String myAdress = dataSnapshot.child("adress").getValue().toString();

                    /*Picasso.with(ProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);*/

                    userProfName.setText(myProfileName);
                    userStatus.setText(myProfileStatus);
                    userCity.setText("City: "  + myCity);
                    userPhoneNumber.setText("PhoneNumber: "  + myPhoneNumber);
                    userUniversity.setText("University: "  + myUniversity);
                    userUniversityProgram.setText("University  Program: "  + myUniProgram);
                    userUniversityYear.setText("Year: "  + myUniYear);
                    userAdress.setText("Adress: "  + myAdress);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMyPostsActivity() {
        Intent loginIntent = new Intent(ProfileActivity.this, MyPostsActivity.class);
        startActivity(loginIntent);

    }

}
