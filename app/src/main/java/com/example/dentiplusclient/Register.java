package com.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final java.util.UUID UUID = null;
    private EditText inputEmail, inputPassword,username,userphone;
    private Button btnBack, btnSignUp;
    private FirebaseAuth auth;
    private ImageView registerimg;


    private void signup(){

        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final  String name=  username.getText().toString().trim();
        final String phone= userphone.getText().toString().trim();
        final String request ="nothing";

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration failed. \nEmail already exist",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // registration succ
                        else {
                            Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            // heta di ashn a3ml add li info el users fl database 3ndi
                                Users user = new Users(name, email, phone,request);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerimg = (ImageView) findViewById(R.id.imageViewbackregister);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        inputEmail=(EditText)findViewById(R.id.editTextEmailReg);
        inputPassword=(EditText)findViewById(R.id.editTextPasswordReg);

        username=(EditText)findViewById(R.id.editTextName);
        userphone= (EditText)findViewById(R.id.editTextPhone);

        btnSignUp = (Button) findViewById(R.id.buttonsignup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(); // create user in aut and add it's infos in the DB
            }
        });

        registerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}