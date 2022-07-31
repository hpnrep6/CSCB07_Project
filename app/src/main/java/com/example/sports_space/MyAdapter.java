package com.example.sports_space;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context ct;
    String eventName[];
    long eventVenue[];
    long eventStart[];
    long eventEnd[];
    int occupancy[];

    public MyAdapter(Context ct, String eventName[], long eventVenue[], long eventStart[], long eventEnd[], int occupancy[]){
        this.ct = ct;
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.occupancy = occupancy;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.event_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.eventDate.setText(eventDate[position]);
        holder.eventName.setText(eventName[position]);
        holder.eventStart.setText(String.format("%o",eventStart[position]));
        holder.eventEnd.setText(String.format("%o",eventEnd[position]));
        holder.eventVenue.setText(String.format("%o",eventVenue[position]));
    }

    @Override
    public int getItemCount() {
        return eventName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eventName, eventStart, eventEnd, eventDate, eventVenue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventStart = itemView.findViewById(R.id.eventStart);
            eventEnd = itemView.findViewById(R.id.eventEnd);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventVenue = itemView.findViewById(R.id.eventVenue);
        }
    }
}
