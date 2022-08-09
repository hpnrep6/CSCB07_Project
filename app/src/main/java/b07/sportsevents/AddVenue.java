package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import b07.sportsevents.db.Venue;

public class AddVenue extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        ((Button) findViewById(R.id.addVenueAddButton)).setOnClickListener(createVenue);

        ((Button) findViewById(R.id.addVenueBackButton)).setOnClickListener(back);

        ((TextView) findViewById(R.id.addVenueSportField)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                Log.d("add venue", text.toString());
                Log.d("add venue", "" + text.toString().contains("\n"));

                if (text.toString().contains("\n")) {
                    ((TextView) findViewById(R.id.addVenueSportField)).setText("");

                    View createdView = getLayoutInflater().inflate(R.layout.fragment_add_venue_sports_listing, null);
                    ((LinearLayout) findViewById(R.id.addVenueSportsContainer)).addView(createdView);
                    ((TextView) createdView.findViewById(R.id.addVenueSportsItem)).setText(text.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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