package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityHomeAuthorityBinding;
import com.example.lrtproject.databinding.ActivityReportHomeABinding;
import com.example.lrtproject.databinding.ActivityReportHomePBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReportHomeA extends MenuAuthority{

    ActivityReportHomeABinding reportHomeABinding;

    RecyclerView rv_report_h;
    ArrayList<ReportA> reportAArrayList;
    ReportAdapterA reportAdapterA;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser authority;
    String userID;
    ProgressDialog progressDialog;
    ArrayList<String> listUserId = new ArrayList<>();
    ItemTouchHelper itemTouchHelper;
    SearchView searchViewAA;

    TextView FirstName, LastName, PhoneNumber, TypeOfCrime, DateCrime, TimeCrime, Station, Description, StatusReport;
    TextView latitude, longitude, tvOpt;
    Button btnUpdateA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportHomeABinding = ActivityReportHomeABinding.inflate(getLayoutInflater());
        setContentView(reportHomeABinding.getRoot());
        allocateActivityTitle("Report Authority");

        // System.out.println("1");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authority = mAuth.getCurrentUser();
        userID = authority.getUid();

        // System.out.println("2");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data....");
        progressDialog.show();

        rv_report_h = findViewById(R.id.rv_report_h);
        rv_report_h.setHasFixedSize(true);
        rv_report_h.setLayoutManager(new LinearLayoutManager(this));
        /*itemTouchHelper = new ItemTouchHelper(simpleCallBackA);
        itemTouchHelper.attachToRecyclerView(rv_report_h);*/

        //System.out.println("3");


        //System.out.println("4");

        searchViewAA = findViewById(R.id.searchViewAA);
        searchViewAA.clearFocus();
        searchViewAA.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        reportAArrayList = new ArrayList<>();
        reportAdapterA = new ReportAdapterA(this, reportAArrayList);
        rv_report_h.setAdapter(reportAdapterA);

        //hapus
        reportAdapterA.setDialog(new ReportAdapterA.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Response", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReportHomeA.this);
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
                                String docId = reportAArrayList.get(pos).getDocumentId();
                                deleteData(docId);
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        // System.out.println("5");
    }

    private void filterList(String s) {
        ArrayList<ReportA> filteredList = new ArrayList<>();
        for (ReportA reportA : reportAArrayList){
            if (reportA.getDescription().toLowerCase().contains(s.toLowerCase())||
                    reportA.getStatusReport().toLowerCase().contains(s.toLowerCase()) ||
                    reportA.getFirstName().toLowerCase().contains(s.toLowerCase()) ||
                    reportA.getDateCrime().toLowerCase().contains(s.toLowerCase()) ||
                    reportA.getTypeOfCrime().toLowerCase().contains(s.toLowerCase()) ||
                    reportA.getStation().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(reportA);
            }
        }
        if (filteredList.isEmpty()){

        }else {
            reportAdapterA.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reportAChangeListener();
    }

    private void reportAChangeListener() {
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
                    progressDialog.dismiss();
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
                progressDialog.dismiss();
            }
        });
    }

    private void reportAChangeListener2() {

        System.out.println("size: " + listUserId.size());
        for (int i = 0; i < listUserId.size(); i++) {
            CollectionReference colRef;
            colRef = db.collection("Passengers/" + listUserId.get(i) + "/Report");

            colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    //reportAArrayList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentId = document.getId();
                        System.out.println("Report Id : " + document.getId());
                        System.out.println("Description: " + document.getString("Description"));
                        ReportA reportA = new ReportA(
                                document.getString("ReportId"),
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
                        reportAArrayList.add(reportA);
                        progressDialog.dismiss();

                    }
                    reportAdapterA.notifyDataSetChanged();
                }
            });
        }
    }

    private void deleteData(String id){
        progressDialog.show();
        db.collection("Reports").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Failed delete", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(ReportHomeA.this, ReportHomeA.class));
                reportAChangeListener();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(ReportHomeA.this, HomeAuthority.class));
    }
}