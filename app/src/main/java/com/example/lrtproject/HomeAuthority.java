package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityHomeAuthorityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeAuthority extends MenuAuthority {

    ActivityHomeAuthorityBinding activityHomeAuthorityBinding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userID;
    private FirebaseUser authority;
    TextView txtAuth;
    CardView ReportAA, NotiA, SecurityA, ProfileA;
    RecyclerView urgent_rvv;
    ArrayList<ReportA> reportAArrayList;
    ReportAdapterA reportAdapterA;
    ArrayList<String> listUserId2 = new ArrayList<>();
    ProgressDialog progressDialog;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeAuthorityBinding = ActivityHomeAuthorityBinding.inflate(getLayoutInflater());
        setContentView(activityHomeAuthorityBinding.getRoot());
        allocateActivityTitle("Home Authority");

        progressDialog = new ProgressDialog(HomeAuthority.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Save data...");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authority = mAuth.getCurrentUser();
        userID = authority.getUid();

        txtAuth = findViewById(R.id.txtAuth);
        ReportAA = findViewById(R.id.ReportAA);
        NotiA = findViewById(R.id.NotiA);
        SecurityA = findViewById(R.id.SecurityA);
        ProfileA = findViewById(R.id.ProfileA);

        urgent_rvv = findViewById(R.id.urgent_rvv);
        urgent_rvv.setHasFixedSize(true);
        urgent_rvv.setLayoutManager(new LinearLayoutManager(this));

        reportAArrayList = new ArrayList<>();
        reportAdapterA = new ReportAdapterA(this, reportAArrayList);
        urgent_rvv.setAdapter(reportAdapterA);

        sv = findViewById(R.id.sv);
        sv.clearFocus();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String d) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String d) {
                filterList(d);
                return true;
            }
        });
        //Hello auth
        txtAuth.setText(authority.getEmail());

        //menu page
        ReportAA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeAuthority.this,ReportHomeA.class);
                startActivity(intent);
            }
        });
        NotiA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeAuthority.this,NotificationAuthority.class);
                startActivity(intent);
            }
        });
        SecurityA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeAuthority.this,SecurityListA.class);
                startActivity(intent);
            }
        });
        ProfileA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeAuthority.this,ProfileAuthority.class);
                startActivity(intent);
            }
        });

        //hapus
        reportAdapterA.setDialog(new ReportAdapterA.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Response", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeAuthority.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //intent to start activity
                                Intent intent = new Intent(getApplicationContext(), ReportFormA.class);
                                //put data in intent
                                intent.putExtra("ReportId", reportAArrayList.get(pos).getDocumentId());
                                intent.putExtra("FirstName", reportAArrayList.get(pos).getFirstName());
                                intent.putExtra("LastName", reportAArrayList.get(pos).getLastName());
                                intent.putExtra("PhoneNumber", reportAArrayList.get(pos).getPhoneNumber());
                                intent.putExtra("TypeOfCrime", reportAArrayList.get(pos).getTypeOfCrime());
                                intent.putExtra("DateCrime", reportAArrayList.get(pos).getDateCrime());
                                intent.putExtra("TimeCrime", reportAArrayList.get(pos).getTimeCrime());
                                intent.putExtra("Station", reportAArrayList.get(pos).getStation());
                                intent.putExtra("Latitude", reportAArrayList.get(pos).getLatitude());
                                intent.putExtra("Longitude", reportAArrayList.get(pos).getLongitude());
                                intent.putExtra("Description", reportAArrayList.get(pos).getDescription());
                                //start activity
                                startActivity(intent);
                                break;
                            case 1:
                                deleteData(reportAArrayList.get(pos).getDocumentId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

    }

    private void filterList(String newdata) {

        ArrayList<ReportA> fl = new ArrayList<>();
        for (ReportA reportA : reportAArrayList){
            if (reportA.getDescription().toLowerCase().contains(newdata.toLowerCase()) ||
                    reportA.getStatusReport().toLowerCase().contains(newdata.toLowerCase()) ||
                    reportA.getFirstName().toLowerCase().contains(newdata.toLowerCase()) ||
                    reportA.getDateCrime().toLowerCase().contains(newdata.toLowerCase()) ||
                    reportA.getStation().toLowerCase().contains(newdata.toLowerCase())){
                fl.add(reportA);
            }
        }
        if (fl.isEmpty()){

        }else {
            reportAdapterA.setFilteredList(fl);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        homeAChangeListener();
    }

    private void homeAChangeListener() {
        CollectionReference collectionReference;
        collectionReference = db.collection("Reports");
        Query query = collectionReference.orderBy("StatusReport", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //reportAArrayList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    /*System.out.println("Report Id : " + document.getId());
                    System.out.println("Description: " + document.getString("Description"));*/

                    ReportA reportA = new ReportA(
                            document.getString("Document Id"),
                            document.getString("FirstName"),
                            document.getString("LastName"),
                            document.getString("PhoneNumber"),
                            document.getString("TypeOfCrime"),
                            document.getString("DateCrime"),
                            document.getString("TimeCrime"),
                            document.getString("Station"),
                            document.getString("Latitude"),
                            document.getString("Longitude"),
                            document.getString("Description"),
                            document.getString("StatusReport"),
                            document.getString("SecurityGuard"),
                            document.getString("GuardID")
                    );
                    reportA.setDocumentId(documentId);
                    reportAArrayList.add(reportA);
                    /*String documentId = document.getId();
                    System.out.println("Document Id: " + documentId);
                    listUserId.add(documentId);*/

                }reportAdapterA.notifyDataSetChanged();
                //System.out.println("User List Size: " + listUserId.size());

                //reportAChangeListener2();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fail detect");
            }
        });
    }

    private void deleteData(String id){
        progressDialog.show();
        db.collection("Reports").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Failed delete", Toast.LENGTH_SHORT).show();
                }
                homeAChangeListener();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(HomeAuthority.this, HomeAuthority.class));

    }
}