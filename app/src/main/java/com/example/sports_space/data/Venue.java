package com.example.sports_space.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Venue extends Table {
    public static final String tableName = "Venues";
    public String venueName,
                  location,
                  description;
    public List<String> sportsOffered;

    public Venue() {

    }

    public Venue(String name, String location, String description, List<String> sports) {
        this.venueName = name;
        this.location = location;
        this.description = description;
        this.sportsOffered = sports;
    }

    public static void getVenue(long id, DataCallback<Venue> callback) {
        FirebaseDatabase.getInstance().getReference().
            child(Long.valueOf(id).toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        // TODO: handle failure just like in cscb36
                        return;
                    }

                    callback.fetchedData((Venue) task.getResult().getValue());
                }
            }
        );
    }

    public static void createVenue(String name, String location, String description, List<String> sports) throws DatabaseException {
        Venue venue = new Venue(name, location, description, sports);
        FirebaseDatabase.getInstance().getReference().
                child(Venue.tableName).
                child(Long.valueOf(venue.getUniqueID()).toString()).
                setValue(venue);
    }
}
