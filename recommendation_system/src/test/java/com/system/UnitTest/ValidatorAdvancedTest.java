package com.system.UnitTest;
import com.system.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ValidatorAdvancedTest {

    DataStore store;
    Validator v;

    @AfterEach
    void tearDown() {
        store = null;
        v = null;
        System.out.println("TEARDOWN DONE");
    }
    // ================================
    // Movie Title TEST CASES
    // ================================

    @Test
    public void test_MovieTitle_0() {  // valid case
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Conjuring", "TC123", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateMovieTitle());
    }

    @Test
    public void test_MovieTitle_1() { // valid case long one
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Jurassic Park The Final Act", "TC123", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateMovieTitle());
    }

    @Test
    public void test_MovieTitle_2() { // non valid: first letter lowercase
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("baywatch", "TC123", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        assertThrows(DataValidationException.class, () -> v.validateMovieTitle());
    }

    @Test
    public void test_MovieTitle_3() { // non valid: lowercase in 2nd word
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Angry birds", "TC123", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        assertThrows(DataValidationException.class, () -> v.validateMovieTitle());
    }

    // ================================
    // Movie ID TEST CASES
    // ================================

    @Test
    public void test_MovieId_0() { // valid case
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Conjuring", "TC123", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);
        when(store.isMovieIdUnique(anyString())).thenReturn(true);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateMovieId());
    }

    @Test
    public void test_MovieId_1() { // valid case long one
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Jurassic Park The Final Act", "JPTFA598", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);
        when(store.isMovieIdUnique(anyString())).thenReturn(true);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateMovieId());
    }

    @Test
    public void test_MovieId_2() { // valid case
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", "I915", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);
        when(store.isMovieIdUnique(anyString())).thenReturn(true);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateMovieId());
    }

    @Test
    public void test_MovieId_3() { // non valid: non unique number
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Jurassic Park The Final Act", "JPTFA555", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);
        when(store.isMovieIdUnique("555")).thenReturn(false);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id numbers {JPTFA555} aren’t unique";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_4() { // missing letter
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Jurassic Park The Final Act", "JPFA555", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {JPFA555} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_5() { // extra letter
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Walking Dead", "TWDD104", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {TWDD104} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_6() { // lowercase letters
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Walking Dead", "twd104", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {twd104} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_7() { // 1 lowercase letter
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Walking Dead", "TwD104", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {TwD104} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_8() { // wrong letters but uppercase
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Shutter Island", "FR104", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {FR104} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_9() { // extra number
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Shutter Island", "SR1047", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {SR1047} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_10() { // missing number
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Shutter Island", "SR10", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {SR10} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_11() { // non digit
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Shutter Island", "SR1A4", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {SR1A4} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void test_MovieId_12() { // special character
        store = mock(DataStore.class);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Jurassic Park The Final Act", "J@TFA547", new ArrayList<>()));

        when(store.getAllMovies()).thenReturn(movies);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateMovieId());

        String expectedMessage = "ERROR: Movie Id letters {J@TFA547} are wrong";
        assertEquals(expectedMessage, ex.getMessage());
    }
    // ================================
    // USER NAME TEST CASES
    // ================================

    @Test
    void test_UserName_0() { // valid case
        store = mock(DataStore.class);

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed Ali", "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateUserName());
    }

    @Test
    void test_UserName_1() { // valid case single letter
        store = mock(DataStore.class);

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("A", "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateUserName());
    }

    @Test
    void test_UserName_2() { // invalid case with numbers
        store = mock(DataStore.class);

        String name = "Ahmed123";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_3() { // invalid case starting with space
        store = mock(DataStore.class);

        String name = " Ahmed";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_4() { // invalid case with special character
        store = mock(DataStore.class);

        String name = "Ahmed@Mah";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_5() { // invalid case with multiple spaces
        store = mock(DataStore.class);

        String name = "Ahmed  Ali";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_6() { // invalid case trailing space
        store = mock(DataStore.class);

        String name = "Ahmed ";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_7() { // invalid case empty
        store = mock(DataStore.class);

        String name = "";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_8() { // invalid case with hyphen
        store = mock(DataStore.class);

        String name = "Ahmed-Mah";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_9() { // invalid case with apostrophe
        store = mock(DataStore.class);

        String name = "O'Ahmed";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(name, "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName());

        assertEquals("ERROR: User Name {" + name + "} is wrong", ex.getMessage());
    }

    // ================================
    // USER ID TEST CASES
    // ================================

    @Test
    void test_UserId_0() { // valid case
        store = mock(DataStore.class);

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", "123456789", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);
        when(store.isUserIdUnique(anyString())).thenReturn(true);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateUserId());
    }

    @Test
    void test_UserId_1() { // valid case with last letter
        store = mock(DataStore.class);

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", "12345678A", new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);
        when(store.isUserIdUnique(anyString())).thenReturn(true);

        v = new Validator(store);

        assertDoesNotThrow(() -> v.validateUserId());
    }

    @Test
    void test_UserId_2() { // invalid case too short
        store = mock(DataStore.class);

        String id = "12345A";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_3() { // invalid case too long
        store = mock(DataStore.class);

        String id = "1234567890";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_4() { // invalid case with special character
        store = mock(DataStore.class);

        String id = "1234567$A";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_5() { // invalid case last two letters
        store = mock(DataStore.class);

        String id = "1234567AB";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_6() { // invalid case numbers and letters
        store = mock(DataStore.class);

        String id = "123DE678C";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_7() { // invalid case not starting with number
        store = mock(DataStore.class);

        String id = "A23456789";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_8() { // invalid case not unique
        store = mock(DataStore.class);

        String id = "12345678A";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>()),
                new User("Ali", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);
        when(store.isUserIdUnique(id)).thenReturn(false);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {" + id + "} is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_9() { // invalid case empty
        store = mock(DataStore.class);

        String id = "";

        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User("Ahmed", id, new ArrayList<>())
        ));

        when(store.getAllUsers()).thenReturn(users);

        v = new Validator(store);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId());

        assertEquals("ERROR: User Id {} is wrong", ex.getMessage());
    }
}
