package b07.sportsevents.db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Map;

import b07.sportsevents.R;
import b07.sportsevents.ViewEvents;

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
    public static String  text = "wait plz";
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
    public static void getDescription(long eventID, AppCompatActivity A,View view)
    {
        DatabaseReference event =  FirebaseDatabase.getInstance().getReference().child(getTableName()).child(String.valueOf(eventID));

        event.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful())
                {
                    SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
                        DataSnapshot datasnapshot=task.getResult();
                    text ="Sport: "+String.valueOf(datasnapshot.child("sport").getValue());
                    text += "\nDescription: "+String.valueOf(datasnapshot.child("description").getValue());
                    text += "\nTime Slot: "+ format.format(Long.parseLong(datasnapshot.child("startTime").getValue().toString()) * 1000L)
                            +" to " + format.format(Long.parseLong(datasnapshot.child("endTime").getValue().toString()) * 1000L);
                    text +="\nPlayers: "+ViewEvents.getOccupancy(task.getResult().getValue(Event.class));
                    ViewEvents.alert(text,A);

                }

            }
        });

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
