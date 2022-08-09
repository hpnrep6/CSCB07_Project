package b07.sportsevents.db;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import b07.sportsevents.R;
import b07.sportsevents.ViewVenues;

public class Venue extends DBTable<Venue> {
    public String name;
    public String location;
    public String description;
    public List<String> sportsOfferedList;

    public Venue() {}

    public Venue(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.ID = getUniqueID();
    }

    public static String getTableName() {
        return "Venues";
    }

    public static Venue getInstance() {
        return new Venue();
    }

    @Override
    public String toString() {
        return name + " " + location + " " + description + " " + sportsOfferedList;
    }

    public List<Venue> getAllVenues(AppCompatActivity a){
        ArrayList<Venue> all_venues = new ArrayList<Venue>();
        Venue.getInstance().queryAll(Venue.getTableName(), a, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = Objects.requireNonNull(task.getResult()).getChildren();

                for (DataSnapshot venue : venues) {
                    //String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);
                    all_venues.add(readVenue);
                    //venue_names.add(readVenue.name);
                    //assert readVenue != null;
                }
            }
        });
        return all_venues;
    }

    public List<String> getAllVenueNames(AppCompatActivity a){
        ArrayList<String> all_venue_names = new ArrayList<String>();
        Venue.getInstance().queryAll(Venue.getTableName(), a, new DBCallback<Task<DataSnapshot>>() {
            @Override
            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {

                Iterable<DataSnapshot> venues = Objects.requireNonNull(task.getResult()).getChildren();

                for (DataSnapshot venue : venues) {
                    //String key = venue.getKey();
                    Venue readVenue = venue.getValue(Venue.class);
                    //all_venue.add(readVenue);
                    all_venue_names.add(readVenue.name);
                    //assert readVenue != null;
                }
            }
        });
        return all_venue_names;
    }

//    public void setVenueNameById(long id, AppCompatActivity a, String view_id, View v) {
//        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Venues").child(String.valueOf(id)).child("name");
//        d.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                ((TextView) v.findViewById(R.id.eventVenue)).setText((String) snapshot.getValue());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                System.out.println("The read failed: ");
//            }
//        });
//    }

    public long getVenueIdByName(String name){

        return 0;
    }

//    public String getVenueNameById(long id, AppCompatActivity a){
//        ArrayList<String> name = new ArrayList<String>();
//        Venue.getInstance().queryAll(Venue.getTableName(), a, new DBCallback<Task<DataSnapshot>>() {
//            @Override
//            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {
//
//                Iterable<DataSnapshot> venues = Objects.requireNonNull(task.getResult()).getChildren();
//
//                for (DataSnapshot venue : venues) {
//                    //String key = venue.getKey();
//                    Venue readVenue = venue.getValue(Venue.class);
//                    if (String.valueOf(id).equals(venue.getKey())){
//                        name.add(readVenue.name);
//                    }
//                }
//            }
//        });
//        return name.get(0);
//    }

// returns an empty string?
//    public String getVenueNameByID(AppCompatActivity a, long venueID){
//        final String[] venue_name = new String[1];
//        Venue.getInstance().queryAll(Venue.getTableName(), a, new DBCallback<Task<DataSnapshot>>() {
//            @Override
//            public void queriedData(Task<DataSnapshot> task, AppCompatActivity activity) {
//
//                Iterable<DataSnapshot> venues = Objects.requireNonNull(task.getResult()).getChildren();
//
//                for (DataSnapshot venue : venues) {
//                    if (Long.parseLong(Objects.requireNonNull(venue.getKey())) == venueID) {
//                        //System.out.println(venue.getKey() + " " + event.venueID);
//                        venue_name[0] = Objects.requireNonNull(venue.getValue(Venue.class)).name.toString();
//                        //System.out.println((venue.getValue(Venue.class)).name);
//                    }
//                }
//            }
//        });
//        return venue_name[0];
//    }
}
