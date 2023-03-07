package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFormP extends AppCompatActivity {

    RadioButton rbYes, rbNo, rblrtSentul, rblrtPwtc, rblrtJamek;
    RadioGroup rgE, rgSs, rgC;
    TextView result;
    Button btnSubmitR, btnCancelR;
    TextView tvRName, tvRName2, tvRPhone, tvRDate, tvRTime;
    LinearLayout lldate, lltime;
    EditText etCrime, etDesc;
    CheckBox cbAbd, cbPick, cbHar, cbLost, cbOther;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String tCrime = "";
    String latitude = "";
    String longitude = "";
    FirebaseUser passenger;
    String userID;
    String sdf;
    String usertoken;
    List<String> deviceTokens = new ArrayList<>();
    ArrayList<String> listdt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form_p);

        tvRName = findViewById(R.id.tvRnamee);
        tvRName2 = findViewById(R.id.tvRnamee2);
        tvRPhone = findViewById(R.id.tvRphonee);
        tvRDate = findViewById(R.id.tvRdatee);
        tvRTime = findViewById(R.id.tvRtimee);
        //etCrime = findViewById(R.id.etCrime);
        etDesc = findViewById(R.id.tvRdescc);
        btnSubmitR = findViewById(R.id.btnSubmitR);
        btnCancelR = findViewById(R.id.btnCancelR);
        lltime = findViewById(R.id.lltime);
        lldate = findViewById(R.id.lldate);
        /*rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);*/
        rblrtSentul = findViewById(R.id.rblrtSentul);
        rblrtPwtc = findViewById(R.id.rblrtPwtc);
        rblrtJamek =findViewById(R.id.rblrtJamek);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //call db
        DocumentReference documentReference = db.collection("Passengers").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvRName.setText(document.getString("First Name"));
                        tvRName2.setText(document.getString("Last Name"));
                        tvRPhone.setText(document.getString("Phone Number"));
                        Log.w(TAG, "Success.");
                    }else{
                        Log.d(TAG, "DocumentSnapshot data: " + userID);
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.w(TAG, "Error getting documents.");
                }
            }
        });

        //Date
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //date
        tvRDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        //time
        tvRTime.setText(new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date()));
        //date
        tvRDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(ReportFormP.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        tvRDate.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        //time
        tvRTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReportFormP.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hourOfDay,minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                        tvRTime.setText(sdf.format(calendar.getTime()));
                    }
                },12,0,false);
                timePickerDialog.show();
            }
        });

        //radio group station
        rgSs = (RadioGroup) findViewById(R.id.rgStation);
        rgSs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int j) {
                longitude = " ";
                latitude = " ";
                switch (j){
                    case R.id.rblrtSentul:
                        Toast.makeText(getApplicationContext(), "Selected: Sentul", Toast.LENGTH_SHORT).show();
                        latitude = "3.1783405";
                        longitude ="101.6933983";
                        break;
                    case R.id.rblrtPwtc:
                        Toast.makeText(getApplicationContext(), "Selected: PWTC", Toast.LENGTH_SHORT).show();
                        latitude = "3.1666485";
                        longitude ="101.691387";
                        break;
                    case R.id.rblrtJamek:
                        Toast.makeText(getApplicationContext(), "Selected: Masjid Jamek", Toast.LENGTH_SHORT).show();
                        latitude = "3.1494274";
                        longitude ="101.6942388";
                        break;
                }
            }
        });

        //check box
        cbAbd = findViewById(R.id.cbAbd);
        cbPick = findViewById(R.id.cbPick);
        cbHar = findViewById(R.id.cbHar);
        cbLost =findViewById(R.id.cbLost);
        cbOther = findViewById(R.id.cbOther);

        sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss",
                Locale.getDefault()).format(new Date());
        //Button
        btnSubmitR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkbox
                tCrime =" ";
                if (cbAbd.isChecked()){
                    tCrime +=" [1] " + cbAbd.getText().toString();
                }
                if (cbPick.isChecked()){
                    tCrime +=" [2] " + cbPick.getText().toString();
                }
                if (cbHar.isChecked()){
                    tCrime +=" [3] " + cbHar.getText().toString();
                }
                if (cbLost.isChecked()){
                    tCrime +=" [4] " + cbLost.getText().toString();
                }
                if (cbOther.isChecked()){
                    tCrime +=" [5] " + cbOther.getText().toString();
                }
                //Toast.makeText(getApplicationContext(), tCrime + "are selected", Toast.LENGTH_LONG).show();

                submitButton();

            }
        });
        btnCancelR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportFormP.this, ReportHomeP.class));
            }
        });
    }


    private void submitButton() {
        CollectionReference collectionReference;
        collectionReference = db.collection("Passengers").document(userID).collection("Report");

        //database all
        String id = UUID.randomUUID().toString();
        Map<String, Object> passengerReport = new HashMap<>();
        passengerReport.put("ReportId", id);
        passengerReport.put("FirstName", tvRName.getText().toString().trim());
        passengerReport.put("LastName", tvRName2.getText().toString().trim());
        passengerReport.put("PhoneNumber", tvRPhone.getText().toString().trim());

        /*if (rgE.getCheckedRadioButtonId()== R.id.rbYes){
            passengerReport.put("Emergency", rbYes.getText().toString().trim());
            passengerReport.put("StatusReport", "Emergency");
        }else{
            passengerReport.put("Emergency", rbNo.getText().toString().trim());
            passengerReport.put("StatusReport", "Received");
        }*/
        passengerReport.put("StatusReport", "Received");
        passengerReport.put("TypeOfCrime", tCrime);

        passengerReport.put("DateCrime", tvRDate.getText().toString().trim());

        passengerReport.put("TimeCrime", tvRTime.getText().toString().trim());

        if (rgSs.getCheckedRadioButtonId()== R.id.rblrtSentul){
            passengerReport.put("Station", rblrtSentul.getText().toString().trim());
            passengerReport.put("Latitude", latitude);
            passengerReport.put("Longitude", longitude);
        }else if (rgSs.getCheckedRadioButtonId()== R.id.rblrtPwtc){
            passengerReport.put("Station", rblrtPwtc.getText().toString().trim());
            passengerReport.put("Latitude", latitude);
            passengerReport.put("Longitude", longitude);
        }else if (rgSs.getCheckedRadioButtonId()== R.id.rblrtJamek){
            passengerReport.put("Station", rblrtJamek.getText().toString().trim());
            passengerReport.put("Latitude", latitude);
            passengerReport.put("Longitude", longitude);
        }
        passengerReport.put("Description", etDesc.getText().toString().trim());
        passengerReport.put("SecurityGuard", "none");
        passengerReport.put("Timestamp", sdf);
        db.collection("Reports").document(id).set(passengerReport).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ReportFormP.this, "Reports stored successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "FOR ALL DB SUCCESS!" );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(ReportFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //only user
        Map<String, Object> report = new HashMap<>();

        report.put("FirstName", tvRName.getText().toString().trim());
        report.put("LastName", tvRName2.getText().toString().trim());
        report.put("PhoneNumber", tvRPhone.getText().toString().trim());

        /*if (rgE.getCheckedRadioButtonId()== R.id.rbYes){
            report.put("Emergency", rbYes.getText().toString().trim());
            report.put("StatusReport", "Emergency");
        }else{
            report.put("Emergency", rbNo.getText().toString().trim());
            report.put("StatusReport", "Received");
        }*/

        report.put("StatusReport", "Received");

        report.put("TypeOfCrime", tCrime);

        report.put("DateCrime", tvRDate.getText().toString().trim());

        report.put("TimeCrime", tvRTime.getText().toString().trim());

        if (rgSs.getCheckedRadioButtonId()== R.id.rblrtSentul){
            report.put("Station", rblrtSentul.getText().toString().trim());
            report.put("Latitude", latitude);
            report.put("Longitude", longitude);
        }else if (rgSs.getCheckedRadioButtonId()== R.id.rblrtPwtc){
            report.put("Station", rblrtPwtc.getText().toString().trim());
            report.put("Latitude", latitude);
            report.put("Longitude", longitude);
        }else if (rgSs.getCheckedRadioButtonId()== R.id.rblrtJamek){
            report.put("Station", rblrtJamek.getText().toString().trim());
            report.put("Latitude", latitude);
            report.put("Longitude", longitude);
        }

        report.put("Description", etDesc.getText().toString().trim());

        collectionReference.add(report).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            DocumentReference documentReference = task.getResult();
                            String id = documentReference.getId();
                            documentReference.update("reportID", id);
                        }
                        Toast.makeText(ReportFormP.this, "Report send successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: user report is created for " + userID);
                        Intent intent = new Intent(ReportFormP.this, ReportHomeP.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(ReportFormP.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // Add the pending intent to the notification
       FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "Someone need help!",
                "Location: " + latitude + " " + longitude + " | Date Crime: " + tvRDate.getText().toString().trim(),
               getApplicationContext(), ReportFormP.this);
        notificationsSender.SendNotifications();

        db.collection("DeviceTokens").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                    CollectionReference cr = document.getReference().collection("Notifications");

                    Map<String, Object> data = new HashMap<>();
                    data.put("title", "Someone need help!");
                    data.put("message", "Location: " + latitude + " " + longitude + " | Date Crime: " + tvRDate.getText().toString().trim());
                    data.put("timestamp", Timestamp.now());
                    cr.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting documents.", e);
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReportFormP.this, ReportHomeP.class));
    }

}