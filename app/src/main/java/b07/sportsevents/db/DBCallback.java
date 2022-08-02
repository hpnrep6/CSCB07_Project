package b07.sportsevents.db;

import androidx.appcompat.app.AppCompatActivity;

public interface DBCallback<T> {
    void queriedData(T value, AppCompatActivity activity);
}
