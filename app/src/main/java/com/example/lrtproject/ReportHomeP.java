package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityHomePassengerBinding;
import com.example.lrtproject.databinding.ActivityReportHomePBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportHomeP extends MenuPassenger {

    ActivityReportHomePBinding activityReportHomePBinding;

    FloatingActionButton addReport;
    RecyclerView rv_report;
    ArrayList<ReportP> reportPArrayList;
    ReportAdapterP reportAdapterP;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;
    ProgressDialog progressDialog;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReportHomePBinding = ActivityReportHomePBinding.inflate(getLayoutInflater());
        setContentView(activityReportHomePBinding.getRoot());
        allocateActivityTitle("Emergency Report");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();

        // = new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        //progressDialog.setMessage("Fetching data....");
        //progressDialog.show();

        //search atas
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        addReport = findViewById(R.id.add_report);
        rv_report = findViewById(R.id.rv_report);
        rv_report.setHasFixedSize(true);
        rv_report.setLayoutManager(new LinearLayoutManager(this));

        //progressDialog = new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        //progressDialog.setMessage("Fetching data....");
        //progressDialog.show();

        reportPArrayList = new ArrayList<>();
        reportAdapterP = new ReportAdapterP(this, reportPArrayList);
        rv_report.setAdapter(reportAdapterP);

        ReportChangeListener();

        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportHomeP.this, ReportFormP.class));
            }
        });

    }

    private void searchData(String query) {

    }
    private void filterList(String text) {
        ArrayList<ReportP> filteredList = new ArrayList<>();
        for (ReportP reportP : reportPArrayList){
            if (reportP.getDescription().toLowerCase().contains(text.toLowerCase()) ||
            reportP.getDateCrime().toLowerCase().contains(text.toLowerCase()) ||
            reportP.getTypeOfCrime().toLowerCase().contains(text.toLowerCase()) ||
            reportP.getStation().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(reportP);
            }
        }
        if (filteredList.isEmpty()){

        }else {
            reportAdapterP.setFilteredList(filteredList);
        }
    }

    private void ReportChangeListener() {

        CollectionReference collectionReference;
        collectionReference = db.collection("Passengers").document(userID).collection("Report");
        Query query = collectionReference.orderBy("DateCrime", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        reportPArrayList.clear();
                        for (DocumentSnapshot snapshot: task.getResult()){
                            String documentId = snapshot.getId();
                            ReportP reportP = new ReportP(
                                    snapshot.getString("ID"),
                                    snapshot.getString("FirstName"),
                                    snapshot.getString("LastName"),
                                    snapshot.getString("PhoneNumber"),
                                    snapshot.getString("DateCrime"),
                                    snapshot.getString("TypeOfCrime"),
                                    snapshot.getString("Emergency"),
                                    snapshot.getString("TimeCrime"),
                                    snapshot.getString("Station"),
                                    snapshot.getString("Description"),
                                    snapshot.getString("StatusReport")
                            );
                            reportP.setDocumentId(documentId);
                            reportPArrayList.add(reportP);
                            Log.d(TAG,"Success detect" + documentId);
                            //if (progressDialog.isShowing())
                                //progressDialog.dismiss();
                        }reportAdapterP.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Fail detect");
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(ReportHomeP.this, HomePassenger.class));

    }

}