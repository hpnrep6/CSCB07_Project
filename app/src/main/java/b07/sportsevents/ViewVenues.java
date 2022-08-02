package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ViewVenues extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venues);

        Venue.getInstance().queryAll(Venue.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = task.getResult().getChildren();
                Iterator venueIterator = venues.iterator();

                while (venueIterator.hasNext()) {
                    DataSnapshot venue = (DataSnapshot) venueIterator.next();
                    String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);

//                    Log.d("view venue", readVenue.toString());
                    addVenueToScreen(key, readVenue);
                }
            }
        });
    }

    private void addVenueToScreen(String id, Venue venue) {
        String name = venue.name;
        String location = venue.location;
        String description = venue.description;

        View createdView = getLayoutInflater().inflate(R.layout.fragment_view_venues_venue, null);
        ((LinearLayout) findViewById(R.id.viewVenueContainer)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.viewVenueName)).setText(name);
        ((TextView) createdView.findViewById(R.id.viewVenueLocation)).setText(location);
        ((TextView) createdView.findViewById(R.id.viewVenueDescription)).setText(description);
        ((TextView) createdView.findViewById(R.id.viewVenueID)).setText(id);
        ((Button) createdView.findViewById(R.id.viewVenueCreateEvent)).setOnClickListener(onCreateEventClick);
    }

    private View.OnClickListener onCreateEventClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View parent = ((View) view.getParent());
            String id = ((TextView) parent.findViewById(R.id.viewVenueID)).getText().toString();
            String name = ((TextView) parent.findViewById(R.id.viewVenueName)).getText().toString();

            Intent intent = new Intent(ViewVenues.this, AddEvent.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);

            startActivity(intent);

            Log.d("view venue", "id " + id);
        }
    };
}