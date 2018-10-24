package com.example.user.loginsignup;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
   // private String name,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.editTextWelcome);
        buttonLogout = (Button) findViewById(R.id.logout);

        //displaying logged in user name

        //adding listener to button
        buttonLogout.setOnClickListener(this);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);

                textViewUserEmail.setText("Welcome "+name+ " you are logged in as "+role);
               /* if(role.equals("Admin")){


                    // Start listing users from the beginning, 1000 at a time.
                    ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
                    while (page != null) {
                        for (ExportedUserRecord user : page.getValues()) {
                            System.out.println("User: " + user.getUid());
                        }
                        page = page.getNextPage();
                    }

// Iterate through all users. This will still retrieve users in batches,
// buffering no more than 1000 users in memory at a time.
                    page = FirebaseAuth.getInstance().listUsers(null);
                    for (ExportedUserRecord user : page.iterateAll()) {
                        System.out.println("User: " + user.getUid());
                    }

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}