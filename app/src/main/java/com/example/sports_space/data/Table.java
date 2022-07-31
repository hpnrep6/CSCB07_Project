package com.example.sports_space.data;

public abstract class Table {

    protected long getUniqueID() {
        return System.currentTimeMillis() - (long) (Math.random() * 1231231);
    }
}
