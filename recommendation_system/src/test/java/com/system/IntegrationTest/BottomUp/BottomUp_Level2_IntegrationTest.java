package com.system.IntegrationTest.BottomUp;

import com.system.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BottomUp_Level2_IntegrationTest {

    @Mock
    private RecommendationEngine mockedEngine;

    private Path moviesFile;
    private Path usersFile;

    @BeforeEach
    void setUp() throws Exception {
        moviesFile = Files.createTempFile("movies_bu2", ".txt");
        String movieContent = 
            "The Matrix,TM101\n" +
            "Action,Sci-Fi\n" +
            "Inception,I102\n" +
            "Action,Thriller\n" +
            "The Godfather,TG103\n" +
            "Drama,Crime\n";
        Files.writeString(moviesFile, movieContent);

        usersFile = Files.createTempFile("users_bu2", ".txt");
        String userContent = 
            "John Smith,12345678A\n" +
            "TM101,I102\n" +
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
    @DisplayName("Level 2: Parsing with validators produces valid data for mocked engine")
    void testParsingFeedsMockedEngine() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();
        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(3, movies.size());
        assertEquals(2, users.size());

        when(mockedEngine.getRecommendationsForUser(any(User.class)))
            .thenReturn(Arrays.asList("Mock Recommendation"));

        for (User user : users) {
            List<String> recs = mockedEngine.getRecommendationsForUser(user);
            assertNotNull(recs);
            assertEquals(1, recs.size());
        }

        verify(mockedEngine, times(2)).getRecommendationsForUser(any(User.class));
    }

    @Test
    @DisplayName("Level 2: Validators run during parsing")
    void testValidatorsRunDuringParsing() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        assertDoesNotThrow(() -> {
            ArrayList<Movie> movies = Parsing.parseMovies();
            ArrayList<User> users = Parsing.parseUsers();

            for (Movie m : movies) {
                assertTrue(Character.isUpperCase(m.getTitle().charAt(0)), 
                    "Movie title should start with uppercase");
            }
        });
    }

    @Test
    @DisplayName("Level 2: Parsed Movie data has correct structure")
    void testParsedMovieDataStructure() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        Movie matrix = movies.get(0);
        assertEquals("The Matrix", matrix.getTitle());
        assertEquals("TM101", matrix.getMovieId());
        assertTrue(matrix.getGenres().contains("Action"));
        assertTrue(matrix.getGenres().contains("Sci-Fi"));

        Movie inception = movies.get(1);
        assertEquals("Inception", inception.getTitle());
        assertEquals("I102", inception.getMovieId());
    }

    @Test
    @DisplayName("Level 2: Parsed User data has correct structure")
    void testParsedUserDataStructure() throws Exception {
        Parsing.setUserFile(usersFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        User john = users.get(0);
        assertEquals("John Smith", john.getUserName());
        assertEquals("12345678A", john.getUserId());
        assertTrue(john.getLikedMovieIds().contains("TM101"));
        assertTrue(john.getLikedMovieIds().contains("I102"));

        User jane = users.get(1);
        assertEquals("Jane Doe", jane.getUserName());
        assertEquals("87654321B", jane.getUserId());
    }

    @Test
    @DisplayName("Level 2: Mocked engine receives correctly parsed users")
    void testMockedEngineReceivesCorrectUsers() throws Exception {
        Parsing.setMovieFile(moviesFile.toString());
        Parsing.setUserFile(usersFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        when(mockedEngine.getRecommendationsForUser(argThat(u -> 
            u != null && "12345678A".equals(u.getUserId()))))
            .thenReturn(Arrays.asList("The Godfather"));

        when(mockedEngine.getRecommendationsForUser(argThat(u -> 
            u != null && "87654321B".equals(u.getUserId()))))
            .thenReturn(Arrays.asList("The Matrix", "Inception"));

        List<String> johnRecs = mockedEngine.getRecommendationsForUser(users.get(0));
        List<String> janeRecs = mockedEngine.getRecommendationsForUser(users.get(1));

        assertEquals(1, johnRecs.size());
        assertEquals(2, janeRecs.size());
    }

    @Test
    @DisplayName("Level 2: Validation rejects invalid movie data during parsing")
    void testValidationRejectsInvalidMovie() throws Exception {
        Path invalidMoviesFile = Files.createTempFile("invalid_movies", ".txt");
        String invalidContent = 
            "the matrix,tm101\n" +
            "Action\n";
        Files.writeString(invalidMoviesFile, invalidContent);

        try {
            Parsing.setMovieFile(invalidMoviesFile.toString());

            assertThrows(DataValidationException.class, () -> Parsing.parseMovies());
        } finally {
            Files.deleteIfExists(invalidMoviesFile);
        }
    }

    @Test
    @DisplayName("Level 2: Validation rejects invalid user data during parsing")
    void testValidationRejectsInvalidUser() throws Exception {
        Path invalidUsersFile = Files.createTempFile("invalid_users", ".txt");
        String invalidContent = 
            "John Smith,123\n" +
            "TM101\n";
        Files.writeString(invalidUsersFile, invalidContent);

        try {
            Parsing.setUserFile(invalidUsersFile.toString());

            assertThrows(DataValidationException.class, () -> Parsing.parseUsers());
        } finally {
            Files.deleteIfExists(invalidUsersFile);
        }
    }
}
