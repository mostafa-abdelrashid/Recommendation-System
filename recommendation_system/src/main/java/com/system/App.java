package com.system;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        /*
         * System.out.println("Enter movie file path:");
         * Scanner movie_dir = new Scanner(System.in);
         * String x=movie_dir.nextLine();
         * Parsing.setMovieFile(x);
         * movie_dir.close();
         * System.out.println("Enter user file path:");
         * Scanner user_dir = new Scanner(System.in);
         * String y=user_dir.nextLine();
         * Parsing.setUserFile(y);
         * user_dir.close();
         * 
         * List<Movie> movies = Parsing.parseMovies();
         * System.out.println("=== MOVIES PARSED ===");
         * for (Movie m : movies) {
         * System.out.println("Title: " + m.getTitle());
         * System.out.println("ID: " + m.getMovieId());
         * System.out.println("Genres: " + m.getGenres());
         * System.out.println("-----------------------");
         * }
         * 
         * // Read users
         * List<User> users = Parsing.parseUsers();
         * System.out.println("\n=== USERS PARSED ===");
         * for (User u : users) {
         * System.out.println("Name: " + u.getUserName());
         * System.out.println("ID: " + u.getUserId());
         * System.out.println("Liked Movies: " + u.getLikedMovieIds());
         * System.out.println("-----------------------");
         * }
         */
        String movieFilePath = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\movies.txt";
        String userFilePath = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\users.txt";
        String outputFile = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\recommendation.txt";

        try {

            Parsing.setMovieFile(movieFilePath);
            Parsing.setUserFile(userFilePath);

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();
            DataStore dataStore = new DataStore(movies, users);
            // Validator valid = new Validator(dataStore);
            /*
             * 
             * valid.validateMovieTitle();
             * valid.validateMovieId();
             * valid.validateUserName();
             * valid.validateUserId();
             */

            RecommendationEngine engine = new RecommendationEngine(dataStore);

            OutputWriter.writeRecommendationsToFile(users, engine, outputFile);

            System.out.println("✔ Recommendations written to: " + outputFile);
            System.out.println("Movies: " + movies.size());
            System.out.println("Users : " + users.size());

            // =====================================================
            // 5) Optional: Print sample output
            // =====================================================
            /*
             * System.out.println("\n=== Sample Recommendations ===");
             * for (User u : users) {
             * List<String> recs = engine.getRecommendationsForUser(u);
             * System.out.println("User: " + u.getUserName());
             * System.out.println("Recommendations: " + recs);
             * System.out.println("---------------------------");
             * }
             */

        } catch (Exception e) {
            // أي خطأ تاني
            System.out.println("❌ Unexpected Error: " + e.getMessage());
        }
    }
}
