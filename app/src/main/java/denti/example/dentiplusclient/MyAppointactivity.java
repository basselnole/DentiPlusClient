package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.dentiplusclient.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyAppointactivity extends AppCompatActivity {
    private String request_key="request_key";
    private String key;

    private String name,phone,address,cause,date,time;
    private TextView textViewname,textViewphone,textViewaddress,textViewcause,textViewdate,textViewtime,textViewnumber;
    private TextView textViewday,textViewhour,textViewmin,textViewsec;
    //firebase setup
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
   // private DatabaseReference myRef = database.getReference("Requests");
   private DatabaseReference myRef = database.getReference();

    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = currentFirebaseUser.getUid();

    private String day,month,year,hour,minute;
    private Intent intent_appo;



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

                textViewday.setText("" + days);
                textViewhour.setText("" + hours);
                textViewmin.setText("" + minutes);
                textViewsec.setText("" + seconds);
            }

            @Override
            public void onFinish() {
                intent_appo = new Intent(MyAppointactivity.this, AppointementDone.class);
                intent_appo.putExtra(request_key, key);
                startActivity(intent_appo);
                finish();
            }
        };
        cdt.start();

        } else{
            intent_appo = new Intent(MyAppointactivity.this, AppointementDone.class);
            intent_appo.putExtra(request_key, key);
            startActivity(intent_appo);
            finish();
         }
    }

    private void get_reservation_info(String req_key_1){
       // myRef.child(req_key_1).addListenerForSingleValueEvent(new ValueEventListener() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String x = dataSnapshot.child("contact").getValue(String.class);
                textViewnumber.setText(x);

                name = dataSnapshot.child("Requests").child(req_key_1).child("patient_name").getValue().toString();
                phone = dataSnapshot.child("Requests").child(req_key_1).child("patient_phone").getValue().toString();
                address = dataSnapshot.child("Requests").child(req_key_1).child("patient_address").getValue().toString();

                cause = dataSnapshot.child("Requests").child(req_key_1).child("reservation_Cause").getValue().toString();
                date = dataSnapshot.child("Requests").child(req_key_1).child("reservation_Date").getValue().toString();
                time = dataSnapshot.child("Requests").child(req_key_1).child("reservation_Time").getValue().toString();

                textViewname.setText(""+name);
                textViewphone.setText(""+phone);
                textViewcause.setText(""+cause);
                textViewaddress.setText(""+address);

                textViewdate.setText(""+date);
                textViewtime.setText(""+time);


                String[] parts_date = date.split("-");
                day = parts_date[0]; // day
                month = parts_date[1]; // month
                year = parts_date[2]; // year

                String[] parts_time = time.split(":");
                hour = parts_time[0]; // hour
                minute = parts_time[1]; // minutes

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


        textViewnumber = (TextView) findViewById(R.id.textViewcancel2);
        textViewname = (TextView) findViewById(R.id.textViewname);
        textViewphone = (TextView) findViewById(R.id.textViewphone);
        textViewaddress = (TextView) findViewById(R.id.textViewaddress);
        textViewcause = (TextView) findViewById(R.id.textViewcause);

        textViewdate = (TextView) findViewById(R.id.textViewdate);
        textViewtime = (TextView) findViewById(R.id.textViewtime);

        textViewday = (TextView) findViewById(R.id.textViewday);
        textViewhour = (TextView) findViewById(R.id.textViewhour);
        textViewmin = (TextView) findViewById(R.id.textViewminute);
        textViewsec = (TextView) findViewById(R.id.textViewsecond);


       // get the logged in user request key
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key=snapshot.getValue().toString();
                //textViewdatecount.setText(key);
                get_reservation_info(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MyAppointactivity.this);
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