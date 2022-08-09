package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import b07.sportsevents.db.User;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((Button) findViewById(R.id.homeAddVenue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddVenue.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeManageVenues)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ManageVenues.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeLogOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.logout();
                Intent intent = new Intent(Home.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        ((Button) findViewById(R.id.homeMyEvents)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ViewEvents.class);
                intent.putExtra("filter", ViewEvents.Filter.USER);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeViewVenues)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ViewVenues.class);
                intent.putExtra("filter", ViewVenues.Filter.ALL);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeViewEvents)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ViewEvents.class);
                intent.putExtra("filter", ViewEvents.Filter.ALL);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeMyProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, MyProfile.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.homeViewBySport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ViewBySport.class);
                startActivity(intent);
            }
        });
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
            case R.id.Upcoming_events:
                Intent ve = new Intent(this, ViewEvents.class);
                startActivity(ve);
                return true;
            case R.id.My_Profile:
                Intent mp = new Intent(this, MyProfile.class);
                startActivity(mp);
                return true;
            case R.id.Schedule_Events:
                Intent se = new Intent(this, ViewVenues.class);
                startActivity(se);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }