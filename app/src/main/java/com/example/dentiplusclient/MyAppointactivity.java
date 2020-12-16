package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyAppointactivity extends AppCompatActivity {
    private String request_key="request_key";
    private String key;

    private String name,phone,address,cause,date,time;
    private TextView textViewname,textViewphone,textViewaddress,textViewcause,textViewdate,textViewtime,textViewdatecount;
    //firebase setup
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Requests");

    DatabaseReference referenceupdate = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference reference_del = FirebaseDatabase.getInstance().getReference("Requests");

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentFirebaseUser.getUid();

    private String day,month,year,hour,minute;
    private MaterialAlertDialogBuilder builder;

    private void update_request(){
        // update request under user key
        referenceupdate.child(uid).child("request").setValue("nothing");
    }

    private void delete_request(final String remove){
        //delete request from DB
        update_request();
        reference_del.child(remove).removeValue();


        Intent newintent = new Intent(MyAppointactivity.this, ReservationActivity.class);
        startActivity(newintent);
        finish();
    }



    // counter
    private void counter(int yearint,int monthint,int dayint,int hourint,int minint) {

        Calendar start_calendar = Calendar.getInstance();
        Calendar end_calendar = Calendar.getInstance();
        // end_calendar.set(2020, 11, 20,9,58); // 10 = November, month start at 0 = January
        end_calendar.set(yearint, monthint - 1, dayint, hourint, minint);

        long start_millis = start_calendar.getTimeInMillis(); //get the start time in milliseconds
        long end_millis = end_calendar.getTimeInMillis(); //get the end time in milliseconds
        long total_millis = (end_millis - start_millis); //total time in milliseconds

        // before starting counter check if we didn't passed the time
    if(end_millis>start_millis){

        //1000 = 1 second interval
        CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                textViewdatecount.setText("Days: " + days + "\nhours: " + hours + "\nminutes: " + minutes + "\nseconds: " + seconds); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @Override
            public void onFinish() {
                delete_request(key);

            }
        };
        cdt.start();

        } else{
            //Toast.makeText(MyAppointactivity.this, "Appointment Declined", Toast.LENGTH_SHORT).show();
        builder = new MaterialAlertDialogBuilder(MyAppointactivity.this);
         builder.setMessage("Appointment Done")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete_request(key);
                    }
                }).show();
         }
    }

    private void get_reservation_info(String req_key_1){
        myRef.child(req_key_1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // key 1 (unique id)

                name = dataSnapshot.child("patient_name").getValue().toString();
                phone = dataSnapshot.child("patient_phone").getValue().toString();
                address = dataSnapshot.child("patient_address").getValue().toString();

                cause = dataSnapshot.child("reservation_Cause").getValue().toString();
                date = dataSnapshot.child("reservation_Date").getValue().toString();
                time = dataSnapshot.child("reservation_Time").getValue().toString();

                textViewname.setText("Name: "+name);
                textViewphone.setText("Phone: "+phone);
                textViewcause.setText("Appointment Cause: "+cause);
                textViewaddress.setText("Address: "+address);

                textViewdate.setText("Date: "+date);
                textViewtime.setText("At: "+time);


                String[] parts_date = date.split("-");
                day = parts_date[0]; // day
                month = parts_date[1]; // month
                year = parts_date[2]; // year

                String[] parts_time = time.split(":");
                hour = parts_time[0]; // hour
                minute = parts_time[1]; // minutes
                //textViewdatecount.setText(Integer.parseInt(hour)+" "+Integer.parseInt(minute));
                counter(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointactivity);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Preparing...");
        progressDialog.show();

        textViewname = (TextView) findViewById(R.id.textViewname);
        textViewphone = (TextView) findViewById(R.id.textViewphone);
        textViewaddress = (TextView) findViewById(R.id.textViewaddress);
        textViewcause = (TextView) findViewById(R.id.textViewcause);

        textViewdate = (TextView) findViewById(R.id.textViewdate);
        textViewtime = (TextView) findViewById(R.id.textViewtime);

        textViewdatecount = (TextView) findViewById(R.id.textViewdatecount);


       // get the logged in user request key
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key=snapshot.getValue().toString();
                //textViewdatecount.setText(key);
                get_reservation_info(key);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
        myRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // key 1 (unique id)

                    name = dataSnapshot.child("patient_name").getValue().toString();
                    phone = dataSnapshot.child("patient_phone").getValue().toString();
                    address = dataSnapshot.child("patient_address").getValue().toString();

                    cause = dataSnapshot.child("reservation_Cause").getValue().toString();
                    date = dataSnapshot.child("reservation_Date").getValue().toString();
                    time = dataSnapshot.child("reservation_Time").getValue().toString();

                    textViewname.append(name);
                    textViewphone.append(phone);
                    textViewcause.append(cause);
                    textViewaddress.append(address);

                    textViewdate.setText("Date: "+date);
                    textViewtime.setText("At: "+time);


                String[] parts_date = date.split("-");
                day = parts_date[0]; // day
                month = parts_date[1]; // month
                year = parts_date[2]; // year

                String[] parts_time = time.split(":");
                hour = parts_time[0]; // hour
                minute = parts_time[1]; // minutes
                //textViewdatecount.setText(Integer.parseInt(hour)+" "+Integer.parseInt(minute));
               counter(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void delete_request(final String remove){
        //delete request from DB
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
        reference.child(remove).removeValue();

        // update request under user key
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        final DatabaseReference referenceupdate = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        referenceupdate.setValue("nothing");

    }

    // counter
 private void counter(int yearint,int monthint,int dayint,int hourint,int minint) {

        Calendar start_calendar = Calendar.getInstance();
        Calendar end_calendar = Calendar.getInstance();
        // end_calendar.set(2020, 11, 20,9,58); // 10 = November, month start at 0 = January

        end_calendar.set(yearint,monthint-1 ,dayint ,hourint,minint);

        long start_millis = start_calendar.getTimeInMillis(); //get the start time in milliseconds
        long end_millis = end_calendar.getTimeInMillis(); //get the end time in milliseconds
        long total_millis = (end_millis - start_millis); //total time in milliseconds

        //1000 = 1 second interval
        CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                textViewdatecount.setText("Days: "+days + "\nhours: " + hours + "\nminutes: " + minutes + "\nseconds: " + seconds); //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            @Override
            public void onFinish() {


                textViewdatecount.setText(key);
                // delete the request and update request child under user
                //delete_request(key);

            }
        };
        cdt.start();
*/
    }
}