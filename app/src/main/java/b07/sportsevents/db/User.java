package b07.sportsevents.db;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.List;

import b07.sportsevents.R;

public class User extends DBTable<User> {
    public static enum UserStatus {
        ALREADY_LOGGED_IN,
        LOGIN_SUCCESS,
        LOGIN_FAILED,
        REGISTER_SUCCESS,
        REGISTER_FAILED,
        IS_ADMIN
    };

    @Exclude
    protected String userID;
    public long registrationDate;
    public String name;
    public List<String> eventsRegisteredIDs;
    public String privileges;

    public User() {}

    private User(String name) {
        this.name = name;
    }

    public static String getTableName() {
        return "Users";
    }

    @Exclude
    public static User getInstance() {
        return new User();
    }

    public static void login(String email, String password, AppCompatActivity activity, UserCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            callback.userStatus(UserStatus.ALREADY_LOGGED_IN, activity);
        }

        try{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        callback.userStatus(UserStatus.LOGIN_SUCCESS, activity);
                    } else {
                        callback.userStatus(UserStatus.LOGIN_FAILED, activity);
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(activity, "Invalid email, name, or password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void register(String email, String password, String name, AppCompatActivity activity, UserCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            callback.userStatus(UserStatus.ALREADY_LOGGED_IN, activity);
        }

        if (email == null || password == null) {
            callback.userStatus(UserStatus.REGISTER_FAILED, activity);
        }

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("user", "login attempted");
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                User userSend = new User(name);
                                userSend.privileges = "Customer";

                                FirebaseDatabase.getInstance().getReference().child(getTableName()).
                                        child(user.getUid()).setValue(userSend);
                                callback.userStatus(UserStatus.REGISTER_SUCCESS, activity);
                            } else {
                                Log.d("user", "login failed");
                                callback.userStatus(UserStatus.REGISTER_FAILED, activity);
                            }
                        }
                    }
            );
        } catch (IllegalArgumentException e) {
            Toast.makeText(activity, "Invalid email, name, or password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


    public boolean isAdmin(AppCompatActivity activity) {
        final boolean[] admin = {false};
        User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(), activity, new DBCallback<Task<DataSnapshot>>(){
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                String name = value.getResult().child("privileges").getValue().toString();
                Log.d("privil", name);
                if (name.equals("admin"))
                    admin[0] = true;
            }
        });
        return admin[0];
    }

}
