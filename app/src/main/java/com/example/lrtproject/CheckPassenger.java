package com.example.lrtproject;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.lrtproject.databinding.ActivityCheckPassengerBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CheckPassenger extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityCheckPassengerBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference passengerLocationRef = db.collection("passengerLocation");
    private Map<String, Marker> markers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCheckPassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.checkMap);
        mapFragment.getMapAsync(CheckPassenger.this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Fetch the location data of all passengers and display their locations on the map
        passengerLocationRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("MapsActivity", "Error : " + e.getMessage());
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot document = dc.getDocument();
                    LatLng latLng = new LatLng(document.getDouble("latitude"), document.getDouble("longitude"));
                    switch (dc.getType()) {
                        case ADDED:
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Passengers"));
                            break;
                        case MODIFIED:
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Passengers"));
                            break;
                    }
                }
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng eiffel = new LatLng(3.1569, 101.7123);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel, 16));

    }
}