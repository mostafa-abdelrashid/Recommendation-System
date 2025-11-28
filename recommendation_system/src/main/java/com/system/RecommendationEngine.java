package com.system;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationEngine {
    private DataStore dataStore;
    
    public RecommendationEngine(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    public List<String> getRecommendationsForUser(User user) {
        Set<String> recommendations = new HashSet<>();
        List<String> likedMovieIds = user.getLikedMovieIds();
        // Get all genres from ALL liked movies (like counting unique words)
        Set<String> allLikedGenres = new HashSet<>();
        for (String likedMovieId : likedMovieIds) {
            Movie likedMovie = dataStore.getMovieById(likedMovieId);
            if (likedMovie != null) {
                allLikedGenres.addAll(likedMovie.getGenres());
            }
        }
        System.out.println("User " + user.getUserName() + " likes these genres: " + allLikedGenres);
        // Now find movies that share genres with user's preferences
        // Using approach similar to word counting - process each movie only once
        for (Movie movie : dataStore.getAllMovies()) {
            // Skip movies the user already liked
            if (!likedMovieIds.contains(movie.getMovieId())) {
                // Check if this movie shares any genre with user's liked genres
                boolean sharesGenre = false;
                for (String genre : movie.getGenres()) {
                    if (allLikedGenres.contains(genre)) {
                        sharesGenre = true;
                        break; // No need to check other genres
                    }
                }
                if (sharesGenre) {
                    recommendations.add(movie.getTitle());
                }
            }
        }
        return new ArrayList<>(recommendations);
    }
}