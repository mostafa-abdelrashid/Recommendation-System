package com.system.IntegrationTest.BottomUp;

import com.system.*;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUp_Level4_IntegrationTest {

    private Path moviesFile;
    private Path usersFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws Exception {
        moviesFile = Files.createTempFile("movies_bu4", ".txt");
        String movieContent = 
            "The Matrix,TM101\n" +
            "Action,Sci-Fi\n" +
            "Inception,I102\n" +
            "Action,Thriller\n" +
            "The Godfather,TG103\n" +
            "Drama,Crime\n" +
            "Titanic,T104\n" +
            "Drama,Romance\n" +
            "Die Hard,DH105\n" +
            "Action,Adventure\n";
        Files.writeString(moviesFile, movieContent);

        usersFile = Files.createTempFile("users_bu4", ".txt");
        String userContent = 
            "John Smith,12345678A\n" +
            "TM101\n" +
            "Jane Doe,87654321B\n" +
            "TG103\n" +
            "Bob Wilson,11223344C\n" +
            "I102,TG103\n";
        Files.writeString(usersFile, userContent);

        outputFile = Files.createTempFile("output_bu4", ".txt");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(moviesFile);
        Files.deleteIfExists(usersFile);
        Files.deleteIfExists(outputFile);
    }

    @Test
    @DisplayName("Level 4: Complete end-to-end integration from Bottom-Up")
    void testCompleteBottomUpIntegration() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);

        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        assertTrue(Files.exists(outputFile));
        List<String> output = Files.readAllLines(outputFile);
        assertTrue(output.size() >= 6);
    }

    @Test
    @DisplayName("Level 4: Output file contains correct user information")
    void testOutputContainsCorrectUserInfo() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);
        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        List<String> output = Files.readAllLines(outputFile);

        assertTrue(output.get(0).contains("John Smith"));
        assertTrue(output.get(0).contains("12345678A"));

        assertTrue(output.get(2).contains("Jane Doe"));
        assertTrue(output.get(2).contains("87654321B"));

        assertTrue(output.get(4).contains("Bob Wilson"));
        assertTrue(output.get(4).contains("11223344C"));
    }

    @Test
    @DisplayName("Level 4: Output contains valid recommendations")
    void testOutputContainsValidRecommendations() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);
        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        List<String> output = Files.readAllLines(outputFile);

        String johnRecs = output.get(1);
        assertFalse(johnRecs.contains("The Matrix"), "Should not recommend liked movie");

        String janeRecs = output.get(3);
        assertFalse(janeRecs.contains("The Godfather"), "Should not recommend liked movie");
    }

    @Test
    @DisplayName("Level 4: All validation layers function correctly")
    void testAllValidationLayersFunction() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(5, movies.size());
        assertEquals(3, users.size());

        for (Movie m : movies) {
            String[] words = m.getTitle().split("\\s+");
            for (String word : words) {
                assertTrue(Character.isUpperCase(word.charAt(0)), 
                    "Movie title words should start uppercase: " + m.getTitle());
            }
        }

        for (User u : users) {
            assertEquals(9, u.getUserId().length(), "User ID should be 9 chars");
            assertTrue(u.getUserName().matches("^[A-Za-z]+(?: [A-Za-z]+)*$"), 
                "User name should be letters and spaces only");
        }
    }

    @Test
    @DisplayName("Level 4: Data flows correctly through all layers")
    void testDataFlowThroughAllLayers() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        assertNotNull(movies);
        assertNotNull(users);

        RecommendationEngine engine = new RecommendationEngine(movies);
        for (User user : users) {
            List<String> recs = engine.getRecommendationsForUser(user);
            assertNotNull(recs);
        }

        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());
        String content = Files.readString(outputFile);
        assertFalse(content.isEmpty());
    }

    @Test
    @DisplayName("Level 4: System handles user with multiple liked movies")
    void testUserWithMultipleLikedMovies() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);

        User bob = users.get(2);
        List<String> bobRecs = engine.getRecommendationsForUser(bob);

        assertFalse(bobRecs.contains("Inception"));
        assertFalse(bobRecs.contains("The Godfather"));

        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());
        assertTrue(Files.exists(outputFile));
    }

    @Test
    @DisplayName("Level 4: Complete system produces consistent results")
    void testSystemProducesConsistentResults() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies1 = Parsing.parseMovies();
        ArrayList<User> users1 = Parsing.parseUsers();
        RecommendationEngine engine1 = new RecommendationEngine(movies1);

        Path output1 = Files.createTempFile("consistent_test1", ".txt");
        OutputWriter.writeRecommendationsToFile(users1, engine1, output1.toString());

        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies2 = Parsing.parseMovies();
        ArrayList<User> users2 = Parsing.parseUsers();
        RecommendationEngine engine2 = new RecommendationEngine(movies2);

        Path output2 = Files.createTempFile("consistent_test2", ".txt");
        OutputWriter.writeRecommendationsToFile(users2, engine2, output2.toString());

        try {
            String content1 = Files.readString(output1);
            String content2 = Files.readString(output2);
            assertEquals(content1, content2, "System should produce consistent results");
        } finally {
            Files.deleteIfExists(output1);
            Files.deleteIfExists(output2);
        }
    }
}
