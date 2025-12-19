
package com.system;
import java.util.ArrayList;

public class Movie {
    private final String title;
    private final String movieId;
    private final ArrayList<String> genres;

    public Movie(String title, String movieId, ArrayList<String> genres) {
        this.title = title;
        this.movieId = movieId;
        this.genres = genres;
    }
    public String getTitle() { return title; }
    public String getMovieId() { return movieId; }
    public ArrayList<String> getGenres() { return genres; }
}