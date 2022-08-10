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
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Sport;
import b07.sportsevents.db.User;
import b07.sportsevents.db.Venue;

public class ViewEvents extends AppCompatActivity{
    public static enum Filter {
        ALL,
        USER,
        SPORT,
        VENUE
    }

    public static Event editedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        //spinner selection lists
        //spinner 1
        String[] filter_by = {"View All", "Sport", "Venue"};
        //spinner 2
        List<String> all_sports = Sport.getInstance().getAllSportNames(this);
        System.out.println(all_sports);
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
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
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
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
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
//        if(((LinearLayout) findViewById(R.id.viewMyEvents)).getChildCount()==0){
//            View createdView = getLayoutInflater().inflate(R.layout.no_available_events, null);
//            ((LinearLayout) findViewById(R.id.viewMyEvents)).addView(createdView);
//        }
//        else{
//            System.out.println(((LinearLayout) findViewById(R.id.viewMyEvents)).getChildCount());
//        }

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

    private boolean isAdmin = false;

    void loadScreen(String type, String specified){
        LinearLayout ll=findViewById(R.id.viewMyEvents);
        ll.removeAllViews();

        User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                ViewEvents.this.isAdmin = !((String) value.getResult().child("privileges").getValue()).equals("Customer");


                Event.getInstance().queryAll(Event.getTableName(), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
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

                        long currentTime = System.currentTimeMillis();
                        //System.out.println(String.valueOf(unixTime));
                        while (eventIterator.hasNext()) {

                            DataSnapshot event = (DataSnapshot) eventIterator.next();

                            long endTime = event.getValue(Event.class).endTime * 1000L;

                            if (currentTime <= endTime) {
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
        });

    }

    public static String getOccupancy(Event event) {
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
        String occupancy = getOccupancy(event);

        View createdView = getLayoutInflater().inflate(R.layout.event_layout, null);

        ((LinearLayout) findViewById(R.id.viewMyEvents)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.eventName)).setText(name);
        setVenueNameById(event.venueID, "eventVenue", createdView);
        ((TextView) createdView.findViewById(R.id.eventID)).setText(id);
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
        ((TextView) createdView.findViewById(R.id.eventStart)).setText(event.startTime == 0 ? "No date selected" : format.format(event.startTime * 1000L));
        ((TextView) createdView.findViewById(R.id.eventEnd)).setText(event.endTime == 0 ? "No date selected" : format.format(event.endTime * 1000L));

        if (isAdmin) {
            ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setText("Edit event");

            ((Button) createdView.findViewById(R.id.viewEventsEventEnrol2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ViewEvents.this, AddEvent.class);
                    intent.putExtra("event",  0);
                    intent.putExtra("eventid", id);
                    editedEvent = event;
                    startActivity(intent);
                }
            });
        } else {
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

        ((Button) createdView.findViewById(R.id.moreInfo)).setOnClickListener(description);

    }

    private final View.OnClickListener description = new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            View parent = ((View) view.getParent());
            String id = ((TextView) ((View) parent.getParent()).findViewById(R.id.eventID)).getText().toString();
            Event.getDescription(Long.parseLong(id),ViewEvents.this,view);
            //alert(text);

        }
    };
    public static void alert(String message, AppCompatActivity A) {
        AlertDialog dlg = new AlertDialog.Builder(A).setTitle("Description")
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

            User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(), ViewEvents.this, new DBCallback<Task<DataSnapshot>>() {
                @Override
                public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                    if (((String) value.getResult().child("privileges").getValue()).equals("Customer")) {
                        if (((Button) view).getText().toString().equals("Event Full")) {
                            return;
                        }

                        View parent = ((View) view.getParent());
                        String id = ((TextView) ((View) parent.getParent()).findViewById(R.id.eventID)).getText().toString();
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
                                            ((TextView) ((View) ((View) view.getParent()).getParent()).findViewById(R.id.eventOccupancy)).setText(
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
                                            ((TextView) ((View) ((View) view.getParent()).getParent()).findViewById(R.id.eventOccupancy)).setText(
                                                    getOccupancy(updatedEvent)
                                            );
                                        }
                                    });
                                    Log.d("view events", "joined");
                                }


                            });

                        }
                        return;
                    } else {
                        Toast.makeText(activity, "Admins may not join events.", Toast.LENGTH_SHORT).show();
                    }
                }
            });



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