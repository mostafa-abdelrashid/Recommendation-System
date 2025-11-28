package com.system;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputWriter {
    public static void writeRecommendationsToFile(List<User> users,
            RecommendationEngine engine,
            String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                // Write user name and ID
                writer.write(user.getUserName() + "," + user.getUserId());
                writer.newLine();

                // Get recommendations for user
                List<String> recommendations = engine.getRecommendationsForUser(user);

                // Write recommended movie titles
                if (recommendations.isEmpty()) {
                    writer.write("No recommendations available");
                } else {
                    StringBuilder movieTitles = new StringBuilder();
                    for (int i = 0; i < recommendations.size(); i++) {
                        movieTitles.append(recommendations.get(i));
                        if (i < recommendations.size() - 1) {
                            movieTitles.append(",");
                        }
                    }
                    writer.write(movieTitles.toString());
                }
                writer.newLine();
            }
            System.out.println("Recommendations written to: " + filename);

        } catch (IOException e) {
            System.out.println("Error writing to recommendations file: " + e.getMessage());
        }
    }

    public static void writeErrorToFile(String errorMessage, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(errorMessage);
            System.out.println("Error written to: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing error to file: " + e.getMessage());
        }
    }
}