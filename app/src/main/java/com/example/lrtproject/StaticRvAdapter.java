package com.example.lrtproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder>{

    private ArrayList<StaticRvModel> items;
    int row_index = -1;

    public StaticRvAdapter(ArrayList<StaticRvModel> items) {

        this.items = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item,parent,false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull StaticRVViewHolder holder, int position) {
        StaticRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Choice: " + items.get(holder.getAbsoluteAdapterPosition()).getText(), Toast.LENGTH_SHORT).show();

                if (items.get(holder.getAbsoluteAdapterPosition()).getText().equals("New Report")){
                    v.getContext().startActivity(new Intent(v.getContext(), ReportHomeP.class));
                }
                if (items.get(holder.getAbsoluteAdapterPosition()).getText().equals("Incidents")){
                    v.getContext().startActivity(new Intent(v.getContext(), IncidentPassenger.class));
                }
                if (items.get(holder.getAbsoluteAdapterPosition()).getText().equals("Profile")){
                    v.getContext().startActivity(new Intent(v.getContext(), ProfilePassenger.class));
                }
            }
        });

        if (row_index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
        }else{
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public StaticRVViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
