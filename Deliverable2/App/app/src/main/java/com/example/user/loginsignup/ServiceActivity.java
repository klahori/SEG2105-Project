
package com.example.user.loginsignup;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ServiceActivity extends AppCompatActivity   {

    EditText editTextName;
    EditText editTextCost;
    Button buttonAddService;
    ListView listViewService;

    List<Service> services;
    DatabaseReference databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        editTextName = (EditText) findViewById(R.id.editTextService);
        editTextCost = (EditText) findViewById(R.id.editTextCost);
        listViewService = (ListView) findViewById(R.id.listViewServices);
        buttonAddService = (Button) findViewById(R.id.addButton);
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
                    ServiceList serviceAdapter = new ServiceList(ServiceActivity.this, services);
                listViewService.setAdapter(serviceAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
                });

        //adding an onclicklistener to button
        buttonAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
                }
                });

        listViewService.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Service service = services.get(i);
                                    showUpdateDeleteDialog(service.getId(), service.getServiceName());
                                    return true;
                                }
                            });
                        }


                        @Override
                        protected void onStart() {
                            super.onStart();
                        }


                        private void showUpdateDeleteDialog(final String serviceId, String serviceName) {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.update_dialog, null);
                            dialogBuilder.setView(dialogView);

                            final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextService);
                            final EditText editTextPrice  = (EditText) dialogView.findViewById(R.id.editTextCost);
                            final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
                            final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

                            dialogBuilder.setTitle(serviceName);
                            final AlertDialog b = dialogBuilder.create();
                            b.show();

                            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String name = editTextName.getText().toString().trim();
                                    double price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));
                                    if (!TextUtils.isEmpty(name)) {
                                        updateService(serviceId, name, price);
                                        b.dismiss();
                                    }
                                }
                            });

                            buttonDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteService(serviceId);
                                    b.dismiss();
                                }
                            });
                        }

                        private void updateService(String id, String service, double cost) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Services").child(id);
                            Service service1 = new Service(id,service,cost);
                            dR.setValue(service1);
                            Toast.makeText(getApplicationContext(), "Service Updated", Toast.LENGTH_LONG).show();
                        }

                        private boolean deleteService(String id) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Services").child(id);
                            dR.removeValue();
                            Toast.makeText(getApplicationContext(), "Service Deleted", Toast.LENGTH_LONG).show();
                            return true;
                        }

                        private void addService() {
                            String name = editTextName.getText().toString().trim();
                            double price=Double.parseDouble(String.valueOf(editTextCost.getText().toString()));
                            if (!TextUtils.isEmpty(name)){
                                String id= databaseService.push().getKey();
                                Service service = new Service(id,name,price);
                                databaseService.child(id).setValue(service);
                                editTextName.setText("");
                                editTextCost.setText("");

                                Toast.makeText(this, "Service added", Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(this, "Please enter a Service", Toast.LENGTH_LONG).show();
                            }

                        }
                    }