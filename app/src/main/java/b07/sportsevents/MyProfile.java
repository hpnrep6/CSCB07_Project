package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import b07.sportsevents.db.DBCallback;
import b07.sportsevents.db.User;

public class MyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) findViewById(R.id.userEmail)).setText(user.getEmail());

        //set name and email
        User.getInstance().queryByID(FirebaseAuth.getInstance().getUid(), User.getTableName(),this, new DBCallback<Task<DataSnapshot>>(){
            @Override
            public void queriedData(Task<DataSnapshot> value, AppCompatActivity activity) {
                String name = value.getResult().child("name").getValue().toString();
                ((TextView) findViewById(R.id.userName)).setText(name);
            }
        });

        //logout button
        ((Button) findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.logout();
                Intent intent = new Intent(MyProfile.this, Login.class);
                startActivity(intent);
            }
        });
    }
}