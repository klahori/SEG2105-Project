package com.example.user.loginsignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseUser;
    private EditText phoneNumEdit, addressEdit, companyEdit, licenceEdit,descriptionEdit;

    private Button btn_set_date_time, submit;
    private String date_time, dateDay, endTime, startTime, dateup;
    private int mYear, mMonth, mDay, cYear, cMonth, cDay, sHour, sMinute, eHour, eMinute;

    DatabaseReference databaseAvailability;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phoneNumEdit = (EditText) findViewById(R.id.phoneNum);
        btn_set_date_time = (Button) findViewById(R.id.btn_set_date_time);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        databaseAvailability = rootRef1.child("Users").child(firebaseUser.getUid()).child("Availability");

        addressEdit = (EditText) findViewById(R.id.address);
        companyEdit = (EditText) findViewById(R.id.companyName);
        licenceEdit = findViewById(R.id.licensed);
        descriptionEdit =(EditText) findViewById(R.id.description);

        findViewById(R.id.submitBtn).setOnClickListener(this);

        findViewById(R.id.btn_set_date_time).setOnClickListener(this);
        findViewById(R.id.backBtn).setOnClickListener(this);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting the name and role of logged in user
                String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String companyName = dataSnapshot.child("companyName").getValue(String.class);
                String licence = dataSnapshot.child("licence").getValue(String.class);
                String description = dataSnapshot.child("description").getValue(String.class);
                // setting the text so it welcome the user by first name and tells them they are logged in as the role they  have
                phoneNumEdit.setText(phoneNumber);
                addressEdit.setText(address);

                if (companyName != null) {
                    companyEdit.setText(companyName);
                } else {
                    companyEdit.setHint("Company Name");
                }
                if (licence != null) {
                    licenceEdit.setText(licence);
                } else {
                    licenceEdit.setHint("Are you Licenced please type Yes or No");
                }
                if (description != null) {
                    descriptionEdit.setText(description);
                }
                else {
                    descriptionEdit.setHint("Please enter a description about yourself");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

    }

    void completeProfile() {
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef1.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting the name and role of logged in user
                String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String day = dataSnapshot.child("day").getValue(String.class);
                String month = dataSnapshot.child("month").getValue(String.class);
                String year = dataSnapshot.child("year").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                final String yes,no;
                yes="Yes";
                no="No";

                // setting the text so it welcome the user by first name and tells them they are logged in as the role they  have
                final String companyNamef = companyEdit.getText().toString().trim();
                final String descriptionf = descriptionEdit.getText().toString().trim();
                final String licence = licenceEdit.getText().toString().trim();
                if (companyNamef.isEmpty()) {
                    companyEdit.setError(getString(R.string.companyError));
                    companyEdit.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    phoneNumEdit.setError(getString(R.string.input_error_phone));
                    phoneNumEdit.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    addressEdit.setError(getString(R.string.input_error_address));
                    addressEdit.requestFocus();
                    return;
                }
                if (!((licence.equals(yes))||(licence.equals(no))||(licence.equals("")))){
                    licenceEdit.setError(getString(R.string.errorLicence));
                    addressEdit.requestFocus();
                    return;
                }

                User user = new User();
                user.setCompanyName(companyNamef);
                user.setFirstName(firstName);
                user.setAddress(address);
                user.setDay(day);
                user.setEmail(email);
                user.setLastName(lastName);
                user.setMonth(month);
                user.setRole(role);
                user.setPhone(phoneNumber);
                user.setYear(year);
                user.setUsername(username);
                user.setDescription(descriptionf);
                user.setLicence(licence);


                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                //going through database user and special id  to get to the specific user logged in

                rootRef.child("Users").child(firebaseUser.getUid()).setValue(user).
                        addOnCompleteListener(ProfileActivity.this,
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileActivity.this, getString(R.string.updatedInfo), Toast.LENGTH_LONG).show();// tell the user account was made

                                        } else {
                                            Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();// if error tell user account was not made and the get the reason why
                                        }
                                    }
                                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }


    private void datePicker() {

        // Get Current Date
        final Calendar m = Calendar.getInstance();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        cYear = localDate.getYear();
        cMonth = localDate.getMonthValue();
        cDay = localDate.getDayOfMonth();

        mYear = m.get(Calendar.YEAR);
        mMonth = m.get(Calendar.MONTH);
        mDay = m.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if (year < cYear) {

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_year), duration);
                            toast.show();
                            return;
                        }


                        if ( month < cMonth-1 ) {
                            Context context = getApplicationContext();
                            //CharSequence text = "Hello toast!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_month), duration);
                            toast.show();
                            return;
                        }

                        if (day < cDay ) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_day), duration);
                            toast.show();
                            return;
                        }
                        dateDay = "Date: " + day + "-" + (month + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        StartingTime();
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }


    private void StartingTime() {
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

                        EndTime();
                    }
                }, sHour, sMinute, false);
        stimePickerDialog.show();
    }

    private void EndTime() {
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

                            Toast toast = Toast.makeText(context, getString(R.string.sHourError), duration);
                            toast.show();
                            return;
                        }
                        if (minute < sMinute && hourOfDay == sHour) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.sMinuteError), duration);
                            toast.show();
                            return;
                        }

                        eHour = hourOfDay;
                        eMinute = minute;
                        endTime = " End Time: " + hourOfDay + ":" + minute;
                        add();
                    }

                }, eHour, eMinute, false);
        timePickerDialog.show();
    }

    private void add() {

        final String day = dateDay;
        final String startTime1 = startTime;
        final String endTime1 = endTime;

        Avalibility avaliable1 = new Avalibility();
        avaliable1.setDate(day);
        avaliable1.setStartTime(startTime1);
        avaliable1.setEndTime(endTime1);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in

        databaseAvailability = rootRef.child("Users").child(firebaseUser.getUid()).child("Availability");


        String id = databaseAvailability.push().getKey();
        Avalibility avaliable = new Avalibility(id, dateDay, startTime, endTime);
        databaseAvailability.child(id).setValue(avaliable);


        Toast.makeText(this, "Availability has been added", Toast.LENGTH_LONG).show();


    }






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:// if register button pressed
                completeProfile();//follow registerUser function
                break;
            case R.id.btn_set_date_time:
                    datePicker();
                    break;
            case R.id.backBtn:
                Intent intent = new Intent(ProfileActivity.this,ServiceProviderActivity.class);
                startActivity(intent);

                break;

        }
    }



}
