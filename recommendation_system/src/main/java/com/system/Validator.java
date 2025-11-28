
package com.system;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private final DataStore dataStore;

    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?: [A-Za-z]+)*$");

    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[0-9]{8}[A-Za-z0-9]$");

    public Validator(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private ArrayList<String> extractCapitalLetters() {
        ArrayList<Movie> allMovies = dataStore.getAllMovies();
        ArrayList<String> capitalStrings = new ArrayList<String>();
        StringBuilder capitals = new StringBuilder();
        for (Movie movie : allMovies) {
            String title = movie.getTitle();
            if (title != null) {
                for (char c : title.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        capitals.append(c);
                    }
                }
            }
            capitalStrings.add(capitals.toString());
        }
        return capitalStrings;
    }

    public void validateMovieTitle() throws DataValidationException {
        ArrayList<Movie> allMovies = dataStore.getAllMovies();
        for (Movie movie : allMovies) {
            String[] words = movie.getTitle().split("\\s+");
            for (String word : words) {
                if (word.isEmpty())
                    continue;
                if (!Character.isUpperCase(word.charAt(0))) {
                    throw new DataValidationException("ERROR: Movie Title {" + movie.getTitle() + "} is wrong");
                }
            }
        }
    }

    public void validateMovieId() throws DataValidationException {
        ArrayList<String> requiredPrefix = extractCapitalLetters();
        ArrayList<Movie> allMovies = dataStore.getAllMovies();
        for (Movie movie : allMovies) {
            for (String Prefix : requiredPrefix) {
                if ((!movie.getMovieId().matches(Prefix + "\\d{3}"))) {
                    throw new DataValidationException("ERROR: Movie Id letters {" + movie.getMovieId() + "} are wrong");
                }
                String numberSuffix;
                try {
                    numberSuffix = movie.getMovieId().substring(Prefix.length());
                } catch (IndexOutOfBoundsException e) {
                    throw new DataValidationException("ERROR: Movie Id {" + movie.getMovieId() + "} are wrong");
                }
                if (numberSuffix.length() != 3 || !numberSuffix.matches("\\d{3}")) {
                    throw new DataValidationException(
                            "ERROR: Movie Id numbers {" + movie.getMovieId() + "} aren’t unique");
                }
                if (numberSuffix.chars().distinct().count() != 3) {
                    throw new DataValidationException(
                            "ERROR: Movie Id numbers {" + movie.getMovieId() + "} aren’t unique");
                }

                if (!dataStore.isMovieIdUnique(numberSuffix)) {
                    throw new DataValidationException(
                            "ERROR: Movie Id numbers {" + movie.getMovieId() + "} aren’t unique");
                }
                break;
            }
        }
    }

    public void validateUserName() throws DataValidationException {
        ArrayList<User> allUsers = dataStore.getAllUsers();
        for (User user : allUsers) {
            Matcher matcher = USER_NAME_PATTERN.matcher(user.getUserName());
            if (!matcher.matches()) {
                throw new DataValidationException("ERROR: User Name {" + user.getUserName() + "} is wrong");
            }

        }
    }

    public void validateUserId() throws DataValidationException {
        ArrayList<User> allUsers = dataStore.getAllUsers();
        for (User user : allUsers) {
            Matcher matcher = USER_ID_PATTERN.matcher(user.getUserId());
            if (!matcher.matches() || user.getUserId().length() != 9) {
                throw new DataValidationException("ERROR: User Id {" + user.getUserId() + "} is wrong");
            }
            if (!dataStore.isUserIdUnique(user.getUserId())) {
                throw new DataValidationException("ERROR: User Id {" + user.getUserId() + "} is wrong");
            }
        }
    }
}