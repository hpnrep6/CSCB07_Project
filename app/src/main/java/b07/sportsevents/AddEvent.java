package b07.sportsevents;

import b07.sportsevents.db.Event;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class  AddEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private boolean showingCalendar = false;
    private String venueID;

    public enum TimeSelectorChoice {
        DAY_START,
        DAY_END
    };
    public TimeSelectorChoice timeSelction = TimeSelectorChoice.DAY_START;

    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ((Spinner) findViewById(R.id.addEventStartTimeSelector)).setOnTouchListener(onStartDayClick);
        ((Spinner) findViewById(R.id.addEventEndTimeSelector)).setOnTouchListener(onEndDayClick);

        ((Button) findViewById(R.id.addEventConfirmButton)).setOnClickListener(onConfirmClick);

        Bundle bundle = getIntent().getExtras();

        venueID = bundle.getString("id");

        ((TextView) findViewById(R.id.addEventVenueName)).setText(bundle.getString("name"));
    }

    View.OnClickListener onConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Event createdEvent = new Event(
                    ((TextView) findViewById(R.id.addEventName)).getText().toString(),
                    ((TextView) findViewById(R.id.addEventSport)).getText().toString(),
                    ((TextView) findViewById(R.id.addEventDescription)).getText().toString(),
                    Long.parseLong(venueID),
                    startTime,
                    endTime,
                    Integer.parseInt(((TextView) findViewById(R.id.addEventPlayers)).getText().toString()));

            Event.getInstance().writeOne(createdEvent,Event.getTableName(), AddEvent.this);
        }
    };

    private void showSelectorDialog() {
        showingCalendar = true;
        DialogFragment dateSelector = new DateSelector();
        dateSelector.show(getSupportFragmentManager(), null);
    }

    View.OnTouchListener onStartDayClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (showingCalendar) {
                return true;
            }

            timeSelction = TimeSelectorChoice.DAY_START;
            showSelectorDialog();
            return true;
        }
    };

    View.OnTouchListener onEndDayClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d("a", "Asdasd");
            if (showingCalendar) {
                return true;
            }

            timeSelction = TimeSelectorChoice.DAY_END;
            showSelectorDialog();
            return true;
        }
    };

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        showingCalendar = false;
        Log.d("add event", "" + (calendar.getTimeInMillis() / 1000L));

        long time = (calendar.getTimeInMillis() / 1000L);

        switch (timeSelction) {
            case DAY_START:
                startTime = time;
                break;
            case DAY_END:
                endTime = time;
                break;
        }
    }
}