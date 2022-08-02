package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import b07.sportsevents.db.Event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Venue;

public class ViewEvents extends AppCompatActivity {
    public static enum Filter {
        ALL,
        USER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        Bundle bundle = getIntent().getExtras();

        Venue.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> events = task.getResult().getChildren();
                Iterator eventIterator = events.iterator();

                boolean filterByUser = false;

                if (((Filter) bundle.get("filter")) == Filter.USER) {
                    filterByUser = true;
                }

                while (eventIterator.hasNext()) {
                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    String key = event.getKey();
                    Event readEvent = event.getValue(Event.class);

                    addEventToScreen(key, readEvent, filterByUser);
                }
            }
        });
    }

    private String getOccupancy(Event event) {
        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
        return numberEnrolled + "/" + event.maxPlayers;
    }

    private void addEventToScreen(String id, Event event, boolean filterByUser) {
        if (!filterByUser) {
            addEventToScreen(id, event);
            return;
        }

        if (event.registeredUsers == null) {
            return;
        }

        if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
            addEventToScreen(id, event);
        }
    }

    private void addEventToScreen(String id, Event event) {
        String name = event.name;
        String sport = event.sport;
        String venue = String.valueOf(event.venueID);

        String occupancy = getOccupancy(event);

        View createdView = getLayoutInflater().inflate(R.layout.fragment_view_events_event, null);
        ((LinearLayout) findViewById(R.id.viewEventsContainer)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.viewEventsEventName)).setText(name);
        ((TextView) createdView.findViewById(R.id.viewEventsEventSport)).setText(sport);
        ((TextView) createdView.findViewById(R.id.viewEventsEventVenue)).setText(venue);
        ((TextView) createdView.findViewById(R.id.viewEventsEventOccupancy)).setText(occupancy);
        ((TextView) createdView.findViewById(R.id.viewEventsEventID)).setText(id);

        if (event.registeredUsers != null) {
            if (event.registeredUsers.size() >= event.maxPlayers) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setText("Event Full");
            }

            if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setText("Drop Event");
            }
        }

        ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setOnClickListener(onEnrolClick);
    }

    private View.OnClickListener onEnrolClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((Button) view).getText().toString().equals("Event Full")) {
                return;
            }

            View parent = ((View) view.getParent());
            String id = ((TextView) parent.findViewById(R.id.viewEventsEventID)).getText().toString();
            Log.d("event", "" + ((Button) view).getText().toString().equals("Join this Event"));

            if (((Button) view).getText().toString().equals("Join this Event")) {
                Event.enrolUser(FirebaseAuth.getInstance().getUid(), Long.parseLong(id), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                    @Override
                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {

                        ((Button) view).setText("Drop Event");

                        Event.getInstance().queryByID(Long.parseLong(id), Event.getTableName(), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                            @Override
                            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                                Event updatedEvent = value.getResult().getValue(Event.class);
                                Log.d("event", "asd");
                                ((TextView) ((View) view.getParent()).findViewById(R.id.viewEventsEventOccupancy)).setText(
                                        getOccupancy(updatedEvent)
                                );
                            }
                        });

                        Log.d("view events", "joined");
                    }
                });
            } else {
                Event.dropUser(FirebaseAuth.getInstance().getUid(), Long.parseLong(id), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                    @Override
                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                        ((Button) view).setText("Join this Event");

                        Event.getInstance().queryByID(Long.parseLong(id), Event.getTableName(), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                            @Override
                            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                                Event updatedEvent = value.getResult().getValue(Event.class);
                                Log.d("event", "asd");
                                ((TextView) ((View) view.getParent()).findViewById(R.id.viewEventsEventOccupancy)).setText(
                                        getOccupancy(updatedEvent)
                                );
                            }
                        });
                        Log.d("view events", "joined");
                    }
                });
            }
            return;
        }
    };

}