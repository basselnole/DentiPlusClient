package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.dentiplusclient.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

   private VideoView videoView;
   private String video_url;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Button loginbtn,signupbtn;
    private TextView textViewcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       videoView =(VideoView)findViewById(R.id.videoView);
       loginbtn =(Button)findViewById(R.id.button_login);
       signupbtn =(Button)findViewById(R.id.button_register);
        textViewcontact =(TextView) findViewById(R.id.textcontact);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Preparing...");
        progressDialog.show();

        // display textview contact
        myRef.child("contact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               final String x = dataSnapshot.getValue(String.class);
               textViewcontact.setText(x);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //display video
        myRef.child("Video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    video_url = dataSnapshot.getValue(String.class);

                 //   Toast.makeText(MainActivity.this, video_url , Toast.LENGTH_LONG).show();
                  videoView.setVideoPath(video_url);
                  progressDialog.dismiss();

                  videoView.start();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



loginbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Intent myIntent = new Intent(MainActivity.this, Login.class);
        startActivity(myIntent);
        finish();
    }
   });

       signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Register.class);
                startActivity(myIntent);
                finish();


            }
        });
    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
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