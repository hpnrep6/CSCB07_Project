package com.example.sports_space;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EventPopupScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        //displays all event info
        //has a join event button, if clicked it will add this event to MyEvents and decrease capacity of event by one
    }

}
