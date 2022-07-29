package com.example.sports_space;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sports_space.data.Venue;

public class AddVenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        ((Button) findViewById(R.id.addVenueConfirm)).setOnClickListener(confirmVenue);
    }

    private View.OnClickListener confirmVenue = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = ((TextView) findViewById(R.id.addVenueName)).getText().toString();
            String location = ((TextView) findViewById(R.id.addVenueLocation)).getText().toString();
            String description = ((TextView) findViewById(R.id.addVenueDescription)).getText().toString();

            Venue.createVenue(name, location, description, null);

            ((TextView) findViewById(R.id.addVenueName)).setText("");
            ((TextView) findViewById(R.id.addVenueLocation)).setText("");
            ((TextView) findViewById(R.id.addVenueDescription)).setText("");
        }
    };
}