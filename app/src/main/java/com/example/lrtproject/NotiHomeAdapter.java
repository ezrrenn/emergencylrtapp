package com.example.lrtproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lrtproject.fragment.LocationMaps;
import com.example.lrtproject.fragment.MapsFragment;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotiHomeAdapter extends RecyclerView.Adapter<NotiHomeAdapter.NotiVHolder>{


    Context contextNoti;
    ArrayList<Noti> notificationDataArrayList;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    public NotiHomeAdapter(Context contextNoti, ArrayList<Noti> notificationDataArrayList){
        this.contextNoti = contextNoti;
        this.notificationDataArrayList = notificationDataArrayList;
    }

    public void setFilteredList(ArrayList<Noti> filteredList){
        this.notificationDataArrayList = filteredList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public NotiHomeAdapter.NotiVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextNoti).inflate(R.layout.recycler_notification, parent,false);

        return new NotiVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiHomeAdapter.NotiVHolder holder, int position) {
        Noti notificationData = notificationDataArrayList.get(position);
        holder.title.setText(notificationData.title);
        holder.body.setText(notificationData.body);
        holder.timestamp.setText(notificationData.getTimestamp().toString());

        // Format the timestamp to display the date and time
        Timestamp timestamp = notificationData.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String date = dateFormat.format(timestamp.toDate());
        holder.timestamp.setText(date);
    }

    @Override
    public int getItemCount() {
        return notificationDataArrayList.size();
    }

    public class NotiVHolder extends RecyclerView.ViewHolder{

        TextView title, body, timestamp;

        public NotiVHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlenoti);
            body = itemView.findViewById(R.id.message_et);
            timestamp = itemView.findViewById(R.id.timestamp_et);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapsFragment.class);
                    context.startActivity(intent);
                }
            });
        }
    }


}
