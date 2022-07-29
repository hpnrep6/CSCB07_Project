package com.example.sports_space.data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Sport extends Table {
    public static final String tableName = "Sports";

    public String name;
    public int occupancy;

    public Sport() {}

    public Sport(String name, int occupancy) {
        this.name = name;
        this.occupancy = occupancy;
    }

    public static void getSport(String name, AppCompatActivity activity, DataCallback<Task<DataSnapshot>> callback) {
        FirebaseDatabase.getInstance().getReference().
            child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

    public static void createSport(String name, int occupancy) {
        Sport sport = new Sport(name, occupancy);
        FirebaseDatabase.getInstance().getReference().
                child(Sport.tableName).
                child(name).setValue(occupancy);
    }

}
