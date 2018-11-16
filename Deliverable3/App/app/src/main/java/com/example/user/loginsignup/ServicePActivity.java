package com.example.user.loginsignup;

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


public class ServicePActivity extends AppCompatActivity {
    private EditText et_show_date_time,test1,test2,upDate;
    private Button btn_set_date_time;
    private String date_time,dateDay,endTime,startTime,dateup;
    ListView listViewAval;

    List<Avalibility> aval;
    DatabaseReference databaseAvailability;



    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_p);

        test1 = (EditText) findViewById(R.id.test1);
        test2 = (EditText) findViewById(R.id.test2);
        upDate=(EditText) findViewById(R.id.editTextDate);
        et_show_date_time = (EditText) findViewById(R.id.et_show_date_time);
        btn_set_date_time = (Button) findViewById(R.id.btn_set_date_time);
        listViewAval = (ListView) findViewById(R.id.listViewAvilability);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        databaseAvailability= rootRef.child("Users").child(firebaseUser.getUid()).child("Availability");


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
                AvaliableList avalAdapter = new AvaliableList(ServicePActivity.this, aval);//change aval list
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
        btn_set_date_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();
                completeProfile();

            }
        });
    }


    void completeProfile(){
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef1.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }




    private void datePicker(){

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
                        dateDay ="Date: " +day + "-" + (month+1) + "-" + year;
                        date_time ="Date: " +day + "-" + (month+1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        StartingTime();
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }



    private void StartingTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        sHour = c.get(Calendar.HOUR_OF_DAY);
        sMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog stimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int shourOfDay, int sminute) {
                        if(shourOfDay<sHour ){
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, getString(R.string.sHourError), duration);
                            toast.show();
                            return;
                        }
                        if(sminute<sMinute&&shourOfDay==sHour ){
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, getString(R.string.sMinuteError), duration);
                            toast.show();
                            return;
                        }

                        startTime=" Start time: "+shourOfDay + ":" + sminute;
                        date_time=date_time+" Start time: "+shourOfDay + ":" + sminute;

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
                        test1.setText(hourOfDay + "-" + minute);
                        test2.setText(sHour + "-" + sMinute);
                        eHour = hourOfDay;
                        eMinute = minute;
                        endTime = " End Time: " + hourOfDay + ":" + minute;
                        et_show_date_time.setText(date_time + " End Time: " + hourOfDay + ":" + minute);
                        add();
                    }

                }, eHour, eMinute, false);
        timePickerDialog.show();
    }
private void add(){

    final String day = dateDay;
    final String startTime1 = startTime;
    final String endTime1 = endTime;

Avalibility avaliable1 =new Avalibility();
    avaliable1.setDate(day);
    avaliable1.setStartTime(startTime1);
    avaliable1.setEndTime(endTime1);



    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    //going through database user and special id  to get to the specific user logged in

    databaseAvailability= rootRef.child("Users").child(firebaseUser.getUid()).child("Availability");



            String id= databaseAvailability.push().getKey();
            Avalibility avaliable = new Avalibility(id,dateDay,startTime,endTime);
            databaseAvailability.child(id).setValue(avaliable);


            Toast.makeText(this, "Availability has been added", Toast.LENGTH_LONG).show();


    }


    private void pickDate(){
        // Get Current Date
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_aval, null);

        final EditText editTextDate = (EditText) dialogView.findViewById(R.id.editTextDate);
        final EditText editTextsTime  = (EditText) dialogView.findViewById(R.id.editTextsTime);
        final EditText editTexteTime  = (EditText) dialogView.findViewById(R.id.editTexteTime);


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
                        editTextDate.setText(dateup);
                        //*************Call Time Picker Here ********************
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
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
                    editTextDate.setError(getString(R.string.no_serviceName2));
                    editTextDate.requestFocus();
                    return;
                }
                if (sTime.isEmpty()) {
                    editTextsTime.setError(getString(R.string.noPrice));
                    editTextsTime.requestFocus();
                    return;
                }
                if (eTime.isEmpty()) {
                    editTexteTime.setError(getString(R.string.noPrice));
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
                //editTextDate.setText(dateup);
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
        DatabaseReference dR = databaseAvailability;

        Avalibility avali = new Avalibility(id,date,sTime,eTime);
        dR.setValue(avali);
        Toast.makeText(getApplicationContext(), "Availability Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteService(String id) {
        DatabaseReference dR = databaseAvailability;
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Availability Deleted", Toast.LENGTH_LONG).show();
        return true;
    }




    }







