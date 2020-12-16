package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("Requests");
    private String request_key="request_key";
    private String key,request_status;


    private TextView textViewconfirmation;

    //when declined
    private void delete_request(final String parent){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
        reference.child(parent).removeValue();

        Intent newintent = new Intent(WaitResponse.this, ReservationActivity.class);
        startActivity(newintent);
        finish();
    }
   //when confirmed
    private void add_request(final String req_parent){

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        reference.setValue(req_parent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_response);

        textViewconfirmation=(TextView)findViewById(R.id.textViewconfirmation);
        // to know when status changes
        Intent intent = getIntent();
        key = intent.getStringExtra(request_key);

        // get request status
        myRef.child(key).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    request_key = dataSnapshot.getValue().toString();

                    if (request_key.equals("1")) { // request accepted

                        Toast.makeText(WaitResponse.this, "Appointment Confirmed", Toast.LENGTH_SHORT).show();
                        textViewconfirmation.setText("Appointment Confirmed");
                        add_request(key);

                        MaterialAlertDialogBuilder  builder = new MaterialAlertDialogBuilder(WaitResponse.this);
                        builder.setMessage("Appointment Confirmed")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent newintent = new Intent(WaitResponse.this, MyAppointactivity.class);
                                        newintent.putExtra(request_key,key);
                                        startActivity(newintent);
                                        finish();
                                    }
                                }).show();

                    }else if(request_key.equals("2")){ //pending
                        Toast.makeText(WaitResponse.this, "pending", Toast.LENGTH_SHORT).show();
                    }else if(request_key.equals("0")){ //declined
                         Toast.makeText(WaitResponse.this, "Appointment Declined", Toast.LENGTH_SHORT).show();
                         textViewconfirmation.setText("Appointment Declined");

                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(WaitResponse.this);
                        builder.setMessage("Appointment Declined")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        delete_request(key);
                                    }
                                }).show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}