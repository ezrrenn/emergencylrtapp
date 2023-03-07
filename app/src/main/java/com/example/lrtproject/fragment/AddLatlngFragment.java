package com.example.lrtproject.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lrtproject.FcmNotificationsSender;
import com.example.lrtproject.IncidentPassenger;
import com.example.lrtproject.R;
import com.example.lrtproject.databinding.FragmentAddLatlngBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddLatlngFragment extends Fragment {

    FragmentAddLatlngBinding binding;

    //form
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser passenger;
    String userID;
    EditText etWDesc;
    TextView tvWDate, tvWTime;
    RadioGroup rgWPerson, rgGender, rgWStation;
    RadioButton rbSelf, rbElse, rbMale, rbFemale, rbOthers,rbSentul, rbPwtc, rbJamek;
    Button btnWPost;
    String tCrime="";
    String latitude = "";
    String longitude = "";
    CheckBox rbA, rbP, rbS, rbL, rbO, rbT;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    //end form

    public AddLatlngFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        passenger = mAuth.getCurrentUser();
        userID = passenger.getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddLatlngBinding.inflate(inflater, container, false);
        View v = inflater.inflate(R.layout.fragment_add_latlng, container, false);

        Bundle bundle = this.getArguments();
        String nameAdd = bundle.getString("Station");
        String latValue = bundle.getString("Latitude");
        String longValue = bundle.getString("Longitude");
        binding.nameAdd.setText(nameAdd);
        binding.Latitudee2.setText(latValue);
        binding.Longitudee2.setText(longValue);

        etWDesc = v.findViewById(R.id.incidentDescription);
        tvWDate = v.findViewById(R.id.tvWDate);
        tvWTime = v.findViewById(R.id.tvWTime);
        rgGender = (RadioGroup) v.findViewById(R.id.rgGender);
        rgWPerson = (RadioGroup) v.findViewById(R.id.rgWPerson);
        rbSelf = v.findViewById(R.id.rbSelf);
        rbElse = v.findViewById(R.id.rbElse);
        rbMale = v.findViewById(R.id.rbMale);
        rbFemale = v.findViewById(R.id.rbFemale);
        rbOthers = v.findViewById(R.id.rbOthers);
        rbA = v.findViewById(R.id.cbA);
        rbP = v.findViewById(R.id.cbP);
        rbS = v.findViewById(R.id.cbS);
        rbL = v.findViewById(R.id.cbL);
        rbT = v.findViewById(R.id.cbT);
        rbO = v.findViewById(R.id.cbO);



        //radio group person
        binding.rgWPerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbSelf.isChecked()) {
                    Toast.makeText(getContext(), "Myself", Toast.LENGTH_SHORT).show();
                } else if (rbElse.isChecked()) {
                    Toast.makeText(getContext(), "Someone Else", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //radio group gender
        binding.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int x) {
                switch (x){
                    case R.id.rbMale:
                        Toast.makeText(getContext(), "Selected: Male", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbFemale:
                        Toast.makeText(getContext(), "Selected: Female", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbOthers:
                        Toast.makeText(getContext(), "Selected: Other", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //Date
        binding.tvWDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, dateSetListener, year,month,day);

                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDataSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                binding.tvWDate.setText(date);
            }
        };

        //time
        binding.tvWTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, timeSetListener, 12,0,false);

                timePickerDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                binding.tvWTime.setText(sdf.format(calendar.getTime()));
            }
        };



        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tCrime =" ";
                if (binding.cbA.isChecked()){
                    tCrime +=" [1] " + rbA.getText().toString();
                }
                if (binding.cbP.isChecked()){
                    tCrime +=" [2] " + rbP.getText().toString();
                }
                if (binding.cbS.isChecked()){
                    tCrime +=" [3] " + rbS.getText().toString();
                }
                if (binding.cbL.isChecked()){
                    tCrime +=" [4] " + rbL.getText().toString();
                }
                if (binding.cbT.isChecked()){
                    tCrime +=" [5] " + rbL.getText().toString();
                }
                if (binding.cbO.isChecked()){
                    tCrime +=" [6] " + rbO.getText().toString();
                }


                Map<String, Object> incident = new HashMap<>();
                if (binding.rgWPerson.getCheckedRadioButtonId()== R.id.rbSelf){
                    incident.put("SharingFor", binding.rbSelf.getText().toString().trim());
                }else{
                    incident.put("SharingFor", binding.rbElse.getText().toString().trim());
                }
                if (binding.rgGender.getCheckedRadioButtonId()== R.id.rbMale){
                    incident.put("Gender", binding.rbMale.getText().toString().trim());
                }else if (binding.rgGender.getCheckedRadioButtonId()== R.id.rbFemale){
                    incident.put("Gender", binding.rbFemale.getText().toString().trim());
                } else {
                    incident.put("Gender", binding.rbOthers.getText().toString().trim());
                }
                incident.put("EstimateDate", binding.tvWDate.getText().toString().trim());
                incident.put("EstimateTime", binding.tvWTime.getText().toString().trim());
                incident.put("ListOfCrime", tCrime);
                incident.put("Station", binding.nameAdd.getText().toString().trim());
                incident.put("Latitude", binding.Latitudee2.getText().toString().trim());
                incident.put("Longitude", binding.Longitudee2.getText().toString().trim());
                incident.put("IncidentDescription", binding.incidentDescription.getText().toString().trim());

                db.collection("Incidents").add(incident).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Fragment fragment = new MapsFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.fragment_frame, fragment).commit();
                        Toast.makeText(getContext(), "Incident stored successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "FOR ALL DB SUCCESS!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        btnWPost.setVisibility(View.VISIBLE);
                    }
                });

                CollectionReference collectionReference;
                collectionReference = db.collection("Passengers").document(userID).collection("My Incidents");
                collectionReference.add(incident).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d(TAG, "OWN DB SUCCESS!");
                        startActivity(new Intent(getContext(), IncidentPassenger.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Log.e(TAG, "OWN DB NOT SUCCESS!");
                    }
                });
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "New incident!" ,
                        binding.Latitudee2.getText().toString().trim() + " , " + binding.Longitudee2.getText().toString().trim() + " on " + binding.tvWDate.getText().toString().trim() + " at " +
                                binding.tvWTime.getText().toString(), getContext(), getActivity());
                notificationsSender.SendNotifications();

                db.collection("DeviceTokens").whereEqualTo("Role", "Passenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //List<String> documentIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            CollectionReference cr = document.getReference().collection("Notifications");

                            Map<String, Object> data = new HashMap<>();
                            data.put("title", "New incident!");
                            data.put("message", binding.Latitudee2.getText().toString().trim() + " , " + binding.Longitudee2.getText().toString().trim() + " on " + binding.tvWDate.getText().toString().trim() + " at " +
                                    binding.tvWTime.getText().toString());
                            data.put("timestamp", Timestamp.now());
                            cr.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });

                db.collection("DeviceTokens").whereEqualTo("Role", "Guard").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //List<String> documentIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            CollectionReference cr = document.getReference().collection("Notifications");

                            Map<String, Object> data = new HashMap<>();
                            data.put("title", "New incident!");
                            data.put("message", binding.Latitudee2.getText().toString().trim() + " , " + binding.Longitudee2.getText().toString().trim() + " on " + binding.tvWDate.getText().toString().trim() + " at " +
                                    binding.tvWTime.getText().toString());
                            data.put("timestamp", Timestamp.now());
                            cr.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });
            }
        });



        return binding.getRoot();
    }
}