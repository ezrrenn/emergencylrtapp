package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IncidentFormP extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;

    EditText etWDesc;
    TextView tvWDate, tvWTime;
    RadioGroup rgWPerson, rgGender, rgWStation;
    RadioButton rbSelf, rbElse, rbMale, rbFemale, rbOthers,rbSentul, rbPwtc, rbJamek;
    Button btnWPost, btnWCancel;
    String tCrime="";
    String latitude = "";
    String longitude = "";
    CheckBox rbA, rbP, rbS, rbL, rbO;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_form_p);

        etWDesc = findViewById(R.id.etWDesc);
        tvWDate = findViewById(R.id.tvWDate);
        tvWTime = findViewById(R.id.tvWTime);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rgWPerson = (RadioGroup) findViewById(R.id.rgWPerson);
        rgWStation = (RadioGroup) findViewById(R.id.rgWStation);
        rbSelf = findViewById(R.id.rbSelf);
        rbElse = findViewById(R.id.rbElse);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOthers = findViewById(R.id.rbOthers);
        rbSentul = findViewById(R.id.rbSentul);
        rbPwtc = findViewById(R.id.rbPwtc);
        rbJamek = findViewById(R.id.rbJamek);
        rbA = findViewById(R.id.cbA);
        rbP = findViewById(R.id.cbP);
        rbS = findViewById(R.id.cbS);
        rbL = findViewById(R.id.cbL);
        rbO = findViewById(R.id.cbO);
        btnWPost = findViewById(R.id.btnWPost);
        btnWCancel = findViewById(R.id.btnWCancel);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //radio group person
        rgWPerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbSelf.isChecked()) {
                    Toast.makeText(IncidentFormP.this, "Myself", Toast.LENGTH_SHORT).show();
                } else if (rbElse.isChecked()) {
                    Toast.makeText(IncidentFormP.this, "Someone Else", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //radio group station
        rgWStation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int j) {
                longitude = " ";
                latitude = " ";
                switch (j){
                    case R.id.rbSentul:
                        Toast.makeText(IncidentFormP.this, "Selected: Sentul", Toast.LENGTH_SHORT).show();
                        latitude = "3.1783405";
                        longitude ="101.6933983";
                        break;
                    case R.id.rbPwtc:
                        Toast.makeText(IncidentFormP.this, "Selected: PWTC", Toast.LENGTH_SHORT).show();
                        latitude = "3.1666485";
                        longitude ="101.691387";
                        break;
                    case R.id.rbJamek:
                        Toast.makeText(IncidentFormP.this, "Selected: Masjid Jamek", Toast.LENGTH_SHORT).show();
                        latitude = "3.1494274";
                        longitude ="101.6942388";
                        break;
                }
            }
        });

        //radio group gender
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int x) {
                switch (x){
                    case R.id.rbMale:
                        Toast.makeText(IncidentFormP.this, "Selected: Male", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbFemale:
                        Toast.makeText(IncidentFormP.this, "Selected: Female", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbOthers:
                        Toast.makeText(IncidentFormP.this, "Selected: Other", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //Date
        tvWDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(IncidentFormP.this, android.R.style.Theme_DeviceDefault_Dialog, dateSetListener, year,month,day);

                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDataSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                tvWDate.setText(date);
            }
        };

        //time
        tvWTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(IncidentFormP.this, android.R.style.Theme_DeviceDefault_Dialog, timeSetListener, 12,0,false);

                timePickerDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                tvWTime.setText(sdf.format(calendar.getTime()));
            }
        };



        btnWPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postButton();
            }
        });


        btnWCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IncidentFormP.this, IncidentPassenger.class));
            }
        });

    }

    private void postButton() {
        //checkbox
        tCrime =" ";
        if (rbA.isChecked()){
            tCrime +=" [1] " + rbA.getText().toString();
        }
        if (rbP.isChecked()){
            tCrime +=" [2] " + rbP.getText().toString();
        }
        if (rbS.isChecked()){
            tCrime +=" [3] " + rbS.getText().toString();
        }
        if (rbL.isChecked()){
            tCrime +=" [4] " + rbL.getText().toString();
        }
        if (rbO.isChecked()){
            tCrime +=" [5] " + rbO.getText().toString();
        }

        //database all
        //String incidentID = mAuth.getUid();
        //DocumentReference documentReference = db.collection("Incidents").document(incidentID);
        Map<String, Object> incident = new HashMap<>();
        if (rgWPerson.getCheckedRadioButtonId()== R.id.rbSelf){
            incident.put("SharingFor", rbSelf.getText().toString().trim());
        }else{
            incident.put("SharingFor", rbElse.getText().toString().trim());
        }
        if (rgGender.getCheckedRadioButtonId()== R.id.rbMale){
            incident.put("Gender", rbMale.getText().toString().trim());
        }else if (rgGender.getCheckedRadioButtonId()== R.id.rbFemale){
            incident.put("Gender", rbFemale.getText().toString().trim());
        } else {
            incident.put("Gender", rbOthers.getText().toString().trim());
        }
        incident.put("EstimateDate", tvWDate.getText().toString().trim());
        incident.put("EstimateTime", tvWTime.getText().toString().trim());
        incident.put("ListOfCrime", tCrime);
        if (rgWStation.getCheckedRadioButtonId()== R.id.rbSentul){
            incident.put("AtStation", rbSentul.getText().toString().trim());
            incident.put("Latitude", latitude);
            incident.put("Longitude", longitude);
        }else if (rgWStation.getCheckedRadioButtonId()== R.id.rbPwtc){
            incident.put("AtStation", rbPwtc.getText().toString().trim());
            incident.put("Latitude", latitude);
            incident.put("Longitude", longitude);
        }else if (rgWStation.getCheckedRadioButtonId()== R.id.rbJamek){
            incident.put("AtStation", rbJamek.getText().toString().trim());
            incident.put("Latitude", latitude);
            incident.put("Longitude", longitude);
        }
        incident.put("IncidentDescription", etWDesc.getText().toString().trim());

        db.collection("Incidents").add(incident).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(IncidentFormP.this, "Incident stored successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "FOR ALL DB SUCCESS!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IncidentFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IncidentFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnWPost.setVisibility(View.VISIBLE);
            }
        });

        //database only user
        CollectionReference collectionReference;
        collectionReference = db.collection("Passengers").document(passenger.getUid()).collection("My Incidents");
        Map<String, Object> passengerIncident = new HashMap<>();

        if (rgWPerson.getCheckedRadioButtonId()== R.id.rbSelf){
            passengerIncident.put("SharingFor", rbSelf.getText().toString().trim());
        }else{
            passengerIncident.put("SharingFor", rbElse.getText().toString().trim());
        }
        if (rgGender.getCheckedRadioButtonId()== R.id.rbMale){
            passengerIncident.put("Gender", rbMale.getText().toString().trim());
        }else if (rgGender.getCheckedRadioButtonId()== R.id.rbFemale){
            passengerIncident.put("Gender", rbFemale.getText().toString().trim());
        } else {
            passengerIncident.put("Gender", rbOthers.getText().toString().trim());
        }
        passengerIncident.put("EstimateDate", tvWDate.getText().toString().trim());
        passengerIncident.put("EstimateTime", tvWTime.getText().toString().trim());
        passengerIncident.put("ListOfCrime", tCrime);
        if (rgWStation.getCheckedRadioButtonId()== R.id.rbSentul){
            passengerIncident.put("AtStation", rbSentul.getText().toString().trim());
            passengerIncident.put("Latitude", latitude);
            passengerIncident.put("Longitude", longitude);
        }else if (rgWStation.getCheckedRadioButtonId()== R.id.rbPwtc){
            passengerIncident.put("AtStation", rbPwtc.getText().toString().trim());
            passengerIncident.put("Latitude", latitude);
            passengerIncident.put("Longitude", longitude);
        }else if (rgWStation.getCheckedRadioButtonId()== R.id.rbJamek){
            passengerIncident.put("AtStation", rbJamek.getText().toString().trim());
            passengerIncident.put("Latitude", latitude);
            passengerIncident.put("Longitude", longitude);
        }
        passengerIncident.put("IncidentDescription", etWDesc.getText().toString().trim());

        collectionReference.add(passengerIncident).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentReference> task) {
                Toast.makeText(IncidentFormP.this, "Submit successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "OWN DB SUCCESS!");
                Intent intent = new Intent(IncidentFormP.this, IncidentPassenger.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(IncidentFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "New incident!" ,
                latitude + longitude + " at " + tvWDate.getText().toString().trim() + " " + tvWTime.getText().toString(), getApplicationContext(), this);
        notificationsSender.SendNotifications();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(IncidentFormP.this, IncidentPassenger.class));
    }
}