package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointementDone extends AppCompatActivity {

    private TextView textView;
    private Button btn_done_ok;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Requests");
    private String request_key="request_key";

    private DatabaseReference referenceupdate_1 = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = currentFirebaseUser.getUid();
    private String key;

    private Intent intent_done,intent_done_2;

    private void update_request(){
        // check that the request is updated succ
        referenceupdate_1.child(uid).child("request").setValue("nothing", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    referenceupdate_1.child(uid).child("request").setValue("nothing");
                } else {
                    //System.out.println("Data saved successfully.");
                    Toast.makeText(AppointementDone.this, "Thank You" , Toast.LENGTH_LONG).show();
                    btn_done_ok.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //when declined
    private void delete_request(final String parent){
        myRef.child(parent).removeValue();
        update_request();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointement_done);

        intent_done = getIntent();
        key = intent_done.getStringExtra(request_key);

        btn_done_ok=(Button)findViewById(R.id.btn_ok_done);
        delete_request(key);

        btn_done_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 intent_done_2 = new Intent(AppointementDone.this, whatnext.class);
                 startActivity(intent_done_2);
                 finish(); // work without async task but behnag shwia
            }
        });

    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppointementDone.this);
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
/*
    //your async task.
    public class TalkToServer extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            delete_request(key);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress dialog
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // dismiss dialog
           // intent_done_2 = new Intent(AppointementDone.this, whatnext.class);
           // startActivity(intent_done_2);
           // finish();
           /* Intent i = new Intent(context, whatnext.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }*/

}