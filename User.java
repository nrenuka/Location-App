package com.example.android.locationreached;

/**
 * Created by renuka on 4/29/17.
 */

public class User {
    public String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }
}
