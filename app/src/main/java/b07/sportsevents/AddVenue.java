package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import b07.sportsevents.db.Venue;

public class AddVenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        ((Button) findViewById(R.id.addVenueAddButton)).setOnClickListener(createVenue);

        ((Button) findViewById(R.id.addVenueBackButton)).setOnClickListener(back);
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AddVenue.this, Home.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener createVenue = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Venue venue = new Venue(
                    ((TextView) findViewById(R.id.addVenueName)).getText().toString(),
                    ((TextView) findViewById(R.id.addVenueLocation)).getText().toString(),
                    ((TextView) findViewById(R.id.addVenueDescription)).getText().toString()
            );

            Venue.getInstance().writeOne(venue, Venue.getTableName(), AddVenue.this);
        }
    };
}