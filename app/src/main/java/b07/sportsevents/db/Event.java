package b07.sportsevents.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class Event extends DBTable<Event> {
    public String name;
    protected long ID;
    public long venueID;
    public long startTime, endTime;
    public int players = 0;
    public int maxPlayers;
    public String description;
    public String sport;
    public Map<String, Long> registeredUsers;

    public Event() {}

    public Event(String name, String sport, String description, long venueID, long startTime, long endTime, int players) {
        this.name = name;
        this.sport = sport;
        this.description = description;
        this.venueID = venueID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxPlayers = players;

//        this.ID = getUniqueID();
    }

    public static String getTableName() {
        return "Events";
    }

    public static Event getInstance() {
        return new Event();
    }

    public static void enrolUser(String userID, long eventID, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> callback) {
        DatabaseReference event =  FirebaseDatabase.getInstance().getReference().child(getTableName()).child(String.valueOf(eventID));
        event.child("registeredUsers").child(userID).setValue(0);

        FirebaseDatabase.getInstance().getReference().child(User.getTableName()).child(userID).child("eventsRegisteredIDs").child(String.valueOf(eventID)).setValue(0);

        callback.queriedData(null, activity);
    }

    public static void dropUser(String userID, long eventID, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> callback) {
        DatabaseReference event =  FirebaseDatabase.getInstance().getReference().child(getTableName()).child(String.valueOf(eventID));
        event.child("registeredUsers").child(userID).removeValue();

        FirebaseDatabase.getInstance().getReference().child(User.getTableName()).child(userID).child("eventsRegisteredIDs").child(String.valueOf(eventID)).removeValue();

        callback.queriedData(null, activity);
    }
}
