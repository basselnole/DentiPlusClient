package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ImageView backimg;
    private String requests;
    private String status;
    private String request_key="request_key";

    // remember user email and pass
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    // check reservation status if user already did a reservation
    private void check_reservation_status(String request_key_1){
        //check the req status if exist
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Requests");

        // get request status
        myRef.child(request_key_1).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                status = dataSnapshot.getValue().toString();

                if (status.equals("1")) { // request accepted
                    Intent newintent = new Intent(Login.this, MyAppointactivity.class);
                    newintent.putExtra(request_key,request_key_1);
                    startActivity(newintent);
                    finish();
                }else if(status.equals("0")){ //declined
                    Intent newintent = new Intent(Login.this, AppointementDeclined.class);
                    newintent.putExtra(request_key,request_key_1);
                    startActivity(newintent);
                    finish();
                }else if(status.equals("2")){ //pendingg
                    Intent newintent = new Intent(Login.this, WaitResponse.class);
                    newintent.putExtra(request_key,request_key_1);
                    startActivity(newintent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // check if user has already a reservation
    private void check_user_reservation(){


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();

        // check if the logged in user has a request or not
       // final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("request");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)){
                    requests = snapshot.child(uid).child("request").getValue().toString();

                    if (requests.equals("nothing")){ // there's no requests
                        Intent intent = new Intent(Login.this, ReservationActivity.class);
                        startActivity(intent);
                        finish();
                    }else{ //there's an appointment so check the request status (2 = pending)
                        check_reservation_status(requests);
                    }
                }else{
                    Toast toast=Toast.makeText(getApplicationContext(),"Email Does not exist",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        inputEmail=(EditText)findViewById(R.id.editTextEmail);
        inputPassword=(EditText)findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.btn_login);
        backimg = (ImageView) findViewById(R.id.imageViewbacklogin);

        // check if user has already logged in previously on this device
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String pass = pref.getString(PREF_PASSWORD, null);

        if (username == null || pass == null) {
            //do nothing
        }else{
            inputEmail.setText(username);
            inputPassword.setText(pass);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Signing in...");
                progressDialog.show();

                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //   progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(Login.this, "Email or Password in incorrect", Toast.LENGTH_LONG).show();
                                } else{
                                    //save user info on the phone
                                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                            .edit()
                                            .putString(PREF_USERNAME, email)
                                            .putString(PREF_PASSWORD, password)
                                            .commit();

                                    check_user_reservation();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Login.this);
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