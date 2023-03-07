package com.example.lrtproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.lrtproject.fragment.MapsFragment;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        Fragment fragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment,
                        fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BlankActivity.this, HomePassenger.class));
    }
}