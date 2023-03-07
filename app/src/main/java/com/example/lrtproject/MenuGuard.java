package com.example.lrtproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuGuard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayoutG;

    @Override
    public void setContentView(View view){
        drawerLayoutG = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_menu_guard,null);
        FrameLayout container = drawerLayoutG.findViewById(R.id.activityContainer1);
        container.addView(view);
        super.setContentView(drawerLayoutG);

        Toolbar toolbar = drawerLayoutG.findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayoutG.findViewById(R.id.navigationView2);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayoutG,toolbar,R.string.menu_Open, R.string.menu_Close);
        drawerLayoutG.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayoutG.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_home1:
                startActivity(new Intent(this, HomeGuard.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_profile1:
                startActivity(new Intent(this, ProfileGuard.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_noti1:
                startActivity(new Intent(this, NotificationGuard.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_logout1:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                //overridePendingTransition(0,0);
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