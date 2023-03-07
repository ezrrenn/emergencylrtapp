package com.example.lrtproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.common.base.Preconditions.checkArgument;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MulticastMessage {
    public static void send(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("DeviceTokens").whereEqualTo("Role","Authority").whereEqualTo("Role","Guard").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete (@NonNull Task < QuerySnapshot > task) {
                        if (task.isSuccessful()) {
                            List<String> deviceTokens = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                deviceTokens.add(document.getString("DeviceToken"));
                                System.out.println("Device Token" + deviceTokens);
                                System.out.println("ID each: " + document.getString("ID"));
                                        /*FcmNotificationsSender notificationsSender = new FcmNotificationsSender(deviceTokens, "Someone need help!",
                                                latitude + longitude + "Phone Number:" + tvRPhone.getText().toString(), getApplicationContext(), ReportFormP.this);
                                        notificationsSender.SendNotifications();*/

                            }Arrays.asList(deviceTokens);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e){
                        Log.e(TAG, "onFailure " + e.getMessage());
                    }
                });

       /* FirebaseMessaging.MulticastMessage message = FirebaseMessaging.MulticastMessage.builder()
                .setTokens(tokens)
                .setNotification(new Notification("title", "body"))
                .build();
        FirebaseMessaging.getInstance().sendMulticast(message);*/

    }



}