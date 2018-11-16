package com.example.user.loginsignup;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import com.example.user.loginsignup.MainActivity;
import com.example.user.loginsignup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;



public class ServiceProviderActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //defining views
    private MultiAutoCompleteTextView textViewUser;
    private ArrayList<String> array;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //initializing views
        array = new ArrayList<String>();
        textViewUser = (MultiAutoCompleteTextView) findViewById(R.id.editTextWelcome);
        findViewById(R.id.logout).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listUser);
        findViewById(R.id.profileBtn).setOnClickListener(this);
        findViewById(R.id.servicesBtn).setOnClickListener(this);



        //adding listener to button

        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting the name and role of logged in user
                String name = dataSnapshot.child("firstName").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                // setting the text so it welcome the user by first name and tells them they are logged in as the role they  have
                textViewUser.setText("Welcome " + name + " you are logged in as " + role);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
        DatabaseReference rotRef = FirebaseDatabase.getInstance().getReference();
        //starting point is set in the data base of users
        DatabaseReference usersdRef = rotRef.child("Services");
        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // goes through all the users in the database

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // gets all users usernames
                    String username = ds.child("serviceName").getValue(String.class);
                    Log.d("TAG", username);
                    // adds all usernames in an arraylist
                    array.add(username);
                }
                //converts arraylist data to string
                ArrayAdapter<String> adapter = new ArrayAdapter(ServiceProviderActivity.this, android.R.layout.simple_list_item_1, array);
                // in a listview adds the data from the converter
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:// if register button pressed
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.profileBtn:// if register button pressed
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.servicesBtn:// if register button pressed
                startActivity(new Intent(this, ServicePActivity.class));

                break;
        }
    }
}