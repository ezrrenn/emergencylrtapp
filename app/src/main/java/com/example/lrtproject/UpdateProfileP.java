package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileP extends AppCompatActivity {

    EditText etUName, etULast, etUPhone, etUEme, etUEmail;
    Button btnSave;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_p);

        etUName = findViewById(R.id.etUName);
        etULast = findViewById(R.id.etULast);
        etUPhone = findViewById(R.id.etUPhone);
        etUEme = findViewById(R.id.etUEme);
        etUEmail = findViewById(R.id.etUEmail);
        btnSave = findViewById(R.id.btnSave);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();

        Intent data = getIntent();
        String tvBName = data.getStringExtra("First Name");
        String tvBLast = data.getStringExtra("Last Name");
        String tvBPhone = data.getStringExtra("Phone Number");
        String tvBEme = data.getStringExtra("Emergency");
        String tvBEmail = data.getStringExtra("Email");

        etUName.setText(tvBName);
        etULast.setText(tvBLast);
        etUPhone.setText(tvBPhone);
        etUEme.setText(tvBEme);
        etUEmail.setText(tvBEmail);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etUEmail.getText().toString();

                passenger.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = db.collection("Passengers").document(userID);
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("First Name", etUName.getText().toString());
                        edited.put("Last Name", etULast.getText().toString());
                        edited.put("Phone Number", etUPhone.getText().toString());
                        edited.put("Emergency", etUEme.getText().toString());
                        edited.put("Email", email);

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProfileP.this,"Profile updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProfileP.this, ProfilePassenger.class);
                                intent.putExtra("Emergency", etUEme.getText().toString().trim());
                                startActivity(intent);
                                finish();
                            }
                        });
                        Toast.makeText(UpdateProfileP.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileP.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateProfileP.this, ProfilePassenger.class));
    }

}