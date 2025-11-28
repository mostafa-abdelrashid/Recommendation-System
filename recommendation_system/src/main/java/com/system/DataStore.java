
package com.system;

import java.util.ArrayList;

class DataStore {
    private  ArrayList<Movie> allMovies = new ArrayList<>();
    private  ArrayList<User> allUsers = new ArrayList<>();

    public DataStore(ArrayList<Movie> allMovies, ArrayList<User> allUsers) {
        this.allMovies = allMovies;
        this.allUsers = allUsers;
    }
    
    public ArrayList<Movie> getAllMovies() { return allMovies; }
    public ArrayList<User> getAllUsers() { return allUsers; }
    
    public Movie getMovieById(String movieId) {
        for (Movie movie : allMovies) {
            if (movie.getMovieId().equals(movieId)) {
                return movie;
            }
        }
        return null; 
    }
    public boolean isUserIdUnique(String userId) {
        for (User user : allUsers) {
            if (user.getUserId().equals(userId)) {
                return false;
            }
        }
        return true;
    }
    public boolean isMovieIdUnique(String movieId) {
        for (Movie movie : allMovies) {
            if (movie.getMovieId().equals(movieId)) {
                return false;
            }
        }
        return true;
    }
}

