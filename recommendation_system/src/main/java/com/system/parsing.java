package com.system;

import java.io.*;
import java.util.*;

public class Parsing {
    private static String MovieFile;
    private static String UserFile;
    private static MovieValidator movieValidator;
    private static UserValidator userValidator;

    public static void setMovieFile(String MovieFile) {
        Parsing.MovieFile = MovieFile;
        movieValidator = new MovieValidator();
    }

    public static void setUserFile(String UserFile) {
        Parsing.UserFile = UserFile;
        userValidator = new UserValidator();
    }

    private static boolean isMovieTitleLine(String line) {
        String[] parts = line.split(",");
        for (String part : parts) {
            if (part.trim().matches("[A-Z]+\\d+")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUserLine(String line) {
        String[] parts = line.split(",");
        for (String part : parts) {
            if (part.trim().matches("\\d{8}[A-Za-z0-9]")) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Movie> parseMovies() throws DataValidationException {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try (BufferedReader br = new BufferedReader(new FileReader(MovieFile))) {
            String titleAndId;
            String genresLine;

            while ((titleAndId = br.readLine()) != null) {
                if (titleAndId.trim().isEmpty()) {
                    continue;
                }
                
                genresLine = br.readLine();
                while (genresLine != null && genresLine.trim().isEmpty()) {
                    genresLine = br.readLine();
                }

                if (genresLine == null) {
                    throw new DataValidationException("ERROR: Missing genre list for movie: " + titleAndId.trim());
                }

                if (isMovieTitleLine(genresLine)) {
                    throw new DataValidationException("ERROR: Missing genre list for movie: " + titleAndId.trim());
                }

                String[] parts = titleAndId.split(",");
                
                if (parts.length < 2) {
                    throw new DataValidationException("ERROR: Invalid movie format - missing comma");
                }

                String title = parts[0].trim();
                String id = parts[1].trim();

                String[] genres = genresLine.split(",");
                ArrayList<String> genreList = new ArrayList<>();
                for (String g : genres) {
                    genreList.add(g.trim());
                }
                movieValidator.validateMovieTitle(new Movie(title, id, genreList));
                movieValidator.validateMovieId(new Movie(title, id, genreList));

                movies.add(new Movie(title, id, genreList));
            }

        } catch (IOException e) {
            System.out.println("Error reading movies file: " + e.getMessage());
        }

        return movies;
    }

    public static ArrayList<User> parseUsers() throws DataValidationException {
        ArrayList<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(UserFile))) {
            String nameAndId;
            String likedMoviesLine;

            while ((nameAndId = br.readLine()) != null) {
                if (nameAndId.trim().isEmpty()) {
                    continue;
                }
                
                likedMoviesLine = br.readLine();
                while (likedMoviesLine != null && likedMoviesLine.trim().isEmpty()) {
                    likedMoviesLine = br.readLine();
                }

                if (likedMoviesLine == null) {
                    throw new DataValidationException("ERROR: Missing liked movies list for user: " + nameAndId.trim());
                }

                if (isUserLine(likedMoviesLine)) {
                    throw new DataValidationException("ERROR: Missing liked movies list for user: " + nameAndId.trim());
                }

                String[] parts = nameAndId.split(",");
                
                if (parts.length < 2) {
                    throw new DataValidationException("ERROR: Invalid user format - missing comma");
                }

                String name = parts[0].trim();
                String id = parts[1].trim();

                String[] liked = likedMoviesLine.split(",");
                ArrayList<String> likedList = new ArrayList<>();
                for (String m : liked) {
                    likedList.add(m.trim());
                }
                userValidator.validateUserName(new User(name, id, likedList));
                userValidator.validateUserId(new User(name, id, likedList));

                users.add(new User(name, id, likedList));
            }

        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }

        return users;
    }
}
