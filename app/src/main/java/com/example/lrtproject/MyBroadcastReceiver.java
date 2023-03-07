package com.example.lrtproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent){
        /*mp=MediaPlayer.create(context, R.raw.sirens);
        mp.start();
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        Toast.makeText(context, "Alarm fired!!", Toast.LENGTH_SHORT).show();
        Log.d("TAG","Fired " + System.currentTimeMillis());*/
    }
}
