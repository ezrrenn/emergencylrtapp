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

import com.example.lrtproject.databinding.ActivityNotificationAuthorityBinding;
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

public class NotificationAuthority extends MenuAuthority {

    ActivityNotificationAuthorityBinding activityNotificationAuthorityBinding;

    RecyclerView rv_notiA;
    ArrayList<NotiA> notiArrayList;
    NotiAuthAdapter notiAuthAdapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userID;
    ArrayList<String> listNoti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationAuthorityBinding = ActivityNotificationAuthorityBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationAuthorityBinding.getRoot());
        allocateActivityTitle("Communications");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        rv_notiA = findViewById(R.id.rv_notiA);
        rv_notiA.setHasFixedSize(true);
        rv_notiA.setLayoutManager(new LinearLayoutManager(this));


        notiArrayList = new ArrayList<>();
        notiAuthAdapter = new NotiAuthAdapter(this, notiArrayList);
        rv_notiA.setAdapter(notiAuthAdapter);

        retrieveNotiA();
    }

    private void retrieveNotiA() {
        CollectionReference cr;
        cr = db.collection("DeviceTokens");
        Query query = cr.whereEqualTo("ID", currentUser.getUid());
        System.out.println("TENGOKKKKKK" + currentUser.getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String documentId = doc.getId();
                        System.out.println("NI TENGOK JUGAAAAAa" + documentId);
                        cr.document(documentId)
                                .collection("Notifications").orderBy("timestamp", Query.Direction.DESCENDING)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                                String documentID = ds.getId();
                                                System.out.println("HAI TENGOK NI " + documentID);
                                                NotiA notis = new NotiA(
                                                        ds.getString("title"),
                                                        ds.getString("message"),
                                                        ds.getTimestamp("timestamp")
                                                );
                                                notis.setDocumentId(documentID);
                                                notiArrayList.add(notis);

                                            }
                                            notiAuthAdapter.notifyDataSetChanged();
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(NotificationAuthority.this, HomeAuthority.class));

    }
}