package com.example.lrtproject.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lrtproject.R;
import com.example.lrtproject.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LocationMaps extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private ActivityAddMarkerMapBinding binding;
    MapView mMapview;
    SupportMapFragment mapFragment;
    FragmentMapsBinding binding1;

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser guard;
    String guardID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_maps, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapLocationGuard);
        if (mapFragment != null) {
            mapFragment.getMapAsync(LocationMaps.this);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        guard = mAuth.getCurrentUser();
        guardID = guard.getUid();

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        CollectionReference mOrderRef = mDatabase.collection("Incidents");

        mOrderRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    if (documentSnapshot.contains("Latitude") && documentSnapshot.contains("Longitude")) {
                        String lat = (String) documentSnapshot.get("Latitude");
                        String lon = (String) documentSnapshot.get("Longitude");
                        String title = (String) documentSnapshot.get("EstimateDate");
                        String title1 = (String) documentSnapshot.get("EstimateTime");
                        String msg1 = (String) documentSnapshot.get("SharingFor");
                        String msg2 = (String) documentSnapshot.get("ListOfCrime");
                        String msg3 = (String) documentSnapshot.get("Gender");

                        if (lat != null && lon != null && !lat.isEmpty() && !lon.isEmpty()) {
                            double latitude = Double.parseDouble(lat.trim());
                            double longitude = Double.parseDouble(lon.trim());
                            //addmarker(latitude,longitude );

                            LatLng location = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title(title + " (" + title1 + ")")
                                    .snippet(msg1 + " | " + msg2 + " | " + msg3);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                        }
                    }
                }
            }
        });
    }

    private void addmarker(double latitude, double longitude) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }
}