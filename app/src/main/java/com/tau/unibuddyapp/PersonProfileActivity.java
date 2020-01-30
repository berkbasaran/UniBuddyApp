package com.tau.unibuddyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity {

    private TextView userProfName, userStatus, userCity, userPhoneNumber, userUniversity,userUniversityProgram, userUniversityYear, userAdress;
    /*private CircleImageView userProfileImage;*/

    private DatabaseReference UsersRef;
    private String receiverUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        IntializeFields();


        UsersRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
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

                    /*Picasso.with(PersonProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);*/

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

    private void IntializeFields() {

        userProfName = (TextView) findViewById(R.id.person_full_name);
        userStatus = (TextView) findViewById(R.id.person_profile_status);
        userCity = (TextView) findViewById(R.id.person_profile_city);
        userPhoneNumber = (TextView) findViewById(R.id.person_phone_number);
        userUniversity = (TextView) findViewById(R.id.person_profile_university);
        userUniversityProgram = (TextView) findViewById(R.id.person_profile_uniprogram);
        userUniversityYear = (TextView) findViewById(R.id.person_profile_uniyear);
        userAdress = (TextView) findViewById(R.id.person_profile_adress);
        /*userProfileImage = (CircleImageView) findViewById(R.id.person_profile_pic);*/


    }
}
