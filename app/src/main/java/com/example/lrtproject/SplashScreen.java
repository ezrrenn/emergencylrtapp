package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //private static final long SPLASH_TIME_OUT = 2000;

    //Variable
    Animation topAnim, bottomAnim;
    ImageView image1,image2;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser passenger, authority,guard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        //Window window = getWindow();
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        passenger = mAuth.getCurrentUser();
        authority = mAuth.getCurrentUser();
        guard = mAuth.getCurrentUser();


        //Animation
        //topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //image1 = findViewById(R.id.imageAtas);
        image2 = findViewById(R.id.imageBawah);

        //image1.setAnimation(bottomAnim);
        image2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CollectionReference dcc = db.collection("DeviceTokens");
                if (mAuth.getCurrentUser() != null){
                    DocumentReference documentReference = db.collection("Passengers").document(passenger.getUid());
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()){

                                    dcc.whereEqualTo("ID", passenger.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot doc : task.getResult()){
                                                String documentId = doc.getId();
                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (task.isSuccessful()){
                                                            String token = task.getResult();
                                                            dcc.whereEqualTo("DeviceToken", token).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()){
                                                                        dcc.document(documentId).update("DeviceToken", token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                deleteOldDocuments();
                                                                                Log.d(TAG, "Device token updated in Firestore");
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error updating device token in Firestore", e);
                                                                            }
                                                                        });

                                                                    }else {
                                                                        // Get new FCM registration token
                                                                        Map<String, Object> dt = new HashMap<>();
                                                                        dt.put("DeviceToken", token);
                                                                        dt.put("ID", passenger.getUid());
                                                                        dt.put("Role", "Passenger");
                                                                        dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Log.d("TAG", "Success!!!!" +token);
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                Thread thread = new Thread(){
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        try {
                                                            sleep(3000);
                                                            startActivity(new Intent(SplashScreen.this, HomePassenger.class));
                                                            finish();
                                                        }catch (Exception e){

                                                        }
                                                    }
                                                }; thread.start();
                                                Log.d(TAG, "Login as Passenger");
                                                    /*Intent intent = new Intent(SplashScreen.this,HomeAuthority.class);
                                                    startActivity(intent);*/
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }if (mAuth.getCurrentUser() != null){
                    DocumentReference documentReference = db.collection("Authority").document(authority.getUid());
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()){
                                    startActivity(new Intent(SplashScreen.this, HomeAuthority.class));
                                    finish();
                                    dcc.whereEqualTo("ID", authority.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot doc : task.getResult()){
                                                String documentId = doc.getId();
                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (task.isSuccessful()){
                                                            String token = task.getResult();
                                                            dcc.whereEqualTo("DeviceToken", token).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()){
                                                                        dcc.document(documentId).update("DeviceToken", token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                deleteOldDocuments();
                                                                                Log.d(TAG, "Device token updated in Firestore");
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error updating device token in Firestore", e);
                                                                            }
                                                                        });

                                                                    }else {
                                                                        // Get new FCM registration token
                                                                        Map<String, Object> dt = new HashMap<>();
                                                                        dt.put("DeviceToken", token);
                                                                        dt.put("ID", authority.getUid());
                                                                        dt.put("Role", "Authority");
                                                                        dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Log.d("TAG", "Success!!!!" +token);
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        }else{
                                                            return;
                                                        }
                                                    }
                                                });
                                                Thread thread = new Thread(){
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        try {
                                                            sleep(3000);
                                                            startActivity(new Intent(SplashScreen.this, HomeAuthority.class));
                                                            finish();
                                                        }catch (Exception e){

                                                        }
                                                    }
                                                }; thread.start();
                                                Log.d(TAG, "Login as Authority");
                                                    /*Intent intent = new Intent(SplashScreen.this,HomeAuthority.class);
                                                    startActivity(intent);*/
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }if (mAuth.getCurrentUser() != null){
                    DocumentReference docRef = db.collection("SecurityGuard").document(guard.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()){
                                    startActivity(new Intent(SplashScreen.this, HomeGuard.class));
                                    finish();
                                    dcc.whereEqualTo("ID", guard.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot doc : task.getResult()){
                                                String documentId = doc.getId();
                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (task.isSuccessful()){
                                                            String token = task.getResult();
                                                            dcc.whereEqualTo("DeviceToken", token).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()){
                                                                        dcc.document(documentId).update("DeviceToken", token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                deleteOldDocuments();
                                                                                Log.d(TAG, "Device token updated in Firestore");
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error updating device token in Firestore", e);
                                                                            }
                                                                        });

                                                                    }else {
                                                                        // Get new FCM registration token
                                                                        Map<String, Object> dt = new HashMap<>();
                                                                        dt.put("DeviceToken", token);
                                                                        dt.put("ID", guard.getUid());
                                                                        dt.put("Role", "Guard");
                                                                        dcc.add(dt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Log.d("TAG", "Success!!!!" +token);
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                Thread thread = new Thread(){
                                                    @Override
                                                    public void run() {
                                                        super.run();
                                                        try {
                                                            sleep(3000);
                                                            startActivity(new Intent(SplashScreen.this, HomeGuard.class));
                                                            finish();
                                                        }catch (Exception e){

                                                        }
                                                    }
                                                }; thread.start();
                                                Log.d(TAG, "Login as Security Guard");
                                                    /*Intent intent = new Intent(SplashScreen.this,HomeGuard.class);
                                                    startActivity(intent);*/
                                            }
                                        }
                                    });

                                }
                            }
                        }
                    });
                }else{
                    Intent intent = new Intent(SplashScreen.this,RegisterActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },SPLASH_SCREEN);
    }

    private void deleteOldDocuments() {
        long thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000);
        db.collection("DeviceTokens").document(passenger.getUid()).collection("Notifications")
                .whereLessThan("timestamp", new Timestamp(new Date(thirtyDaysAgo)))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }

}