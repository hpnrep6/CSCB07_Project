package com.example.sports_space;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sports_space.data.DataCallback;
import com.example.sports_space.data.Venue;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SAMPLEViewVenueDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleview_venue_detail);

        ((Button) findViewById(R.id.viewVenueDetailButton)).setOnClickListener(confirmVenue);
    }

    private View.OnClickListener confirmVenue = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            long id = Long.parseLong(((TextView) findViewById(R.id.viewVenueDetailID)).getText().toString());

            Venue.getVenue(id, (AppCompatActivity) SAMPLEViewVenueDetail.this, new DataCallback<Task<DataSnapshot>>() {
                @Override
                public void fetchedData(Task venue, AppCompatActivity activity) {
                    SAMPLEViewVenueDetail.initVenue(venue, activity);
                }
            });
        }
    };

    private static void initVenue(Task<DataSnapshot> venueMap, AppCompatActivity activity) {
        String details = "";
        details += venueMap.getResult().child("venueName").getValue() + "\n\n";
        details += venueMap.getResult().child("location").getValue() + "\n\n";
        details += venueMap.getResult().child("description").getValue();

        ((TextView)activity.findViewById(R.id.viewVenueDetailDetails)).setText(details);
    }
}