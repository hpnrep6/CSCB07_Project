package com.example.sports_space.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Event extends Table {
    public static final String tableName = "Events";

    public String title;
    public String sport;
    public long venueID;
    public long startTime, endTime;
    public int occupancy;
    public List<String> usersRegistered;

    public Event() {}

    public Event(String title, long venueID, long startTime, long endTime, int occupancy) {
        this.title = title;
        this.venueID = venueID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.occupancy = occupancy;
    }

    public static void getEvent(long eventID, AppCompatActivity activity, DataCallback<Task<DataSnapshot>> callback) {
        FirebaseDatabase.getInstance().getReference().
            child(Long.valueOf(eventID).toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        // TODO: handle failure just like in cscb36
                        return;
                    }

                    callback.fetchedData(task, activity);
                }
            }
        );
    }

    public static void createEvent(String title, long venueID, long startTime, long endTime, int occupancy) {
        Event event = new Event(title, venueID, startTime, endTime, occupancy);
        FirebaseDatabase.getInstance().getReference().
                child(Event.tableName).
                child(Long.valueOf(event.getUniqueID()).toString()).
                setValue(event);
    }
}
