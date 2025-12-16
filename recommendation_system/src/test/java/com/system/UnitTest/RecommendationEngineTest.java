package com.system.UnitTest;
import com.system.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RecommendationEngineTest {
    private ArrayList<Movie> allMovies;
    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        this.allMovies = new ArrayList<>();
        this.recommendationEngine = new RecommendationEngine(this.allMovies);
    }

    private <T> ArrayList<T> arrayListOf(T... elements) {
        return new ArrayList(Arrays.asList(elements));
    }

    @Test
    @DisplayName("Should return empty list when user has no liked movies")
    void getRecommendationsForUser_NoLikedMovies_ReturnsEmptyList() {
        User user = new User("John Smith", "12345678A", new ArrayList());
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when liked movies don't exist in data store")
    void getRecommendationsForUser_LikedMoviesNotFound_ReturnsEmptyList() {
        User user = new User("Emma Watson", "12345678A", this.arrayListOf("XX999", "YY888"));
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should recommend movies with matching genres")
    void getRecommendationsForUser_MatchingGenres_ReturnsRecommendations() {
        User user = new User("Michael Johnson", "12345678A", this.arrayListOf("TDK101", "TSR102"));
        Movie likedActionMovie = new Movie("The Dark Knight", "TDK101", this.arrayListOf("Action", "Thriller"));
        Movie likedDramaMovie = new Movie("The Shawshank Redemption", "TSR102", this.arrayListOf("Drama", "Romance"));
        Movie recommendedAction = new Movie("Die Hard", "DH201", this.arrayListOf("Action", "Adventure"));
        Movie recommendedDrama = new Movie("Forrest Gump", "FG202", this.arrayListOf("Drama"));
        Movie unrelatedMovie = new Movie("The Hangover", "TH301", this.arrayListOf("Comedy"));
        
        allMovies.add(likedActionMovie);
        allMovies.add(likedDramaMovie);
        allMovies.add(recommendedAction);
        allMovies.add(recommendedDrama);
        allMovies.add(unrelatedMovie);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Die Hard"));
        Assertions.assertTrue(recommendations.contains("Forrest Gump"));
        Assertions.assertFalse(recommendations.contains("The Hangover"));
        Assertions.assertFalse(recommendations.contains("The Dark Knight"));
        Assertions.assertFalse(recommendations.contains("The Shawshank Redemption"));
    }

    @Test
    @DisplayName("Should not recommend movies user already liked")
    void getRecommendationsForUser_ExcludesAlreadyLikedMovies() {
        User user = new User("Sarah Connor", "12345678A", this.arrayListOf("I101"));
        Movie likedMovie = new Movie("Inception", "I101", this.arrayListOf("Action"));
        Movie similarMovie = new Movie("The Matrix", "TM201", this.arrayListOf("Action"));
        
        allMovies.add(likedMovie);
        allMovies.add(similarMovie);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("The Matrix"));
        Assertions.assertFalse(recommendations.contains("Inception"));
    }

    @Test
    @DisplayName("Should handle movies with multiple genres correctly")
    void getRecommendationsForUser_MultipleGenres_ReturnsCorrectRecommendations() {
        User user = new User("Robert Downey", "12345678A", this.arrayListOf("TG101"));
        Movie multiGenreMovie = new Movie("The Godfather", "TG101", this.arrayListOf("Action", "Comedy", "Drama"));
        Movie actionComedy = new Movie("Rush Hour", "RH201", this.arrayListOf("Action", "Comedy"));
        Movie dramaRomance = new Movie("Titanic", "T202", this.arrayListOf("Drama", "Romance"));
        Movie horrorMovie = new Movie("The Conjuring", "TC301", this.arrayListOf("Horror"));
        
        allMovies.add(multiGenreMovie);
        allMovies.add(actionComedy);
        allMovies.add(dramaRomance);
        allMovies.add(horrorMovie);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Rush Hour"));
        Assertions.assertTrue(recommendations.contains("Titanic"));
        Assertions.assertFalse(recommendations.contains("The Conjuring"));
    }

    @Test
    @DisplayName("Should return unique recommendations (no duplicates)")
    void getRecommendationsForUser_ReturnsUniqueRecommendations() {
        User user = new User("Chris Evans", "12345678A", this.arrayListOf("A101"));
        Movie likedMovie = new Movie("Avengers", "A101", this.arrayListOf("Action"));
        Movie duplicate1 = new Movie("Iron Man", "IM201", this.arrayListOf("Action"));
        Movie duplicate2 = new Movie("Iron Man", "IM202", this.arrayListOf("Action"));
        
        allMovies.add(likedMovie);
        allMovies.add(duplicate1);
        allMovies.add(duplicate2);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Iron Man"));
    }

    @Test
    @DisplayName("Should handle empty genres in movies")
    void getRecommendationsForUser_EmptyGenres_HandlesCorrectly() {
        User user = new User("Tom Hanks", "12345678A", this.arrayListOf("JP101"));
        Movie likedMovie = new Movie("Jurassic Park", "JP101", this.arrayListOf("Action"));
        Movie noGenreMovie = new Movie("Unknown Film", "UF201", new ArrayList());
        
        allMovies.add(likedMovie);
        allMovies.add(noGenreMovie);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should handle case where all movies are already liked")
    void getRecommendationsForUser_AllMoviesLiked_ReturnsEmptyList() {
        User user = new User("Leonardo Dicaprio", "12345678A", this.arrayListOf("T101", "I102"));
        Movie movie1 = new Movie("Titanic", "T101", this.arrayListOf("Action"));
        Movie movie2 = new Movie("Inception", "I102", this.arrayListOf("Drama"));
        
        allMovies.add(movie1);
        allMovies.add(movie2);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertTrue(recommendations.isEmpty());
    }

    @Test
    @DisplayName("Should recommend based on partial genre matches")
    void getRecommendationsForUser_PartialGenreMatches_ReturnsRecommendations() {
        User user = new User("Keanu Reeves", "12345678A", this.arrayListOf("JW101"));
        Movie actionThriller = new Movie("John Wick", "JW101", this.arrayListOf("Action", "Thriller"));
        Movie actionAdventure = new Movie("Mad Max Fury Road", "MMFR201", this.arrayListOf("Action", "Adventure"));
        Movie pureThriller = new Movie("Gone Girl", "GG301", this.arrayListOf("Thriller"));
        
        allMovies.add(actionThriller);
        allMovies.add(actionAdventure);
        allMovies.add(pureThriller);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Mad Max Fury Road"));
        Assertions.assertTrue(recommendations.contains("Gone Girl"));
    }

    @Test
    @DisplayName("Should work with single genre movies")
    void getRecommendationsForUser_SingleGenreMovies_WorksCorrectly() {
        User user = new User("Jim Carrey", "12345678A", this.arrayListOf("TM101"));
        Movie comedy1 = new Movie("The Mask", "TM101", this.arrayListOf("Comedy"));
        Movie comedy2 = new Movie("Dumb And Dumber", "DAD201", this.arrayListOf("Comedy"));
        Movie drama = new Movie("Schindlers List", "SL301", this.arrayListOf("Drama"));
        
        allMovies.add(comedy1);
        allMovies.add(comedy2);
        allMovies.add(drama);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(1, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Dumb And Dumber"));
        Assertions.assertFalse(recommendations.contains("Schindlers List"));
    }

    @Test
    @DisplayName("Should handle mixed valid and invalid liked movies")
    void getRecommendationsForUser_MixedValidInvalidLikedMovies_ReturnsCorrectRecommendations() {
        User user = new User("Brad Pitt", "12345678A", this.arrayListOf("FC101", "XX999", "S102"));
        Movie valid1 = new Movie("Fight Club", "FC101", this.arrayListOf("Action"));
        Movie valid2 = new Movie("Seven", "S102", this.arrayListOf("Drama"));
        Movie recAction = new Movie("Gladiator", "G201", this.arrayListOf("Action"));
        Movie recDrama = new Movie("The Departed", "TD202", this.arrayListOf("Drama"));
        
        allMovies.add(valid1);
        allMovies.add(valid2);
        allMovies.add(recAction);
        allMovies.add(recDrama);
        
        List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
        Assertions.assertEquals(2, recommendations.size());
        Assertions.assertTrue(recommendations.contains("Gladiator"));
        Assertions.assertTrue(recommendations.contains("The Departed"));
    }
}
