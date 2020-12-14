package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    private AlertDialog.Builder builder;

    private void delete_request(final String parent){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
        reference.child(parent).removeValue();
        
       /*
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reference.child(parent).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_response);

        builder = new AlertDialog.Builder(this);

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

                        builder.setMessage("Appointment Confirmed")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        delete_request(request_key);
                                        //finish();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.setTitle("Appointment Confirmed");
                        alert.show();

                    }else if(request_key.equals("2")){ //pending
                        Toast.makeText(WaitResponse.this, "pending", Toast.LENGTH_SHORT).show();
                    }else if(request_key.equals("0")){ //declined
                         Toast.makeText(WaitResponse.this, "Appointment Declined", Toast.LENGTH_SHORT).show();

                        Toast.makeText(WaitResponse.this, "Appointment Declined", Toast.LENGTH_SHORT).show();

                        builder.setMessage("Appointment Declined")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        delete_request(request_key);
                                        //finish();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.setTitle("Appointment Declined");
                        alert.show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}