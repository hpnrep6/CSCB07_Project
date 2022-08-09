package b07.sportsevents.db;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sport extends DBTable<Sport> {
    public String name;

    public static String getTableName() {
        return "Sports";
    }

    public Sport() {}

    public Sport(String name) {
        this.name = name;
    }

    @Exclude
    public static Sport getInstance() {
        return new Sport();
    }

    public static void addSportToVenue(String sportName, long venueID, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> callback) {

        FirebaseDatabase.getInstance().getReference().child(getTableName()).child(sportName).setValue(0);
        DatabaseReference venueOfferedList = FirebaseDatabase.getInstance().getReference().child(Venue.getTableName()).child(String.valueOf(venueID)).child("sportsOfferedList");

        venueOfferedList.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> sportsOffered = (ArrayList<String>) task.getResult().getValue();

                if (sportsOffered == null) {
                    sportsOffered = new ArrayList<>();
                }

                if (!sportsOffered.contains(sportName)) {
                    sportsOffered.add(sportName);
                    venueOfferedList.setValue(sportsOffered).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            callback.queriedData(null, activity);
                        }
                    });
                }
            }
        });
    }

    public List<String> getAllSportNames(AppCompatActivity a){
        List<String> all_sports = new ArrayList<String>();
        Sport.getInstance().queryAll(Sport.getTableName(), a, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> sports = Objects.requireNonNull(task.getResult()).getChildren();

                for (DataSnapshot sport : sports) {
                    String sport_name = sport.getKey();
                    all_sports.add(sport_name);
                }
            }
        });
        return all_sports;
    }
}
