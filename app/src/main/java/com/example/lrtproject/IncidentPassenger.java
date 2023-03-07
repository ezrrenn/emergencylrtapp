package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
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

import com.example.lrtproject.databinding.ActivityIncidentFormPBinding;
import com.example.lrtproject.databinding.ActivityIncidentPassengerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class IncidentPassenger extends MenuPassenger {

    ActivityIncidentPassengerBinding activityIncidentPassengerBinding;
    FloatingActionButton addIncident, mapPage;
    RecyclerView rv_incident;
    ArrayList<PassengerIncident> incidentPArrayList;
    IncidentAdapterP incidentAdapterP;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;
    ProgressDialog progressDialog;
    ItemTouchHelper itemTouchHelper;
    SearchView searchVieww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIncidentPassengerBinding = ActivityIncidentPassengerBinding.inflate(getLayoutInflater());
        setContentView(activityIncidentPassengerBinding.getRoot());
        allocateActivityTitle("My Incidents");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();

        searchVieww = findViewById(R.id.searchView11);
        searchVieww.clearFocus();
        searchVieww.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        addIncident = findViewById(R.id.add_incident);
        mapPage = findViewById(R.id.mapPage);
        rv_incident = findViewById(R.id.rv_incident);
        rv_incident.setHasFixedSize(true);
        rv_incident.setLayoutManager(new LinearLayoutManager(this));

        incidentPArrayList = new ArrayList<>();
        incidentAdapterP = new IncidentAdapterP(this, incidentPArrayList);
        rv_incident.setAdapter(incidentAdapterP);

        IncidentChangeListener();

        addIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IncidentPassenger.this, BlankActivity.class));
            }
        });
        mapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IncidentPassenger.this, BlankActivity.class));
            }
        });
    }

    private void filterList(String text) {
        ArrayList<PassengerIncident> filteredList = new ArrayList<>();
        for (PassengerIncident pi : incidentPArrayList){
            if (pi.getStation() != null && pi.getStation().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(pi);
            }
        }
        if (filteredList.isEmpty()){

        }else {
            incidentAdapterP.setFilteredList(filteredList);
        }
    }

    private void IncidentChangeListener() {

        CollectionReference collectionReference;
        collectionReference = db.collection("Passengers").document(userID).collection("My Incidents");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                incidentPArrayList.clear();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    String documentId = snapshot.getId();
                    PassengerIncident passengerIncident = new PassengerIncident(
                            snapshot.getString("SharingFor"),
                            snapshot.getString("Gender"),
                            snapshot.getString("EstimateDate"),
                            snapshot.getString("EstimateTime"),
                            snapshot.getString("Station"),
                            snapshot.getString("ListOfCrime"),
                            snapshot.getString("Latitude"),
                            snapshot.getString("Longitude"),
                            snapshot.getString("IncidentDescription")
                    );
                    passengerIncident.setDocId(documentId);
                    incidentPArrayList.add(passengerIncident);
                    Log.d(TAG, "Success detect" + documentId);
                }
                incidentAdapterP.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fail detect");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(IncidentPassenger.this, HomePassenger.class));

    }
}
