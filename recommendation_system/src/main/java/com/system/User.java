package com.system;
import java.util.ArrayList;

public class User {
private final String userName;
    private final String userId;
    private final ArrayList<String> likedMovieIds; // Simple list structure

    public User(String userName, String userId, ArrayList<String> likedMovieIds) {
        this.userName = userName;
        this.userId = userId;
        this.likedMovieIds = likedMovieIds;
    }
    public String getUserName() { return userName; }
    public String getUserId() { return userId; }
    public ArrayList<String> getLikedMovieIds() { return likedMovieIds; }
}
