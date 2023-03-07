package com.example.lrtproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.lrtproject.databinding.ActivityReportHomePBinding;
import com.example.lrtproject.databinding.ActivityTransitMapBinding;

public class TransitMap extends MenuPassenger {

    ActivityTransitMapBinding activityTransitMapBinding;
    //ImageView imageView;
    private ZoomableImageView zoomableImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTransitMapBinding = ActivityTransitMapBinding.inflate(getLayoutInflater());
        setContentView(activityTransitMapBinding.getRoot());
        allocateActivityTitle("Transit LRT Map");

        //imageView = findViewById(R.id.imgLRT);
        zoomableImageView = findViewById(R.id.imgLRT);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            startActivity(new Intent(TransitMap.this, HomePassenger.class));

    }
}