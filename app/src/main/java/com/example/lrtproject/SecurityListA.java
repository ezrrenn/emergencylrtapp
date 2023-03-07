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
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivitySecurityListBinding;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SecurityListA extends MenuAuthority {

    ActivitySecurityListBinding activitySecurityListBinding;

    RecyclerView rv_s;
    ArrayList<SecurityA> securityAArrayList;
    SecurityListAdapter securityListAdapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySecurityListBinding = ActivitySecurityListBinding.inflate(getLayoutInflater());
        setContentView(activitySecurityListBinding.getRoot());
        allocateActivityTitle("List of Security Guard");

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rv_s = findViewById(R.id.rv_s);
        rv_s.setHasFixedSize(true);
        rv_s.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelper = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper.attachToRecyclerView(rv_s);

        securityAArrayList = new ArrayList<>();
        securityListAdapter = new SecurityListAdapter(this, securityAArrayList);
        rv_s.setAdapter(securityListAdapter);

        ListSecurityChangeListener();
    }

    private void ListSecurityChangeListener() {

        CollectionReference collectionReference;
        collectionReference = db.collection("SecurityGuard");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                securityAArrayList.clear();
                for (DocumentSnapshot snapshot: task.getResult()){
                    String documentId = snapshot.getId();
                    SecurityA securityA = new SecurityA(
                            snapshot.getId(),
                            snapshot.getString("First Name"),
                            snapshot.getString("Last Name"),
                            snapshot.getString("Email"),
                            snapshot.getString("Phone Number"),
                            snapshot.getString("Duty")
                    );
                    securityAArrayList.add(securityA);
                    Log.d(TAG,"Success detect" + documentId);
                    //if (progressDialog.isShowing())
                    //progressDialog.dismiss();
                }securityListAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Fail detect");
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback2 = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            CollectionReference collectionReference;
            collectionReference = db.collection("SecurityGuard");

            AlertDialog.Builder builder =new AlertDialog.Builder(SecurityListA.this);
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
                                                        Toast.makeText(SecurityListA.this,"Successfully deleted", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "Report " + documentID + " deleted");
                                                        ListSecurityChangeListener();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SecurityListA.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(SecurityListA.this,"Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    startActivity(new Intent(SecurityListA.this, SecurityListA.class));
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            securityAArrayList.remove(viewHolder.getAbsoluteAdapterPosition());
            //notify adapter
            securityListAdapter.notifyDataSetChanged();

        }
    };
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(SecurityListA.this, HomeAuthority.class));

    }
}