package com.example.lrtproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportAdapterG extends RecyclerView.Adapter<ReportAdapterG.GuardViewHolder> {

    Context contextReportG;
    ArrayList<ReportG> reportGArrayList;
    private ReportAdapterA.Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(ReportAdapterA.Dialog dialog){
        this.dialog = dialog;
    }

    public ReportAdapterG(Context contextReportG, ArrayList<ReportG> reportGArrayList) {
        this.contextReportG = contextReportG;
        this.reportGArrayList = reportGArrayList;
    }

    public void setFilteredList(ArrayList<ReportG> filteredList){
        this.reportGArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportAdapterG.GuardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextReportG).inflate(R.layout.recycler_report_item_g, parent,false);

        return new GuardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapterG.GuardViewHolder holder, int position) {
        ReportG reportG = reportGArrayList.get(position);
        holder.FirstNameG.setText(reportG.FirstName);
        holder.LastNameG.setText(reportG.LastName);
        holder.PhoneNumberG.setText(reportG.PhoneNumber);
        holder.TypeOfCrimeG.setText(reportG.TypeOfCrime);
        holder.DateCrimeG.setText(reportG.DateCrime);
        holder.TimeCrimeG.setText(reportG.TimeCrime);
        holder.StationG.setText(reportG.Station);
        holder.latitudeG.setText(reportG.latitude);
        holder.longitudeG.setText(reportG.longitude);
        holder.DescriptionG.setText(reportG.Description);
        holder.StatusReportG.setText(reportG.StatusReport);
    }

    @Override
    public int getItemCount() {
        return reportGArrayList.size();
    }

    public class GuardViewHolder extends RecyclerView.ViewHolder{

        TextView FirstNameG, LastNameG, PhoneNumberG, TypeOfCrimeG, DateCrimeG, TimeCrimeG, StationG, DescriptionG, StatusReportG;
        TextView latitudeG, longitudeG, SecurityGuardG;

        public GuardViewHolder(@NonNull View itemView) {
            super(itemView);

            FirstNameG = itemView.findViewById(R.id.FirstNameG);
            LastNameG = itemView.findViewById(R.id.LastNameG);
            PhoneNumberG = itemView.findViewById(R.id.PhoneNumberG);
            TypeOfCrimeG = itemView.findViewById(R.id.TypeCrimeG);
            DateCrimeG = itemView.findViewById(R.id.DateReportG);
            TimeCrimeG = itemView.findViewById(R.id.TimeReportG);
            StationG = itemView.findViewById(R.id.EmergencyStationG);
            latitudeG = itemView.findViewById(R.id.latitudeG);
            longitudeG = itemView.findViewById(R.id.longitudeG);
            DescriptionG = itemView.findViewById(R.id.DescEmergencyG);
            StatusReportG = itemView.findViewById(R.id.StatusReportG);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog !=null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });

        }
    }
}
