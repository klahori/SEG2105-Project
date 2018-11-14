package com.example.user.loginsignup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class ServicePActivity extends AppCompatActivity {
    private EditText et_show_date_time,test1,test2;
    private Button btn_set_date_time;
    private String date_time;

    private int mYear,mMonth,mDay,cYear,cMonth,cDay,sHour,sMinute,eHour,eMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_p);
        String date_time = "";
        int mYear;
        int mMonth;
        int mDay;
        int eHour;
        int eMinute;
        int sHour;
        int sMinute;
        test1 = (EditText) findViewById(R.id.test1);
        test2 = (EditText) findViewById(R.id.test2);

        et_show_date_time = (EditText) findViewById(R.id.et_show_date_time);
        btn_set_date_time = (Button) findViewById(R.id.btn_set_date_time);

        btn_set_date_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();

            }
        });
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
                        if(day<cMonth&&year==cYear){
                            Context context = getApplicationContext();
                            //CharSequence text = "Hello toast!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, getString(R.string.invalid_month), duration);
                            toast.show();
                            return;
                        }
                        test1.setText(year+"-"+month+"-"+day);
                        test2.setText(cYear+"-"+ cMonth+"-" + cDay);
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

                        sHour = shourOfDay;
                        sMinute = sminute;

                        date_time=date_time+" Start time: "+shourOfDay + ":" + sminute;

                        EndTime();
                    }
                }, sHour, sMinute, false);
        stimePickerDialog.show();
    }

    private void EndTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        eHour = c.get(Calendar.HOUR_OF_DAY);
        eMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        eHour = hourOfDay;
                        eMinute = minute;

                        et_show_date_time.setText(date_time+" End Time: "+hourOfDay + ":" + minute);
                    }
                }, eHour, eMinute, false);
        timePickerDialog.show();
    }


    }




