package com.example.user.loginsignup;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.loginsignup.MainActivity;
import com.example.user.loginsignup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ServiceProviderActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //defining views
    private MultiAutoCompleteTextView textViewUser;
    ListView listViewAval;
    private Button btn_set_date_time;
    private String date_time,dateDay,endTime,startTime,dateup;
    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;


    List<Avalibility> aval;
    DatabaseReference databaseAvailability;



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
        listViewAval = (ListView) findViewById(R.id.listViewAvilability);


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


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        databaseAvailability = rootRef.child("Users").child(firebaseUser.getUid()).child("Availability");


        aval = new ArrayList<>();


        super.onStart();
        databaseAvailability.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aval.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Avalibility avaliable = postSnapshot.getValue(Avalibility.class);
                    aval.add(avaliable);
                }
                AvaliableList avalAdapter = new AvaliableList(ServiceProviderActivity.this, aval);//change aval list
                listViewAval.setAdapter(avalAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listViewAval.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Avalibility avalible = aval.get(i);
                showUpdateDeleteDialog(avalible.getId(), avalible.getDate());
                return true;
            }
        });


        findViewById(R.id.logout).setOnClickListener(this);

        textViewUser = (MultiAutoCompleteTextView) findViewById(R.id.editTextWelcome);

        findViewById(R.id.profileBtn).setOnClickListener(this);
        findViewById(R.id.servicesBtn).setOnClickListener(this);

    }


        private String pickDate(){
            // Get Current Date

            final Calendar m = Calendar.getInstance();
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            cYear  = localDate.getYear();
            cMonth = localDate.getMonthValue();
            cDay   = localDate.getDayOfMonth();

            mYear = m.get(Calendar.YEAR);
            mMonth = m.get(Calendar.MONTH);
            mDay = m.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {

                            if(year<cYear){

                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, getString(R.string.invalid_year), duration);
                                toast.show();
                                return;
                            }
                            if(day<cDay&&year==cYear&&month==cMonth ){
                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, getString(R.string.invalid_day), duration);
                                toast.show();
                                return;
                            }
                            if(day<cDay&&month==cMonth&&year==cYear){
                                Context context = getApplicationContext();
                                //CharSequence text = "Hello toast!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, getString(R.string.invalid_month), duration);
                                toast.show();
                                return;
                            }

                            dateup ="Date: " +day + "-" + (month+1) + "-" + year;
                            //*************Call Time Picker Here ********************
                            Context context = getApplicationContext();

                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, ("you choose"+dateup), duration);
                            toast.show();
                        }

                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
            return dateup;
        }

    private String EndTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        eHour = c.get(Calendar.HOUR_OF_DAY);
        eMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < sHour) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.eHourError), duration);
                            toast.show();
                            return;
                        }
                        if (minute < sMinute && hourOfDay == sHour) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.eMinuteError), duration);
                            toast.show();
                            return;
                        }

                        eHour = hourOfDay;
                        eMinute = minute;
                        endTime = " End Time: " + hourOfDay + ":" + minute;
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, ("you choose"+endTime), duration);
                        toast.show();
                    }

                }, eHour, eMinute, false);
        timePickerDialog.show();
    return endTime;
    }




    private String StartingTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        sHour = c.get(Calendar.HOUR_OF_DAY);
        sMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog stimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int shourOfDay, int sminute) {
                        if (shourOfDay < sHour) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, getString(R.string.sHourError), duration);
                            toast.show();
                            return;
                        }
                        if (sminute < sMinute && shourOfDay == sHour) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, getString(R.string.sMinuteError), duration);
                            toast.show();
                            return;
                        }
                        sHour=shourOfDay;
                        sMinute=sminute;
                        startTime = " Start time: " + shourOfDay + ":" + sminute;
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, ("you choose"+startTime), duration);
                        toast.show();
                    }
                }, sHour, sMinute, false);
        stimePickerDialog.show();
        return startTime;
    }

        @Override
        protected void onStart() {
            super.onStart();
        }


        private void showUpdateDeleteDialog(final String serviceId, String avaliableDate) {




            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.update_aval, null);
            dialogBuilder.setView(dialogView);

            final EditText editTextDate = (EditText) dialogView.findViewById(R.id.editTextDate);
            final EditText editTextsTime  = (EditText) dialogView.findViewById(R.id.editTextsTime);
            final EditText editTexteTime  = (EditText) dialogView.findViewById(R.id.editTexteTime);
            final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
            final Button dateConfirm = (Button) dialogView.findViewById(R.id.buttonConfirm);
            final Button startTimeConfirm = (Button) dialogView.findViewById(R.id.buttonStartTimeConfirm);
            final Button endTimeConfirm = (Button) dialogView.findViewById(R.id.buttonEndTimeConfirm);

            final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
            final Button dateUpdate = (Button) dialogView.findViewById(R.id.dateBtn);
            final Button startTimeUpdate = (Button) dialogView.findViewById(R.id.startBtn);
            final Button endTimeUpdate = (Button) dialogView.findViewById(R.id.endBtn);

            dialogBuilder.setTitle(avaliableDate);
            final AlertDialog b = dialogBuilder.create();
            b.show();




            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = editTextDate.getText().toString().trim();
                    String sTime = editTextsTime.getText().toString().trim();
                    String eTime = editTexteTime.getText().toString().trim();


                    if (date.isEmpty()) {
                        editTextDate.setError(getString(R.string.dateError));
                        editTextDate.requestFocus();
                        return;
                    }
                    if (sTime.isEmpty()) {
                        editTextsTime.setError(getString(R.string.sTimeError));
                        editTextsTime.requestFocus();
                        return;
                    }
                    if (eTime.isEmpty()) {
                        editTexteTime.setError(getString(R.string.eTimeError));
                        editTexteTime.requestFocus();
                        return;
                    }

                    if (!TextUtils.isEmpty(date)) {
                        updateAvaliable(serviceId, date, sTime,eTime);
                        b.dismiss();
                    }
                }
            });






            dateUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickDate();
                }
            });
            dateConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                editTextDate.setText(dateup);


                }
            });


            startTimeUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartingTime();
                }
            });
            startTimeConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextsTime.setText(startTime);


                }
            });







            endTimeUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EndTime();
                }
            });
            endTimeConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTexteTime.setText(endTime);


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

        private void updateAvaliable(String id, String date, String sTime,String eTime) {
            DatabaseReference dR = databaseAvailability.child(id);

            Avalibility avali = new Avalibility(id,date,sTime,eTime);
            dR.setValue(avali);
            Toast.makeText(getApplicationContext(), "Availability Updated", Toast.LENGTH_LONG).show();
        }

        private boolean deleteService(String id) {
            DatabaseReference dR = databaseAvailability.child(id);
            dR.removeValue();
            Toast.makeText(getApplicationContext(), "Availability Deleted", Toast.LENGTH_LONG).show();
            return true;
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