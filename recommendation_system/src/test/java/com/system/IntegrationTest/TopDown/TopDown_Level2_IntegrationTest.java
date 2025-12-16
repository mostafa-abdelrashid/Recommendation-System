package com.system.IntegrationTest.TopDown;

import com.system.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopDown_Level2_IntegrationTest {

    private Path moviesFile;
    private Path usersFile;

    @BeforeEach
    void setUp() throws Exception {
        moviesFile = Files.createTempFile("movies_integration", ".txt");
        String movieContent = 
            "The Matrix,TM101\n" +
            "Action,Sci-Fi\n" +
            "Inception,I102\n" +
            "Action,Thriller\n" +
            "The Godfather,TG103\n" +
            "Drama,Crime\n" +
            "Titanic,T104\n" +
            "Drama,Romance\n";
        Files.writeString(moviesFile, movieContent);

        usersFile = Files.createTempFile("users_integration", ".txt");
        String userContent = 
            "John Smith,12345678A\n" +
            "TM101\n" +
            "Jane Doe,87654321B\n" +
            "TG103\n";
        Files.writeString(usersFile, userContent);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(moviesFile);
        Files.deleteIfExists(usersFile);
    }

    @Test
    @DisplayName("Level 2: Real Parsing feeds correct data to RecommendationEngine")
    void testRealParsingWithRecommendationEngine() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(4, movies.size());
        assertEquals(2, users.size());

        RecommendationEngine engine = new RecommendationEngine(movies);

        User actionFan = users.get(0);
        List<String> recommendations = engine.getRecommendationsForUser(actionFan);

        assertTrue(recommendations.contains("Inception"));
        assertFalse(recommendations.contains("The Matrix"));
    }

    @Test
    @DisplayName("Level 2: Parsed data flows correctly to mocked OutputWriter")
    void testParsedDataToMockedOutputWriter() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            OutputWriter.writeRecommendationsToFile(users, engine, "test_output.txt");

            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(
                    argThat(u -> u.size() == 2),
                    eq(engine),
                    eq("test_output.txt")
                ),
                times(1)
            );
        }
    }

    @Test
    @DisplayName("Level 2: Validation occurs during parsing")
    void testValidationDuringParsing() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        assertDoesNotThrow(() -> {
            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            for (Movie m : movies) {
                assertNotNull(m.getTitle());
                assertNotNull(m.getMovieId());
                assertNotNull(m.getGenres());
            }

            for (User u : users) {
                assertNotNull(u.getUserName());
                assertNotNull(u.getUserId());
                assertNotNull(u.getLikedMovieIds());
            }
        });
    }

    @Test
    @DisplayName("Level 2: Full integration flow with real Parsing and mocked Output")
    void testFullFlowRealParsingMockedOutput() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            for (User user : users) {
                List<String> recs = engine.getRecommendationsForUser(user);
                assertNotNull(recs);
            }

            OutputWriter.writeRecommendationsToFile(users, engine, "recommendations.txt");

            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(any(), any(), anyString()),
                times(1)
            );
        }
    }

    @Test
    @DisplayName("Level 2: Drama genre recommendations from parsed data")
    void testDramaRecommendationsFromParsedData() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        RecommendationEngine engine = new RecommendationEngine(movies);

        User dramaFan = users.get(1);
        List<String> recommendations = engine.getRecommendationsForUser(dramaFan);

        assertTrue(recommendations.contains("Titanic"));
        assertFalse(recommendations.contains("The Godfather"));
    }
}
