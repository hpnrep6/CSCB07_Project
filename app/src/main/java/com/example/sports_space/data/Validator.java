package com.example.sports_space.data;

import com.google.firebase.auth.FirebaseAuth;

public class Validator {

    public static boolean validateFullName(String name) {
        return !name.contains("0123456789");
    }
}
