package com.example.user.loginsignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeOwnerActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private ListView listService;
    private MultiAutoCompleteTextView textViewUser;
    private EditText searchField;
    private ImageButton searchButton;
    private RadioButton byType, byTime, byRating;
    private RadioGroup searchParameterGroup;
    private String searchType = "";
    List<ServiceSearch> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();


        //initializing views
        buttonLogout = (Button) findViewById(R.id.logout);
        listService = (ListView) findViewById(R.id.listServices);
        textViewUser = (MultiAutoCompleteTextView) findViewById(R.id.editTextWelcome);
        searchField = (EditText) findViewById(R.id.searchField);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        byType = (RadioButton) findViewById(R.id.radio_ByType);
        byTime = (RadioButton) findViewById(R.id.radio_ByTime);
        byRating = (RadioButton) findViewById(R.id.radio_ByRating);
        searchParameterGroup = (RadioGroup) findViewById(R.id.search_parameters);
        services = new ArrayList<>();

        buttonLogout.setOnClickListener(this);
        searchButton.setOnClickListener(this);

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

        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef3 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef3.child("Users").child(uid);
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



    }


    private void firebaseUserSearch(String searchText) {

        Log.d("TAG", searchText);

        //Getting the Database Reference
        DatabaseReference rootRef3 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        //DatabaseReference serviceRef = rootRef3.child("Services");
        Query serviceRef;
        //TODO need to fix the search parameters
        if (searchType.equals("Type")){
            serviceRef = rootRef3.child("Services").orderByChild("serviceName").startAt(searchText).endAt(searchText + "\uf8ff");
        } else if (searchType.equals("Time")){
            serviceRef = rootRef3.child("Services").orderByKey();
        } else if (searchType.equals("Rating")){
            serviceRef = rootRef3.child("Services").orderByValue();
        } else {
            serviceRef = rootRef3.child("Services").orderByChild("serviceName").startAt(searchText).endAt(searchText + "\uf8ff");
        }

        //clear the Previous search
        services.clear();

        ValueEventListener serviceNameEvent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // gets all users search results
                    String serviceName = ds.child("serviceName").getValue(String.class);
                    String cost = ds.child("cost").getValue().toString();
                    //String rating = ds.child("rating").getValue(String.class);
                    String logData = ("Service Name: " + serviceName + " Cost: " + cost + " ");
                    Log.d("TAG", logData);
                    //create of type serviceSearch
                    ServiceSearch service = new ServiceSearch(serviceName, cost, "Unrated");
                    // adds all services in an arraylist
                    services.add(service);
                }
                ServiceSearchList serviceAdapter = new ServiceSearchList(HomeOwnerActivity.this, services);
                listService.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        serviceRef.addListenerForSingleValueEvent(serviceNameEvent);

        listService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),BookingService.class);
                intent.putExtra("serviceName", services.get(i).getServiceName());
                startActivity(intent);
            }
        });


    }


    //Radio Buttons
    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_ByType:
                if (checked)
                    // Admin
                    searchType = "Type";
                break;
            case R.id.radio_ByTime:
                if (checked)
                    // Service Provider
                    searchType = "Time";
                break;
            case R.id.radio_ByRating:
                if (checked)
                    // Home Owner
                    searchType = "Rating";
                break;
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:// if logout button pressed
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.searchButton:// if search button pressed
                //collect Text to be searched
                String searchText = searchField.getText().toString();
                //method to search firebase
                firebaseUserSearch(searchText);
                break;
        }
    }



}
