package com.system.IntegrationTest.TopDown;

import com.system.*;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopDown_Level3_IntegrationTest {

    private Path moviesFile;
    private Path usersFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws Exception {
        moviesFile = Files.createTempFile("movies_e2e", ".txt");
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

        usersFile = Files.createTempFile("users_e2e", ".txt");
        String userContent = 
            "John Smith,12345678A\n" +
            "TM101\n" +
            "Jane Doe,87654321B\n" +
            "TG103\n" +
            "Bob Wilson,11223344C\n" +
            "I102,TG103\n";
        Files.writeString(usersFile, userContent);

        outputFile = Files.createTempFile("output_e2e", ".txt");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(moviesFile);
        Files.deleteIfExists(usersFile);
        Files.deleteIfExists(outputFile);
    }

    @Test
    @DisplayName("Level 3: Complete end-to-end integration - Parse, Recommend, Output")
    void testCompleteEndToEndFlow() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(5, movies.size());
        assertEquals(3, users.size());

        RecommendationEngine engine = new RecommendationEngine(movies);

        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        assertTrue(Files.exists(outputFile));
        List<String> outputLines = Files.readAllLines(outputFile);

        assertTrue(outputLines.size() >= 6);

        assertTrue(outputLines.get(0).contains("John Smith"));
        assertTrue(outputLines.get(0).contains("12345678A"));
    }

    @Test
    @DisplayName("Level 3: Verify output file format is correct")
    void testOutputFileFormat() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);
        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        List<String> outputLines = Files.readAllLines(outputFile);

        for (int i = 0; i < outputLines.size(); i += 2) {
            String userLine = outputLines.get(i);
            assertTrue(userLine.contains(","), "User line should have format: name,id");
        }
    }

    @Test
    @DisplayName("Level 3: Verify recommendations are correct in output")
    void testRecommendationsInOutput() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);
        OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());

        List<String> outputLines = Files.readAllLines(outputFile);

        String johnRecommendations = outputLines.get(1);
        assertFalse(johnRecommendations.contains("The Matrix"), "Should not recommend already-liked movie");

        String janeRecommendations = outputLines.get(3);
        assertFalse(janeRecommendations.contains("The Godfather"), "Should not recommend already-liked movie");
    }

    @Test
    @DisplayName("Level 3: System handles user with no matching recommendations")
    void testUserWithNoRecommendations() throws Exception {
        Path specialUsersFile = Files.createTempFile("users_special", ".txt");
        String specialUserContent = 
            "Lone Fan,99999999X\n" +
            "TM101,I102,DH105\n";
        Files.writeString(specialUsersFile, specialUserContent);

        try {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(specialUsersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            List<String> recs = engine.getRecommendationsForUser(users.get(0));
            
            OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());
            assertTrue(Files.exists(outputFile));

            List<String> outputLines = Files.readAllLines(outputFile);
            assertTrue(outputLines.size() >= 2);
        } finally {
            Files.deleteIfExists(specialUsersFile);
        }
    }

    @Test
    @DisplayName("Level 3: End-to-end validation - all layers work together")
    void testAllLayersWorkTogether() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        assertDoesNotThrow(() -> {
            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();
            RecommendationEngine engine = new RecommendationEngine(movies);
            OutputWriter.writeRecommendationsToFile(users, engine, outputFile.toString());
        });

        String content = Files.readString(outputFile);
        assertFalse(content.isEmpty(), "Output file should not be empty");
    }
}
