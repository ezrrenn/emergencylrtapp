package com.example.lrtproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView txtLogin;
    EditText etFirstName, etLastName, etPhoneNum, etEmail, etPass;
    Button btnRegis;
    RadioButton passengercheck, authoritycheck, securitycheck;
    RadioGroup rgUserR;
    String txtFirstName, txtLastName, txtPhoneNum, txtEmail, txtPass;
    String emailPattern = "[a-zA-Z0-9+_.-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String userID;
    String registerAs = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("new_user_forums");


        txtLogin = findViewById(R.id.txtLogin);
        etFirstName = findViewById(R.id.etFirstNameP);
        etLastName = findViewById(R.id.etLastNameP);
        etPhoneNum = findViewById(R.id.etPhoneP);
        etEmail = findViewById(R.id.etEmailPR);
        etPass = findViewById(R.id.etPassRP);
        btnRegis = findViewById(R.id.btnRegis);
        passengercheck = findViewById(R.id.passengercheck);
        authoritycheck = findViewById(R.id.authoritycheck);
        securitycheck = findViewById(R.id.securitycheck);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Initialize Firestore
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            finish();
            return;
        }

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFirstName = etFirstName.getText().toString();
                txtLastName = etLastName.getText().toString();
                txtPhoneNum = etPhoneNum.getText().toString().trim();
                txtEmail = etEmail.getText().toString().trim();
                txtPass = etPass.getText().toString().trim();

                rgUserR = (RadioGroup) findViewById(R.id.rgUserR);
                rgUserR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                            case R.id.passengercheck:
                                Toast.makeText(RegisterActivity.this, "Register as: Passenger", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.authoritycheck:
                                Toast.makeText(RegisterActivity.this, "Register as: Authority", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.securitycheck:
                                Toast.makeText(RegisterActivity.this, "Register as: Security Guard", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });



                if (!TextUtils.isEmpty(txtFirstName)) {
                    if (!TextUtils.isEmpty(txtLastName)) {
                        if (!TextUtils.isEmpty(txtPhoneNum)) {
                            if (txtPhoneNum.length() == 10 || txtPhoneNum.length() == 11) {
                                if (!TextUtils.isEmpty((txtEmail))) {
                                    if (txtEmail.matches(emailPattern)) {
                                        if (!TextUtils.isEmpty(txtPass)) {
                                            RegisterP();
                                        } else {
                                            etPass.setError("Password is required");
                                        }

                                    } else {
                                        etEmail.setError("Enter a valid Email Address");
                                    }

                                } else {
                                    etEmail.setError("Email is required");
                                }

                            } else {
                                etPhoneNum.setError("Enter a valid phone number");
                            }
                        } else {
                            etPhoneNum.setError("Phone number is required");
                        }

                    } else {
                        etLastName.setError("Last name is required");
                    }
                } else {
                    etFirstName.setError("First name is required");
                }
            }
        });
    }

    private void RegisterP() {
        btnRegis.setVisibility(View.INVISIBLE);

        CollectionReference dcc = db.collection("DeviceTokens");

        mAuth.createUserWithEmailAndPassword(txtEmail,txtPass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        userID = mAuth.getCurrentUser().getUid();
                        if (rgUserR.getCheckedRadioButtonId() == R.id.passengercheck){
                            DocumentReference documentReference = db.collection("Passengers").document(userID);
                            Map<String, Object> passenger = new HashMap<>();
                            passenger.put("First Name", txtFirstName);
                            passenger.put("Last Name", txtLastName);
                            passenger.put("Phone Number", txtPhoneNum);
                            passenger.put("Email", txtEmail);
                            passenger.put("Role", "Passenger");

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task.getResult();
                                            Map<String, Object> dt = new HashMap<>();
                                            dt.put("DeviceToken", token);
                                            dt.put("ID", userID);
                                            dt.put("Role", "Passenger");

                                            dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("TAG", "Success!!!!" +token);
                                                }
                                            });

                                        }
                                    });

                            documentReference.set(passenger)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void avoid) {
                                            Toast.makeText(RegisterActivity.this, "Data stored successfully", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "onSuccess: Passenger Profile is created for " + userID);
                                            Intent intent = new Intent(RegisterActivity.this, HomePassenger.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            btnRegis.setVisibility(View.VISIBLE);
                                        }
                                    });

                        }else if (rgUserR.getCheckedRadioButtonId() == R.id.authoritycheck){
                            DocumentReference documentReference = db.collection("Authority").document(userID);
                            Map<String, Object> authority = new HashMap<>();
                            authority.put("First Name", txtFirstName);
                            authority.put("Last Name", txtLastName);
                            authority.put("Phone Number", txtPhoneNum);
                            authority.put("Email", txtEmail);
                            authority.put("Role", "Authority");

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task.getResult();
                                            Map<String, Object> dt = new HashMap<>();
                                            dt.put("DeviceToken", token);
                                            dt.put("ID", userID);
                                            dt.put("Role", "Authority");

                                            dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("TAG", "Success!!!!" +token);
                                                }
                                            });

                                        }
                                    });

                            documentReference.set(authority)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void avoid) {
                                            Toast.makeText(RegisterActivity.this, "Data stored successfully", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "onSuccess: Authority Profile is created for " + userID);
                                            Intent intent = new Intent(RegisterActivity.this, HomeAuthority.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            btnRegis.setVisibility(View.VISIBLE);
                                        }
                                    });

                        }else if (rgUserR.getCheckedRadioButtonId() == R.id.securitycheck){
                            DocumentReference documentReference = db.collection("SecurityGuard").document(userID);
                            Map<String, Object> guard = new HashMap<>();
                            guard.put("GuardID", userID);
                            guard.put("First Name", txtFirstName);
                            guard.put("Last Name", txtLastName);
                            guard.put("Phone Number", txtPhoneNum);
                            guard.put("Email", txtEmail);
                            guard.put("Role", "Guard");

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task.getResult();
                                            Map<String, Object> dt = new HashMap<>();
                                            dt.put("DeviceToken", token);
                                            dt.put("ID", userID);
                                            dt.put("Role", "Guard");

                                            dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("TAG", "Success!!!!" +token);
                                                }
                                            });

                                        }
                                    });

                            documentReference.set(guard)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void avoid) {
                                            Toast.makeText(RegisterActivity.this, "Data stored successfully", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "onSuccess: Guard Profile is created for " + userID);
                                            Intent intent = new Intent(RegisterActivity.this, HomeGuard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            btnRegis.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }

                        //all
                        DocumentReference documentReference = db.collection("Users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("First Name", txtFirstName);
                        user.put("Last Name", txtLastName);
                        user.put("Phone Number", txtPhoneNum);
                        user.put("Email", txtEmail);
                        if (rgUserR.getCheckedRadioButtonId() == R.id.passengercheck){
                            user.put("Role", "Passenger");
                        }else if (rgUserR.getCheckedRadioButtonId() == R.id.authoritycheck){
                            user.put("Role", "Authority");
                        }else if (rgUserR.getCheckedRadioButtonId() == R.id.securitycheck){
                            user.put("Role", "Guard");
                        }

                        documentReference.set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        Log.d("TAG", "onSuccess: Profile is created succesfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
    }

}