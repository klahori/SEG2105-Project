package com.example.user.loginsignup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.BatchUpdateException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingService extends AppCompatActivity implements View.OnClickListener {

    private TextView text;
    private ListView serviceList;
    private List<ServiceInformation> services;
    private List<String> idNames;
    private Button back_btn;
    private int cYear,cMonth,cDay, mYear, mMonth, mDay,sHour,sMinute,eHour,eMinute, startAvalHour, startAvalMin, endAvalHour ,endAvalMin;
    private String dateup,chosenStartTime, chosenEndTime;
    private String currentUserid, avalibilityId;
    private int selectedRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_service);


        //text = (TextView) findViewById(R.id.printMessage);
        serviceList = (ListView) findViewById(R.id.serviceInfoList);
        back_btn = (Button) findViewById(R.id.back_btn);
        services = new ArrayList<>();
        idNames = new ArrayList<>();

        back_btn.setOnClickListener(this);

        //Getting the service Name that was clicked
        Intent intent = getIntent();
        final String serviceName = intent.getStringExtra("serviceName");
        //text.setText(serviceName);
        //Getting the current User Id from FireBase
        currentUserid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("TAG","+++++++++++++++++++++++++ UserID = " + currentUserid);

        DatabaseReference rootRef3 = FirebaseDatabase.getInstance().getReference();
        Query serviceRef = rootRef3.child("Users").orderByChild("role").equalTo("Service Provider");

        ValueEventListener getServiceProviders = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    final String uid = ds.getKey();
                    final DatabaseReference rootRef4 = FirebaseDatabase.getInstance().getReference();
                    Log.d("TAG","===================================" + serviceName);
                    Query getSelectedServices = rootRef4.child("Users").child(uid).child("Service")
                            .orderByChild("serviceName").equalTo(serviceName);


                    String logData = ("------------------------------------uid: " + uid + "    ----------------------------");
                    Log.d("TAG", logData);

                    ValueEventListener getservices = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    //get service provider id
                                    String id = ds.getRef().getParent().getParent().getKey();
                                    String servicename = ds.child("serviceName").getValue(String.class);
                                    String cost = ds.child("cost").getValue().toString();
                                    String email = ds.getRef().getParent().getParent().child("email").getKey().toString();
                                    String rating = ds.getRef().getParent().getParent().child("rating").getKey().toString();
                                    Log.d("TAG", "----------------------------------- rating: " + rating + "DataSnapshot Info: " + ds.getRef().getParent().getParent().child("rating").toString());

                                    String data2 = ds.toString();
                                    Log.d("TAG", "-----------------------------------Got ServiceName :" + servicename + "  Id =   " + id + "cost = " + cost);
                                    Log.d("TAG","<<<<<<<<<<<<<<<<<<<<<<<<data2 = " + data2 + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                                    Log.d("TAG", uid);
                                    //create of type ServiceInformation
                                    ServiceInformation tempService = new ServiceInformation(servicename, cost, id);
                                    tempService.setEmail(email);
                                    idNames.add(id);
                                    //add to arraylist
                                    services.add(tempService);
                                    break;
                                }
                                int arraySize = services.size();
                                //String arrayInfo = services.get(0).getServiceName();
                                Log.d("TAG", "---------------------------------------------ArraySize = " + arraySize);
                                Log.d("TAG", "<<<<<<<<<<<<<<<<<<<<<<<<<<First Query>>>>>>>>>>>>>>>");
                                for (int i=0; i<idNames.size(); i++) {
                                    Log.d("TAG", "==========IdName" + idNames.get(i));
                                    final int finalI = i;
                                    Log.d("TAG", "---------------"+ finalI);
                                    Query getServiceAval = rootRef4.child("Users").child(idNames.get(i))
                                            .child("Availability").orderByChild("date");
                                    ValueEventListener getsAvalInfo = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    String date = ds.child("date").getValue().toString();
                                                    String startTime = ds.child("startTime").getValue().toString();
                                                    String endTime = ds.child("endTime").getValue().toString();
                                                    avalibilityId = ds.child("id").getValue().toString();
                                                    Log.d("TAG", "--------------- finalI"+ finalI + "---------------------------------------");
                                                    services.get(0).setDate(date);
                                                    services.get(0).setStartTime(startTime);
                                                    services.get(0).setEndTime(endTime);
                                                    Log.d("TAG", "=================== Date: " + date + " startTime: " + startTime+ " EndTime: " + endTime + " ====================================");

                                                }
                                                ServiceInformationList serviceListInfoAdapter = new ServiceInformationList(BookingService.this, services );
                                                serviceList.setAdapter(serviceListInfoAdapter);
                                            } else {
                                                Log.d("TAG", "=================== Did not find Availability ====================================");

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };
                                    getServiceAval.addListenerForSingleValueEvent(getsAvalInfo);
                                    Log.d("TAG", "<<<<<<<<<<<<<<<<<<<<<<<<<<Second Query>>>>>>>>>>>>>>>");



                                    //here
                                    DatabaseReference refRating = FirebaseDatabase.getInstance().getReference().child("Users").child(idNames.get(i));
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                String rating = dataSnapshot.child("rating").getValue().toString();
                                                String email = dataSnapshot.child("email").getValue().toString();
                                                services.get(0).setRating(rating);
                                                services.get(0).setEmail(email);

                                                ServiceInformationList serviceListInfoAdapter = new ServiceInformationList(BookingService.this, services);
                                                serviceList.setAdapter(serviceListInfoAdapter);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };
                                    refRating.addListenerForSingleValueEvent(eventListener);







                                }

                                ServiceInformationList serviceListInfoAdapter = new ServiceInformationList(BookingService.this, services );
                                serviceList.setAdapter(serviceListInfoAdapter);
                                Log.d("TAG", "<<<<<<<<<<<<<<<<<<<<<<<<<<After ListAdapter Inserted>>>>>>>>>>>>>>>");
                            } else {
                                Log.d("TAG", "=================== Data does not exist====================================");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    getSelectedServices.addListenerForSingleValueEvent(getservices);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        serviceRef.addListenerForSingleValueEvent(getServiceProviders);


        serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = services.get(i).getServiceName();
                String date = services.get(i).getDate();
                String start = services.get(i).getStartTime();
                String end = services.get(i).getEndTime();
                String serviceid = services.get(i).getId();
                Log.d("TAG", "========================= Name: " + name + "Date: " + date + "/n Start Time" + start + " End Time: " + end);
                bookServiceDialog(name, date, start, end, serviceid);
            }
        });

        serviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = services.get(i).getId();
                rateServiceDialog(id);
                return true;
            }
        });




    }

    private void bookServiceDialog (final String serviceName, final String date, final String startTime, final String endTime, final String serviceId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_book_time, null);
        dialogBuilder.setView(dialogView);

        final EditText textDate = (EditText) dialogView.findViewById(R.id.textDate);
        final EditText textStartTime = (EditText) dialogView.findViewById(R.id.textStartTime);
        final EditText textEndTime = (EditText) dialogView.findViewById(R.id.textEndTime);
        final Button selectStartTime = (Button) dialogView.findViewById(R.id.selectStartTime);
        final Button selectEndTime = (Button) dialogView.findViewById(R.id.selectEndTime);
        final Button confirmStartTime = (Button) dialogView.findViewById(R.id.confirmStartTime);
        final Button confirmEndTime = (Button) dialogView.findViewById(R.id.confirmEndTime);

        final Button back_Btn = (Button) dialogView.findViewById(R.id.back_Btn);
        final Button book_Btn = (Button) dialogView.findViewById(R.id.book_Btn);

        Log.d("TAG", "========================= Date: " + date);


        dialogBuilder.setTitle("Book Service for " + serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        textDate.setText(date);

        book_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = textDate.getText().toString().trim();
                String sTime = textStartTime.getText().toString().trim();
                String eTime = textEndTime.getText().toString().trim();


                if (date.isEmpty()) {
                    textDate.setError(getString(R.string.dateError));
                    textDate.requestFocus();
                    return;
                }
                if (sTime.isEmpty()) {
                    textStartTime.setError(getString(R.string.sTimeError));
                    textStartTime.requestFocus();
                    return;
                }
                if (eTime.isEmpty()) {
                    textStartTime.setError(getString(R.string.eTimeError));
                    textStartTime.requestFocus();
                    return;
                }

                if (!TextUtils.isEmpty(date)) {
                    updateBookTime(serviceName, date, sTime,eTime, serviceId, endTime);
                    b.dismiss();
                }
            }
        });

        /*
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(date);
            }
        });
        confirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textDate.setText(dateup);
            }
        });
        */

        selectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartingTime(startTime, endTime);
            }
        });

        confirmStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textStartTime.setText(chosenStartTime);
            }
        });


        selectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndingTime(startTime, endTime);
            }
        });

        confirmEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEndTime.setText(chosenEndTime);
            }
        });

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }







    private void updateBookTime(String serviceName, String date, String startTime, String endTime, String serviceId, String endAvalTime) {
    //private void updateBookTime() {

        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);
        Avalibility booking = new Avalibility(currentUserid, date, startTime, endTime, serviceName);
        user.child("Booking").setValue(booking);

        DatabaseReference serviceAvailability = FirebaseDatabase.getInstance().getReference().child("Users").child(serviceId);
        //Calculate New StartTime and EndTime (End/time should be the same)
        String newStartTime = endTime;
        Avalibility updateServiceAvailability = new Avalibility(currentUserid, date, newStartTime, endAvalTime);
        Log.d("TAG", "====================== avalibilityId = " + avalibilityId);
        //serviceAvailability.child("Availability").child(avalibilityId).setValue(updateServiceAvailability);

        Toast.makeText(getApplicationContext(), "Booking Set", Toast.LENGTH_LONG).show();



    }







    private void rateServiceDialog(final String id) {
        AlertDialog.Builder rateDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_rating, null);
        rateDialog.setView(view);
        final EditText commentText = (EditText) view.findViewById(R.id.commentText);

        rateDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "The Service is not Rated", Toast.LENGTH_LONG).show();
            }
        });

        rateDialog.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String text = commentText.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Did not Rate. No Comment was written", Toast.LENGTH_LONG).show();
                    return;
                } else if (selectedRating == 0) {
                    Toast.makeText(getApplicationContext(), "Did not Rate. No Rating selected", Toast.LENGTH_LONG).show();

                } else {
                    rateServiceOnFirebase(id, selectedRating, text);
                    Toast.makeText(getApplicationContext(), "Rating Message: " + text + " Rating: " + selectedRating, Toast.LENGTH_LONG).show();
                }

            }
        });

        rateDialog.create().show();
    }


    private void rateServiceOnFirebase (String id, final int rating, final String comment) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String currentRating = dataSnapshot.child("rating").getValue().toString();
                    String numberOfRatings = dataSnapshot.child("numberOfRatings").getValue().toString();
                    Log.d("TAG", "================= Current Rating: " + currentRating);

                    //Get the numbe of past Ratings
                    int ratingNumber;
                    if(numberOfRatings.isEmpty()){
                        ratingNumber = 1;
                        ref.child("numberOfRatings").setValue(ratingNumber);
                    }else{
                        ratingNumber = Integer.parseInt(numberOfRatings)+1;
                        ref.child("numberOfRatings").setValue(ratingNumber);
                    }

                    //Set rating on Firebase
                    if(currentRating.isEmpty()){
                        //Create rating
                        ref.child("rating").setValue(rating);
                    } else if (currentRating.equals("unrated")){
                        //Set rating
                        ref.child("rating").setValue(rating);
                    } else {
                        //Average the rating
                        int cRating = Integer.parseInt(currentRating);
                        cRating = (cRating*(ratingNumber-1) + rating)/ratingNumber;
                        String cRatingString = String.valueOf(cRating);
                        ref.child("rating").setValue(cRatingString);
                    }
                    //Set the Last Comment
                    ref.child("LastComment").setValue(comment);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);

    }

    //Radio Buttons
    public void onRatingRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rating1:
                if (checked)
                    // update_rating
                    selectedRating = 1;
                break;
            case R.id.rating2:
                if (checked)
                    // update_rating
                    selectedRating = 2;
                break;
            case R.id.rating3:
                if (checked)
                    // update_rating
                    selectedRating = 3;
                break;
            case R.id.rating4:
                if (checked)
                    // update_rating
                    selectedRating = 4;
                break;
            case R.id.rating5:
                if (checked)
                    // update_rating
                    selectedRating = 5;
                break;
        }
    }



