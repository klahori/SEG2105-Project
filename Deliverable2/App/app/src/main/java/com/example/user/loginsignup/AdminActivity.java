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



public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //defining views
    private Button buttonLogout,buttonServices;
    private MultiAutoCompleteTextView textViewUser;
    private ArrayList<String> array;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
        buttonLogout = (Button) findViewById(R.id.logout);
        listView = (ListView) findViewById(R.id.listUser);
        buttonServices = (Button) findViewById(R.id.services);


        //adding listener to button
        buttonLogout.setOnClickListener(this);

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
                //if users role is admin


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
        DatabaseReference rotRef = FirebaseDatabase.getInstance().getReference();
        //starting point is set in the data base of users
        DatabaseReference usersdRef = rotRef.child("Users");
        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // goes through all the users in the database

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // gets all users usernames
                    String username = ds.child("username").getValue(String.class);
                    Log.d("TAG", username);
                    // adds all usernames in an arraylist
                    array.add(username);
                }
                //converts arraylist data to string
                ArrayAdapter<String> adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_list_item_1, array);
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
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view == buttonServices) {
            //starting login activity
            startActivity(new Intent(this, ServiceActivity.class));
        }
    }
}