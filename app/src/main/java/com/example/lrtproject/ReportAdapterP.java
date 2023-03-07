package com.example.lrtproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;

public class ReportAdapterP extends RecyclerView.Adapter<ReportAdapterP.MyViewHolder> {

    Context context;
    ArrayList<ReportP> reportPArrayList;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public ReportAdapterP(Context context, ArrayList<ReportP> reportPArrayList) {
        this.context = context;
        this.reportPArrayList = reportPArrayList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void setFilteredList(ArrayList<ReportP> filteredList){
        this.reportPArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportAdapterP.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recyler_report_item_p,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapterP.MyViewHolder holder, int position) {

        ReportP reportP = reportPArrayList.get(position);

        holder.DateCrime.setText(reportP.DateCrime);
        holder.TypeOfCrime.setText(reportP.TypeOfCrime);
        holder.TimeCrime.setText(reportP.TimeCrime);
        holder.Station.setText(reportP.Station);
        holder.Description.setText(reportP.Description);
        holder.StatusReport.setText(reportP.StatusReport);

        //boolean isExpanded = reportPArrayList.get(position).isExpanded();
        //holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.DateCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reportPArrayList.get(holder.getAbsoluteAdapterPosition()).expanded){
                    holder.expandableLayout.setVisibility(View.GONE);
                    reportPArrayList.get(holder.getAbsoluteAdapterPosition()).expanded=false;
                }else {
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    reportPArrayList.get(holder.getAbsoluteAdapterPosition()).expanded=true;
                }
            }
        });

        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Report")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.ic_baseline_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Passengers").document(auth.getCurrentUser().getUid())
                                        .collection("Report")
                                        .document(reportPArrayList.get(holder.getAbsoluteAdapterPosition()).getDocumentId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    reportPArrayList.remove(reportPArrayList.get(holder.getAbsoluteAdapterPosition()));
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        return reportPArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DateCrime, TypeOfCrime, Emergency, TimeCrime, Station, Description, StatusReport;
        ConstraintLayout expandableLayout, layout1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateCrime = itemView.findViewById(R.id.date_crime);
            TypeOfCrime = itemView.findViewById(R.id.type_crime);
            TimeCrime = itemView.findViewById(R.id.time_crime);
            Station = itemView.findViewById(R.id.station_crime);
            Description = itemView.findViewById(R.id.desc_crime);
            StatusReport = itemView.findViewById(R.id.DutyS1);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            layout1 = itemView.findViewById(R.id.layout1);
        }
    }
}
