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

import com.example.lrtproject.databinding.ActivityProfileAuthorityBinding;
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

public class ProfileAuthority extends MenuAuthority {

    ActivityProfileAuthorityBinding activityProfileAuthorityBinding;
    TextView tvBNameA, tvBLastA, tvBPhoneA, tvBDutyA, tvBEmailA;
    private Button btnEdit;
    private Button btnDel;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser authority;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileAuthorityBinding = ActivityProfileAuthorityBinding.inflate(getLayoutInflater());
        setContentView(activityProfileAuthorityBinding.getRoot());
        allocateActivityTitle("Profile");

        btnEdit = findViewById(R.id.btnEdit);
        tvBNameA = findViewById(R.id.tvBNameA);
        tvBLastA = findViewById(R.id.tvBLastA);
        tvBPhoneA = findViewById(R.id.tvBPhoneA);
        tvBDutyA = findViewById(R.id.tvBDutyA);
        tvBEmailA = findViewById(R.id.tvBEmailA);
        btnDel = findViewById(R.id.btnDel);
        btnEdit = findViewById(R.id.btnEdit);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        authority = mAuth.getCurrentUser();
        userID = authority.getUid();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileAuthority.this, UpdateProfileA.class);
                i.putExtra("First Name", tvBNameA.getText().toString().trim());
                i.putExtra("Last Name", tvBLastA.getText().toString().trim());
                i.putExtra("Phone Number", tvBPhoneA.getText().toString().trim());
                //i.putExtra("Duty", tvBDutyA.getText().toString().trim());
                i.putExtra("Email", tvBEmailA.getText().toString().trim());
                startActivity(i);
            }
        });

        DocumentReference documentReference = db.collection("Authority").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvBNameA.setText(document.getString("First Name"));
                        tvBLastA.setText(document.getString("Last Name"));
                        tvBPhoneA.setText(document.getString("Phone Number"));
                        tvBDutyA.setText(document.getString("Duty"));
                        tvBEmailA.setText(document.getString("Email"));
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

        //panggil emergency contact dari update profile
        Intent data = getIntent();
        String etvBDuty = data.getStringExtra("Duty");
        tvBDutyA.setText(etvBDuty);

        //delete
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ProfileAuthority.this);
                builder.setTitle("Do you want to delete your account?");
                builder.setIcon(R.drawable.ic_baseline_warning);
                builder.setMessage("Your account will be deleted permanently. Thank you for your hard work!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("Authority").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Authority")
                                                    .document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(ProfileAuthority.this,"Successfully deleted", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "User account deleted");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ProfileAuthority.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            authority.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        mAuth.signOut();
                                                        startActivity(new Intent(ProfileAuthority.this, RegisterActivity.class));
                                                        Log.d(TAG, "User authentication deleted");
                                                        finish();
                                                    } else {
                                                        try {
                                                            throw task.getException();
                                                        }catch (Exception e){
                                                            Toast.makeText(ProfileAuthority.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });

                                        }else{
                                            Toast.makeText(ProfileAuthority.this,"Failed", Toast.LENGTH_SHORT).show();
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(ProfileAuthority.this, HomeAuthority.class));

    }
}