package com.example.sports_space.data;

public interface DataCallback<T> {
    void fetchedData(T value);
}
