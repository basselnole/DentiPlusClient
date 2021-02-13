package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;

public class reset extends AppCompatActivity {
private Button reset_now;
private EditText rest_email;
private ImageView backimg;
    // reset password
    private void reset_pass(String emailAddress){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // do something when mail was sent successfully.
                            Toast.makeText(getApplicationContext(), "Please check your email address", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(reset.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error please try again later", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

     reset_now=(Button)findViewById(R.id.btn_reset_now);
     rest_email=(EditText) findViewById(R.id.editTextEmailreset);
        backimg = (ImageView) findViewById(R.id.imageViewbackreset);

     final ProgressDialog progressDialog = new ProgressDialog(this);

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reset.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reset_now.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             progressDialog.setTitle("Please wait!...");
             progressDialog.show();

             final String emailreset = rest_email.getText().toString();

             if (TextUtils.isEmpty(emailreset)) {
                 Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
                 return;
             }else{
                 reset_pass(emailreset);
                 progressDialog.dismiss();
             }


         }
     });
    }
}