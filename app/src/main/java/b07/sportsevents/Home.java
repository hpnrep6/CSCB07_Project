package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        ((Button) findViewById(R.id.homeLogOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.logout();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
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
}