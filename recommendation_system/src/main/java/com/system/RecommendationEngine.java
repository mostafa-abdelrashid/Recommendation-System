package com.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationEngine {
    private ArrayList<Movie> allMovies;

    public RecommendationEngine(ArrayList<Movie> allMovies) {
        this.allMovies = allMovies;
    }

    public List<String> getRecommendationsForUser(User user) {
        Set<String> recommendations = new HashSet<>();
        List<String> likedMovieIds = user.getLikedMovieIds();

        Set<String> allLikedGenres = new HashSet<>();
        for (String likedMovieId : likedMovieIds) {
            Movie likedMovie = getMovieById(likedMovieId);
            if (likedMovie != null) {
                allLikedGenres.addAll(likedMovie.getGenres());
            }
        }


        for (Movie movie : allMovies) {

            if (!likedMovieIds.contains(movie.getMovieId())) {

                boolean sharesGenre = false;
                for (String genre : movie.getGenres()) {
                    if (allLikedGenres.contains(genre)) {
                        sharesGenre = true;
                        break;
                    }
                }
                if (sharesGenre) {
                    recommendations.add(movie.getTitle());
                }
            }
        }
        return new ArrayList<>(recommendations);
    }

    public Movie getMovieById(String movieId) {
        for (Movie movie : allMovies) {
            if (movie.getMovieId().equals(movieId)) {
                return movie;
            }
        }
        return null;
    }
}