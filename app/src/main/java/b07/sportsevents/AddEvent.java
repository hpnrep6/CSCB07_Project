package b07.sportsevents;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.DBTable;
import b07.sportsevents.db.Event;
import b07.sportsevents.db.Sport;
import b07.sportsevents.db.User;
import b07.sportsevents.db.Venue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

public class  AddEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String venueID;
    boolean editEvent = false;

    public enum TimeSelectorChoice {
        DAY_START,
        DAY_END
    }

    private String eventID;

    public TimeSelectorChoice timeSelection = TimeSelectorChoice.DAY_START;

    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ((Spinner) findViewById(R.id.addEventStartTimeSelector)).setOnTouchListener(onStartDayClick);
        ((Spinner) findViewById(R.id.addEventEndTimeSelector)).setOnTouchListener(onEndDayClick);

        ((TextView) findViewById(R.id.addEventStartTimeDate)).setText("No date selected");
        ((TextView) findViewById(R.id.addEventEndTimeDate)).setText("No date selected");

        ((Button) findViewById(R.id.addEventConfirmButton)).setOnClickListener(onConfirmClick);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("event")) {
            Event event = ViewEvents.editedEvent;

            String name = event.name;
            int occupancy = event.maxPlayers;
            String description = event.description;
            startTime = event.startTime;
            endTime = event.endTime;

            ((TextView) findViewById(R.id.addEventName)).setText(name);
            ((TextView) findViewById(R.id.addEventPlayers)).setText(String.valueOf(occupancy));
            ((TextView) findViewById(R.id.addEventDescription)).setText(description);

            SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm");

            ((TextView) findViewById(R.id.addEventStartTimeDate)).setText(startTime == 0 ? "No date selected" : format.format(startTime * 1000L));
            ((TextView) findViewById(R.id.addEventEndTimeDate)).setText(endTime == 0 ? "No date selected" : format.format(endTime * 1000L));
            venueID = String.valueOf(event.venueID);

            ((Button) findViewById(R.id.addEventConfirmButton)).setText("Edit event");

            editEvent = true;
            eventID = bundle.getString("eventid");
        } else {
            venueID = bundle.getString("id");
        }

        ((TextView) findViewById(R.id.addEventVenueName)).setText(bundle.getString("name"));

        Event.getInstance().queryByID(venueID, Venue.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                Venue venue = (Venue) value.getResult().getValue(Venue.class);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEvent.this, android.R.layout.simple_spinner_item, android.R.id.text1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((Spinner) findViewById(R.id.addEventSport)).setAdapter(adapter);

                if (venue.sportsOfferedList == null) return;

                adapter.addAll(venue.sportsOfferedList);
            }
        });


        ((Button) findViewById(R.id.addEventBackButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEvent.this, ViewVenues.class);
                intent.putExtra("filter", ViewVenues.Filter.ALL);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener onConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object selected = ((Spinner) findViewById(R.id.addEventSport)).getSelectedItem();
            if (selected == null) {
                Toast.makeText(AddEvent.this, "No sport selected.", Toast.LENGTH_SHORT).show();
            }
            String sport = selected.toString();
            try {
                Event createdEvent = new Event(
                        ((TextView) findViewById(R.id.addEventName)).getText().toString(),
                        sport,
                        ((TextView) findViewById(R.id.addEventDescription)).getText().toString(),
                        Long.parseLong(venueID),
                        startTime,
                        endTime,
                        Integer.parseInt(((TextView) findViewById(R.id.addEventPlayers)).getText().toString()));

                if (editEvent) {
                    Log.d("add event", eventID);
                    Event.getInstance().overwriteOne(createdEvent, eventID, Event.getTableName(), AddEvent.this);
                } else {
                    Event.getInstance().writeOne(createdEvent, Event.getTableName(), AddEvent.this);
                }
                Intent intent;

                if (editEvent) {
                    intent = new Intent(AddEvent.this, ViewEvents.class);
                    intent.putExtra("filter", ViewEvents.Filter.ALL);
                } else {
                    intent = new Intent(AddEvent.this, ViewVenues.class);
                    intent.putExtra("filter", ViewVenues.Filter.ALL);
                }

                if (editEvent)
                    Toast.makeText(AddEvent.this, "Event updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddEvent.this, "Event created", Toast.LENGTH_SHORT).show();
                startActivity(intent);


            } catch (Exception e) {
                Toast.makeText(AddEvent.this, "Failed to create event. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showSelectorDialog() {
        DialogFragment dateSelector = new DateSelector();
        dateSelector.show(getSupportFragmentManager(), null);
    }

    private void showTimeSelectorDialog(final Calendar calendar) {
        TimePickerDialog timeSelector = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                long time = (calendar.getTimeInMillis() / 1000L);
                switch (timeSelection) {
                    case DAY_START:
                        if (endTime != 0 && time > endTime) {
                            Toast.makeText(AddEvent.this, "Invalid start time.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startTime = time;
                        break;
                    case DAY_END:
                        if (startTime != 0 && startTime > time) {
                            Toast.makeText(AddEvent.this, "Invalid end time.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        endTime = time;

                        break;
                }

                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm");

                ((TextView) findViewById(R.id.addEventStartTimeDate)).setText(startTime == 0 ? "No date selected" : format.format(startTime * 1000L));
                ((TextView) findViewById(R.id.addEventEndTimeDate)).setText(endTime == 0 ? "No date selected" : format.format(endTime * 1000L));

                Log.d("add event", "" + format.format(calendar.getTime()) + " " + hour + " " + minute);
            }
        }, 0, 0, false);

        timeSelector.show();
    }

    View.OnTouchListener onStartDayClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                timeSelection = TimeSelectorChoice.DAY_START;
                showSelectorDialog();
            }
            return true;

        }
    };

    View.OnTouchListener onEndDayClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                timeSelection = TimeSelectorChoice.DAY_END;
                showSelectorDialog();
            }
            return true;
        }
    };

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        showTimeSelectorDialog(calendar);
    }


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
