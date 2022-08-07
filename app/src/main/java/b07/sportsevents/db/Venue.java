package b07.sportsevents.db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.List;

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


}
