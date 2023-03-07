package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.lrtproject.databinding.ActivityNotificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Notification extends MenuPassenger {

    ActivityNotificationBinding activityNotificationBinding;

    RecyclerView rv_noti;
    ArrayList<Noti> notificationDataArrayList;
    NotiHomeAdapter notiHomeAdapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userID;
    ArrayList<String> listNoti = new ArrayList<>();
    //SearchView searchViewNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationBinding.getRoot());
        allocateActivityTitle("Communications");

//Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();

        rv_noti = findViewById(R.id.rv_noti);
        rv_noti.setHasFixedSize(true);
        rv_noti.setLayoutManager(new LinearLayoutManager(this));


        notificationDataArrayList = new ArrayList<>();
        notiHomeAdapter = new NotiHomeAdapter(this, notificationDataArrayList);
        rv_noti.setAdapter(notiHomeAdapter);

        retrieveNoti();

        /*notiHomeAdapter.setDialog(new NotiHomeAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(Notification.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });*/
    }

    private void filterList(String nw) {
        ArrayList<Noti> filteredList = new ArrayList<>();
        for (Noti notificationData : notificationDataArrayList){
            if (notificationData.getTitle().toLowerCase().contains(nw.toLowerCase())){
                filteredList.add(notificationData);
            }
        }
        if (!filteredList.isEmpty()){
            notiHomeAdapter.setFilteredList(filteredList);
        }
    }

    private void retrieveNoti(){
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
                                            Noti notis = new Noti(
                                                    ds.getString("title"),
                                                    ds.getString("message"),
                                                    ds.getTimestamp("timestamp")
                                            );
                                            notis.setDocumentId(documentID);
                                            notificationDataArrayList.add(notis);

                                        }
                                        notiHomeAdapter.notifyDataSetChanged();
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
            startActivity(new Intent(Notification.this, HomePassenger.class));

    }
    }

