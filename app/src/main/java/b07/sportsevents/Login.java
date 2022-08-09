package b07.sportsevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import b07.sportsevents.db.User;
import b07.sportsevents.db.UserCallback;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (User.isLoggedIn()) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }

        ((Button) findViewById(R.id.loginRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.loginConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User.login(
                        ((TextView) Login.this.findViewById(R.id.loginEmail)).getText().toString(),
                        ((TextView) Login.this.findViewById(R.id.loginPassword)).getText().toString(),
                        Login.this,
                        new UserCallback() {
                            @Override
                            public void userStatus(User.UserStatus status, AppCompatActivity activity) {

                                if (status == User.UserStatus.LOGIN_SUCCESS) {
                                    Intent intent = new Intent(Login.this, Home.class);
                                    Toast.makeText(activity, "Login success. Bringing you to the home page.", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }

                                if (status == User.UserStatus.LOGIN_FAILED) {
                                    Toast.makeText(activity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );

            }
        });
    }

}