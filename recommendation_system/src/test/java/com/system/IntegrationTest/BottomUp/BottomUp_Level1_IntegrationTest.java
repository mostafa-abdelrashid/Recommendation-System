package com.system.IntegrationTest.BottomUp;

import com.system.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUp_Level1_IntegrationTest {

    private MovieValidator movieValidator;
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        movieValidator = new MovieValidator();
        userValidator = new UserValidator();
    }

    @Test
    @DisplayName("Level 1: MovieValidator accepts valid Movie with correct title format")
    void testMovieValidatorAcceptsValidTitle() {
        Movie validMovie = new Movie("The Matrix", "TM101", new ArrayList<>(Arrays.asList("Action", "Sci-Fi")));

        assertDoesNotThrow(() -> movieValidator.validateMovieTitle(validMovie));
    }

    @Test
    @DisplayName("Level 1: MovieValidator accepts valid Movie with correct ID format")
    void testMovieValidatorAcceptsValidId() {
        Movie validMovie = new Movie("The Matrix", "TM101", new ArrayList<>(Arrays.asList("Action", "Sci-Fi")));

        assertDoesNotThrow(() -> movieValidator.validateMovieId(validMovie));
    }

    @Test
    @DisplayName("Level 1: MovieValidator rejects Movie with lowercase title")
    void testMovieValidatorRejectsLowercaseTitle() {
        Movie invalidMovie = new Movie("the matrix", "TM101", new ArrayList<>(Arrays.asList("Action")));

        assertThrows(DataValidationException.class, () -> movieValidator.validateMovieTitle(invalidMovie));
    }

    @Test
    @DisplayName("Level 1: MovieValidator rejects Movie with invalid ID prefix")
    void testMovieValidatorRejectsInvalidIdPrefix() {
        Movie invalidMovie = new Movie("The Matrix", "XX101", new ArrayList<>(Arrays.asList("Action")));

        assertThrows(DataValidationException.class, () -> movieValidator.validateMovieId(invalidMovie));
    }

    @Test
    @DisplayName("Level 1: MovieValidator detects duplicate movie IDs")
    void testMovieValidatorDetectsDuplicateIds() {
        Movie movie1 = new Movie("The Matrix", "TM101", new ArrayList<>(Arrays.asList("Action")));
        Movie movie2 = new Movie("The Mask", "TM101", new ArrayList<>(Arrays.asList("Comedy")));

        movieValidator.setMovie(movie1);

        assertFalse(movieValidator.isMovieIdUnique(movie2.getMovieId()));
    }

    @Test
    @DisplayName("Level 1: UserValidator accepts valid User with correct name format")
    void testUserValidatorAcceptsValidName() {
        User validUser = new User("John Smith", "12345678A", new ArrayList<>(Arrays.asList("TM101")));

        assertDoesNotThrow(() -> userValidator.validateUserName(validUser));
    }

    @Test
    @DisplayName("Level 1: UserValidator accepts valid User with correct ID format")
    void testUserValidatorAcceptsValidId() {
        User validUser = new User("John Smith", "12345678A", new ArrayList<>(Arrays.asList("TM101")));

        assertDoesNotThrow(() -> userValidator.validateUserId(validUser));
    }

    @Test
    @DisplayName("Level 1: UserValidator rejects User with numbers in name")
    void testUserValidatorRejectsNumbersInName() {
        User invalidUser = new User("John123 Smith", "12345678A", new ArrayList<>());

        assertThrows(DataValidationException.class, () -> userValidator.validateUserName(invalidUser));
    }

    @Test
    @DisplayName("Level 1: UserValidator rejects User with invalid ID format")
    void testUserValidatorRejectsInvalidIdFormat() {
        User invalidUser = new User("John Smith", "123", new ArrayList<>());

        assertThrows(DataValidationException.class, () -> userValidator.validateUserId(invalidUser));
    }

    @Test
    @DisplayName("Level 1: UserValidator detects duplicate user IDs")
    void testUserValidatorDetectsDuplicateIds() {
        User user1 = new User("John Smith", "12345678A", new ArrayList<>());
        User user2 = new User("Jane Doe", "12345678A", new ArrayList<>());

        userValidator.setUser(user1);

        assertFalse(userValidator.isUserIdUnique(user2.getUserId()));
    }

    @Test
    @DisplayName("Level 1: Both validators work together for valid data")
    void testBothValidatorsWithValidData() {
        Movie movie = new Movie("Inception", "I102", new ArrayList<>(Arrays.asList("Action", "Thriller")));
        User user = new User("Alice Johnson", "87654321B", new ArrayList<>(Arrays.asList("I102")));

        assertDoesNotThrow(() -> {
            movieValidator.validateMovieTitle(movie);
            movieValidator.validateMovieId(movie);
            userValidator.validateUserName(user);
            userValidator.validateUserId(user);
        });
    }

    @Test
    @DisplayName("Level 1: Validators maintain independent state")
    void testValidatorsMaintainIndependentState() {
        Movie movie1 = new Movie("The Matrix", "TM101", new ArrayList<>());
        Movie movie2 = new Movie("The Mask", "TM102", new ArrayList<>());
        User user1 = new User("John Smith", "12345678A", new ArrayList<>());
        User user2 = new User("Jane Doe", "87654321B", new ArrayList<>());

        movieValidator.setMovie(movie1);
        movieValidator.setMovie(movie2);
        userValidator.setUser(user1);
        userValidator.setUser(user2);

        assertFalse(movieValidator.isMovieIdUnique("TM101"));
        assertFalse(movieValidator.isMovieIdUnique("TM102"));
        assertTrue(movieValidator.isMovieIdUnique("XX999"));

        assertFalse(userValidator.isUserIdUnique("12345678A"));
        assertFalse(userValidator.isUserIdUnique("87654321B"));
        assertTrue(userValidator.isUserIdUnique("99999999X"));
    }
}
