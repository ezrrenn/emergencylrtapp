package com.example.lrtproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuPassenger extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_menu_passenger,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navView = drawerLayout.findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_Open, R.string.menu_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.homeP:
                Log.i("MENU_DRAWER_TAG", "Home item is clicked");
                startActivity(new Intent(this, HomePassenger.class));
                overridePendingTransition(0,0);
                break;

            case R.id.reportP:
                Log.i("MENU_DRAWER_TAG", "Report item is clicked");
                startActivity(new Intent(this, ReportHomeP.class));
                overridePendingTransition(0,0);
                break;

            case R.id.commentP:
                Log.i("MENU_DRAWER_TAG", "Comment item is clicked");
                startActivity(new Intent(this, IncidentPassenger.class));
                overridePendingTransition(0,0);
                break;

            case R.id.profileP:
                Log.i("MENU_DRAWER_TAG", "Profile item is clicked");
                startActivity(new Intent(this, ProfilePassenger.class));
                overridePendingTransition(0,0);
                break;

            case R.id.notiP:
                Log.i("MENU_DRAWER_TAG", "Noti item is clicked");
                startActivity(new Intent(this, Notification.class));
                overridePendingTransition(0,0);
                break;

            case R.id.logoutP:
                Log.i("MENU_DRAWER_TAG", "Logout is clicked");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0,0);
                break;
        }

        return false;
    }

    protected  void allocateActivityTitle (String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}