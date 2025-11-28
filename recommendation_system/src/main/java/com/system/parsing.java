package com.system;
import java.io.*;
import java.util.*;
 class Parsing {
    private static String MovieFile;
    private static String UserFile;


    public static void setMovieFile(String MovieFile) {
        Parsing.MovieFile = MovieFile;
    }
    public static void setUserFile(String UserFile) {
    Parsing.UserFile = UserFile;
    }
    // =============================
    // ===== MOVIE PARSING =========
    // =============================

    public static ArrayList<Movie> parseMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try (BufferedReader br = new BufferedReader(new FileReader(MovieFile))) {
            String titleAndId;
            String genresLine;

            while ((titleAndId = br.readLine()) != null &&
                   (genresLine = br.readLine()) != null) {

                // Split title, id
                String[] parts = titleAndId.split(",");
               

                String title = parts[0].trim();
                String id = parts[1].trim();

                // Split genres
                String[] genres = genresLine.split(",");
                ArrayList<String> genreList = new ArrayList<>();
                for (String g : genres) genreList.add(g.trim());

                // Store movie
                movies.add(new Movie(title, id, genreList));
            }

        } catch (IOException e) {
            System.out.println("Error reading movies file: " + e.getMessage());
        }

        return movies;
    }


    // =============================
    // ===== USER PARSING ==========
    // =============================

    public static ArrayList<User> parseUsers() {
        ArrayList<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(UserFile))) {
            String nameAndId;
            String likedMoviesLine;

            while ((nameAndId = br.readLine()) != null &&
                   (likedMoviesLine = br.readLine()) != null) {

                // name, id
                String[] parts = nameAndId.split(",");

                String name = parts[0].trim();
                String id = parts[1].trim();

                // liked movie ids
                String[] liked = likedMoviesLine.split(",");
                ArrayList<String> likedList = new ArrayList<>();
                for (String m : liked) likedList.add(m.trim());

                users.add(new User(name, id, likedList));
            }

        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }

        return users;
    }
}

