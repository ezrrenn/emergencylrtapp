package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.RequiresApi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lrtproject.databinding.ActivityHomePassengerBinding;
import com.example.lrtproject.fragment.MapsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomePassenger extends MenuPassenger{

    ActivityHomePassengerBinding activityHomePassengerBinding;
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private ArrayList<StaticRvModel> item;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageView btnLogout;
    TextView txtPass, txtEmail;
    String userID;
    private FirebaseUser passenger;
    CardView ReportCard, IncidentCard, GeofenceCard, NotiCard, LRTCard, ProfileCard;

    Button btntry, btntry2;
    //alarm
    Button btnStart, btnStop;
    MediaPlayer mp;
    PendingIntent pendingIntent;

    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent repeatAlarmIntent;
    public static final String ALARM_TYPE = "ALARM_TYPE";
    public static final String ALARM_TYPE_REPEAT = "ALARM_TYPE_REPEAT";
    public static final String ALARM_DESCRIPTION = "ALARM_DESCRIPTION";

    @SuppressLint("ServiceCast") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomePassengerBinding = ActivityHomePassengerBinding.inflate(getLayoutInflater());
        setContentView(activityHomePassengerBinding.getRoot());
        allocateActivityTitle("Rail Alert!");

        /*FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("new_user_forums");*/

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();
        txtPass = findViewById(R.id.txtPass);
        txtEmail = findViewById(R.id.emaill);
        ReportCard = findViewById(R.id.ReportCard);
        IncidentCard = findViewById(R.id.IncidentCard);
        GeofenceCard = findViewById(R.id.GeofenceCard);
        NotiCard = findViewById(R.id.NotiCard);
        LRTCard = findViewById(R.id.LRTCard);
        ProfileCard = findViewById(R.id.ProfileCard);
        btntry = findViewById(R.id.buttonTRY);
        btntry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomePassenger.this, BlankActivity.class);
                startActivity(intent);
            }
        });
        btntry2 = findViewById(R.id.buttonTRY2);
        btntry2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomePassenger.this, TrackPassenger.class);
                startActivity(intent);
            }
        });

        //Hello passenger
        txtPass.setText(passenger.getEmail());
        txtEmail.setText(passenger.getDisplayName());

        //alarm
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp == null){
                    mp = MediaPlayer.create(HomePassenger.this, R.raw.sirens);
                    mp.setLooping(true);
                    btnStart.setVisibility(View.GONE);
                    btnStop.setVisibility(View.VISIBLE);
                    // Add the pending intent to the notification
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "Someone has triggered the alarm button!",
                            "Please alert your surrounding that need help. Your help is matter for them.", getApplicationContext(), HomePassenger.this);
                    notificationsSender.SendNotifications();

                    db.collection("DeviceTokens").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                                CollectionReference cr = document.getReference().collection("Notifications");

                                Map<String, Object> data = new HashMap<>();
                                data.put("title", "Someone has triggered the alarm button!");
                                data.put("message", "Please alert your surrounding that need help. Your help is matter for them.");
                                data.put("timestamp", Timestamp.now());
                                cr.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error getting documents.", e);
                        }
                    });
                }
                mp.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null){
                    mp.release();
                    mp = null;
                    btnStart.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.GONE);
                }
            }
        });

        btnStop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mp = MediaPlayer.create(HomePassenger.this, R.raw.sirens);
                Toast.makeText(HomePassenger.this, "Alarm started", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

       /* btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(this,MyBroadcastReceiver.class);
        repeatAlarmIntent = PendingIntent.getBroadcast(this, 200, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);*/

       /* btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*startAlert();*//*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(HomePassenger.this, MyBroadcastReceiver.class);
                    repeatAlarmIntent = PendingIntent.getBroadcast(HomePassenger.this,200,intent,PendingIntent.FLAG_IMMUTABLE);

                    intent.putExtra(ALARM_TYPE, ALARM_TYPE_REPEAT);
                    intent.putExtra(ALARM_DESCRIPTION, "Repeat Alarm");

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis(),0,
                            repeatAlarmIntent);
                    Toast.makeText(HomePassenger.this,"Alarm set", Toast.LENGTH_SHORT).show();

                }else{
                    intent = new Intent(HomePassenger.this, MyBroadcastReceiver.class);

                    repeatAlarmIntent = PendingIntent.getBroadcast(HomePassenger.this,0,intent,PendingIntent.FLAG_IMMUTABLE);

                    if (alarmManager == null){
                        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    }

                    *//*alarmManager.cancel(repeatAlarmIntent);
                    Toast.makeText(HomePassenger.this, "Alarm cancelled", Toast.LENGTH_SHORT).show();*//*
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePassenger.this, MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
        });*/

        /*public void startAlert(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(this, MyBroadcastReceiver.class);
            repeatAlarmIntent = PendingIntent.getBroadcast(this,200,intent,PendingIntent.FLAG_IMMUTABLE);

            intent.putExtra(ALARM_TYPE, ALARM_TYPE_REPEAT);
            intent.putExtra(ALARM_DESCRIPTION, "Repeat Alarm");

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),0,
                    repeatAlarmIntent);
            Toast.makeText(this,"Alarm set", Toast.LENGTH_SHORT).show();

        }else{
            intent = new Intent(this, MyBroadcastReceiver.class);

            repeatAlarmIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager == null){
                alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
            }

            alarmManager.cancel(repeatAlarmIntent);
            Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
        }


    }*/

        Log.d(TAG, "DocumentSnapshot data: " + userID);

        //btnLogout = findViewById(R.id.btnLogout);
        //btnLogout.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View view) {
           //     mAuth.signOut();
           //     Intent intent = new Intent(HomePassenger.this, LoginActivity.class);
           //     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           //     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           //     startActivity(intent);
              //  finish();
           //     Toast.makeText(HomePassenger.this,"Logout Successful!", Toast.LENGTH_SHORT).show();
          //  }
       // });

        //menu page
        ReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,ReportHomeP.class);
                startActivity(intent);
            }
        });
        IncidentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,IncidentPassenger.class);
                startActivity(intent);
            }
        });
        GeofenceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,BlankActivity.class);
                startActivity(intent);
            }
        });
        NotiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,Notification.class);
                startActivity(intent);
            }
        });
        LRTCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,TransitMap.class);
                startActivity(intent);
            }
        });
        ProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePassenger.this,ProfilePassenger.class);
                startActivity(intent);
            }
        });

        //nav drawer

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();

        }

   /* @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                intent.putExtra(ALARM_TYPE, ALARM_TYPE_REPEAT);
                intent.putExtra(ALARM_DESCRIPTION, "Repeat alarm");

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), 60000,
                        repeatAlarmIntent);
                Toast.makeText(this, "Alarm set.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStop:
                alarmManager.cancel(repeatAlarmIntent);
                Toast.makeText(this,"Alarm cancelled.", Toast.LENGTH_SHORT).show();
                break;
        }
    }*/
}
