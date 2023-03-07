package com.example.lrtproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityReportHomePBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileA extends AppCompatActivity {

    EditText etUNameA, etULastA, etUPhoneA, selectDutyA, etUEmailA;
    Button btnSubmitA;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser authority;
    String userID;
    Spinner dutyType;
    ArrayList<String> duty_type;
    String duty;

    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;

    String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etUNameA = findViewById(R.id.etUNameA);
        etULastA = findViewById(R.id.etULastA);
        etUPhoneA = findViewById(R.id.etUPhoneA);
        /*textInputLayout = findViewById(R.id.textInputLayout);
        autoCompleteTextView = findViewById(R.id.dropitems);*/
        etUEmailA = findViewById(R.id.etUEmailA);
        btnSubmitA = findViewById(R.id.btnSubmitA);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authority = mAuth.getCurrentUser();
        userID = authority.getUid();

        //drop
        /*ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(UpdateProfileA.this, R.layout.items_list, dutyType);
        autoCompleteTextView.setAdapter(itemAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOption = adapterView.getItemAtPosition(i).toString();
            }
        });*/

        Intent data = getIntent();
        String tvBNameA = data.getStringExtra("First Name");
        String tvBLastA = data.getStringExtra("Last Name");
        String tvBPhoneA = data.getStringExtra("Phone Number");
        String tvBEmailA = data.getStringExtra("Email");

        etUNameA.setText(tvBNameA);
        etULastA.setText(tvBLastA);
        etUPhoneA.setText(tvBPhoneA);
        //spinnerGuard.setAdapter(dutyA);
        etUEmailA.setText(tvBEmailA);

        //Spinner
        dutyType = (Spinner) findViewById(R.id.spinnerType);

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

        btnSubmitA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etUEmailA.getText().toString();

                authority.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = db.collection("Authority").document(userID);
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("First Name", etUNameA.getText().toString());
                        edited.put("Last Name", etULastA.getText().toString());
                        edited.put("Phone Number", etUPhoneA.getText().toString());
                        edited.put("Duty", duty);
                        edited.put("Email", email);

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProfileA.this,"Profile updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProfileA.this, ProfileAuthority.class);
                                intent.putExtra("Duty", duty);
                                startActivity(intent);
                                finish();
                            }
                        });
                        Toast.makeText(UpdateProfileA.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileA.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed() {
            startActivity(new Intent(UpdateProfileA.this, ProfileAuthority.class));

    }
}