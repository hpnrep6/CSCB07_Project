package com.example.sports_space;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sports_space.data.User;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(loginClick);

        Button logout = (Button) findViewById(R.id.userLogout);
        logout.setOnClickListener(logoutClick);
    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView email = (TextView) findViewById((R.id.loginEmail));
            TextView password = (TextView) findViewById((R.id.loginPassword));

            User.login(Login.this, email.getText().toString(), password.getText().toString());
        }
    };

    private View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            User.logout((AppCompatActivity) Login.this);
        }
    };
}