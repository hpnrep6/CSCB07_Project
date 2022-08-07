package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import b07.sportsevents.db.Event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Sport;
import b07.sportsevents.db.Venue;

public class ViewEvents extends AppCompatActivity{
    public static enum Filter {
        ALL,
        USER,
        SPORT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        //Bundle bundle = getIntent().getExtras();

        String[] filter_by = {"View All", "Filter by Sport", "Filter by Venue"};
        ArrayList<Venue> all_venues = new ArrayList<Venue>();
        List<String> all_sports = new ArrayList<String>();
        List<String> venue_names = new ArrayList<String>();
        Venue.getInstance().queryAll(Venue.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = Objects.requireNonNull(task.getResult()).getChildren();

                for (DataSnapshot venue : venues) {
                    //String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);
                    all_venues.add(readVenue);
                    venue_names.add(readVenue.name);
                    //assert readVenue != null;
                }
            }
        });
        Sport.getInstance().queryAll(Sport.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> sports = Objects.requireNonNull(task.getResult()).getChildren();

                for (DataSnapshot sport : sports) {
                    String sport_name = sport.getKey();
                    //Sport readSport = sport.getValue(Sport.class);
                    all_sports.add(sport_name);
                    //assert readSport!= null;
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, filter_by);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String specified_selected = parent.getItemAtPosition(pos).toString();
                String filter_selected = spinner.getSelectedItem().toString();
                loadScreen(filter_selected, specified_selected);
                //Toast.makeText(parent.getContext(), filter_selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String specified_selected = "";
                String filter_selected = parent.getItemAtPosition(pos).toString();
                if (filter_selected.equals("Filter by Sport")){
                    spinner2.setVisibility(View.VISIBLE);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter adapter2 = new ArrayAdapter(ViewEvents.this,
                            android.R.layout.simple_spinner_item, all_sports);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner2.setAdapter(adapter2);
                    //specified_selected = spinner2.getSelectedItem().toString();
                }
                else if (filter_selected.equals("Filter by Venue")){
                    spinner2.setVisibility(View.VISIBLE);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter adapter2 = new ArrayAdapter(ViewEvents.this,
                            android.R.layout.simple_spinner_item, venue_names);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner2.setAdapter(adapter2);
                    //specified_selected = spinner2.getSelectedItem().toString();
                }
                else{
                   spinner2.setVisibility(View.INVISIBLE);
                   loadScreen(filter_selected, specified_selected);
                }
                //Toast.makeText(parent.getContext(), filter_selected, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

//        Venue.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
//            @Override
//            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {
//
//                Iterable<DataSnapshot> events = task.getResult().getChildren();
//                Iterator eventIterator = events.iterator();
//
//                //Filter filter = ((Filter) bundle.get("filter"));
//
//                String selection = spinner.getSelectedItem().toString();
//                Filter filter = Filter.ALL;
//                if (selection == "Sport"){
//                    filter = Filter.SPORT;
//                }
//
//                while (eventIterator.hasNext()) {
//                    DataSnapshot event = (DataSnapshot) eventIterator.next();
//                    String key = event.getKey();
//                    Event readEvent = event.getValue(Event.class);
//
//                    switch (filter) {
//                        case ALL: {
//                            addEventToScreen(key, readEvent);
//                            break;
//                        }
//
//                        case USER: {
//                            addEventToScreenFilterByUser(key, readEvent);
//                            break;
//                        }
//
//                        case SPORT: {
//                            //addEventToScreenFilterBySport(key, readEvent, (String) bundle.get("sport"));
//                            addEventToScreenFilterBySport(key, readEvent, "basketball");
//                            break;
//                        }
//                    }
//                }
//            }
//        });
    }

    private void loadScreen(String type, String specified){
        LinearLayout ll =findViewById(R.id.viewEventsContainer);
        ll.removeAllViews();
        Venue.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> events = task.getResult().getChildren();
                Iterator eventIterator = events.iterator();

                //Filter filter = ((Filter) bundle.get("filter"));
                String sport ="";
                //String selection = spinner.getSelectedItem().toString();
                Filter filter = Filter.ALL;
                if (Objects.equals(type, "Filter by Sport")){
                    filter = Filter.SPORT;
                    sport = specified;
                }
                else if (Objects.equals(type, "Filter by Venue")){
                    //filter = Filter.VENUE;
                    //String venue = specified;
                }

                while (eventIterator.hasNext()) {
                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    String key = event.getKey();
                    Event readEvent = event.getValue(Event.class);

                    switch (filter) {
                        case ALL: {
                            addEventToScreen(key, readEvent);
                            break;
                        }

                        case USER: {
                            addEventToScreenFilterByUser(key, readEvent);
                            break;
                        }

                        case SPORT: {
                            //addEventToScreenFilterBySport(key, readEvent, (String) bundle.get("sport"));
                            addEventToScreenFilterBySport(key, readEvent, sport);
                            break;
                        }
                    }
                }
            }
        });
    }

    private String getOccupancy(Event event) {
        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
        return numberEnrolled + "/" + event.maxPlayers;
    }

    private void addEventToScreenFilterBySport(String id, Event event, String sport) {
        if (event.sport.equals(sport)) {
            Log.d("events", sport);
            addEventToScreen(id, event);
        }
    }

    private void addEventToScreenFilterByUser(String id, Event event) {
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
        //String venue = Venue.getInstance().getVenueName(event.venueID);
        String start = String.valueOf(event.startTime);
        String end = String.valueOf(event.endTime);
        String occupancy = getOccupancy(event);

        //View createdView = getLayoutInflater().inflate(R.layout.fragment_view_events_event, null);
        View createdView = getLayoutInflater().inflate(R.layout.event_layout, null);

        ((LinearLayout) findViewById(R.id.viewEventsContainer)).addView(createdView);

        //((TextView) createdView.findViewById(R.id.viewEventsEventName)).setText(name);
        //((TextView) createdView.findViewById(R.id.viewEventsEventSport)).setText(sport);
        //((TextView) createdView.findViewById(R.id.viewEventsEventVenue)).setText(venue);
        //((TextView) createdView.findViewById(R.id.viewEventsEventOccupancy)).setText(occupancy);
//        ((TextView) createdView.findViewById(R.id.viewEventsEventID)).setText(id);

        ((TextView) createdView.findViewById(R.id.eventName)).setText(name);
        ((TextView) createdView.findViewById(R.id.eventStart)).setText(start);
        ((TextView) createdView.findViewById(R.id.eventVenue)).setText(venue);
        ((TextView) createdView.findViewById(R.id.eventEnd)).setText(end);
        ((TextView) createdView.findViewById(R.id.eventID)).setText(id);
/*
        if (event.registeredUsers != null) {
            if (event.registeredUsers.size() >= event.maxPlayers) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setText("Event Full");
            }

            if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setText("Drop Event");
            }
        }

        ((Button) createdView.findViewById(R.id.viewEventsEventEnrol)).setOnClickListener(onEnrolClick);
*/
    }
/*
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
    };*/
    public void launchPopup(View v){
        Intent intent = new Intent(this, EventPopupScreen.class);
        View parent = ((View) v.getParent());
        String id = ((TextView) parent.findViewById(R.id.eventID)).getText().toString();
        //pass the event's id to the popup screen
        intent.putExtra("event_id", id);
        startActivity(intent);
    }
}