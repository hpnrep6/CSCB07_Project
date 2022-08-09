package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import b07.sportsevents.db.Venue;

public class AddVenue extends AppCompatActivity {
    String Name;
    String Location;
    String Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        ((Button) findViewById(R.id.addVenueAddButton)).setOnClickListener(createVenue);

        ((Button) findViewById(R.id.addVenueBackButton)).setOnClickListener(back);
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AddVenue.this, Home.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener createVenue = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Venue venue = new Venue(
                    ((TextView) findViewById(R.id.addVenueName)).getText().toString(),
                    ((TextView) findViewById(R.id.addVenueLocation)).getText().toString(),
                    ((TextView) findViewById(R.id.addVenueDescription)).getText().toString()
            );
            Name=((TextView) findViewById(R.id.addVenueName)).getText().toString();
            Location=((TextView) findViewById(R.id.addVenueLocation)).getText().toString();
            Description=((TextView) findViewById(R.id.addVenueDescription)).getText().toString() ;

            //Venue.getInstance().writeOne(venue, Venue.getTableName(), AddVenue.this);
            alert(Name+" at "+Location+"\nDescription: "+Description,venue,view);
        }
    };
    public  void alert(String message,Venue venue,View v) {
        AlertDialog dlg = new AlertDialog.Builder(AddVenue.this).setTitle("Create Venue?")
                .setMessage(message)
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Venue.getInstance().writeOne(venue, Venue.getTableName(), AddVenue.this);
                        Intent intent = new Intent(AddVenue.this, Home.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("I made a mistake!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();
                    }

                })
//                .setNeutralButton("Yes and Create an Event", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        Venue.getInstance().writeOne(venue, Venue.getTableName(), AddVenue.this);
//                        Intent intent = new Intent(AddVenue.this, AddEvent.class);
//                        String id=Long.toString(venue.ID);
//                        String name=venue.name;
//                        intent.putExtra("id",id);
//                        intent.putExtra("name",name);
//                        startActivity(intent);
//
//                    }
//
//                })
                .create();
        dlg.show();


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