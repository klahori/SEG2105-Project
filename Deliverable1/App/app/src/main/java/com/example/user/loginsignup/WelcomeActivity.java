package com.example.user.loginsignup;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //view objects
    private Button buttonLogout;
    private MultiAutoCompleteTextView textViewUser;
    private ArrayList<String> iteam;
    private ListView listView;

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
        iteam =new ArrayList<String>();
        textViewUser = (MultiAutoCompleteTextView) findViewById(R.id.editTextWelcome);
        buttonLogout = (Button) findViewById(R.id.logout);
        listView=(ListView) findViewById(R.id.listUser);


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



                textViewUser.setText("Welcome " + name + " you are logged in as " + role);
           /*     for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String username = ds.child("username").getValue(String.class);
                    textViewUser.setText(username);
                }
*/


            /*   if (role.equals("Admin")) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String username = ds.child("username").getValue(String.class);

                        iteam.add(username);


                    }

            }

                ArrayAdapter<String> adapter = new ArrayAdapter(WelcomeActivity.this, android.R.layout.simple_list_item_1, iteam);

                listView.setAdapter(adapter);*/


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        uidRef.addListenerForSingleValueEvent(eventListener);




/*
        DatabaseReference reef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = rootRef.child("Users");
        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                //textViewUser.setText("Welcome " + name + " you are logged in as " + role);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String username = ds.child("username").getValue(String.class);
                    textViewUser.setText(username);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(eventListener1);


*/


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