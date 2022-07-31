package com.example.sports_space;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports_space.data.Event;

public class UpcomingEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events);

        //example data
        //display all of the upcoming events as soon as it opens:
        Event e1 = new Event("Basketball", 12390L, 17, 18, 20);
        Event e2 = new Event("Table Tennis Tournament", 12391L, 12, 23, 20);
        Event e3 = new Event("Soccer", 13390L, 17, 18, 20);
        Event e4 = new Event("Badminton", 12013L, 12, 23, 20);

        String[] eventName = {e1.title, e2.title, e3.title, e4.title};
        long[] venueID = {e1.venueID, e2.venueID, e3.venueID, e4.venueID};
        long[] startTime = {e1.startTime, e2.startTime, e3.startTime, e4.startTime};
        long[] endTime = {e1.endTime, e2.endTime, e3.startTime, e4.startTime};
        int[] occupancy = {e1.occupancy, e2.occupancy, e3.occupancy, e4.occupancy};
        //example data

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        MyAdapter myAdapter = new MyAdapter(this, eventName, venueID, startTime, endTime, occupancy);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //FOR LOOP: for each event that hasn't passed...
        //add the event to the screen

        //change event fields
        /*String s = "event1";
        ConstraintLayout eventBox = (ConstraintLayout)findViewById(R.id.event1); //replace event1 with id of event
        TextView event_name = (TextView)eventBox.findViewById(R.id.eventName);
        event_name.setText("Event Name 1"); //replace Event Name 1 with event name
        TextView event_venue = (TextView)eventBox.findViewById(R.id.eventVenue);
        event_venue.setText("Venue Name");
        TextView event_date = (TextView)eventBox.findViewById(R.id.eventDate);
        event_venue.setText("Event Date");
        TextView event_start = (TextView)eventBox.findViewById(R.id.eventStart);
        event_venue.setText("Start time");
        TextView event_end = (TextView)eventBox.findViewById(R.id.eventEnd);
        event_venue.setText("End Time");*/
    }

    public void launchPopup(View v){
        Intent intent = new Intent(this, EventPopupScreen.class);
        //pass the event's id to the popup screen
        //intent.putExtra(pass id here);
        startActivity(intent);

    }

}
