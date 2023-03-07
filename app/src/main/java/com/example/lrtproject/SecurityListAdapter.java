package com.example.lrtproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecurityListAdapter extends RecyclerView.Adapter<SecurityListAdapter.ListViewHolder> {

    Context contextListS;
    ArrayList<SecurityA> securityAArrayList;

    public SecurityListAdapter(Context contextListS, ArrayList<SecurityA> securityAArrayList) {
        this.contextListS = contextListS;
        this.securityAArrayList = securityAArrayList;
    }

    @NonNull
    @Override
    public SecurityListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(contextListS).inflate(R.layout.recycler_list_guard_a, parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SecurityListAdapter.ListViewHolder holder, int position) {

        SecurityA securityA = securityAArrayList.get(position);
        holder.sFirstName.setText(securityA.sFirstName);
        holder.sLastName.setText(securityA.sLastName);
        holder.sEmail.setText(securityA.sEmail);
        holder.sPhoneNumber.setText(securityA.sPhoneNumber);
        holder.sDuty.setText(securityA.sDuty);

        holder.sFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (securityAArrayList.get(holder.getAbsoluteAdapterPosition()).expanded){
                    holder.expandable.setVisibility(View.GONE);
                    securityAArrayList.get(holder.getAbsoluteAdapterPosition()).expanded=false;
                }else {
                    holder.expandable.setVisibility(View.VISIBLE);
                    securityAArrayList.get(holder.getAbsoluteAdapterPosition()).expanded=true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return securityAArrayList.size();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder{

        TextView sFirstName, sLastName, sEmail, sPhoneNumber, sDuty;
        ConstraintLayout expandable, layoutSecurityList;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            sFirstName = itemView.findViewById(R.id.SName);
            sLastName = itemView.findViewById(R.id.LastNameS1);
            sEmail = itemView.findViewById(R.id.EmailS1);
            sPhoneNumber = itemView.findViewById(R.id.PhoneNumberS1);
            sDuty = itemView.findViewById(R.id.DutySS1);

            expandable = itemView.findViewById(R.id.expandable);
            layoutSecurityList = itemView.findViewById(R.id.layoutSecurityList);
        }
    }
}
