package com.example.lrtproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lrtproject.fragment.LocationMaps;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotiAuthAdapter extends RecyclerView.Adapter<NotiAuthAdapter.NotiAHolder> {

    Context contextNotiA;
    ArrayList<NotiA> notiArrayList;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(NotiAuthAdapter.Dialog dialog){
        this.dialog = dialog;
    }

    public NotiAuthAdapter(Context contextNotiA, ArrayList<NotiA> notiArrayList){
        this.contextNotiA = contextNotiA;
        this.notiArrayList = notiArrayList;
    }

    @NonNull
    @Override
    public NotiAuthAdapter.NotiAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextNotiA).inflate(R.layout.recycler_item_noti, parent,false);

        return new NotiAHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiAuthAdapter.NotiAHolder holder, int position) {
        NotiA noti = notiArrayList.get(position);
        holder.titleA.setText(noti.title);
        holder.bodyA.setText(noti.body);
        holder.timestampA.setText(noti.getTimestamp().toString());

        // Format the timestamp to display the date and time
        Timestamp timestamp = noti.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String date = dateFormat.format(timestamp.toDate());
        holder.timestampA.setText(date);
    }

    @Override
    public int getItemCount() {
        return notiArrayList.size();
    }

    public class NotiAHolder extends RecyclerView.ViewHolder{

        TextView titleA, bodyA, timestampA;

        public NotiAHolder(@NonNull View itemView) {
            super(itemView);
            titleA = itemView.findViewById(R.id.titlenoti1);
            bodyA = itemView.findViewById(R.id.message_et1);
            timestampA = itemView.findViewById(R.id.timestamp_et1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ReportHomeA.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
