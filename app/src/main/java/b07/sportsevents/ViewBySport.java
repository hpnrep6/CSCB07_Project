package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.Sport;

public class ViewBySport extends AppCompatActivity {

    private HashMap<String, Long> sports = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_sport);

        Sport.getInstance().queryAll(Sport.getTableName(), this, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                sports = (HashMap<String, Long>) value.getResult().getValue();

                for (String s: sports.keySet()) {
                    addSportEntry(s);

                    Log.d("sport", s);
                }
            }
        });
    }

    private void addSportEntry(String sportName) {
        View createdView = getLayoutInflater().inflate(R.layout.fragment_view_sports_sport, null);
        ((LinearLayout) findViewById(R.id.viewSportContainer)).addView(createdView);

        ((TextView) createdView.findViewById(R.id.viewSportSportName)).setText(sportName);

        ((Button) createdView.findViewById(R.id.viewSportSportSearchEvent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBySport.this, ViewEvents.class);
                intent.putExtra("filter", ViewEvents.Filter.SPORT);
                intent.putExtra("sport", sportName);
                startActivity(intent);
            }
        });

        ((Button) createdView.findViewById(R.id.viewSportSportSearchVenue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBySport.this, ViewVenues.class);
                intent.putExtra("filter", ViewVenues.Filter.SPORT);
                intent.putExtra("sport", sportName);
                startActivity(intent);
            }
        });
    }

}