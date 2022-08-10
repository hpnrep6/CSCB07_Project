package b07.sportsevents;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Event;
import b07.sportsevents.db.User;
import b07.sportsevents.db.Venue;

public class MyEvents extends ViewEvents {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Event.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> events = task.getResult().getChildren();
                Iterator eventIterator = events.iterator();
                long unixTime = Instant.now().getEpochSecond();
                long endTime ;
                while (eventIterator.hasNext()) {
                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    endTime =event.getValue(Event.class).endTime;
                    unixTime = Instant.now().getEpochSecond();
                    if(unixTime<endTime)
                    {
                        String key = event.getKey();
                        Event readEvent = event.getValue(Event.class);

                        addEventToScreenFilterByUser(key, readEvent);
                    }


                }
            }
        });
    }

//
//    private void addEventToScreenFilterByUser(String id, Event event) {
//        if (event.registeredUsers == null) {
//            return;
//        }
//
//        if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
//            addEventToScreen(id, event);
//        }
//    }
//
//    String getOccupancy(Event event) {
//        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
//        return numberEnrolled + "/" + event.maxPlayers;
//    }
//
//    private void addEventToScreen(String id, Event event) {
//        String name = event.name;
//        String sport = event.sport;
//        String start = String.valueOf(event.startTime);
//        String end = String.valueOf(event.endTime);
//        String occupancy = getOccupancy(event);
//        //String venue = Venue.getInstance().getVenueNameByID(this, event.venueID);
//        String venue = String.valueOf(event.venueID);
//
//
//        View createdView = getLayoutInflater().inflate(R.layout.event_layout, null);
//
//        ((LinearLayout) findViewById(R.id.viewMyEvents)).addView(createdView);
//
//        ((TextView) createdView.findViewById(R.id.eventName)).setText(name);
//        ((TextView) createdView.findViewById(R.id.eventStart)).setText(start);
//        ViewEvents.setVenueNameById(event.venueID, "eventVenue", createdView);
//        ((TextView) createdView.findViewById(R.id.eventEnd)).setText(end);
//        ((TextView) createdView.findViewById(R.id.eventID)).setText(id);
//
//
//
//        if (event.registeredUsers != null) {
//            if (event.registeredUsers.size() >= event.maxPlayers) {
//                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setText("Event Full");
//            }
//
//            if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
//                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setText("Drop Event");
//            }
//        }
//
//        ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setOnClickListener(onEnrolClick);
//    }
//
//
//    private View.OnClickListener onEnrolClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if (((Button) view).getText().toString().equals("Event Full")) {
//                return;
//            }
//
//            View parent = ((View) view.getParent());
//            String id = ((TextView) parent.findViewById(R.id.eventID)).getText().toString();
//            Log.d("event", "" + ((Button) view).getText().toString().equals("Join this Event"));
//
//            if (((Button) view).getText().toString().equals("Join this Event")) {
//                Event.enrolUser(FirebaseAuth.getInstance().getUid(), Long.parseLong(id), MyEvents.this, new DBCallback<Task<DataSnapshot>>() {
//                    @Override
//                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
//
//                        ((Button) view).setText("Drop Event");
//
//                        Event.getInstance().queryByID(Long.parseLong(id), Event.getTableName(), MyEvents.this, new DBCallback<Task<DataSnapshot>>() {
//                            @Override
//                            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
//                                Event updatedEvent = value.getResult().getValue(Event.class);
//                                Log.d("event", "asd");
//                                ((TextView) ((View) view.getParent()).findViewById(R.id.eventOccupancy)).setText(
//                                        getOccupancy(updatedEvent)
//                                );
//                            }
//                        });
//
//                        Log.d("view events", "joined");
//                    }
//                });
//            } else {
//                Event.dropUser(FirebaseAuth.getInstance().getUid(), Long.parseLong(id), MyEvents.this, new DBCallback<Task<DataSnapshot>>() {
//                    @Override
//                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
//                        ((Button) view).setText("Join this Event");
//
//                        Event.getInstance().queryByID(Long.parseLong(id), Event.getTableName(), MyEvents.this, new DBCallback<Task<DataSnapshot>>() {
//                            @Override
//                            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
//                                Event updatedEvent = value.getResult().getValue(Event.class);
//                                Log.d("event", "asd");
//                                ((TextView) ((View) view.getParent()).findViewById(R.id.eventOccupancy)).setText(
//                                        getOccupancy(updatedEvent)
//                                );
//                            }
//                        });
//                        Log.d("view events", "joined");
//                    }
//                });
//            }
//            return;
//        }
//    };

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
