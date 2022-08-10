package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.User;
import b07.sportsevents.db.Venue;

public class ViewVenues extends AppCompatActivity {
    public static enum Filter {
        ALL,
        SPORT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venues);

        Bundle bundle = getIntent().getExtras();
        Log.d("venue", "asd");
        Venue.getInstance().queryAll(Venue.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = task.getResult().getChildren();
                Iterator venueIterator = venues.iterator();

                while (venueIterator.hasNext()) {
                    DataSnapshot venue = (DataSnapshot) venueIterator.next();
                    String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);

                    switch ((Filter) bundle.get("filter")) {
                        case ALL: {
                            addVenueToScreen(key, readVenue);
                            break;
                        }

                        case SPORT: {
                            addVenueToStringFilterBySport(key, readVenue, (String) bundle.get("sport"));
                            break;
                        }

                    }
                }
            }
        });
    }

    public void addVenueToStringFilterBySport(String id, Venue venue, String sport) {
        if (venue.sportsOfferedList == null) {
            return;
        }

        if (venue.sportsOfferedList.contains(sport)) {
            addVenueToScreen(id, venue);
        }
    }

    @SuppressLint("SetTextI18n")
    private void addVenueToScreen(String id, Venue venue) {
        String name = venue.name;
        String location = venue.location;
        String description = venue.description;
        String sports = venue.sportsOfferedList != null ? venue.sportsOfferedList.toString() : " No sports offered ";

        //View createdView = getLayoutInflater().inflate(R.layout.fragment_view_venues_venue, null);
        View createdView = getLayoutInflater().inflate(R.layout.venue_layout, null);

        ((LinearLayout) findViewById(R.id.viewVenueContainer)).addView(createdView);
        ((Button) createdView.findViewById(R.id.venueButton)).setText("Create an event at this venue");
        ((TextView) createdView.findViewById(R.id.venueSports2)).setText(sports.substring(1,sports.length()-1));
        ((TextView) createdView.findViewById(R.id.viewVenueName)).setText(name);
        ((TextView) createdView.findViewById(R.id.viewVenueLocation)).setText(location);
        ((TextView) createdView.findViewById(R.id.viewVenueDescription)).setText(description);
        ((TextView) createdView.findViewById(R.id.viewVenueID)).setText(id);


      ((Button) createdView.findViewById(R.id.venueButton)).setOnClickListener(onCreateEventClick);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                if (((String) value.getResult().child("privileges").getValue()).equals("Customer")) {
                    inflater.inflate(R.menu.menu_customer, menu);
                } else {
                    inflater.inflate(R.menu.menu_admin, menu);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.My_Events:
                Intent in = new Intent(this, MyEvents.class);
                startActivity(in);
                return true;
//            case R.id.Manage_events:
//                Intent me = new Intent(this, Manageevents.class);
//                startActivity(me);
//                return true;
            case R.id.Manage_Venues:
                Intent mv = new Intent(this, ManageVenues.class);
                startActivity(mv);
                return true;
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