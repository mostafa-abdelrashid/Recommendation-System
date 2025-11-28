package com.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class RecommendationEngineTest {
    @Mock
    private DataStore dataStore;
    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        this.recommendationEngine = new RecommendationEngine(this.dataStore);
    }

    private <T> ArrayList<T> arrayListOf(T... elements) {
        return new ArrayList(Arrays.asList(elements));
    }

    @Test
    @DisplayName("Should return empty list when user has no liked movies")
    void getRecommendationsForUser_NoLikedMovies_ReturnsEmptyList() {
        User user = new User("testUser", "user1", new ArrayList());
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(new ArrayList());
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
        ((DataStore)Mockito.verify(this.dataStore, Mockito.never())).getMovieById(Mockito.anyString());
    }

    @Test
    @DisplayName("Should return empty list when liked movies don't exist in data store")
    void getRecommendationsForUser_LikedMoviesNotFound_ReturnsEmptyList() {
        User user = new User("testUser", "user1", this.arrayListOf("nonExistent1", "nonExistent2"));
        Mockito.when(this.dataStore.getMovieById("nonExistent1")).thenReturn((Movie)null);
        Mockito.when(this.dataStore.getMovieById("nonExistent2")).thenReturn((Movie)null);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(new ArrayList());
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
        ((DataStore)Mockito.verify(this.dataStore)).getMovieById("nonExistent1");
        ((DataStore)Mockito.verify(this.dataStore)).getMovieById("nonExistent2");
    }

    @Test
    @DisplayName("Should recommend movies with matching genres")
    void getRecommendationsForUser_MatchingGenres_ReturnsRecommendations() {
        User user = new User("testUser", "user1", this.arrayListOf("action1", "drama1"));
        Movie likedActionMovie = new Movie("Action Movie", "action1", this.arrayListOf("Action", "Thriller"));
        Movie likedDramaMovie = new Movie("Drama Movie", "drama1", this.arrayListOf("Drama", "Romance"));
        Movie recommendedAction = new Movie("Another Action", "action2", this.arrayListOf("Action", "Adventure"));
        Movie recommendedDrama = new Movie("Another Drama", "drama2", this.arrayListOf("Drama"));
        Movie unrelatedMovie = new Movie("Comedy Movie", "comedy1", this.arrayListOf("Comedy"));
        Mockito.when(this.dataStore.getMovieById("action1")).thenReturn(likedActionMovie);
        Mockito.when(this.dataStore.getMovieById("drama1")).thenReturn(likedDramaMovie);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(likedActionMovie, likedDramaMovie, recommendedAction, recommendedDrama, unrelatedMovie));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Another Action"));
        Assertions.assertTrue(recommendations.contains("Another Drama"));
        Assertions.assertFalse(recommendations.contains("Comedy Movie"));
        Assertions.assertFalse(recommendations.contains("Action Movie"));
        Assertions.assertFalse(recommendations.contains("Drama Movie"));
        ((DataStore)Mockito.verify(this.dataStore)).getMovieById("action1");
        ((DataStore)Mockito.verify(this.dataStore)).getMovieById("drama1");
    }

    @Test
    @DisplayName("Should not recommend movies user already liked")
    void getRecommendationsForUser_ExcludesAlreadyLikedMovies() {
        User user = new User("testUser", "user1", this.arrayListOf("liked1"));
        Movie likedMovie = new Movie("Liked Movie", "liked1", this.arrayListOf("Action"));
        Movie similarMovie = new Movie("Similar Movie", "similar1", this.arrayListOf("Action"));
        Mockito.when(this.dataStore.getMovieById("liked1")).thenReturn(likedMovie);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(likedMovie, similarMovie));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Similar Movie"));
        Assertions.assertFalse(recommendations.contains("Liked Movie"));
    }

    @Test
    @DisplayName("Should handle movies with multiple genres correctly")
    void getRecommendationsForUser_MultipleGenres_ReturnsCorrectRecommendations() {
        User user = new User("testUser", "user1", this.arrayListOf("multi1"));
        Movie multiGenreMovie = new Movie("Multi Genre Movie", "multi1", this.arrayListOf("Action", "Comedy", "Drama"));
        Movie actionComedy = new Movie("Action Comedy", "movie2", this.arrayListOf("Action", "Comedy"));
        Movie dramaRomance = new Movie("Drama Romance", "movie3", this.arrayListOf("Drama", "Romance"));
        Movie horrorMovie = new Movie("Horror Movie", "movie4", this.arrayListOf("Horror"));
        Mockito.when(this.dataStore.getMovieById("multi1")).thenReturn(multiGenreMovie);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(multiGenreMovie, actionComedy, dramaRomance, horrorMovie));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Action Comedy"));
        Assertions.assertTrue(recommendations.contains("Drama Romance"));
        Assertions.assertFalse(recommendations.contains("Horror Movie"));
    }

    @Test
    @DisplayName("Should return unique recommendations (no duplicates)")
    void getRecommendationsForUser_ReturnsUniqueRecommendations() {
        User user = new User("testUser", "user1", this.arrayListOf("liked1"));
        Movie likedMovie = new Movie("Liked Movie", "liked1", this.arrayListOf("Action"));
        Movie duplicate1 = new Movie("Recommended Movie", "rec1", this.arrayListOf("Action"));
        Movie duplicate2 = new Movie("Recommended Movie", "rec2", this.arrayListOf("Action"));
        Mockito.when(this.dataStore.getMovieById("liked1")).thenReturn(likedMovie);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(likedMovie, duplicate1, duplicate2));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Recommended Movie"));
    }

    @Test
    @DisplayName("Should handle empty genres in movies")
    void getRecommendationsForUser_EmptyGenres_HandlesCorrectly() {
        User user = new User("testUser", "user1", this.arrayListOf("liked1"));
        Movie likedMovie = new Movie("Liked Movie", "liked1", this.arrayListOf("Action"));
        Movie noGenreMovie = new Movie("No Genre Movie", "noGenre", new ArrayList());
        Mockito.when(this.dataStore.getMovieById("liked1")).thenReturn(likedMovie);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(likedMovie, noGenreMovie));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should handle case where all movies are already liked")
    void getRecommendationsForUser_AllMoviesLiked_ReturnsEmptyList() {
        User user = new User("testUser", "user1", this.arrayListOf("movie1", "movie2"));
        Movie movie1 = new Movie("Movie 1", "movie1", this.arrayListOf("Action"));
        Movie movie2 = new Movie("Movie 2", "movie2", this.arrayListOf("Drama"));
        Mockito.when(this.dataStore.getMovieById("movie1")).thenReturn(movie1);
        Mockito.when(this.dataStore.getMovieById("movie2")).thenReturn(movie2);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(movie1, movie2));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should recommend based on partial genre matches")
    void getRecommendationsForUser_PartialGenreMatches_ReturnsRecommendations() {
        User user = new User("testUser", "user1", this.arrayListOf("action1"));
        Movie actionThriller = new Movie("Action Thriller", "action1", this.arrayListOf("Action", "Thriller"));
        Movie actionAdventure = new Movie("Action Adventure", "action2", this.arrayListOf("Action", "Adventure"));
        Movie pureThriller = new Movie("Pure Thriller", "thriller1", this.arrayListOf("Thriller"));
        Mockito.when(this.dataStore.getMovieById("action1")).thenReturn(actionThriller);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(actionThriller, actionAdventure, pureThriller));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Action Adventure"));
        Assertions.assertTrue(recommendations.contains("Pure Thriller"));
    }

    @Test
    @DisplayName("Should work with single genre movies")
    void getRecommendationsForUser_SingleGenreMovies_WorksCorrectly() {
        User user = new User("testUser", "user1", this.arrayListOf("comedy1"));
        Movie comedy1 = new Movie("Comedy Movie", "comedy1", this.arrayListOf("Comedy"));
        Movie comedy2 = new Movie("Another Comedy", "comedy2", this.arrayListOf("Comedy"));
        Movie drama = new Movie("Drama Movie", "drama1", this.arrayListOf("Drama"));
        Mockito.when(this.dataStore.getMovieById("comedy1")).thenReturn(comedy1);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(comedy1, comedy2, drama));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Another Comedy"));
        Assertions.assertFalse(recommendations.contains("Drama Movie"));
    }

    @Test
    @DisplayName("Should handle mixed valid and invalid liked movies")
    void getRecommendationsForUser_MixedValidInvalidLikedMovies_ReturnsCorrectRecommendations() {
        User user = new User("testUser", "user1", this.arrayListOf("valid1", "nonExistent", "valid2"));
        Movie valid1 = new Movie("Valid Movie 1", "valid1", this.arrayListOf("Action"));
        Movie valid2 = new Movie("Valid Movie 2", "valid2", this.arrayListOf("Drama"));
        Movie recAction = new Movie("Recommended Action", "rec1", this.arrayListOf("Action"));
        Movie recDrama = new Movie("Recommended Drama", "rec2", this.arrayListOf("Drama"));
        Mockito.when(this.dataStore.getMovieById("valid1")).thenReturn(valid1);
        Mockito.when(this.dataStore.getMovieById("nonExistent")).thenReturn((Movie)null);
        Mockito.when(this.dataStore.getMovieById("valid2")).thenReturn(valid2);
        Mockito.when(this.dataStore.getAllMovies()).thenReturn(this.arrayListOf(valid1, valid2, recAction, recDrama));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Recommended Action"));
        Assertions.assertTrue(recommendations.contains("Recommended Drama"));
        ((DataStore)Mockito.verify(this.dataStore)).getMovieById("nonExistent");
    }
}
