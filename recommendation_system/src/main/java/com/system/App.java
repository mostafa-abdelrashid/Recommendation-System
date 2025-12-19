package com.system;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("== Movie Recommendation System ==");
        System.out.println();
        
        System.out.print("Enter movie file path: ");
        String movieFilePath = scanner.nextLine().trim();
        
        System.out.print("Enter user file path: ");
        String userFilePath = scanner.nextLine().trim();
        
        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine().trim();
        
        scanner.close();

        try {
            Parsing.setMovieFile(movieFilePath);
            Parsing.setUserFile(userFilePath);

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            OutputWriter.writeRecommendationsToFile(users, engine, outputFile);

            System.out.println();
            System.out.println("Recommendations written to: " + outputFile);
            System.out.println("Movies: " + movies.size());
            System.out.println("Users : " + users.size());

        } catch (Exception e) {
            OutputWriter.writeErrorToFile(e.getMessage(), outputFile);
            System.out.println("Error: " + e.getMessage());
        }
    }
}
