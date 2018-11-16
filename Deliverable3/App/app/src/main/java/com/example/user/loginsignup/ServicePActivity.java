package com.example.user.loginsignup;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicePActivity extends AppCompatActivity {
    ListView lv1,lv2;

    List<Service> services;
    private ArrayList<String> array;

    DatabaseReference databaseService;



    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_p);
        lv1 = (ListView) findViewById(R.id.listView1);
        lv2 = (ListView) findViewById(R.id.listView2);
        databaseService = FirebaseDatabase.getInstance().getReference("Services");



        //getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //initializing views
        array = new ArrayList<String>();



        //adding listener to button


        DatabaseReference rotRef = FirebaseDatabase.getInstance().getReference();
        //starting point is set in the data base of users
        DatabaseReference usersdRef = rotRef.child("Services");
        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // goes through all the users in the database

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // gets all users usernames
                    String serviceName = ds.child("serviceName").getValue(String.class);
                    Log.d("TAG", serviceName);
                    long cost = ds.child("cost").getValue(Long.class);
                   // Log.d("TAG", cost);
                    String username =serviceName+" "+cost;
                    // adds all usernames in an arraylist
                    array.add(username);
                }
                //converts arraylist data to string
                ArrayAdapter<String> adapter = new ArrayAdapter(ServicePActivity.this, android.R.layout.simple_list_item_1, array);
                // in a listview adds the data from the converter
                lv1.setAdapter(adapter);
                Utility.setListViewHeightBasedOnChildren(lv1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener1);


        services = new ArrayList<>();
        ServiceList adapter = new ServiceList(ServicePActivity.this, services);

        lv2.setAdapter(adapter);

        Utility.setListViewHeightBasedOnChildren(lv2);
    }


    }







