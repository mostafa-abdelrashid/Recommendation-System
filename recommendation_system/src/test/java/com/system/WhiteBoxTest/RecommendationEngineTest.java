/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.system.WhiteBoxTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.system.Movie;
import com.system.RecommendationEngine;
import com.system.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author genzoo
 */
public class RecommendationEngineTest {
    
    public RecommendationEngineTest() {
    }
    
    /**
    * ADUP Test Cases for getRecommendationsForUser method
    */
    
    
    /**
    * ADUP Test Case 1: The "Perfect Match" & "Already Watched" Path
    */
    @Test
    public void testGetRecommendationsForUser() {
        
        // SETUP: Create Movies 
        // Note: Constructor is Movie(Title, ID, Genres)
        Movie m1 = new Movie("The Matrix", "101", new ArrayList<>(Arrays.asList("Action", "Sci-Fi")));
        Movie m2 = new Movie("John Wick", "102", new ArrayList<>(Arrays.asList("Action", "Thriller")));
        Movie m3 = new Movie("Interstellar", "103", new ArrayList<>(Arrays.asList("Sci-Fi", "Drama")));
        
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1, m2, m3));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // SETUP: Create User who likes m1 ("101")
        // Note: Constructor is User(Name, ID, LikedMovieIds)
        ArrayList<String> neoLikes = new ArrayList<>(Arrays.asList("101"));
        User user = new User("Neo", "u1", neoLikes);

        // EXECUTE
        List<String> result = engine.getRecommendationsForUser(user);

        // ASSERT
        assertTrue(result.contains("John Wick"), "Should recommend John Wick (Action match)");
        assertTrue(result.contains("Interstellar"), "Should recommend Interstellar (Sci-Fi match)");
        assertFalse(result.contains("The Matrix"), "Should NOT recommend The Matrix (Already watched)");
        assertEquals(2, result.size());
    }
    
    /**
    * ADUP Test Case 2: The "No Match" & "Loop Break" Path and mo genre movie
    */
    @Test
    public void testGetMovieById() {
        Movie m1 = new Movie("the boss baby", "200", new ArrayList<>(Arrays.asList("Comedy")));
        Movie m2 = new Movie("Superbad", "201", new ArrayList<>(Arrays.asList("Comedy")));
        Movie m3 = new Movie("The Conjuring", "202", new ArrayList<>(Arrays.asList("Horror")));
        Movie m4 = new Movie("The Truman Show", "203", new ArrayList<>(Arrays.asList("Drama", "Comedy"))); 
        Movie m5 = new Movie("The Show", "204", new ArrayList<>(Arrays.asList()));
        
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m3, m2, m1 ,m4,m5));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // SETUP: User likes m1 , m2 ("200" , "201")
        ArrayList<String> mcLovinLikes = new ArrayList<>(Arrays.asList("200" , "201"));
        User user = new User("McLovin", "u2", mcLovinLikes);

        // EXECUTE
        List<String> result = engine.getRecommendationsForUser(user);

        // ASSERT
        assertTrue(result.contains("The Truman Show"), "Should recommend The Truman Show (Comedy match)");
        assertFalse(result.contains("The Conjuring"), "Should NOT recommend The Conjuring (No genre match)");
        assertEquals(1, result.size());
    }
    /**
     * ADUP Test Case 3: The "Invalid ID" Path (user likes movie that doesn't exist)
     */
    @Test
    public void testGetRecommendations_ADUP_InvalidMovieID() {
        // SETUP: System has only one movie
        Movie m1 = new Movie("Titanic", "301", new ArrayList<>(Arrays.asList("Romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // SETUP: User likes a movie ID "999" that does NOT exist
        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList("999"));
        User user = new User("Ghost", "u3", ghostLikes);

        // EXECUTE
        List<String> result = engine.getRecommendationsForUser(user);

        // ASSERT
        // Should return empty because "999" returns null movie -> no genres collected -> no matches found
        assertTrue(result.isEmpty(), "Should return empty list for invalid liked movie ID");
    }
    
    /**
     * ADUP Test Case 4:user likes movie that doesn't exist and an existing movie
     */
    @Test
    public void testGetRecommendations_ADUP_InvalidThenValidMovieID() {
        // SETUP: System has only one movie
        Movie m1 = new Movie("Titanic", "301", new ArrayList<>(Arrays.asList("Romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // SETUP: User likes a movie ID "999" that does NOT exist
        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList("999" , "301"));
        User user = new User("Ghost", "u3", ghostLikes);

        // EXECUTE
        List<String> result = engine.getRecommendationsForUser(user);

        // ASSERT
        // Should return empty because "999" returns null movie -> no genres collected -> no matches found
        assertTrue(result.isEmpty(), "Should return empty list for invalid liked movie ID");
        assertFalse(result.contains("Titanic"), "Should NOT recommend Titanic(already liked)");
    }
    
    /**
     * ADUP Test Case 5 : there are no movies and user didn't like any
     */
    @Test
    public void testGetRecommendations_ADUP_NoMoviesAndNoUserLikes() {
        // SETUP: System has no movies
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList());
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // SETUP: User doens't like any movie
        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList());
        User user = new User("Ghost", "u3", ghostLikes);

        // EXECUTE
        List<String> result = engine.getRecommendationsForUser(user);

        // ASSERT
        assertTrue(result.isEmpty(), "Should return empty list");
    }
    
    /**
    * ADUP Test Cases for getMovieById method
    */
    
    @Test
    public void testGetMovieByid() {
        Movie m1 = new Movie("speed", "100", new ArrayList<>(Arrays.asList("action")));
        Movie m2 = new Movie("titanic", "101", new ArrayList<>(Arrays.asList("romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1 , m2));
        
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        // Path 1: Match Found
        Movie result = engine.getMovieById("101");
        assertNotNull(result);
        assertEquals("titanic", result.getTitle());

        // Path 2: No Match (Return Null)
        Movie nullResult = engine.getMovieById("999");
        assertNull(nullResult);
    }
    /**
    * ADUP Test Case for getMovieById method no movies
    */
     @Test
    public void testGetMovieByIdNoMovies() {
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList());
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        Movie nullResult = engine.getMovieById("999");
        assertNull(nullResult);
    }
}
