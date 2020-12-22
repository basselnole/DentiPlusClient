package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class WaitResponse extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference myRef = database.getReference("Requests");
    private String request_key="request_key";
    private String key,request_status;

    private TextView textViewconfirmation;

    private DatabaseReference referenceupdate_1 = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = currentFirebaseUser.getUid();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_response);

        textViewconfirmation=(TextView)findViewById(R.id.textViewconfirmation);

        // to know when status changes
        intent = getIntent();
        key = intent.getStringExtra(request_key);



        // get request status
        myRef.child(key).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    request_status = dataSnapshot.getValue().toString();

                    if (request_status.equals("1")) { // request accepted
                        //textViewconfirmation.setText("Appointment Confirmed");
                        Intent newintent = new Intent(WaitResponse.this, MyAppointactivity.class);
                        newintent.putExtra(request_key, key);
                        startActivity(newintent);
                        finish();
                    } else if (request_status.equals("2")) { //pending
                        //Toast.makeText(WaitResponse.this, "Pending", Toast.LENGTH_SHORT).show();
                    } else if (request_status.equals("0")) { //declined
                        Intent newintent = new Intent(WaitResponse.this, AppointementDeclined.class);
                        newintent.putExtra(request_key, key);
                        startActivity(newintent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(WaitResponse.this);
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