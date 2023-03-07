package com.example.lrtproject;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Listener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportAdapterA extends RecyclerView.Adapter<ReportAdapterA.ReportViewHolder>{

    Context contextReportA;
    ArrayList<ReportA> reportAArrayList;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    public ReportAdapterA(Context context, ArrayList<ReportA> reportAArrayList) {
        this.contextReportA = context;
        this.reportAArrayList = reportAArrayList;
    }

    public void setFilteredList(ArrayList<ReportA> filteredList){
        this.reportAArrayList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReportAdapterA.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(contextReportA).inflate(R.layout.recyler_report_item_a, parent,false);

        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapterA.ReportViewHolder holder, int position) {
        ReportA reportA = reportAArrayList.get(position);
        holder.FirstName.setText(reportA.FirstName);
        holder.LastName.setText(reportA.LastName);
        holder.PhoneNumber.setText(reportA.PhoneNumber);
        holder.TypeOfCrime.setText(reportA.TypeOfCrime);
        holder.DateCrime.setText(reportA.DateCrime);
        holder.TimeCrime.setText(reportA.TimeCrime);
        holder.Station.setText(reportA.Station);
        holder.latitude.setText(reportA.latitude);
        holder.longitude.setText(reportA.longitude);
        holder.Description.setText(reportA.Description);
        holder.StatusReport.setText(reportA.StatusReport);
        holder.SecurityGuard.setText(reportA.SecurityGuard);

    }

    @Override
    public int getItemCount() {
        return reportAArrayList.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder{

        TextView FirstName, LastName, PhoneNumber, TypeOfCrime, DateCrime, TimeCrime, Station, Description, StatusReport;
        TextView latitude, longitude, SecurityGuard;
        Button buttonDelete, btnUpdateA;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            FirstName = itemView.findViewById(R.id.FirstName);
            LastName = itemView.findViewById(R.id.LastName);
            PhoneNumber = itemView.findViewById(R.id.PhoneNumber);
            TypeOfCrime = itemView.findViewById(R.id.TypeCrime);
            DateCrime = itemView.findViewById(R.id.DateReport);
            TimeCrime = itemView.findViewById(R.id.TimeReport);
            Station = itemView.findViewById(R.id.EmergencyStation);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            Description = itemView.findViewById(R.id.DescEmergency);
            StatusReport = itemView.findViewById(R.id.StatusReport);
            SecurityGuard = itemView.findViewById(R.id.nameSecurity);

            /*buttonDelete = itemView.findViewById(R.id.btnDeleteA);
            btnUpdateA = itemView.findViewById(R.id.btnUpdateA);*/

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
