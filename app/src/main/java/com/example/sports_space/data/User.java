package com.example.sports_space.data;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sports_space.Login;
import com.example.sports_space.Register;
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
    // Birthday?
    // Events they're registered in
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

    public void register(AppCompatActivity context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // TODO: redirect to main page
            Log.d("user", "already logged in 0");
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);

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

    public static void login(AppCompatActivity context, String email, String password) {
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

    public static void logout(AppCompatActivity context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Log.d("user", "no user logged in");
            return;
        }

        auth.signOut();

        Intent intent = new Intent(context, Register.class);
        context.startActivity(intent);

        Log.d("user", "signed out");
    }
}
