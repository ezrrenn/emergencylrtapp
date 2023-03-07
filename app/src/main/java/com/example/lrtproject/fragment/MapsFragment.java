package com.example.lrtproject.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lrtproject.R;
import com.example.lrtproject.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private ActivityAddMarkerMapBinding binding;
    MapView mMapview;
    SupportMapFragment mapFragment;
    FragmentMapsBinding binding;

    Button searchEdt;
    Spinner spinLrt;
    ArrayList<String> lrtAmpang;
    String lrtA;
    private Marker marker;
    private MarkerOptions markerOptions;

    private int MY_PERMISSIONS_REQUEST_LOCATION = 10001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mapInitialize();

        return binding.getRoot();

    }

    private void mapInitialize() {
        binding.searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER){
                    goToSearchLocation();
                }
                return false;
            }
        });

        lrtAmpang = new ArrayList<>();
        lrtAmpang.add("LRT Sentul Timur");
        lrtAmpang.add("LRT Sentul");
        lrtAmpang.add("LRT Titiwangsa");
        lrtAmpang.add("LRT PWTC");
        lrtAmpang.add("LRT Sultan Ismail");
        lrtAmpang.add("LRT Bandaraya");
        lrtAmpang.add("LRT Masjid Jamek");
        lrtAmpang.add("LRT Plaza Rakyat");
        lrtAmpang.add("LRT Hang Tuah");
        lrtAmpang.add("LRT Pudu");
        lrtAmpang.add("LRT Chan Sow Lin");
        lrtAmpang.add("LRT Miharja");
        lrtAmpang.add("LRT Maluri");
        lrtAmpang.add("LRT Pandan Jaya");
        lrtAmpang.add("LRT Pandan Indah");
        lrtAmpang.add("LRT Cempaka");
        lrtAmpang.add("LRT Cahaya");
        lrtAmpang.add("LRT Ampang");
        ArrayAdapter<String> lrtAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item, lrtAmpang);
        lrtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinLrt.setAdapter(lrtAdapter);
        binding.spinLrt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lrtA = lrtAmpang.get(position).toString();
                binding.searchEdt.setText(lrtA);
                goToSearchLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String latValue = binding.Latitudee.getText().toString();
                String longValue = binding.Longitudee.getText().toString();
                String addName = binding.searchEdt.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("Latitude", latValue);
                bundle.putString("Longitude", longValue);
                bundle.putString("Station", addName);

                Fragment fragment = new AddLatlngFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_frame, fragment)
                        .addToBackStack("name")
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }

    private void goToSearchLocation() {
        String searchLocation = binding.searchEdt.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchLocation, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size()>0){
            Address address = list.get(0);
            String location = address.getAdminArea();
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            gotoLatLng(latitude, longitude, 17f);
            if (marker != null){
                marker.remove();
            }
            markerOptions = new MarkerOptions();
            markerOptions.title(location);

            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            markerOptions.position(new LatLng(latitude, longitude));
            marker = mMap.addMarker(markerOptions);
        }
    }

    private void gotoLatLng(double latitude, double longitude, float v) {
        LatLng latLng = new LatLng(latitude, longitude);
        binding.Latitudee.setText(String.valueOf(latLng.latitude));
        binding.Longitudee.setText(String.valueOf(latLng.longitude));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
        mMap.animateCamera(update);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng eiffel = new LatLng(3.1569, 101.7123);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel, 16));

        // Request location permission if not granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));
                            }
                        }
                    });
        }

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        CollectionReference mOrderRef = mDatabase.collection("Incidents");

        mOrderRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    if (documentSnapshot.contains("Latitude") && documentSnapshot.contains("Longitude")) {
                        String namee = (String) documentSnapshot.get("Station");
                        String lat = (String) documentSnapshot.get("Latitude");
                        String lon = (String) documentSnapshot.get("Longitude");
                        String title = (String) documentSnapshot.getString("ListOfCrime");
                        String msg1 = (String) documentSnapshot.getString("EstimateDate");
                        String msg2 = (String) documentSnapshot.getString("EstimateTime");

                        if (lat != null && lon != null && !lat.isEmpty() && !lon.isEmpty()) {
                            double latitude = Double.parseDouble(lat.trim());
                            double longitude = Double.parseDouble(lon.trim());

                            LatLng location = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title(title + " at " + namee)
                                    .snippet(msg1 + " | " + msg2);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));

                            //LatLng latLng = new LatLng(latitude, longitude);
                            //addmarker((float) latitude, (float) longitude);
                        }


                    }
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                int LayoutSearch = binding.layoutSearch.getVisibility();
                if (LayoutSearch == View.VISIBLE){
                    binding.layoutSearch.setVisibility(View.GONE);
                }else {
                    if (LayoutSearch == View.GONE){
                        binding.layoutSearch.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        if (mMap != null){
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(@NonNull Marker marker) {
                }
                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> list = new ArrayList<>();
                    try {
                        LatLng markerPosition = marker.getPosition();
                        list = geocoder.getFromLocation(markerPosition.latitude, markerPosition.longitude, 1);
                        binding.Latitudee.setText(String.valueOf(markerPosition.latitude));
                        binding.Longitudee.setText(String.valueOf(markerPosition.longitude));
                        Address address = list.get(0);
                        marker.setTitle(address.getAdminArea());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.next.setVisibility(View.VISIBLE);
                }
                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {
                }
            });
    }
}

    private void addmarker(float latitude, float longitude) {

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
    }


}