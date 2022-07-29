package com.example.sports_space;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sports_space.data.DataCallback;
import com.example.sports_space.data.Venue;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class SAMPLEViewVenues extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleview_venues);

        Venue.getAllVenues(this, new DataCallback<Task<DataSnapshot>>() {
            @Override
            public void fetchedData(Task<DataSnapshot> task, AppCompatActivity activity) {
                DataSnapshot snapshot = task.getResult();
                TextView txt = (TextView) activity.findViewById(R.id.viewAllVenuesText);
                String text = "";

                for (DataSnapshot snap: snapshot.getChildren()) {
                    text += snap.child("venueName").getValue() + "\n\n";
                    text += snap.child("location").getValue() + "\n\n";
                    text += snap.child("description").getValue() + "\n\n";
                    text += "========================\n";
                }

                txt.setText(text);
            }
        });
    }
}