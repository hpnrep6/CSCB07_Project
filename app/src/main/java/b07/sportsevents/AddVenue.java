package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import b07.sportsevents.db.Sport;
import b07.sportsevents.db.Venue;

public class AddVenue extends AppCompatActivity {
    private ArrayList<String> sportsList = new ArrayList<>();

    String Name;
    String Location;
    String Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        ((Button) findViewById(R.id.addVenueAddButton)).setOnClickListener(createVenue);

        ((Button) findViewById(R.id.addVenueBackButton)).setOnClickListener(back);

        ((TextView) findViewById(R.id.addVenueSportField)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                Log.d("add venue", text.toString());
                Log.d("add venue", "" + text.toString().contains("\n"));

                if (text.toString().contains("\n")) {
                    ((TextView) findViewById(R.id.addVenueSportField)).setText("");

                    View createdView = getLayoutInflater().inflate(R.layout.fragment_add_venue_sports_listing, null);
                    ((LinearLayout) findViewById(R.id.addVenueSportsContainer)).addView(createdView);
                    String addedString = text.toString().replace("\n", "");
                    ((TextView) createdView.findViewById(R.id.addVenueSportsItem)).setText(addedString);
                    AddVenue.this.sportsList.add(addedString);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            venue.sportsOfferedList = sportsList;

            Name=((TextView) findViewById(R.id.addVenueName)).getText().toString();
            Location=((TextView) findViewById(R.id.addVenueLocation)).getText().toString();
            Description=((TextView) findViewById(R.id.addVenueDescription)).getText().toString();

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

                        Sport.getInstance().writeManyString(sportsList, Sport.getTableName(), AddVenue.this);

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