package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView txtRegister;
    EditText etEmailP, etPassP;
    Button btnLogin;
    String txtEmailP, txtPassP;
    RadioGroup rgUser;
    String emailPattern = "[a-zA-Z0-9+_.-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser passenger, authority, guard;
    String userID, authorityID, guardID;
    RadioButton passengercheckk, authoritycheckk, securitycheckk;
    String userAs = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegister = findViewById(R.id.txtRegister);
        etEmailP = findViewById(R.id.etEmailP);
        etPassP = findViewById(R.id.etPassP);
        btnLogin = findViewById(R.id.btnLoginP);
        passengercheckk = findViewById(R.id.passengercheckk);
        authoritycheckk = findViewById(R.id.authoritycheckk);
        securitycheckk = findViewById(R.id.securitycheckk);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        passenger = mAuth.getCurrentUser();
        authority = mAuth.getCurrentUser();
        guard = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null){
            userID = passenger.getUid();
            return;
        }


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rgUser = (RadioGroup) findViewById(R.id.rgUser);
                rgUser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int j) {
                        switch (j){
                            case R.id.passengercheckk:
                                Toast.makeText(LoginActivity.this, "Login as: Passenger", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.authoritycheckk:
                                Toast.makeText(LoginActivity.this, "Login as: Authority", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.securitycheckk:
                                Toast.makeText(LoginActivity.this, "Login as: Security Guard", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                /*userAs = "";
                if (passengercheckk.isChecked()){
                    userAs += passengercheckk.getText().toString();
                    Toast.makeText(LoginActivity.this, "Login as: Passenger", Toast.LENGTH_SHORT).show();
                }
                else if (authoritycheckk.isChecked()){
                    userAs += authoritycheckk.getText().toString();
                    Toast.makeText(LoginActivity.this, "Login as: Authority", Toast.LENGTH_SHORT).show();
                }
                else if (securitycheckk.isChecked()){
                    userAs += securitycheckk.getText().toString();
                    Toast.makeText(LoginActivity.this, "Login as: Security Guard", Toast.LENGTH_SHORT).show();
                }*/

                txtEmailP = etEmailP.getText().toString().trim();
                txtPassP = etPassP.getText().toString().trim();

                if (!TextUtils.isEmpty((txtEmailP))){
                    if (txtEmailP.matches(emailPattern)){
                        if (!TextUtils.isEmpty(txtPassP)){
                            LoginP();
                        }else {
                            etPassP.setError("Password is required");
                        }

                    }else{
                        etEmailP.setError("Enter a valid Email Address");
                    }

                }else{
                    etEmailP.setError("Email is required");
                }

            }
        });
    }
    private void LoginP() {
        btnLogin.setVisibility(View.INVISIBLE);

        /*String selectedRole = "";
        if (rgUser.getCheckedRadioButtonId() == R.id.passengercheckk){
            selectedRole = "Passengers";
        }else if (rgUser.getCheckedRadioButtonId() == R.id.authoritycheckk){
            selectedRole = "Authority";
        }else if (rgUser.getCheckedRadioButtonId() == R.id.securitycheckk){
            selectedRole = "SecurityGuard";
        }

        db.collection(selectedRole).whereEqualTo("Email", txtEmailP)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        }else {
                            mAuth.signInWithEmailAndPassword(txtEmailP,txtPassP).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (rgUser.getCheckedRadioButtonId() == R.id.passengercheckk ){
                                        //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomePassenger.class);
                                        startActivity(intent);
                                        finish();
                                    }else if (rgUser.getCheckedRadioButtonId() == R.id.authoritycheckk){
                                        //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeAuthority.class);
                                        startActivity(intent);
                                        finish();
                                    }else if (rgUser.getCheckedRadioButtonId() == R.id.securitycheckk){
                                        //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeGuard.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this,"Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    btnLogin.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });*/
        CollectionReference dcc = db.collection("DeviceTokens");
        mAuth.signInWithEmailAndPassword(txtEmailP,txtPassP).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                if (rgUser.getCheckedRadioButtonId() == R.id.passengercheckk ){
                    //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomePassenger.class);
                    startActivity(intent);
                    finish();
                }else if (rgUser.getCheckedRadioButtonId() == R.id.authoritycheckk){
                    //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeAuthority.class);
                    startActivity(intent);
                    finish();
                }else if (rgUser.getCheckedRadioButtonId() == R.id.securitycheckk){
                    //Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeGuard.class);
                    startActivity(intent);
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
    }
}