/*

    private String pickDate(String date){
        // Get Current Date

        final Calendar m = Calendar.getInstance();

        String[] p = date.split(": ");

        String[] parts = p[1].split("-");

        cYear  = Integer.parseInt(parts[2]);
        cMonth = Integer.parseInt(parts[1]);
        cDay   = Integer.parseInt(parts[0]);

        mYear = m.get(Calendar.YEAR);
        mMonth = m.get(Calendar.MONTH);
        mDay = m.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if(year!=cYear){

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_year), duration);
                            toast.show();
                            return;
                        }
                        if(day!=cDay){
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_day), duration);
                            toast.show();
                            return;
                        }
                        if(month!=cMonth){
                            Context context = getApplicationContext();
                            //CharSequence text = "Hello toast!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_month), duration);
                            toast.show();
                            return;
                        } else {

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
*/

    private String StartingTime(String startAvalTime, String endAvalTime) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();

        String[] temp = startAvalTime.split(": ");
        String[] parts = temp[1].split(":");
        startAvalHour = Integer.parseInt(parts[0]);
        startAvalMin = Integer.parseInt(parts[1]);

        temp = endAvalTime.split(": ");
        parts = temp[1].split(":");
        endAvalHour = Integer.parseInt(parts[0]);
        endAvalMin = Integer.parseInt(parts[1]);

        //sHour = c.get(Calendar.HOUR_OF_DAY);
        //sMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog stimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int shourOfDay, int sminute) {
                        //if ((shourOfDay < sHour) || (shourOfDay < startAvalHour) || (shourOfDay > endAvalHour)) {
                        if ((shourOfDay < startAvalHour) || (shourOfDay > endAvalHour)) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context,"Please Choose an Hour in between the Available time slot", duration);
                            toast.show();
                            return;
                        }
                        //if ((sminute < sMinute && shourOfDay == sHour) || (sminute < startAvalMin && shourOfDay == startAvalHour) || (sminute > endAvalMin && shourOfDay == endAvalHour) ) {
                         else if ((sminute < startAvalMin && shourOfDay == startAvalHour) || (sminute > endAvalMin && shourOfDay == endAvalHour) ) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, "Please Choose the Minutes to be in between the Available time slot", duration);
                            toast.show();
                            return;
                        } else {
                            sHour=shourOfDay;
                            sMinute=sminute;
                            chosenStartTime = " Start time: " + shourOfDay + ":" + sminute;
                            Context context = getApplicationContext();

                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, ("you choose "+ chosenStartTime), duration);
                            toast.show();

                        }
                    }
                }, sHour, sMinute, false);
        stimePickerDialog.show();
        return chosenStartTime;
    }


    private String EndingTime(String startAvalTime, String endAvalTime) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        String[] temp = startAvalTime.split(": ");
        String[] parts = temp[1].split(":");
        startAvalHour = Integer.parseInt(parts[0]);
        startAvalMin = Integer.parseInt(parts[1]);

        temp = endAvalTime.split(": ");
        parts = temp[1].split(":");
        endAvalHour = Integer.parseInt(parts[0]);
        endAvalMin = Integer.parseInt(parts[1]);

        eHour = c.get(Calendar.HOUR_OF_DAY);
        eMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if ((hourOfDay < sHour) || (hourOfDay < startAvalHour) || (hourOfDay > endAvalHour)) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Please Choose an Hour in between the Available time slot And after the selected Start time", duration);
                            toast.show();
                            return;
                        }
                        if ((minute < sMinute && hourOfDay == sHour) || (minute < startAvalMin && hourOfDay == startAvalHour) || (minute > endAvalMin && hourOfDay == endAvalHour) ) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Please Choose the Minutes to be in between the Available time slot And after the selected Start time", duration);
                            toast.show();
                            return;
                        }

                        eHour = hourOfDay;
                        eMinute = minute;
                        chosenEndTime = " End Time: " + hourOfDay + ":" + minute;
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, ("you choose "+chosenEndTime), duration);
                        toast.show();
                    }

                }, eHour, eMinute, false);
        timePickerDialog.show();
        return chosenEndTime;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:// if register button pressed
                //logging out the user
                startActivity(new Intent(this, HomeOwnerActivity.class));
                break;
        }
    }





}
