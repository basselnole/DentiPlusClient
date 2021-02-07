package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dentiplusclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final java.util.UUID UUID = null;
    private EditText inputEmail, inputPassword,username,userphone;
    private Button btnBack, btnSignUp;
    private FirebaseAuth auth;
    private ImageView registerimg;

    private ProgressDialog progressDialog;

    private void signup(){

        progressDialog.setTitle("Registering...");
        progressDialog.show();

        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final  String name=  username.getText().toString().trim();
        final String phone= userphone.getText().toString().trim();
        final String request ="nothing";

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter your email address!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

       else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please Enter your name!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Please Enter your phone!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
       else{
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
                            progressDialog.dismiss();
                        }
                        // registration succ
                        else {
                            Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         progressDialog = new ProgressDialog(this);

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

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Register.this);
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