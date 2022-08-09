package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //if (isadmin()){ inflater.inflate(R.menu.menu_admin, menu)} else{;
        inflater.inflate(R.menu.menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.My_Events:
            case R.id.Upcoming_events:
                Intent ve = new Intent(this, ViewEvents.class);
                startActivity(ve);
                return true;
            case R.id.My_Profile:
                Intent mp = new Intent(this, MyProfile.class);
                startActivity(mp);
                return true;
            case R.id.Schedule_Events:
                Intent se = new Intent(this, ViewVenues.class);
                startActivity(se);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}