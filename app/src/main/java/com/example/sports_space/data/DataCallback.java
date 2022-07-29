package com.example.sports_space.data;

import androidx.appcompat.app.AppCompatActivity;

public interface DataCallback<T> {
    void fetchedData(T value, AppCompatActivity activity);
}
