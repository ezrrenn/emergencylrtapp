package com.example.lrtproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class IncidentAdapterP extends RecyclerView.Adapter<IncidentAdapterP.IncidentViewHolder> {

    Context contextIncident;
    ArrayList<PassengerIncident> incidentPArrayList;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public IncidentAdapterP(Context contextIncident, ArrayList<PassengerIncident> incidentPArrayList) {
        this.contextIncident = contextIncident;
        this.incidentPArrayList = incidentPArrayList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void setFilteredList(ArrayList<PassengerIncident> filterList){
        this.incidentPArrayList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IncidentAdapterP.IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(contextIncident).inflate(R.layout.recyler_incident_item_p,parent,false);

        return new IncidentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapterP.IncidentViewHolder holder, int position) {

        PassengerIncident passengerIncident = incidentPArrayList.get(position);

        holder.SharingFor.setText(passengerIncident.SharingFor);
        holder.Gender.setText(passengerIncident.Gender);
        holder.EstimateDate.setText(passengerIncident.EstimateDate);
        holder.EstimateTime.setText(passengerIncident.EstimateTime);
        holder.Station.setText(passengerIncident.Station);
        holder.ListOfCrime.setText(passengerIncident.ListOfCrime);
        holder.IncidentDescription.setText(passengerIncident.IncidentDescription);

        holder.Station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incidentPArrayList.get(holder.getAbsoluteAdapterPosition()).expandedd){
                    holder.expandableLayoutI.setVisibility(View.GONE);
                    incidentPArrayList.get(holder.getAbsoluteAdapterPosition()).expandedd=false;
                }else {
                    holder.expandableLayoutI.setVisibility(View.VISIBLE);
                    incidentPArrayList.get(holder.getAbsoluteAdapterPosition()).expandedd=true;
                }
            }
        });

        holder.layout1I.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(contextIncident)
                        .setTitle("Delete Report")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.ic_baseline_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Passengers").document(auth.getCurrentUser().getUid())
                                        .collection("My Incidents")
                                        .document(incidentPArrayList.get(holder.getAbsoluteAdapterPosition()).getDocId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    incidentPArrayList.remove(incidentPArrayList.get(holder.getAbsoluteAdapterPosition()));
                                                    notifyDataSetChanged();
                                                    Toast.makeText(contextIncident, "Item deleted", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(contextIncident, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
                return;
            }
        });
    }


    @Override
    public int getItemCount() {
        return incidentPArrayList.size();
    }

    public static class IncidentViewHolder extends RecyclerView.ViewHolder{

        TextView SharingFor, Gender, EstimateDate, EstimateTime, ListOfCrime, Station, Latitude, Longitude, IncidentDescription;
        ConstraintLayout expandableLayoutI, layout1I;

        public IncidentViewHolder(@NonNull View itemView) {
            super(itemView);
            SharingFor = itemView.findViewById(R.id.sharingI_crime);
            Gender = itemView.findViewById(R.id.genderI_crime);
            EstimateDate = itemView.findViewById(R.id.dateI_crime);
            EstimateTime = itemView.findViewById(R.id.timeI_crime);
            ListOfCrime = itemView.findViewById(R.id.type_of_crimeII);
            Station = itemView.findViewById(R.id.stationI_crimeII);
            IncidentDescription = itemView.findViewById(R.id.descI_crime);

            expandableLayoutI = itemView.findViewById(R.id.expandableLayoutI);
            layout1I = itemView.findViewById(R.id.layout1I);
        }
    }
}
