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
//                Intent intent = new Intent(Home.this, ViewEvents.class);
//                intent.putExtra("filter", ViewEvents.Filter.USER);
                Intent intent = new Intent(Home.this, MyEvents.class);
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
}