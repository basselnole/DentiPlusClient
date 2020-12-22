package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReservationActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TimePicker timePicker;

    private TextView textViewcalendar;

    private Calendar calendar;
    private String Date,Time;
    private Button done,nextbtn;
    private ImageView back;

    private LinearLayout linearLayout;

    private String name,phone,stN,st,floor,apart,address,cause;
    private EditText editTextname,editTextphone,editTextstN,editTextSt,editTextFloorN,editTextApartn,editTextCause;

    private String request_key="request_key";
    private String key;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference myRef = database.getReference("Requests").push();

    private void add_request(final String req_parent){

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        reference.setValue(req_parent);

    }

    private void send_appointement_request(final String date,final String time,final String name,final String phone,final String address,final String cause){

        ListOfReservationsClass reservation_details = new ListOfReservationsClass();

        reservation_details.setPatient_name(name);
        reservation_details.setPatient_phone(phone);
        reservation_details.setPatient_address(address);

        reservation_details.setReservation_Cause(cause);
        reservation_details.setReservation_Time(time);
        reservation_details.setReservation_Date(date);
        reservation_details.setStatus("2");

        myRef.setValue(reservation_details);
        //get the push key value
        key = myRef.getKey();
        add_request(key);

        Intent intent = new Intent(ReservationActivity.this, WaitResponse.class);
        intent.putExtra(request_key,key);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        calendarView = (CalendarView) findViewById(R.id.calendarView);
        textViewcalendar = (TextView) findViewById(R.id.date_view_text);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        done = (Button) findViewById(R.id.buttondonecalendar);

        nextbtn = (Button) findViewById(R.id.buttonnext);
        linearLayout = (LinearLayout) findViewById(R.id.layoutplus);

        editTextname = (EditText) findViewById(R.id.editTextPatientName);
        editTextphone = (EditText) findViewById(R.id.editTextPatientPhone);
        editTextstN = (EditText) findViewById(R.id.editTextPatientstreetn);
        editTextSt = (EditText) findViewById(R.id.editTextPatientstreet);
        editTextFloorN = (EditText) findViewById(R.id.editTextPatientfloor);
        editTextApartn = (EditText) findViewById(R.id.editTextPatientapart);
        editTextCause = (EditText) findViewById(R.id.editTextPatientcause);

        calendar = Calendar.getInstance();
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_min = calendar.get(Calendar.MINUTE);
        int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH);
        int current_day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.YEAR,current_year);
        calendar.set(Calendar.MONTH,current_month);
        calendar.set(Calendar.DAY_OF_MONTH,current_day);

        calendar.set(Calendar.HOUR,current_hour);
        calendar.set(Calendar.MINUTE,current_min);

        // min date to calendar is today and now
        calendarView.setMinDate(calendar.getTimeInMillis());
        timePicker.setIs24HourView(true);
        timePicker.setHour(current_hour);
        timePicker.setMinute(current_min);


        // Add Listener in calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // In this Listener have one method
            // and in this method we will
            // get the value of DAYS, MONTH, YEARS
            public void onSelectedDayChange(
                    @NonNull CalendarView view,
                    int year,
                    int month,
                    int dayOfMonth)
            {

                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                Date
                        = dayOfMonth + "-"
                        + (month + 1) + "-" + year;
                // set this date in TextView for Display
                textViewcalendar.setText(Date);
                // disappear calendar
                calendarView.setVisibility(View.GONE);
                // appear time picker
                timePicker.setVisibility(View.VISIBLE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                textViewcalendar.setText("Appointment On: "+ Date+" At "+String.format("%02d:%02d",hour,minute));
                Time = String.format("%02d:%02d",hour,minute);
                nextbtn.setVisibility(View.VISIBLE);
            }
        });

        // date and time chosed so enter patient details (name,address,phone)
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.GONE);
                nextbtn.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        //admin can define the starting date and time the reservations are avaliable
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send request to firebase then go to waiting response activity
                name = editTextname.getText().toString();
                phone = editTextphone.getText().toString();
               //address
                stN = editTextstN.getText().toString();
                st = editTextSt.getText().toString();
                floor = editTextFloorN.getText().toString();
                apart = editTextApartn.getText().toString();

                //cause
                cause= editTextCause.getText().toString();

                address = stN +", "+st+"\nApart N°: "+apart+"\nFloor N°: "+floor;

                send_appointement_request(Date,Time,name,phone,address,cause);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReservationActivity.this);
        builder.setMessage("Do you want to close the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .show();
    }
}