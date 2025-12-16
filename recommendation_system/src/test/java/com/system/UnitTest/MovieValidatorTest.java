package com.system.UnitTest;
import com.system.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MovieValidatorTest {

    MovieValidator v;

    @AfterEach
    void tearDown() {
        v = null;
        System.out.println("TEARDOWN DONE");
    }

    @Test
    public void test_MovieTitle_0() {
        Movie movie = new Movie("The Conjuring", "TC123", new ArrayList<>());

        v = new MovieValidator();

        assertDoesNotThrow(() -> v.validateMovieTitle(movie));
    }

    @Test
    public void test_MovieTitle_1() {
        Movie movie = new Movie("Jurassic Park The Final Act", "TC123", new ArrayList<>());

        v = new MovieValidator();

        assertDoesNotThrow(() -> v.validateMovieTitle(movie));
    }

    @Test
    public void test_MovieTitle_2() {
        Movie movie = new Movie("baywatch", "TC123", new ArrayList<>());

        v = new MovieValidator();

        assertThrows(DataValidationException.class, () -> v.validateMovieTitle(movie));
    }

    @Test
    public void test_MovieTitle_3() {
        Movie movie = new Movie("Angry birds", "TC123", new ArrayList<>());

        v = new MovieValidator();

        assertThrows(DataValidationException.class, () -> v.validateMovieTitle(movie));
    }

    @Test
    public void test_MovieId_0() {
        Movie movie = new Movie("The Conjuring", "TC123", new ArrayList<>());

        v = new MovieValidator();

        assertDoesNotThrow(() -> v.validateMovieId(movie));
    }

    @Test
    public void test_MovieId_1() {
        Movie movie = new Movie("Jurassic Park The Final Act", "JPTFA598", new ArrayList<>());

        v = new MovieValidator();

        assertDoesNotThrow(() -> v.validateMovieId(movie));
    }

    @Test
    public void test_MovieId_2() {
        Movie movie = new Movie("Inception", "I915", new ArrayList<>());

        v = new MovieValidator();

        assertDoesNotThrow(() -> v.validateMovieId(movie));
    }

    @Test
    public void test_MovieId_3() {
        Movie movie1 = new Movie("Jurassic Park The Final Act", "JPTFA555", new ArrayList<>());
        Movie movie2 = new Movie("Jurassic Park The Final Act", "JPTFA555", new ArrayList<>());

        v = new MovieValidator();
        v.setMovie(movie1);

        assertFalse(v.isMovieIdUnique(movie2.getMovieId()));
    }

    @Test
    public void test_MovieId_4() {
        Movie movie = new Movie("Jurassic Park The Final Act", "JPFA555", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters JPFA555 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_5() {
        Movie movie = new Movie("The Walking Dead", "TWDD104", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters TWDD104 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_6() {
        Movie movie = new Movie("The Walking Dead", "twd104", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters twd104 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_7() {
        Movie movie = new Movie("The Walking Dead", "TwD104", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters TwD104 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_8() {
        Movie movie = new Movie("Shutter Island", "FR104", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters FR104 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_9() {
        Movie movie = new Movie("Shutter Island", "SR1047", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters SR1047 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_10() {
        Movie movie = new Movie("Shutter Island", "SR10", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters SR10 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_11() {
        Movie movie = new Movie("Shutter Island", "SR1A4", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters SR1A4 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_12() {
        Movie movie = new Movie("Jurassic Park The Final Act", "J@TFA547", new ArrayList<>());

        v = new MovieValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId(movie));

        String expectedMessage = "ERROR: Movie Id letters J@TFA547 are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void isMovieIdUnique_Unique_ReturnsTrue() {
        v = new MovieValidator();
        Movie movie = new Movie("Inception", "I123", new ArrayList<>());
        v.setMovie(movie);
        assertTrue(v.isMovieIdUnique("456"));
    }

    @Test
    public void isMovieIdUnique_Duplicate_ReturnsFalse() {
        v = new MovieValidator();
        Movie movie = new Movie("Titanic", "T123", new ArrayList<>());
        v.setMovie(movie);
        assertFalse(v.isMovieIdUnique("T123"));
    }

    @Test
    public void isMovieIdUnique_EmptyList_ReturnsTrue() {
        v = new MovieValidator();
        assertTrue(v.isMovieIdUnique("anyId"));
    }

    @Test
    public void isMovieIdUnique_NullId_ReturnsTrue() {
        v = new MovieValidator();
        assertTrue(v.isMovieIdUnique(null));
    }
}
