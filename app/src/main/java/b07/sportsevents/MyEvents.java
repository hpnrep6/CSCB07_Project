package b07.sportsevents;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;
import java.util.Objects;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Event;
import b07.sportsevents.db.Venue;

public class MyEvents extends AppCompatActivity {
    public static enum Filter {
            ALL,
            USER,
            SPORT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Event.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> events = task.getResult().getChildren();
                Iterator eventIterator = events.iterator();

                while (eventIterator.hasNext()) {
                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    String key = event.getKey();
                    Event readEvent = event.getValue(Event.class);

                    addEventToScreenFilterByUser(key, readEvent);
                }
            }
        });
    }

    private void addEventToScreenFilterByUser(String id, Event event) {
        if (event.registeredUsers == null) {
            return;
        }

        if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
            addEventToScreen(id, event);
        }
    }
    
    String getOccupancy(Event event) {
        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
        return numberEnrolled + "/" + event.maxPlayers;
    }

    private void addEventToScreen(String id, Event event) {
        String name = event.name;
        String sport = event.sport;
        String start = String.valueOf(event.startTime);
        String end = String.valueOf(event.endTime);
        String occupancy = getOccupancy(event);
        //String venue = Venue.getInstance().getVenueNameByID(this, event.venueID);
        String venue = String.valueOf(event.venueID);


        View createdView = getLayoutInflater().inflate(R.layout.event_layout, null);

        ((LinearLayout) findViewById(R.id.viewMyEvents)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.eventName)).setText(name);
        ((TextView) createdView.findViewById(R.id.eventStart)).setText(start);
        Venue.getInstance().setVenueNameById(event.venueID, this, "eventVenue", createdView);
        ((TextView) createdView.findViewById(R.id.eventEnd)).setText(end);
        ((TextView) createdView.findViewById(R.id.eventID)).setText(id);
    }
}
