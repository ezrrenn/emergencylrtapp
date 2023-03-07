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

import com.example.lrtproject.databinding.ActivityProfilePassengerBinding;
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

public class ProfilePassenger extends MenuPassenger {

    ActivityProfilePassengerBinding activityProfilePassengerBinding;

    TextView tvBName, tvBLast, tvBPhone, tvBEme, tvBEmail;
    private Button btnUpdate;
    private Button btnDelete;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfilePassengerBinding = ActivityProfilePassengerBinding.inflate(getLayoutInflater());
        setContentView(activityProfilePassengerBinding.getRoot());
        allocateActivityTitle("Profile");

        btnUpdate = findViewById(R.id.btnUpdate);
        tvBName = findViewById(R.id.tvBName);
        tvBLast = findViewById(R.id.tvBLast);
        tvBPhone = findViewById(R.id.tvBPhone);
        tvBEme = findViewById(R.id.tvBEme);
        tvBEmail = findViewById(R.id.tvBEmail);
        btnDelete = findViewById(R.id.btnDelete);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePassenger.this, UpdateProfileP.class);
                i.putExtra("First Name", tvBName.getText().toString().trim());
                i.putExtra("Last Name", tvBLast.getText().toString().trim());
                i.putExtra("Phone Number", tvBPhone.getText().toString().trim());
                i.putExtra("Emergency", tvBEme.getText().toString().trim());
                i.putExtra("Email", tvBEmail.getText().toString().trim());
                startActivity(i);
            }
        });

        DocumentReference documentReference = db.collection("Passengers").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvBName.setText(document.getString("First Name"));
                        tvBLast.setText(document.getString("Last Name"));
                        tvBPhone.setText(document.getString("Phone Number"));
                        tvBEme.setText(document.getString("Emergency"));
                        tvBEmail.setText(document.getString("Email"));
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
        String etvBEme = data.getStringExtra("Emergency");
        tvBEme.setText(etvBEme);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ProfilePassenger.this);
                builder.setTitle("Do you want to delete your account?");
                builder.setIcon(R.drawable.ic_baseline_warning);
                builder.setMessage("Your account will be deleted permanently. We gonna miss you.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("Passengers").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Passengers")
                                                    .document(documentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(ProfilePassenger.this,"Successfully deleted", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "User account deleted");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(ProfilePassenger.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            passenger.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        mAuth.signOut();
                                                        startActivity(new Intent(ProfilePassenger.this, RegisterActivity.class));
                                                        Log.d(TAG, "User authentication deleted");
                                                        finish();
                                                    } else {
                                                        try {
                                                            throw task.getException();
                                                        }catch (Exception e){
                                                            Toast.makeText(ProfilePassenger.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });

                                        }else{
                                            Toast.makeText(ProfilePassenger.this,"Failed", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(ProfilePassenger.this, HomePassenger.class));
    }

}