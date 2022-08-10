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

import java.util.Iterator;
import java.util.Objects;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Event;
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

                while (eventIterator.hasNext()) {
                    DataSnapshot event = (DataSnapshot) eventIterator.next();
                    String key = event.getKey();
                    Event readEvent = event.getValue(Event.class);

                    addEventToScreenFilterByUser(key, readEvent);
                }
            }
        });
    }
}
