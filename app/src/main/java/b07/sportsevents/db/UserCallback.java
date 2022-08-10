package b07.sportsevents.db;

import androidx.appcompat.app.AppCompatActivity;

public interface UserCallback {
    void userStatus(User.UserStatus status, AppCompatActivity activity);

}
