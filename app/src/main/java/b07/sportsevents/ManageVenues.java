package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Venue;

public class ManageVenues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_venues);


        Venue.getInstance().queryAll(Venue.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = task.getResult().getChildren();
                Iterator venueIterator = venues.iterator();

                while (venueIterator.hasNext()) {
                    DataSnapshot venue = (DataSnapshot) venueIterator.next();
                    String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);
                    addVenueToScreen(key, readVenue);
                }
            }
        });
    }

    private void addVenueToScreen(String id, Venue venue) {
        String name = venue.name;
        String location = venue.location;
        String sports = venue.sportsOfferedList != null ? venue.sportsOfferedList.toString() : "No sports offered";

        View createdView = getLayoutInflater().inflate(R.layout.fragment_manage_venues_venue_item, null);
        ((LinearLayout) findViewById(R.id.manageVenuesContainer)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.manageVenuesVenueName)).setText(name);
        ((TextView) createdView.findViewById(R.id.manageVenuesVenueLocation)).setText(location);
        ((TextView) createdView.findViewById(R.id.manageVenuesVenueSports)).setText(sports);
        ((TextView) createdView.findViewById(R.id.manageVenuesVenueID)).setText(id);
        ((Button) createdView.findViewById(R.id.manageVenuesVenueDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = ((View) view.getParent());
                String id = ((TextView) parent.findViewById(R.id.manageVenuesVenueID)).getText().toString();

                Venue.getInstance().removeOne(id, Venue.getTableName(), ManageVenues.this);
                ((ViewGroup) view.getParent().getParent()).removeView((View) view.getParent());
            }
        });
    }
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
                Intent in = new Intent(this, ViewEvents.class);
                in.putExtra("filter", ViewEvents.Filter.USER);
                startActivity(in);
            case R.id.Upcoming_events:
                Intent intent = new Intent(this, ViewEvents.class);
                intent.putExtra("filter", ViewEvents.Filter.ALL);
                startActivity(intent);
                return true;
            case R.id.My_Profile:
                Intent mp = new Intent(this, MyProfile.class);
                startActivity(mp);
                return true;
            case R.id.Schedule_Events:
                Intent i = new Intent(this, ViewVenues.class);
                i.putExtra("filter", ViewVenues.Filter.ALL);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}