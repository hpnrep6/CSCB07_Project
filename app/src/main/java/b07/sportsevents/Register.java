package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import b07.sportsevents.db.User;
import b07.sportsevents.db.UserCallback;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (User.isLoggedIn()) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }

        ((Button) findViewById(R.id.registerLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.registerConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("user", "register button clicked");
                User.register(
                        ((TextView) Register.this.findViewById(R.id.registerEmail)).getText().toString(),
                        ((TextView) Register.this.findViewById(R.id.registerPassword)).getText().toString(),
                        ((TextView) Register.this.findViewById(R.id.registerFullName)).getText().toString(),
                        Register.this,
                        new UserCallback() {
                            @Override
                            public void userStatus(User.UserStatus status, AppCompatActivity activity) {
                                if (status == User.UserStatus.REGISTER_SUCCESS) {
                                    Intent intent = new Intent(Register.this, Home.class);
                                    Toast.makeText(activity, "Registration success. Bringing you to the home page.", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }

                                if (status == User.UserStatus.REGISTER_FAILED) {
                                    Toast.makeText(activity, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );

            }
        });

    }
}