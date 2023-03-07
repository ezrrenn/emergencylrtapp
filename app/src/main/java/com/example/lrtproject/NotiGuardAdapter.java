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

public class NotiGuardAdapter extends RecyclerView.Adapter<NotiGuardAdapter.NotiGHolder>{


    Context contextNotiG;
    ArrayList<NotiG> notiGList;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    public NotiGuardAdapter(Context contextNotiG, ArrayList<NotiG> notiGList){
        this.contextNotiG = contextNotiG;
        this.notiGList = notiGList;
    }

    @NonNull
    @Override
    public NotiGuardAdapter.NotiGHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextNotiG).inflate(R.layout.recycler_noti_guard, parent,false);
        return new NotiGHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiGuardAdapter.NotiGHolder holder, int position) {
        NotiG notiG = notiGList.get(position);
        holder.title1.setText(notiG.title);
        holder.body1.setText(notiG.body);
        holder.timestamp1.setText(notiG.getTimestamp().toString());

        // Format the timestamp to display the date and time
        Timestamp timestamp = notiG.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String date = dateFormat.format(timestamp.toDate());
        holder.timestamp1.setText(date);
    }

    @Override
    public int getItemCount() {
        return notiGList.size();
    }

    public class NotiGHolder extends RecyclerView.ViewHolder{

        TextView title1, body1, timestamp1;

        public NotiGHolder(@NonNull View itemView) {
            super(itemView);
            title1 = itemView.findViewById(R.id.titlenotiG);
            body1 = itemView.findViewById(R.id.message_etG);
            timestamp1 = itemView.findViewById(R.id.timestamp_etG);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LocationMaps.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
