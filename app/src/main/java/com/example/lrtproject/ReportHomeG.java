package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityReportHomeGBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportHomeG extends MenuGuard {

    ActivityReportHomeGBinding activityReportHomeGBinding;

    RecyclerView rv_report_gg;
    ArrayList<ReportG> reportGArrayList;
    ReportAdapterG reportAdapterG;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser guard;
    String guardID;
    String hold = "";
    SearchView svG;
    ItemTouchHelper itemTouchHelper;
    String usertoken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportHomeGBinding = ActivityReportHomeGBinding.inflate(getLayoutInflater());
        setContentView(activityReportHomeGBinding.getRoot());
        allocateActivityTitle("Report Guard");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        guard = mAuth.getCurrentUser();
        guardID = guard.getUid();

        svG = findViewById(R.id.searchViewGG);
        svG.clearFocus();
        svG.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String n) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String n) {
                filterList(n);
                return true;
            }
        });

        rv_report_gg = findViewById(R.id.rv_report_gg);
        rv_report_gg.setHasFixedSize(true);
        rv_report_gg.setLayoutManager(new LinearLayoutManager(this));

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_report_gg);

        reportGArrayList = new ArrayList<>();
        reportAdapterG = new ReportAdapterG(this, reportGArrayList);
        rv_report_gg.setAdapter(reportAdapterG);

        //hapus
        reportAdapterG.setDialog(new ReportAdapterA.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Accepted", "Declined"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReportHomeG.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //intent to start activity
                                //Intent intent = new Intent(getApplicationContext(), ReportFormA.class);
                                //put data in intent
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
                                collectionReference.document(reportGArrayList.get(pos).getDocumentId()).set(accept).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

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
                                        startActivity(new Intent(ReportHomeG.this, ReportHomeG.class));
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
                                        fetchDataReport();
                                        startActivity(new Intent(ReportHomeG.this, ReportHomeG.class));
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

    private void filterList(String n) {
        ArrayList<ReportG> flG = new ArrayList<>();
        for (ReportG reportG : reportGArrayList){
            if (reportG.getDescription().toLowerCase().contains(n.toLowerCase()) ||
                    reportG.getDateCrime().toLowerCase().contains(n.toLowerCase()) ||
                    reportG.getTypeOfCrime().toLowerCase().contains(n.toLowerCase()) ||
                    reportG.getStation().toLowerCase().contains(n.toLowerCase())){
                flG.add(reportG);
            }
        }
        if (flG.isEmpty()){

        }else {
            reportAdapterG.setFilteredList(flG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataReport();
    }

    private void fetchDataReport() {

        CollectionReference reportRef = db.collection("Reports");
        CollectionReference guardRef = db.collection("SecurityGuard");
        //Query query = reportRef.orderBy("DateCrime", Query.Direction.ASCENDING);

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

    private void DeclineData(String id){

        Map<String, Object> edited = new HashMap<>();
        edited.put("SecurityGuard", "none");
        edited.put("StatusReport", "Guard Unavailable");
        db.collection("Reports").document(id).set(edited).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                fetchDataReport();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            CollectionReference collectionReference;
            collectionReference = db.collection("Reports");

            AlertDialog.Builder builder =new AlertDialog.Builder(ReportHomeG.this);
            builder.setTitle("Do you want to delete your report?");
            builder.setIcon(R.drawable.ic_baseline_warning);
            builder.setMessage("Your report will be delete in our database.");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    collectionReference.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentID = documentSnapshot.getId();

                                        collectionReference
                                                .document(documentID)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ReportHomeG.this,"Successfully deleted", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ReportHomeG.this, ReportHomeG.class));
                                                        Log.d(TAG, "Report " + documentID + " deleted");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ReportHomeG.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(ReportHomeG.this,"Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    startActivity(new Intent(ReportHomeG.this, ReportHomeG.class));
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            reportGArrayList.remove(viewHolder.getAbsoluteAdapterPosition());
            //notify adapter
            reportAdapterG.notifyDataSetChanged();

        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayoutG.isDrawerOpen(GravityCompat.START))
            drawerLayoutG.closeDrawers();
        else
            startActivity(new Intent(ReportHomeG.this, HomeGuard.class));

    }

    private void sendNotification(String uid, String name){

        CollectionReference docRef = db.collection("DeviceTokens");
        docRef.whereEqualTo("Role", "Authority").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.w(TAG,"Listen failed.", error);
                    return;
                }
                for (DocumentSnapshot document : value.getDocuments()){
                    if (document.exists()){
                        usertoken = document.getString("DeviceToken");
                    }
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FcmNotificationsSender notificationsSender =
                        new FcmNotificationsSender(usertoken, "Guard Unavailable", "Please update the report",
                                getApplicationContext(),ReportHomeG.this);
                notificationsSender.SendNotifications();
            }
        },3000);

    }
}