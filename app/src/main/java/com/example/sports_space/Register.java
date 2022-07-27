package com.example.sports_space;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sports_space.data.User;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(registerClick);
    }

    private View.OnClickListener registerClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView email = (TextView) findViewById((R.id.registerEmail));
            TextView password = (TextView) findViewById((R.id.registerPassword));
            TextView fullname = (TextView) findViewById((R.id.registerFullName));

            User user = new User(email.getText().toString(),
                                 fullname.getText().toString(),
                                 password.getText().toString()
            );

            user.register();
        }
    };
}