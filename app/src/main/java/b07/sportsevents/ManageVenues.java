package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        ((Button) findViewById(R.id.addVenue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageVenues.this, AddVenue.class);
                startActivity(intent);
            }
        });
    }

    private void addVenueToScreen(String id, Venue venue) {
        String name = venue.name;
        String location = venue.location;
        String description = venue.description;
        String sports = venue.sportsOfferedList != null ? venue.sportsOfferedList.toString() : " No sports offered ";

        //View createdView = getLayoutInflater().inflate(R.layout.fragment_manage_venues_venue_item, null);
        View createdView = getLayoutInflater().inflate(R.layout.venue_layout, null);
        ((LinearLayout) findViewById(R.id.manageVenuesContainer)).addView(createdView);
        ((Button) createdView.findViewById(R.id.venueButton)).setText("Delete Venue");

        ((TextView) createdView.findViewById(R.id.viewVenueName)).setText(name);
        ((TextView) createdView.findViewById(R.id.viewVenueLocation)).setText(location);
        ((TextView) createdView.findViewById(R.id.venueSports2)).setText(sports.substring(1,sports.length()-1));
        ((TextView) createdView.findViewById(R.id.viewVenueDescription)).setText(description);
        ((TextView) createdView.findViewById(R.id.viewVenueID)).setText(id);

//        ((TextView) createdView.findViewById(R.id.manageVenuesVenueName)).setText(name);
//        ((TextView) createdView.findViewById(R.id.manageVenuesVenueLocation)).setText(location);
//        ((TextView) createdView.findViewById(R.id.manageVenuesVenueSports)).setText(sports);
//        ((TextView) createdView.findViewById(R.id.manageVenuesVenueID)).setText(id);
        ((Button) createdView.findViewById(R.id.venueButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = ((View) view.getParent());
                String id = ((TextView) parent.findViewById(R.id.manageVenuesVenueID)).getText().toString();

                Venue.getInstance().removeOne(id, Venue.getTableName(), ManageVenues.this);
                ((ViewGroup) view.getParent().getParent()).removeView((View) view.getParent());
            }
        });
    }




}