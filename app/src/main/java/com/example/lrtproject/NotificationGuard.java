package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lrtproject.databinding.ActivityNotificationBinding;
import com.example.lrtproject.databinding.ActivityNotificationGuardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationGuard extends MenuGuard {

    ActivityNotificationGuardBinding activityNotificationGuardBinding;
    RecyclerView rv_notig;
    ArrayList<NotiG> notiGList;
    NotiGuardAdapter notiGuardAdapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userID;
    ArrayList<String> listNoti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationGuardBinding = ActivityNotificationGuardBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationGuardBinding.getRoot());
        allocateActivityTitle("Communications");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        rv_notig = findViewById(R.id.rv_notiG);
        rv_notig.setHasFixedSize(true);
        rv_notig.setLayoutManager(new LinearLayoutManager(this));


        notiGList = new ArrayList<>();
        notiGuardAdapter = new NotiGuardAdapter(this, notiGList);
        rv_notig.setAdapter(notiGuardAdapter);

        retrieveNotiG();

    }

    private void retrieveNotiG() {
        CollectionReference cr;
        cr = db.collection("DeviceTokens");
        Query query = cr.whereEqualTo("ID", currentUser.getUid());
        //System.out.println("TENGOKKKKKK" + currentUser.getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String documentId = doc.getId();
                        //System.out.println("NI TENGOK JUGAAAAAa" + documentId);
                        cr.document(documentId)
                                .collection("Notifications").orderBy("timestamp", Query.Direction.DESCENDING)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                                String documentID = ds.getId();
                                                //System.out.println("HAI TENGOK NI " + documentID);
                                                NotiG notis = new NotiG(
                                                        ds.getString("title"),
                                                        ds.getString("message"),
                                                        ds.getTimestamp("timestamp")
                                                );
                                                notis.setDocumentId(documentID);
                                                notiGList.add(notis);

                                            }
                                            notiGuardAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Fail detect");
                                    }
                                });
                    }
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutG.isDrawerOpen(GravityCompat.START))
            drawerLayoutG.closeDrawers();
        else
            startActivity(new Intent(NotificationGuard.this, HomeGuard.class));

    }

}