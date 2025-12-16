package com.system.IntegrationTest.BottomUp;

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
public class BottomUp_Level3_IntegrationTest {

    private Path moviesFile;
    private Path usersFile;

    @BeforeEach
    void setUp() throws Exception {
        moviesFile = Files.createTempFile("movies_bu3", ".txt");
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

        usersFile = Files.createTempFile("users_bu3", ".txt");
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
    @DisplayName("Level 3: Parsing + RecommendationEngine with mocked OutputWriter")
    void testParsingAndEngineWithMockedOutput() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            User john = users.get(0);
            List<String> johnRecs = engine.getRecommendationsForUser(john);
            assertTrue(johnRecs.contains("Inception"));

            OutputWriter.writeRecommendationsToFile(users, engine, "output.txt");

            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(eq(users), eq(engine), anyString()),
                times(1)
            );
        }
    }

    @Test
    @DisplayName("Level 3: Correct recommendations passed to OutputWriter")
    void testCorrectRecommendationsToOutput() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            User actionFan = users.get(0);
            List<String> actionRecs = engine.getRecommendationsForUser(actionFan);
            assertTrue(actionRecs.contains("Inception"));
            assertFalse(actionRecs.contains("The Matrix"));
            assertFalse(actionRecs.contains("Titanic"));

            User dramaFan = users.get(1);
            List<String> dramaRecs = engine.getRecommendationsForUser(dramaFan);
            assertTrue(dramaRecs.contains("Titanic"));
            assertFalse(dramaRecs.contains("The Godfather"));

            OutputWriter.writeRecommendationsToFile(users, engine, "test.txt");
            mockedOutputWriter.verify(() -> OutputWriter.writeRecommendationsToFile(any(), any(), any()));
        }
    }

    @Test
    @DisplayName("Level 3: Data integrity through Parsing -> Engine -> Output flow")
    void testDataIntegrityThroughFlow() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            assertEquals(4, movies.size());
            assertEquals(2, users.size());

            RecommendationEngine engine = new RecommendationEngine(movies);

            Movie found = engine.getMovieById("TM101");
            assertNotNull(found);
            assertEquals("The Matrix", found.getTitle());

            OutputWriter.writeRecommendationsToFile(users, engine, "integrity_test.txt");

            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(
                    argThat(u -> u.size() == 2),
                    argThat(e -> e.getMovieById("TM101") != null),
                    eq("integrity_test.txt")
                )
            );
        }
    }

    @Test
    @DisplayName("Level 3: Multiple users processed correctly")
    void testMultipleUsersProcessed() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(movies);

            int totalRecommendations = 0;
            for (User user : users) {
                List<String> recs = engine.getRecommendationsForUser(user);
                totalRecommendations += recs.size();
            }

            assertTrue(totalRecommendations > 0);

            OutputWriter.writeRecommendationsToFile(users, engine, "multi_user.txt");
            mockedOutputWriter.verify(() -> OutputWriter.writeRecommendationsToFile(any(), any(), any()));
        }
    }

    @Test
    @DisplayName("Level 3: Validation maintained through Parsing to Engine")
    void testValidationMaintained() throws Exception {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            Parsing.setMovieFile(moviesFile.toString());
            Parsing.setUserFile(usersFile.toString());

            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            for (Movie m : movies) {
                assertTrue(Character.isUpperCase(m.getTitle().charAt(0)));
                assertFalse(m.getMovieId().isEmpty());
            }

            for (User u : users) {
                assertEquals(9, u.getUserId().length());
            }

            RecommendationEngine engine = new RecommendationEngine(movies);
            OutputWriter.writeRecommendationsToFile(users, engine, "validated.txt");
        }
    }
}
