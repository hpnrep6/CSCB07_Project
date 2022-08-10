package b07.sportsevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import b07.sportsevents.db.Event;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
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
        SPORT,
        VENUE
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        //spinner selection lists
        //spinner 1
        String[] filter_by = {"View All", "Sport", "Venue"};
        //spinner 2
        List<String> all_sports = Sport.getInstance().getAllSportNames(this);
        List<String> venue_names = Venue.getInstance().getAllVenueNames(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_dropdown_item, filter_by);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                String specified_selected = "";
                String filter_selected = parent.getItemAtPosition(pos).toString();
                if (filter_selected.equals("Sport")){
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
                else if (filter_selected.equals("Venue")){
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

    public static void setVenueNameById(long id, String view_id, View v) {
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Venues").child(String.valueOf(id)).child("name");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ((TextView) v.findViewById(R.id.eventVenue)).setText((String) snapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: ");
            }
        });
    }

    void loadScreen(String type, String specified){
        LinearLayout ll=findViewById(R.id.viewMyEvents);
        ll.removeAllViews();
        Event.getInstance().queryAll(Event.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> events = task.getResult().getChildren();
                Iterator eventIterator = events.iterator();

                //Filter filter = ((Filter) bundle.get("filter"));
                String sport ="";
                String venue ="";
                //String selection = spinner.getSelectedItem().toString();
                Filter filter = Filter.ALL;
                if (Objects.equals(type, "Sport")){
                    filter = Filter.SPORT;
                    sport = specified;
                }
                else if (Objects.equals(type, "Venue")){
                    filter = Filter.VENUE;
                    venue = specified;
                }

                long unixTime = Instant.now().getEpochSecond();
                long endTime ;
                //System.out.println(String.valueOf(unixTime));
                while (eventIterator.hasNext()) {

                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    endTime =event.getValue(Event.class).endTime;
                    unixTime = Instant.now().getEpochSecond();

                    if(unixTime<endTime)
                    {
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
                            case VENUE:{
                                addEventToScreenFilterByVenue(key, readEvent, venue);
                            }
                        }

                    }

                }
            }
        });
    }

    static String getOccupancy(Event event) {
        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
        return numberEnrolled + "/" + event.maxPlayers;
    }
    public static boolean checkfull(Event event) {
        int numberEnrolled = event.registeredUsers == null ? 0 : event.registeredUsers.size();
        return numberEnrolled <event.maxPlayers;
    }


    private void addEventToScreenFilterByVenue(String id, Event event, String venue_name){
        //compares venue names not IDs
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Venues").child(String.valueOf(event.venueID)).child("name");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null && snapshot.getValue().toString().equals(venue_name)){
                    addEventToScreen(id, event);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: ");
            }
        });
    }

    private void addEventToScreenFilterBySport(String id, Event event, String sport) {
        if (event.sport.equals(sport)) {
            Log.d("events", sport);
            addEventToScreen(id, event);
        }
    }

    void addEventToScreenFilterByUser(String id, Event event) {
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
        String start = String.valueOf(event.startTime);
        String end = String.valueOf(event.endTime);
        String occupancy = getOccupancy(event);

        View createdView = getLayoutInflater().inflate(R.layout.event_layout, null);

        ((LinearLayout) findViewById(R.id.viewMyEvents)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.eventName)).setText(name);
        ((TextView) createdView.findViewById(R.id.eventStart)).setText(start);
        setVenueNameById(event.venueID, "eventVenue", createdView);
        ((TextView) createdView.findViewById(R.id.eventEnd)).setText(end);
        ((TextView) createdView.findViewById(R.id.eventID)).setText(id);

        if (event.registeredUsers != null) {
            if (event.registeredUsers.size() >= event.maxPlayers) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setText("Event Full");
            }

            if (event.registeredUsers.containsKey(FirebaseAuth.getInstance().getUid())) {
                ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setText("Drop Event");
            }
        }
        ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setOnClickListener(onEnrolClick);


    }

    private final View.OnClickListener description =new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            View parent = ((View) view.getParent());
            String id = ((TextView) parent.findViewById(R.id.viewEventsEventID)).getText().toString();
            Event.getDescription(Long.parseLong(id),ViewEvents.this,view);
            //alert(text);

        }
    };
    public static void alert(String message, AppCompatActivity A) {
        AlertDialog dlg = new AlertDialog.Builder(A).setTitle("Description for the event")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .create();
        dlg.show();


    }
    View.OnClickListener onEnrolClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((Button) view).getText().toString().equals("Event Full")) {
                return;
            }

            View parent = ((View) view.getParent());
            String id = ((TextView) parent.findViewById(R.id.eventID)).getText().toString();
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
                                ((TextView) ((View) view.getParent()).findViewById(R.id.eventOccupancy)).setText(
                                        getOccupancy(updatedEvent)

                                );

                            }
                        });

                        Log.d("view events", "joined");
                    }
                });
            } else {
                Event.dropUser(FirebaseAuth.getInstance().getUid(), Long.parseLong(id),ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                    @Override
                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                        ((Button) view).setText("Join this Event");

                        Event.getInstance().queryByID(Long.parseLong(id), Event.getTableName(), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                            @Override
                            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                                Event updatedEvent = value.getResult().getValue(Event.class);
                                Log.d("event", "asd");
                                ((TextView) ((View) view.getParent()).findViewById(R.id.eventOccupancy)).setText(
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


    //just for testing onClick
    public void launchPopup(View v){
        Intent intent = new Intent(this, EventPopupScreen.class);
        View parent = ((View) v.getParent());
        String id = ((TextView) parent.findViewById(R.id.eventID)).getText().toString();
        //pass the event's id to the popup screen
        intent.putExtra("event_id", id);
        startActivity(intent);
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
                Intent in = new Intent(this, MyEvents.class);
                startActivity(in);
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