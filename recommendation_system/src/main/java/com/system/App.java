package com.system;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        String movieFilePath = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\Github_project\\Recommendation-System\\movies.txt";
        String userFilePath = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\Github_project\\Recommendation-System\\users.txt";
        String outputFile = "A:\\Fall2025\\SW\\Lab\\Recommendation_System\\Github_project\\Recommendation-System\\recommendation.txt";

        try {

            Parsing.setMovieFile(movieFilePath);
            Parsing.setUserFile(userFilePath);

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            OutputWriter.writeRecommendationsToFile(users, engine, outputFile);

            System.out.println("Recommendations written to: " + outputFile);
            System.out.println("Movies: " + movies.size());
            System.out.println("Users : " + users.size());


        } catch (Exception e) {
            OutputWriter.writeErrorToFile(e.getMessage(), outputFile);
        }
    }
}
