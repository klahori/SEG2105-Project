package com.example.user.loginsignup;


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

public class ServicePActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv1,lv2,listViewService;

    List<Service> services;
    private ArrayList<String> array;

    DatabaseReference databaseService;
    DatabaseReference databaseAvailability;



    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_p);



        //getting current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        listViewService = (ListView) findViewById(R.id.listView);
        databaseService  = rootRef.child("Users").child(firebaseUser.getUid()).child("Service");
        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.goToAdd).setOnClickListener(this);

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
                ServiceList serviceAdapter = new ServiceList(ServicePActivity.this, services);
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
        final View dialogView = inflater.inflate(R.layout.update_sp_services, null);
        dialogBuilder.setView(dialogView);
        final String id = serviceId;

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteService);

        dialogBuilder.setTitle(serviceName+" "+cost);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteService( id);
                           }
        });


    }


    private boolean deleteService(String id) {
        DatabaseReference dR = databaseService.child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Service Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goToAdd:// if register button pressed
                //logging out the user
                startActivity(new Intent(this, AdminServicesActivity.class));
                break;
            case R.id.back:// if register button pressed
                //logging out the user
                startActivity(new Intent(this, ServiceProviderActivity.class));
                break;

        }
    }
    }










