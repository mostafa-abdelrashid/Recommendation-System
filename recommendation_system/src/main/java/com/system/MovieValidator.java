package com.system;

import java.util.ArrayList;

public class MovieValidator {
    private ArrayList<Movie> allMovies;

    public MovieValidator() {
        allMovies = new ArrayList<Movie>();
    }

    public void setMovie(Movie Movie) {
        allMovies.add(Movie);
    }

    private String extractCapitalLetters(Movie movie) {
        StringBuilder capitals = new StringBuilder();
        String title = movie.getTitle();
        if (title != null) {
            for (char c : title.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    capitals.append(c);
                }
            }
        }
        return capitals.toString();
    }

    public void validateMovieTitle(Movie movie) throws DataValidationException {
        String[] words = movie.getTitle().split("\\s+");
        for (String word : words) {
            if (word.isEmpty())
                continue;
            if (!Character.isUpperCase(word.charAt(0))) {
                throw new DataValidationException("ERROR: Movie Title " + movie.getTitle() + " is wrong");
            }
        }
    }

    public void validateMovieId(Movie movie) throws DataValidationException {
        String Prefix = extractCapitalLetters(movie);
        if ((!movie.getMovieId().matches(Prefix + "\\d{3}"))) {
            throw new DataValidationException("ERROR: Movie Id letters " + movie.getMovieId() + " are wrong");
        }
        String numberSuffix;
        try {
            numberSuffix = movie.getMovieId().substring(Prefix.length());
        } catch (IndexOutOfBoundsException e) {
            throw new DataValidationException("ERROR: Movie Id {" + movie.getMovieId() + "} are wrong");
        }
        if (numberSuffix.length() != 3 || !numberSuffix.matches("\\d{3}")) {
            throw new DataValidationException(
                    "ERROR: Movie Id numbers " + movie.getMovieId() + " aren’t unique");
        }
        if (!isMovieIdUnique(numberSuffix)) {
            throw new DataValidationException(
                    "ERROR: Movie Id numbers " + movie.getMovieId() + " aren’t unique");
        }
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
