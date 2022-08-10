package b07.sportsevents;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Event;
import b07.sportsevents.db.Sport;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

public class  AddEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String venueID;

    public enum TimeSelectorChoice {
        DAY_START,
        DAY_END
    };

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

        venueID = bundle.getString("id");

        ((TextView) findViewById(R.id.addEventVenueName)).setText(bundle.getString("name"));
    }

    View.OnClickListener onConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sport = ((TextView) findViewById(R.id.addEventSport)).getText().toString();
            try {

                Event createdEvent = new Event(
                        ((TextView) findViewById(R.id.addEventName)).getText().toString(),
                        sport,
                        ((TextView) findViewById(R.id.addEventDescription)).getText().toString(),
                        Long.parseLong(venueID),
                        startTime,
                        endTime,
                        Integer.parseInt(((TextView) findViewById(R.id.addEventPlayers)).getText().toString()));

                Event.getInstance().writeOne(createdEvent, Event.getTableName(), AddEvent.this);
                
                Sport.addSportToVenue(sport, Long.parseLong(venueID), AddEvent.this, new DBCallback<Task<DataSnapshot>>() {
                    @Override
                    public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                        Intent intent = new Intent(AddEvent.this, ViewVenues.class);
                        intent.putExtra("filter", ViewVenues.Filter.ALL);
                        Toast.makeText(activity, "Event created", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
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
                long unixTime = Instant.now().getEpochSecond();
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

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
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