package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class ReservationActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TimePicker timePicker;
    private CheckBox checkButtonq2A,checkButtonq2B,checkButtonq2C,checkButtonq2D,checkButtonq2E,checkQ2btn,
            checkButtonq3A,checkButtonq3B,checkButtonq3C,checkButtonq3D,checkButtonq3E,checkQ3btn;// hnkml man hena

    private RadioGroup radioGroupQ2,radioGroupQ3;

    private TextView textViewcalendar;

    private Calendar calendar;

    private String Date,Time;
    private Button done,nextbtn;
    private ImageView back;

    private LinearLayout linearLayout;

    private String name,phone,stN,st,floor,apart,address,cause,stringq1,stringq2,stringq3,stringq4,stringq5,stringq6,stringq7;
    private EditText editTextname,editTextphone,editTextstN,editTextSt,editTextFloorN,editTextApartn,editTextCause
            ,editTextQ1,editTextQ4,editTextQ5,editTextQ6,editTextQ7;

    private String request_key="request_key";
    private String key;
    StringBuilder Q2Answer=new StringBuilder();
    StringBuilder Q3Answer=new StringBuilder();

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

    private String refdate,refday,refmonth,refyear;

    private void get_date_from_DB(){
        //firebase setup
        final FirebaseDatabase databasedate = FirebaseDatabase.getInstance();
        final DatabaseReference mydateref= databasedate.getReference("datetime");

        mydateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { // key 1 (unique id)
                refdate = dataSnapshot.child("date").getValue().toString();
               // reftime = dataSnapshot.child("time").getValue().toString();

                String[] parts_date_ref = refdate.split("-");
                refday = parts_date_ref[0]; // day
                refmonth = parts_date_ref[1]; // month
                refyear = parts_date_ref[2]; // year

                calendar = Calendar.getInstance();
                int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int current_min = calendar.get(Calendar.MINUTE);

                int current_year = Integer.parseInt(refyear);
                int current_month = Integer.parseInt(refmonth);
                int current_day = Integer.parseInt(refday);

                calendar.set(Calendar.YEAR,current_year);
                calendar.set(Calendar.MONTH,current_month-1);
                calendar.set(Calendar.DAY_OF_MONTH,current_day);
               // calendar.set(Calendar.HOUR,current_hour );
               // calendar.set(Calendar.MINUTE,current_min);

                // min date to calendar is today and now
                calendarView.setMinDate(calendar.getTimeInMillis());
                timePicker.setIs24HourView(true);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

     private void get_q2_ans(){

         Q2Answer.setLength(0); //clear it

         if (checkButtonq2A.isChecked()){
             Q2Answer.append(checkButtonq2A.getText().toString()+"\n");
         }
         if (checkButtonq2B.isChecked()){
             Q2Answer.append(checkButtonq2B.getText().toString()+"\n");
         }
         if (checkButtonq2C.isChecked()){
             Q2Answer.append(checkButtonq2C.getText().toString()+"\n");
         }
         if (checkButtonq2D.isChecked()){
             Q2Answer.append(checkButtonq2D.getText().toString()+"\n");
         }
         if (checkButtonq2E.isChecked()){
             Q2Answer.append(checkButtonq2E.getText().toString()+"\n");
         }
     }

    private void get_q3_ans(){

        Q3Answer.setLength(0); //clear it

        if (checkButtonq3A.isChecked()){
            Q3Answer.append(checkButtonq3A.getText().toString()+"\n");
        }
        if (checkButtonq3B.isChecked()){
            Q3Answer.append(checkButtonq3B.getText().toString()+"\n");
        }
        if (checkButtonq3C.isChecked()){
            Q3Answer.append(checkButtonq3C.getText().toString()+"\n");
        }
        if (checkButtonq3D.isChecked()){
            Q3Answer.append(checkButtonq3D.getText().toString()+"\n");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // survey
        //Q2
        checkButtonq2A =(CheckBox) findViewById(R.id.checkquest2hot);
        checkButtonq2B =(CheckBox) findViewById(R.id.checkquest2cold);
        checkButtonq2C =(CheckBox) findViewById(R.id.checkquest2eat);
        checkButtonq2D =(CheckBox) findViewById(R.id.checkquest2spon);
        checkButtonq2E =(CheckBox) findViewById(R.id.checkquest2wake);

       //Q3
        checkButtonq3A =(CheckBox) findViewById(R.id.checkquest3anal);
        checkButtonq3B =(CheckBox) findViewById(R.id.checkquest3top);
        checkButtonq3C =(CheckBox) findViewById(R.id.checkquest3natu);
        checkButtonq3D =(CheckBox) findViewById(R.id.checkquest3disap);
        //

        //Q1,4,5,6
        editTextQ1 = (EditText) findViewById(R.id.editTextquest1);
        editTextQ4 = (EditText) findViewById(R.id.editTextquest4);
        editTextQ5 = (EditText) findViewById(R.id.editTextquest5);
        editTextQ6 = (EditText) findViewById(R.id.editTextquest6);
        editTextQ7 = (EditText) findViewById(R.id.editTextquest7optional);
        //

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
       // editTextCause = (EditText) findViewById(R.id.editTextPatientcause);

        get_date_from_DB();

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
                textViewcalendar.setText("Appointment On: "+ Date+" at "+String.format("%02d:%02d",hour,minute));
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

                get_q2_ans();
                get_q3_ans();

                // send request to firebase then go to waiting response activity
                name = editTextname.getText().toString();
                phone = editTextphone.getText().toString();
               //address
                stN = editTextstN.getText().toString();
                st = editTextSt.getText().toString();
                floor = editTextFloorN.getText().toString();
                apart = editTextApartn.getText().toString();

                //check empty field
                if (name.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the patient name",Toast.LENGTH_SHORT).show();
                }
                else if (phone.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the patient phone",Toast.LENGTH_SHORT).show();
                }
                else if (stN.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the street N°",Toast.LENGTH_SHORT).show();
                }
                else if (st.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the street Name",Toast.LENGTH_SHORT).show();
                }
               else if (floor.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the Floor N°",Toast.LENGTH_SHORT).show();
                }
               else if (apart.matches("")){
                    Toast.makeText(ReservationActivity.this,"Please Enter the apartment N°",Toast.LENGTH_SHORT).show();
                }

                //cause survey
                    //check empty answer
               else if (editTextQ1.getText().toString().matches("")){
                    Toast.makeText(ReservationActivity.this,"Please answer Q1",Toast.LENGTH_SHORT).show();
                }
                else if (Q2Answer.length()==0){
                    Toast.makeText(ReservationActivity.this,"Please answer Q2",Toast.LENGTH_SHORT).show();
                }
                else if (Q3Answer.length()==0){
                    Toast.makeText(ReservationActivity.this,"Please answer Q3",Toast.LENGTH_SHORT).show();
                }
                else if (editTextQ4.getText().toString().matches("")){
                    Toast.makeText(ReservationActivity.this,"Please answer Q4",Toast.LENGTH_SHORT).show();
                }
                else if (editTextQ5.getText().toString().matches("")){
                    Toast.makeText(ReservationActivity.this,"Please answer Q5",Toast.LENGTH_SHORT).show();
                }
                else if (editTextQ6.getText().toString().matches("")){
                    Toast.makeText(ReservationActivity.this,"Please answer Q6",Toast.LENGTH_SHORT).show();
                }
                 else {
                    address = stN + ", " + st + "\nApart N°: " + apart + "\nFloor N°: " + floor;
                    stringq1 = "1. if you are experiencing pain can you give it \na rating out of 10 where 1 is the least painful\nand 10 is the most painful?\n"
                            + editTextQ1.getText().toString() + "\n";
                    stringq2 = "2. what increases the pain?\n"
                            + Q2Answer.toString() + "\n";
                    stringq3 = "3. How do you deal with the pain?\n"
                            + Q3Answer.toString() + "\n";
                    stringq4 = "4. How urgent do you think your emergency is from 1 to 10?\n"
                            + editTextQ4.getText().toString() + "\n";
                    stringq5 = "5. Have you had any operations in the past?\n Can you kindly name it?\n"
                            + editTextQ5.getText().toString() + "\n";
                    stringq6 = "6. Do you take any medications?\n Can you kindly write them done\n"
                            + editTextQ6.getText().toString() + "\n";

                    if (editTextQ7.getText().toString().matches("")){
                        stringq7 = "7. Do you have any comments?\n No";

                    }else{
                            stringq7 = "7. Do you have any comments?\n"
                            + editTextQ7.getText().toString() + "\n";
                       }


                    cause = stringq1 + "\n" + stringq2 + "\n" + stringq3 + "\n" + stringq4 + "\n" + stringq5 + "\n" + stringq6 + "\n" + stringq7;

                    //Toast.makeText(ReservationActivity.this, cause, Toast.LENGTH_SHORT).show();

                    send_appointement_request(Date,Time,name,phone,address,cause);
                }
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