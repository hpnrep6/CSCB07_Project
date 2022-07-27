package com.example.sports_space.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class User {
    private String email;
    private String fullname;
    private String password;

    public User(@NonNull String email, @NonNull String fullname, @NonNull String password) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;

        if (email.isEmpty() || fullname.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("You can't have an empty field");
        }

        // TODO: Add rest of user name, password etc. validation below
    }

    public void register() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // TODO: redirect to main page
            Log.d("user", "already logged in");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("user", "registered");
                    } else {
                        Log.d("user", "registration failed " + task.getException());
                    }
                }
            }
        );
    }

    public static void login(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // TODO: redirect to main page
            Log.d("user", "already logged in");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("user", "logged in");
                    } else {
                        Log.d("user", "log in failed");
                    }
                }
            }
        );
    }
}
