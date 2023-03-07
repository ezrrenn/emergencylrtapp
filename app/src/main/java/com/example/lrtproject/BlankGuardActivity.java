package com.example.lrtproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.lrtproject.databinding.ActivityBlankGuardBinding;
import com.example.lrtproject.databinding.ActivityHomeGuardBinding;
import com.example.lrtproject.fragment.LocationMaps;
import com.example.lrtproject.fragment.MapsFragment;

public class BlankGuardActivity extends MenuGuard {

    ActivityBlankGuardBinding activityBlankGuardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBlankGuardBinding = ActivityBlankGuardBinding.inflate(getLayoutInflater());
        setContentView(activityBlankGuardBinding.getRoot());
        allocateActivityTitle("Passenger's Location");

        Fragment fragment = new LocationMaps();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame2, fragment,
                        fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BlankGuardActivity.this, HomeGuard.class));
    }
}