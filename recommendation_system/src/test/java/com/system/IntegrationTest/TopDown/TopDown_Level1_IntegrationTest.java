package com.system.IntegrationTest.TopDown;

import com.system.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopDown_Level1_IntegrationTest {

    private RecommendationEngine recommendationEngine;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;

    @BeforeEach
    void setUp() {
        movies = new ArrayList<>();
        movies.add(new Movie("The Matrix", "TM101", new ArrayList<>(Arrays.asList("Action", "Sci-Fi"))));
        movies.add(new Movie("Inception", "I102", new ArrayList<>(Arrays.asList("Action", "Thriller"))));
        movies.add(new Movie("The Godfather", "TG103", new ArrayList<>(Arrays.asList("Drama", "Crime"))));
        movies.add(new Movie("Titanic", "T104", new ArrayList<>(Arrays.asList("Drama", "Romance"))));
        movies.add(new Movie("The Hangover", "TH105", new ArrayList<>(Arrays.asList("Comedy"))));

        users = new ArrayList<>();
        users.add(new User("John Smith", "12345678A", new ArrayList<>(Arrays.asList("TM101"))));
        users.add(new User("Jane Doe", "87654321B", new ArrayList<>(Arrays.asList("TG103"))));

        recommendationEngine = new RecommendationEngine(movies);
    }

    @Test
    @DisplayName("Level 1: RecommendationEngine generates correct recommendations with mocked Parsing")
    void testRecommendationEngineWithMockedParsing() throws Exception {
        try (MockedStatic<Parsing> mockedParsing = Mockito.mockStatic(Parsing.class)) {
            mockedParsing.when(Parsing::parseMovies).thenReturn(movies);
            mockedParsing.when(Parsing::parseUsers).thenReturn(users);

            ArrayList<Movie> parsedMovies = Parsing.parseMovies();
            ArrayList<User> parsedUsers = Parsing.parseUsers();

            assertEquals(5, parsedMovies.size());
            assertEquals(2, parsedUsers.size());

            User actionFan = parsedUsers.get(0);
            List<String> recommendations = recommendationEngine.getRecommendationsForUser(actionFan);

            assertTrue(recommendations.contains("Inception"));
            assertFalse(recommendations.contains("The Matrix"));
            assertFalse(recommendations.contains("The Hangover"));
        }
    }

    @Test
    @DisplayName("Level 1: RecommendationEngine outputs to mocked OutputWriter correctly")
    void testRecommendationEngineWithMockedOutputWriter() {
        try (MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {
            User user1 = users.get(0);
            List<String> recs = recommendationEngine.getRecommendationsForUser(user1);

            OutputWriter.writeRecommendationsToFile(users, recommendationEngine, "test_output.txt");

            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(eq(users), eq(recommendationEngine), eq("test_output.txt")),
                times(1)
            );
        }
    }

    @Test
    @DisplayName("Level 1: Verify data flow from mocked Parsing through RecommendationEngine")
    void testDataFlowWithMockedLayers() throws Exception {
        try (MockedStatic<Parsing> mockedParsing = Mockito.mockStatic(Parsing.class);
             MockedStatic<OutputWriter> mockedOutputWriter = Mockito.mockStatic(OutputWriter.class)) {

            mockedParsing.when(Parsing::parseMovies).thenReturn(movies);
            mockedParsing.when(Parsing::parseUsers).thenReturn(users);

            ArrayList<Movie> parsedMovies = Parsing.parseMovies();
            ArrayList<User> parsedUsers = Parsing.parseUsers();

            RecommendationEngine engine = new RecommendationEngine(parsedMovies);

            for (User user : parsedUsers) {
                List<String> recs = engine.getRecommendationsForUser(user);
                assertNotNull(recs);
            }

            OutputWriter.writeRecommendationsToFile(parsedUsers, engine, "output.txt");

            mockedParsing.verify(Parsing::parseMovies, times(1));
            mockedParsing.verify(Parsing::parseUsers, times(1));
            mockedOutputWriter.verify(() -> 
                OutputWriter.writeRecommendationsToFile(any(), any(), anyString()), times(1));
        }
    }

    @Test
    @DisplayName("Level 1: Verify recommendation logic with Drama genre")
    void testDramaGenreRecommendations() {
        User dramaFan = users.get(1);
        List<String> recommendations = recommendationEngine.getRecommendationsForUser(dramaFan);

        assertTrue(recommendations.contains("Titanic"));
        assertFalse(recommendations.contains("The Godfather"));
        assertFalse(recommendations.contains("The Matrix"));
    }

    @Test
    @DisplayName("Level 1: Verify empty recommendations for user with no genre matches")
    void testNoMatchingGenres() {
        User comedyFan = new User("Comedy Fan", "11111111A", new ArrayList<>(Arrays.asList("TH105")));
        List<String> recommendations = recommendationEngine.getRecommendationsForUser(comedyFan);

        assertTrue(recommendations.isEmpty());
    }
}
