package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_response);

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
                        
                    }else if(request_key.equals("2")){ //pending
                        Toast.makeText(WaitResponse.this, "pending", Toast.LENGTH_SHORT).show();
                    }else if(request_key.equals("0")){ //declined
                         Toast.makeText(WaitResponse.this, "declined", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}