package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lrtproject.databinding.ActivityProfileGuardBinding;
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

public class ProfileGuard extends MenuGuard {
    ActivityProfileGuardBinding activityProfileGuardBinding;

    TextView tvBNameG, tvBLastG, tvBPhoneG, tvBDutyG, tvBEmailG;
    private Button btnEditG;
    private Button btnDelG;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser guard;
    String guardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileGuardBinding = ActivityProfileGuardBinding.inflate(getLayoutInflater());
        setContentView(activityProfileGuardBinding.getRoot());
        allocateActivityTitle("Profile");

        btnEditG = findViewById(R.id.btnEditG);
        tvBNameG = findViewById(R.id.tvBNameG);
        tvBLastG = findViewById(R.id.tvBLastG);
        tvBPhoneG = findViewById(R.id.tvBPhoneG);
        tvBDutyG = findViewById(R.id.tvBDutyG);
        tvBEmailG = findViewById(R.id.tvBEmailG);
        btnDelG = findViewById(R.id.btnDelG);
        btnEditG = findViewById(R.id.btnEditG);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        guard = mAuth.getCurrentUser();
        guardID = guard.getUid();

        btnEditG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileGuard.this, UpdateProfileG.class);
                i.putExtra("First Name", tvBNameG.getText().toString().trim());
                i.putExtra("Last Name", tvBLastG.getText().toString().trim());
                i.putExtra("Phone Number", tvBPhoneG.getText().toString().trim());
                //i.putExtra("Duty", tvBDutyA.getText().toString().trim());
                i.putExtra("Email", tvBEmailG.getText().toString().trim());
                startActivity(i);
            }
        });

        DocumentReference documentReference = db.collection("SecurityGuard").document(guardID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvBNameG.setText(document.getString("First Name"));
                        tvBLastG.setText(document.getString("Last Name"));
                        tvBPhoneG.setText(document.getString("Phone Number"));
                        tvBDutyG.setText(document.getString("Duty"));
                        tvBEmailG.setText(document.getString("Email"));
                        Log.w(TAG, "Success.");
                    }else{
                        Log.d(TAG, "DocumentSnapshot data: " + guardID);
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.w(TAG, "Error getting documents.");
                }
            }
        });

        //panggil emergency contact dari update profile
        Intent data = getIntent();
        String etvBDutyG = data.getStringExtra("Duty");
        tvBDutyG.setText(etvBDutyG);

        //delete
        btnDelG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ProfileGuard.this);
                builder.setTitle("Are you gonna leave us?");
                builder.setMessage("Good luck in everything you plan for your life! Thank you for serving us with so good attitude!");
                builder.setIcon(R.drawable.ic_baseline_warning);
                builder.setMessage("Your account will be deleted permanently. Thank you for your hard work!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("SecurityGuard").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("SecurityGuard")
                                                    .document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(ProfileGuard.this,"Successfully deleted", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "User account deleted");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ProfileGuard.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            guard.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        mAuth.signOut();
                                                        startActivity(new Intent(ProfileGuard.this, RegisterActivity.class));
                                                        Log.d(TAG, "User authentication deleted");
                                                        finish();
                                                    } else {
                                                        try {
                                                            throw task.getException();
                                                        }catch (Exception e){
                                                            Toast.makeText(ProfileGuard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });

                                        }else{
                                            Toast.makeText(ProfileGuard.this,"Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (drawerLayoutG.isDrawerOpen(GravityCompat.START))
            drawerLayoutG.closeDrawers();
        else
            startActivity(new Intent(ProfileGuard.this, HomeGuard.class));

    }
}