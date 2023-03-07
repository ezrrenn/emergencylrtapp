package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportFormA extends AppCompatActivity {

    TextView tvRnamee_h, tvRnamee2_h, tvRphonee_h, tvRcrimee_h, tvRdatee_h, tvRtimee_h,
            tvRstationn_h,tvRlatitudee_h, tvRlongitude_h, tvRdescc_h;
    RadioGroup rgStatuss_h;
    RadioButton rbS1, rbS2, rbS3;
    Button btnUpd, btnCan;

    Spinner spinnerGuard1, spinSts;
    ArrayList<String> listGuard;
    ArrayAdapter<String> adapterGuard;
    private QuerySnapshot securityGuard;
    String status ="";
    String nameS = "";
    String idS = "";
    String reportId = "";
    public String idSpin = "";

    String id, fName, Num, Type, Tarikh, Masa, Station,Desc;

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser authority;
    String authID;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form_a);

        progressDialog = new ProgressDialog(ReportFormA.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Save data...");

        tvRnamee_h = findViewById(R.id.tvRnamee_h);
        tvRnamee2_h = findViewById(R.id.tvRnamee2_h);
        tvRphonee_h = findViewById(R.id.tvRphonee_h);
        tvRcrimee_h = findViewById(R.id.tvRcrimee_h);
        tvRdatee_h = findViewById(R.id.tvRdatee_h);
        tvRtimee_h = findViewById(R.id.tvRtimee_h);
        tvRstationn_h = findViewById(R.id.tvRstationn_h);
        tvRlatitudee_h = findViewById(R.id.tvRlatitudee_h);
        tvRlongitude_h = findViewById(R.id.tvRlongitude_h);
        tvRdescc_h = findViewById(R.id.tvRdescc_h);
        btnUpd = findViewById(R.id.btnUpd);
        spinnerGuard1 = findViewById(R.id.spinnerGuard1);
        spinSts = findViewById(R.id.spinSts);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authority = mAuth.getCurrentUser();
        authID = authority.getUid();


        ArrayAdapter<CharSequence> adapterSts = ArrayAdapter.createFromResource(getApplicationContext(), R.array.status_guard, android.R.layout.simple_spinner_item);
        adapterSts.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinSts.setAdapter(adapterSts);
        spinSts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spinSts.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner
        db = FirebaseFirestore.getInstance();
        listGuard = new ArrayList<>();
        adapterGuard = new ArrayAdapter<>(ReportFormA.this, android.R.layout.simple_spinner_item, listGuard);
        adapterGuard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGuard1.setAdapter(adapterGuard);
        //adapterGuard = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listGuard);
        spinnerGuard1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if (adapterGuard != null)
                Toast.makeText(ReportFormA.this,adapterGuard.getItem(i),Toast.LENGTH_LONG).show();
                nameS = spinnerGuard1.getSelectedItem().toString();
                idS = securityGuard.getDocuments().get(i).getId();
                //nameS = securityGuard.getDocuments().get(i).getId();
                Log.e("ID Guard", securityGuard.getDocuments().get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.collection("SecurityGuard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                securityGuard = queryDocumentSnapshots;
                if (queryDocumentSnapshots.size()>0){
                    listGuard.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        listGuard.add(doc.getString("First Name"));
                    }adapterGuard.notifyDataSetChanged();
                }else {
                    Toast.makeText(ReportFormA.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportFormA.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        btnUpd = findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (status.length()>0 && nameS.length()>0){
                    saveData(status,nameS);
                }
            }
        });

        btnCan = findViewById(R.id.btnCan);
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportFormA.this, ReportHomeA.class));
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            reportId = intent.getStringExtra("ReportId");
            tvRnamee_h.setText(intent.getStringExtra("FirstName"));
            tvRnamee2_h.setText(intent.getStringExtra("LastName"));
            tvRphonee_h.setText(intent.getStringExtra("PhoneNumber"));
            tvRcrimee_h.setText(intent.getStringExtra("TypeOfCrime"));
            tvRdatee_h.setText(intent.getStringExtra("DateCrime"));
            tvRtimee_h.setText(intent.getStringExtra("TimeCrime"));
            tvRstationn_h.setText(intent.getStringExtra("Station"));
            tvRlatitudee_h.setText(intent.getStringExtra("Latitude"));
            tvRlongitude_h.setText(intent.getStringExtra("Longitude"));
            tvRdescc_h.setText(intent.getStringExtra("Description"));
        }

    }

    private void saveData(String status3, String guard3){
        Map<String, Object> form = new HashMap<>();
        form.put("FirstName", tvRnamee_h.getText().toString());
        form.put("LastName", tvRnamee2_h.getText().toString());
        form.put("PhoneNumber", tvRphonee_h.getText().toString());
        form.put("TypeOfCrime", tvRcrimee_h.getText().toString());
        form.put("DateCrime", tvRdatee_h.getText().toString());
        form.put("TimeCrime", tvRtimee_h.getText().toString());
        form.put("Station", tvRstationn_h.getText().toString());
        form.put("Latitude", tvRlatitudee_h.getText().toString());
        form.put("Longitude", tvRlongitude_h.getText().toString());
        form.put("Description", tvRdescc_h.getText().toString());
        form.put("StatusReport", status);
        form.put("GuardID", idS);
        form.put("SecurityGuard", nameS);

        progressDialog.show();
        db.collection("Reports").document(reportId).set(form).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Success Update",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(ReportFormA.this, ReportHomeA.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Failed Update",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReportFormA.this, ReportHomeA.class));
    }

}