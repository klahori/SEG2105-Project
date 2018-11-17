package com.example.user.loginsignup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminServicesActivity extends AppCompatActivity implements View.OnClickListener{
    ListView lv1,lv2,listViewService;

    List<Service> services;
    private ArrayList<String> array;

    DatabaseReference databaseService;
    DatabaseReference databaseAvailability;



    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);



        //getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        listViewService = (ListView) findViewById(R.id.lv1);

        databaseService = FirebaseDatabase.getInstance().getReference("Services");
        services = new ArrayList<>();


        super.onStart();
        databaseService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service service = postSnapshot.getValue(Service.class);
                    services.add(service);
                }
                ServiceList serviceAdapter = new ServiceList(AdminServicesActivity.this, services);
                listViewService.setAdapter(serviceAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listViewService.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = services.get(i);
                showUpdateDeleteDialog(service.getId(), service.getServiceName(),service.getCost());
                return true;
            }
        });


    }
    
    @Override
    protected void onStart() {
        super.onStart();
    }


    private void showUpdateDeleteDialog(final String serviceId, String serviceName,double cost) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_admin_services, null);
        dialogBuilder.setView(dialogView);
        final String serviceName1 = serviceName;
        final double cost1=cost;

        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddService);

        dialogBuilder.setTitle(serviceName+" "+cost);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                //going through database user and special id  to get to the specific user logged in
                databaseAvailability = rootRef.child("Users").child(firebaseUser.getUid()).child("Service");


                final FirebaseDatabase database = FirebaseDatabase.getInstance();

// Attach a listener to read the daVta at our posts reference
                databaseAvailability.orderByChild("serviceName")// go through all the id
                        .equalTo(serviceName1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {// if the one entered matches a username that already exist give an error saying username already exists
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, getString(R.string.addingServiceFail), duration);
                            toast.show();
                        }else{

                            String id = databaseAvailability.push().getKey();
                            Service avaliable = new Service(id, serviceName1, cost1);
                            databaseAvailability.child(id).setValue(avaliable);
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, getString(R.string.addingServicesSu), duration);
                            toast.show();
                        }


                    }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:// if register button pressed
                //logging out the user
                startActivity(new Intent(this, ServicePActivity.class));
                break;

        }
    }

}


