package b07.sportsevents.db;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class DBTable <T extends DBTable> {
    @Exclude
    public long ID;

    protected long getUniqueID() {
        return System.currentTimeMillis() - (long) (Math.random() * 1231231);
    }

    public static String getTableName() {
        // Because Java has bad OOP design and there are no static abstract methods
        throw new RuntimeException("getTableName not implemeneted");
    }

    public void queryAll(String tableName, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> cb) {
        FirebaseDatabase.getInstance().getReference().child(tableName).
            get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                cb.queriedData(task, activity);
            }
        });
    }

    public void queryByID(long ID, String tableName, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> cb) {
        FirebaseDatabase.getInstance().getReference().child(tableName).
            child(Long.valueOf(ID).toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                cb.queriedData(task, activity);
            }
        });
    }
    public void queryByID(String ID, String tableName, AppCompatActivity activity, DBCallback<Task<DataSnapshot>> cb) {
        FirebaseDatabase.getInstance().getReference().child(tableName).
                child(ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        cb.queriedData(task, activity);
                    }
                });
    }

    public void writeOne(T value, String tablename, AppCompatActivity activity) {
        FirebaseDatabase.getInstance().getReference().child(tablename).
            child(Long.valueOf(value.getUniqueID()).toString()).setValue(value);
    }

    public void writeOneString(String value, String tablename, AppCompatActivity activity) {
        FirebaseDatabase.getInstance().getReference().child(tablename).
                child(value).setValue(0);
    }

    public void writeMany(List<T> values, String tablename, AppCompatActivity activity) {
        for (int i = 0; i < values.size(); ++i) {
            writeOne(values.get(i), tablename, activity);
        }
    }

    public void writeManyString(ArrayList<String> values, String tablename, AppCompatActivity activity) {
        for (int i = 0; i < values.size(); ++i) {
            writeOneString(values.get(i), tablename, activity);
        }
    }

    public void removeOne(String id, String tablename, AppCompatActivity activity) {
        FirebaseDatabase.getInstance().getReference().child(tablename).
                child(String.valueOf(id)).removeValue();
    }

    public void overwriteOne(T value, String ID, String tablename, AppCompatActivity activity) {
        FirebaseDatabase.getInstance().getReference().child(tablename).
                child(ID).setValue(value);
    }
}
