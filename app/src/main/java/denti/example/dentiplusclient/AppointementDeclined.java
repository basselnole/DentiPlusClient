package denti.example.dentiplusclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dentiplusclient.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointementDeclined extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Requests");
    private String request_key="request_key";

    private DatabaseReference referenceupdate_1 = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = currentFirebaseUser.getUid();
    private String key;
    private Button ok_dec;
    private Intent intent_dec,intent_2;

    private void update_request(){
        // check that the request is updated succ
        referenceupdate_1.child(uid).child("request").setValue("nothing", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    //System.out.println("Data could not be saved. " + firebaseError.getMessage());
                   // Toast.makeText(AppointementDeclined.this, "Data could not be saved. " , Toast.LENGTH_LONG).show();
                } else {
                    //System.out.println("Data saved successfully.");
                    Toast.makeText(AppointementDeclined.this, "Thank You" , Toast.LENGTH_LONG).show();
                    intent_2 = new Intent(AppointementDeclined.this, ReservationActivity.class);
                    startActivity(intent_2);
                    finish();
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
        setContentView(R.layout.activity_appointement_declined);

        intent_dec = getIntent();
        key = intent_dec.getStringExtra(request_key);

        ok_dec=(Button)findViewById(R.id.btn_ok_dec);

        ok_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_request(key);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // your code.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AppointementDeclined.this);
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