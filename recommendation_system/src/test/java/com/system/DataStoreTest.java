package com.system;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DataStore Unit Tests")
class DataStoreTest {
    private DataStore dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new DataStore(new ArrayList<>(), new ArrayList<>());
    }


    private Movie createMovie(String title, String id, String... genres) {
        Movie movie = mock(Movie.class);
        when(movie.getMovieId()).thenReturn(id);
        when(movie.getTitle()).thenReturn(title);
        when(movie.getGenres()).thenReturn(new ArrayList<>(Arrays.asList(genres)));
        return movie;
    }

    private User createUser(String name, String id, String... likedMovieIds) {
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(id);
        when(user.getUserName()).thenReturn(name);
        when(user.getLikedMovieIds()).thenReturn(new ArrayList<>(Arrays.asList(likedMovieIds)));
        return user;
    }

    @Test
    @DisplayName("Should add movies to the data store")
    void addMovie_AddsMoviesSuccessfully() {
        Movie movie1 = createMovie("Inception", "m1", "Action", "Sci-Fi");
        Movie movie2 = createMovie("Titanic", "m2", "Romance");
        dataStore.getAllMovies().add(movie1);
        dataStore.getAllMovies().add(movie2);
        assertEquals(2, dataStore.getAllMovies().size());
        assertTrue(dataStore.getAllMovies().contains(movie1));
        assertTrue(dataStore.getAllMovies().contains(movie2));
    }

    @Test
    @DisplayName("New datastore starts empty for movies and users")
    void newDataStore_IsEmpty() {
        assertTrue(dataStore.getAllMovies().isEmpty(), "Expected no movies on fresh DataStore");
        assertTrue(dataStore.getAllUsers().isEmpty(), "Expected no users on fresh DataStore");
    }

    @Test
    @DisplayName("Should preserve insertion order for movies")
    void addMovie_PreservesInsertionOrder() {
        Movie first = createMovie("First", "m1");
        Movie second = createMovie("Second", "m2");
        dataStore.getAllMovies().add(first);
        dataStore.getAllMovies().add(second);
        List<Movie> movies = dataStore.getAllMovies();
        assertFalse(movies.isEmpty());
        assertEquals(first, movies.get(0));
        assertEquals(second, movies.get(1));
    }

    @Test
    @DisplayName("Should add users to the data store")
    void addUser_AddsUsersSuccessfully() {
        User user1 = createUser("Alice", "u1");
        User user2 = createUser("Bob", "u2");
        dataStore.getAllUsers().add(user1);
        dataStore.getAllUsers().add(user2);
        assertEquals(2, dataStore.getAllUsers().size());
        assertTrue(dataStore.getAllUsers().contains(user1));
        assertTrue(dataStore.getAllUsers().contains(user2));
    }

    @Test
    @DisplayName("Should preserve insertion order for users")
    void addUser_PreservesInsertionOrder() {
        User u1 = createUser("Alice", "u1");
        User u2 = createUser("Bob", "u2");
        dataStore.getAllUsers().add(u1);
        dataStore.getAllUsers().add(u2);
        List<User> users = dataStore.getAllUsers();
        assertFalse(users.isEmpty());
        assertEquals(u1, users.get(0));
        assertEquals(u2, users.get(1));
    }

    @Test
    @DisplayName("Should return movie when ID exists")
    void getMovieById_ExistingId_ReturnsMovie() {
        Movie movie = createMovie("Avatar", "m1", "Fantasy");
        dataStore.getAllMovies().add(movie);
        Movie found = dataStore.getMovieById("m1");
        assertNotNull(found);
        assertEquals("Avatar", found.getTitle());
    }

    @Test
    @DisplayName("Should return null when movie ID does not exist")
    void getMovieById_NonExistingId_ReturnsNull() {
        dataStore.getAllMovies().add(createMovie("Inception", "m1", "Action"));
        Movie found = dataStore.getMovieById("unknown");
        assertNull(found);
    }

    @Test
    @DisplayName("Should handle empty movie list safely")
    void getMovieById_EmptyList_ReturnsNull() {
        assertTrue(dataStore.getAllMovies().isEmpty());
        Movie found = dataStore.getMovieById("any");
        assertNull(found);
    }

    @Test
    @DisplayName("Should return true when user ID is unique")
    void isUserIdUnique_Unique_ReturnsTrue() {
        dataStore.getAllUsers().add(createUser("Alice", "u1"));
        assertTrue(dataStore.isUserIdUnique("u2"));
    }

    @Test
    @DisplayName("Should return false when user ID already exists")
    void isUserIdUnique_Duplicate_ReturnsFalse() {
        dataStore.getAllUsers().add(createUser("Bob", "u1"));
        assertFalse(dataStore.isUserIdUnique("u1"));
    }

    @Test
    @DisplayName("Should return true when no users exist")
    void isUserIdUnique_EmptyList_ReturnsTrue() {
        assertTrue(dataStore.isUserIdUnique("newUser"));
    }

    @Test
    @DisplayName("Should return true when movie ID is unique")
    void isMovieIdUnique_Unique_ReturnsTrue() {
        dataStore.getAllMovies().add(createMovie("Inception", "m1", "Action"));
        assertTrue(dataStore.isMovieIdUnique("m2"));
    }

    @Test
    @DisplayName("Should return false when movie ID already exists")
    void isMovieIdUnique_Duplicate_ReturnsFalse() {
        dataStore.getAllMovies().add(createMovie("Titanic", "m2", "Romance"));
        assertFalse(dataStore.isMovieIdUnique("m2"));
    }

    @Test
    @DisplayName("Should return true when no movies exist")
    void isMovieIdUnique_EmptyList_ReturnsTrue() {
        assertTrue(dataStore.isMovieIdUnique("freshMovie"));
    }

    @Test
    @DisplayName("Should handle null ID in getMovieById")
    void getMovieById_NullId_ReturnsNull() {
        assertNull(dataStore.getMovieById(null));
    }

    @Test
    @DisplayName("Should handle null ID in isUserIdUnique")
    void isUserIdUnique_NullId_ReturnsTrue() {
        assertTrue(dataStore.isUserIdUnique(null));
    }

    @Test
    @DisplayName("Should handle null ID in isMovieIdUnique")  
    void isMovieIdUnique_NullId_ReturnsTrue() {
        assertTrue(dataStore.isMovieIdUnique(null));
    }

    @Test
    @DisplayName("Should handle multiple users and movies together correctly")
    void integration_AddAndRetrieve_MultipleEntities() {
        Movie movie1 = createMovie("Inception", "m1", "Action");
        Movie movie2 = createMovie("Titanic", "m2", "Drama");
        User user1 = createUser("Alice", "u1", "m1");
        User user2 = createUser("Bob", "u2", "m2");
        dataStore.getAllMovies().add(movie1);
        dataStore.getAllMovies().add(movie2);
        dataStore.getAllUsers().add(user1);
        dataStore.getAllUsers().add(user2);
        assertEquals(movie1, dataStore.getMovieById("m1"));
        assertEquals(movie2, dataStore.getMovieById("m2"));
        assertFalse(dataStore.isMovieIdUnique("m2"));
        assertTrue(dataStore.isMovieIdUnique("m3"));
        assertFalse(dataStore.isUserIdUnique("u2"));
    }
}
