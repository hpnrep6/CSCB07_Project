package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.User;

public class MyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
        ((TextView) findViewById(R.id.userEmail)).setText(email);}

        user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) findViewById(R.id.userEmail)).setText(user.getEmail());

        //set name and email
        User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(),this, new DBCallback<Task<DataSnapshot>>(){
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                String name = value.getResult().child("name").getValue().toString();

                String userlevel = value.getResult().child("privileges").getValue().toString();
                ((TextView) findViewById(R.id.userName)).setText(name);
                ((TextView) findViewById(R.id.usertypeprofile)).setText(userlevel);
                // ((TextView) findViewById(R.id.userName)).setText(name);

            }
        });

        //logout button
        ((Button) findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.logout();
                Intent intent = new Intent(MyProfile.this, Login.class);
                finishAffinity();
                startActivity(intent);

            }
        });
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
            case R.id.My_Events: {
                Intent in = new Intent(this, MyEvents.class);
                startActivity(in);
                return true;
            }
            case R.id.Upcoming_events: {

                Intent intent = new Intent(this, ViewEvents.class);
                intent.putExtra("filter", ViewEvents.Filter.ALL);
                startActivity(intent);
                return true;
            }
            case R.id.My_Profile: {
                Intent mp = new Intent(this, MyProfile.class);
                startActivity(mp);
                return true;
            }
            case R.id.Schedule_Events: {
                Intent i = new Intent(this, ViewVenues.class);
                i.putExtra("filter", ViewVenues.Filter.ALL);
                startActivity(i);
                return true;
            }

            case R.id.Manage_Venues: {
                Intent intent = new Intent(this, ManageVenues.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}