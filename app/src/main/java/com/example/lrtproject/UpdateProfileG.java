package com.example.lrtproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileG extends AppCompatActivity {

    EditText etUNameG, etULastG, etUPhoneG, selectDutyG, etUEmailG;
    Button btnSubmitG;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser guard;
    String guardID;
    Spinner dutyType;
    ArrayList<String> duty_type;
    String duty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_g);

        etUNameG = findViewById(R.id.etUNameG);
        etULastG = findViewById(R.id.etULastG);
        etUPhoneG = findViewById(R.id.etUPhoneG);
        etUEmailG = findViewById(R.id.etUEmailG);
        btnSubmitG = findViewById(R.id.btnSubmitG);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        guard = mAuth.getCurrentUser();
        guardID = guard.getUid();

        Intent data = getIntent();
        String tvBNameG = data.getStringExtra("First Name");
        String tvBLastG = data.getStringExtra("Last Name");
        String tvBPhoneG = data.getStringExtra("Phone Number");
        String tvBEmailG = data.getStringExtra("Email");

        etUNameG.setText(tvBNameG);
        etULastG.setText(tvBLastG);
        etUPhoneG.setText(tvBPhoneG);
        //spinnerGuard.setAdapter(dutyA);
        etUEmailG.setText(tvBEmailG);

        //Spinner
        dutyType = (Spinner) findViewById(R.id.spinnerTypeG);
        duty_type = new ArrayList<>();
        duty_type.add("Available");
        duty_type.add("Unavailable");
        //adapterGuard = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listGuard);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,duty_type);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dutyType.setAdapter(adapterType);
        dutyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),adapterType.getItem(i),Toast.LENGTH_LONG).show();
                duty = dutyType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapterType.notifyDataSetChanged();

        // btnSubmitG = findViewById(R.id.btnSubmitG);

        btnSubmitG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etUEmailG.getText().toString();

                guard.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = db.collection("SecurityGuard").document(guardID);
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("First Name", etUNameG.getText().toString());
                        edited.put("Last Name", etULastG.getText().toString());
                        edited.put("Phone Number", etUPhoneG.getText().toString());
                        edited.put("Duty", duty);
                        edited.put("Email", email);

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProfileG.this,"Profile updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProfileG.this, ProfileGuard.class);
                                intent.putExtra("Duty", duty);
                                startActivity(intent);
                                finish();
                            }
                        });
                        Toast.makeText(UpdateProfileG.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileG.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateProfileG.this, ProfileGuard.class));

    }
}