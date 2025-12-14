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

    public static ArrayList<Movie> parseMovies() throws DataValidationException {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try (BufferedReader br = new BufferedReader(new FileReader(MovieFile))) {
            String titleAndId;
            String genresLine;

            while ((titleAndId = br.readLine()) != null &&
                    (genresLine = br.readLine()) != null && !(titleAndId.trim().isEmpty())
                    && !(genresLine.trim().isEmpty())) {

                String[] parts = titleAndId.split(",");

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

            while ((nameAndId = br.readLine()) != null &&
                    (likedMoviesLine = br.readLine()) != null && !(nameAndId.trim().isEmpty())
                    && !(likedMoviesLine.trim().isEmpty())) {

                String[] parts = nameAndId.split(",");

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
