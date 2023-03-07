package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.example.lrtproject.databinding.ActivityHomeGuardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeGuard extends MenuGuard {

    ActivityHomeGuardBinding activityHomeGuardBinding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String guardID;
    private FirebaseUser guard;
    TextView txtG;
    CardView ReportS, ProfileS, locPassenger, notit;
    RecyclerView report_rvv;
    ArrayList<ReportG> reportGArrayList;
    ReportAdapterG reportAdapterG;
  //  ArrayList<String> listUserId2 = new ArrayList<>();
    Button btnTry2;
    SearchView svGuard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeGuardBinding = ActivityHomeGuardBinding.inflate(getLayoutInflater());
        setContentView(activityHomeGuardBinding.getRoot());
        allocateActivityTitle("Home Security Guard");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        guard = mAuth.getCurrentUser();
        guardID = guard.getUid();

        txtG = findViewById(R.id.txtG);
        ReportS = findViewById(R.id.ReportS);
        ProfileS = findViewById(R.id.ProfileS);
        locPassenger = findViewById(R.id.locPassenger);
        notit = findViewById(R.id.noti_all);


        report_rvv = findViewById(R.id.report_rvv);
        report_rvv.setHasFixedSize(true);
        report_rvv.setLayoutManager(new LinearLayoutManager(this));

        reportGArrayList = new ArrayList<>();
        reportAdapterG = new ReportAdapterG(this, reportGArrayList);
        report_rvv.setAdapter(reportAdapterG);

        svGuard = findViewById(R.id.searchViewGE);
        svGuard.clearFocus();
        svGuard.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String z) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String z) {
                filterList(z);
                return true;
            }
        });

        //Hello auth
        txtG.setText(guard.getEmail());

        btnTry2 = findViewById(R.id.buttonMAP);
        btnTry2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeGuard.this, CheckPassenger.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //menu page
        ReportS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeGuard.this, ReportHomeG.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        ProfileS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeGuard.this,ProfileGuard.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        locPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeGuard.this,BlankGuardActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        notit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeGuard.this, NotificationGuard.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        //hapus
        reportAdapterG.setDialog(new ReportAdapterA.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Accepted", "Declined"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeGuard.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Map<String, Object> accept = new HashMap<>();
                                accept.put("PassengerName", reportGArrayList.get(pos).getFirstName());
                                accept.put("PhoneNumber", reportGArrayList.get(pos).getPhoneNumber());
                                accept.put("TypeOfCrime", reportGArrayList.get(pos).getTypeOfCrime());
                                accept.put("DateCrime", reportGArrayList.get(pos).getDateCrime());
                                accept.put("TimeCrime", reportGArrayList.get(pos).getTimeCrime());
                                accept.put("Station", reportGArrayList.get(pos).getStation());
                                accept.put("Latitude", reportGArrayList.get(pos).getLatitude());
                                accept.put("Longitude", reportGArrayList.get(pos).getLongitude());
                                accept.put("Description", reportGArrayList.get(pos).getDescription());
                                accept.put("StatusGuard", "Accepted");

                                CollectionReference collectionReference;
                                collectionReference = db.collection("SecurityGuard").document(guardID).collection("Cases");
                                collectionReference.add(accept).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                    }
                                });
                                Map<String, Object> change = new HashMap<>();
                                change.put("ReportId", reportGArrayList.get(pos).getDocumentId());
                                change.put("FirstName", reportGArrayList.get(pos).getFirstName());
                                change.put("LastName", reportGArrayList.get(pos).getLastName());
                                change.put("PhoneNumber", reportGArrayList.get(pos).getPhoneNumber());
                                change.put("TypeOfCrime", reportGArrayList.get(pos).getTypeOfCrime());
                                change.put("DateCrime", reportGArrayList.get(pos).getDateCrime());
                                change.put("TimeCrime", reportGArrayList.get(pos).getTimeCrime());
                                change.put("Station", reportGArrayList.get(pos).getStation());
                                change.put("Latitude", reportGArrayList.get(pos).getLatitude());
                                change.put("Longitude", reportGArrayList.get(pos).getLongitude());
                                change.put("Description", reportGArrayList.get(pos).getDescription());
                                change.put("SecurityGuard", reportGArrayList.get(pos).getSecurityGuard());
                                change.put("StatusGuard", "Available");
                                change.put("StatusReport", "Accepted");
                                change.put("GuardID", reportGArrayList.get(pos).getIDGuard());
                                //DeclineData(reportGArrayList.get(pos).getDocumentId());

                                db.collection("Reports").document(reportGArrayList.get(pos).getDocumentId()).update(change).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //fetchDataReport();
                                        startActivity(new Intent(HomeGuard.this, HomeGuard.class));
                                    }
                                });
                                //start activity
                                //startActivity(intent);
                                break;
                            case 1:
                                Map<String, Object> edited = new HashMap<>();
                                edited.put("ReportId", reportGArrayList.get(pos).getDocumentId());
                                edited.put("FirstName", reportGArrayList.get(pos).getFirstName());
                                edited.put("LastName", reportGArrayList.get(pos).getLastName());
                                edited.put("PhoneNumber", reportGArrayList.get(pos).getPhoneNumber());
                                edited.put("TypeOfCrime", reportGArrayList.get(pos).getTypeOfCrime());
                                edited.put("DateCrime", reportGArrayList.get(pos).getDateCrime());
                                edited.put("TimeCrime", reportGArrayList.get(pos).getTimeCrime());
                                edited.put("Station", reportGArrayList.get(pos).getStation());
                                edited.put("Latitude", reportGArrayList.get(pos).getLatitude());
                                edited.put("Longitude", reportGArrayList.get(pos).getLongitude());
                                edited.put("Description", reportGArrayList.get(pos).getDescription());
                                edited.put("SecurityGuard", "none");
                                edited.put("StatusReport", "Guard Unavailable");
                                //DeclineData(reportGArrayList.get(pos).getDocumentId());

                                db.collection("Reports").document(reportGArrayList.get(pos).getDocumentId()).set(edited).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //fetchDataReport();
                                        startActivity(new Intent(HomeGuard.this, HomeGuard.class));
                                    }
                                });
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataReport();
    }

    private void filterList(String z) {
        ArrayList<ReportG> flg = new ArrayList<>();
        for (ReportG reportG : reportGArrayList){
            if (reportG.getDescription().toLowerCase().contains(z.toLowerCase()) ||
                    reportG.getDateCrime().toLowerCase().contains(z.toLowerCase()) ||
                    reportG.getTypeOfCrime().toLowerCase().contains(z.toLowerCase()) ||
                    reportG.getStation().toLowerCase().contains(z.toLowerCase()) ||
            reportG.getFirstName().toLowerCase().contains(z.toLowerCase())){
                flg.add(reportG);
            }
        }
        if (flg.isEmpty()){

        }else {
            reportAdapterG.setFilteredList(flg);
        }
    }

    private void fetchDataReport() {
        CollectionReference reportRef = db.collection("Reports");

        reportRef.whereEqualTo("GuardID", guardID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();

                                ReportG reportG = new ReportG(
                                        document.getString("Document Id"),
                                        document.getString("FirstName"),
                                        document.getString("LastName"),
                                        document.getString("PhoneNumber"),
                                        document.getString("TypeOfCrime"),
                                        document.getString("DateCrime"),
                                        document.getString("TimeCrime"),
                                        document.getString("Station"),
                                        document.getString("Description"),
                                        document.getString("SecurityGuard"),
                                        document.getString("StatusGuard"),
                                        document.getString("StatusReport"),
                                        document.getString("Latitude"),
                                        document.getString("Longitude"),
                                        document.getString("GuardID")
                                );
                                reportG.setDocumentId(documentId);
                                reportGArrayList.add(reportG);
                            }reportAdapterG.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting reports: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutG.isDrawerOpen(GravityCompat.START))
            drawerLayoutG.closeDrawers();
        else
            startActivity(new Intent(HomeGuard.this, HomeGuard.class));

    }


}