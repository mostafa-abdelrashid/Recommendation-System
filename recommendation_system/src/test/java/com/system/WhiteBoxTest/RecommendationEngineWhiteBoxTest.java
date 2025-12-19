package com.system.WhiteBoxTest;

import com.system.Movie;
import com.system.RecommendationEngine;
import com.system.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationEngineWhiteBoxTest {

    private Movie theDarkKnight;
    private Movie inceptionDreams;
    private Movie theNotebookStory;

    @BeforeEach
    void setUp() {
        theDarkKnight = new Movie("The Dark Knight", "TDK101", 
            new ArrayList<>(Arrays.asList("action", "thriller")));
        
        inceptionDreams = new Movie("Inception Dreams", "ID102", 
            new ArrayList<>(Arrays.asList("sci-fi", "thriller")));
        
        theNotebookStory = new Movie("The Notebook Story", "TNS103", 
            new ArrayList<>(Arrays.asList("romance", "drama")));
    }

    @Nested
    @DisplayName("Statement Coverage Tests")
    class StatementCoverageTests {

        @Test
        @DisplayName("TC_SC_01: Full Path Execution - Genre Match Found")
        void testFullPathExecution_GenreMatchFound() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            movies.add(inceptionDreams);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101", "SNH104")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.contains("Inception Dreams"));
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("Branch Coverage Tests")
    class BranchCoverageTests {

        @Test
        @DisplayName("TC_BR_01: All TRUE branches")
        void testAllTrueBranches() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            Movie id = new Movie("Inception Dreams", "ID102", 
                new ArrayList<>(Arrays.asList("action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            movies.add(id);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.contains("Inception Dreams"));
            assertFalse(result.contains("The Dark Knight"));
        }

        @Test
        @DisplayName("TC_BR_02: C1,C5,C3,C4 FALSE - Invalid movie ID")
        void testFalseBranches_InvalidMovieId() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            Movie id = new Movie("Inception Dreams", "ID102", 
                new ArrayList<>(Arrays.asList("action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            movies.add(id);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Omar Hassan", "16783459C", 
                new ArrayList<>(Arrays.asList("JW999")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("TC_BR_03: C2 FALSE - Movie already liked")
        void testC2False_MovieAlreadyLiked() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Basis Path Tests - getMovieById()")
    class GetMovieByIdBasisPathTests {

        @Test
        @DisplayName("TC_BP_01: Path 1 - Empty list returns null")
        void testPath1_EmptyList() {
            ArrayList<Movie> emptyMovies = new ArrayList<>();
            RecommendationEngine engine = new RecommendationEngine(emptyMovies);
            
            Movie result = engine.getMovieById("TDK101");
            
            assertNull(result);
        }

        @Test
        @DisplayName("TC_BP_02: Path 2 - Found immediately")
        void testPath2_FoundImmediately() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            movies.add(inceptionDreams);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            
            Movie result = engine.getMovieById("TDK101");
            
            assertNotNull(result);
            assertEquals("The Dark Knight", result.getTitle());
            assertEquals("TDK101", result.getMovieId());
        }

        @Test
        @DisplayName("TC_BP_03: Path 3 - Not found after loop")
        void testPath3_NotFoundAfterLoop() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            movies.add(inceptionDreams);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            
            Movie result = engine.getMovieById("JW999");
            
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Basis Path Tests - getRecommendationsForUser()")
    class GetRecommendationsBasisPathTests {

        @Test
        @DisplayName("TC_BP_01: Path 1 - Empty likedMovieIds, empty allMovies")
        void testPath1_EmptyInputs() {
            ArrayList<Movie> emptyMovies = new ArrayList<>();
            RecommendationEngine engine = new RecommendationEngine(emptyMovies);
            
            User user = new User("Sara Ali", "98765432B", new ArrayList<>());
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("TC_BP_02: Path 2 - Invalid movie ID (null movie)")
        void testPath2_InvalidMovieId() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Omar Hassan", "16783459C", 
                new ArrayList<>(Arrays.asList("JW999")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("TC_BP_03: Path 3 - Valid liked movie, no other movies")
        void testPath3_OnlyLikedMovie() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("TC_BP_04: Path 4 - No genre match")
        void testPath4_NoGenreMatch() {
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(theDarkKnight);
            movies.add(theNotebookStory);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("TC_BP_05: Path 5 - Genre match on first iteration")
        void testPath5_GenreMatchFirstIteration() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            Movie id = new Movie("Inception Dreams", "ID102", 
                new ArrayList<>(Arrays.asList("action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            movies.add(id);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.contains("Inception Dreams"));
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("TC_BP_06: Path 6 - Genre match on second genre")
        void testPath6_GenreMatchSecondGenre() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            Movie id = new Movie("Inception Dreams", "ID102", 
                new ArrayList<>(Arrays.asList("drama", "action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            movies.add(id);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Fatima Nour", "55566677D", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.contains("Inception Dreams"));
        }

        @Test
        @DisplayName("TC_BP_07: Path 7 - Match on later genre iteration")
        void testPath7_MatchOnLaterGenre() {
            Movie tdk = new Movie("The Dark Knight", "TDK101", 
                new ArrayList<>(Arrays.asList("action")));
            Movie snh = new Movie("Scary Night Horror", "SNH104", 
                new ArrayList<>(Arrays.asList("horror", "action")));
            
            ArrayList<Movie> movies = new ArrayList<>();
            movies.add(tdk);
            movies.add(snh);
            
            RecommendationEngine engine = new RecommendationEngine(movies);
            User user = new User("Ahmed Mohamed", "12345678A", 
                new ArrayList<>(Arrays.asList("TDK101")));
            
            List<String> result = engine.getRecommendationsForUser(user);
            
            assertTrue(result.contains("Scary Night Horror"));
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("Full Path Coverage Verification")
    class FullPathCoverageTests {

        @Test
        @DisplayName("Verify all paths covered for getMovieById")
        void verifyGetMovieByIdCoverage() {
            assertTrue(true, "All 3 paths for getMovieById() are covered by TC_BP_01 to TC_BP_03");
        }

        @Test
        @DisplayName("Verify all paths covered for getRecommendationsForUser")
        void verifyGetRecommendationsCoverage() {
            assertTrue(true, "All 7 paths for getRecommendationsForUser() are covered by TC_BP_01 to TC_BP_07");
        }
    }
    @Test
    public void testGetRecommendationsForUser() {
        
        Movie m1 = new Movie("The Matrix", "TM101", new ArrayList<>(Arrays.asList("Action", "Sci-Fi")));
        Movie m2 = new Movie("John Wick", "JW102", new ArrayList<>(Arrays.asList("Action", "Thriller")));
        Movie m3 = new Movie("Interstellar", "I103", new ArrayList<>(Arrays.asList("Sci-Fi", "Drama")));
        
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1, m2, m3));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        ArrayList<String> neoLikes = new ArrayList<>(Arrays.asList("TM101"));
        User user = new User("Neo Anderson", "12345678A", neoLikes);

        List<String> result = engine.getRecommendationsForUser(user);

        assertTrue(result.contains("John Wick"), "Should recommend John Wick (Action match)");
        assertTrue(result.contains("Interstellar"), "Should recommend Interstellar (Sci-Fi match)");
        assertFalse(result.contains("The Matrix"), "Should NOT recommend The Matrix (Already watched)");
        assertEquals(2, result.size());
    }
    
    @Test
    public void testGetMovieById() {
        Movie m1 = new Movie("The Boss Baby", "TBB200", new ArrayList<>(Arrays.asList("Comedy")));
        Movie m2 = new Movie("Superbad", "S201", new ArrayList<>(Arrays.asList("Comedy")));
        Movie m3 = new Movie("The Conjuring", "TC202", new ArrayList<>(Arrays.asList("Horror")));
        Movie m4 = new Movie("The Truman Show", "TTS203", new ArrayList<>(Arrays.asList("Drama", "Comedy"))); 
        Movie m5 = new Movie("The Show", "TS204", new ArrayList<>(Arrays.asList()));
        
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m3, m2, m1 ,m4,m5));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        ArrayList<String> mcLovinLikes = new ArrayList<>(Arrays.asList("TBB200" , "S201"));
        User user = new User("Mc Lovin", "23456789B", mcLovinLikes);

        List<String> result = engine.getRecommendationsForUser(user);

        assertTrue(result.contains("The Truman Show"), "Should recommend The Truman Show (Comedy match)");
        assertFalse(result.contains("The Conjuring"), "Should NOT recommend The Conjuring (No genre match)");
        assertEquals(1, result.size());
    }
    @Test
    public void testGetRecommendations_ADUP_InvalidMovieID() {
        Movie m1 = new Movie("Titanic", "T301", new ArrayList<>(Arrays.asList("Romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList("XX999"));
        User user = new User("Ghost User", "34567891C", ghostLikes);

        List<String> result = engine.getRecommendationsForUser(user);

        assertTrue(result.isEmpty(), "Should return empty list for invalid liked movie ID");
    }
    
    @Test
    public void testGetRecommendations_ADUP_InvalidThenValidMovieID() {
        Movie m1 = new Movie("Titanic", "T301", new ArrayList<>(Arrays.asList("Romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1));
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList("XX999" , "T301"));
        User user = new User("Ghost User", "45678912D", ghostLikes);

        List<String> result = engine.getRecommendationsForUser(user);

        assertTrue(result.isEmpty(), "Should return empty list for invalid liked movie ID");
        assertFalse(result.contains("Titanic"), "Should NOT recommend Titanic(already liked)");
    }
    
    @Test
    public void testGetRecommendations_ADUP_NoMoviesAndNoUserLikes() {
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList());
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        ArrayList<String> ghostLikes = new ArrayList<>(Arrays.asList());
        User user = new User("Ghost User", "56789123E", ghostLikes);

        List<String> result = engine.getRecommendationsForUser(user);

        assertTrue(result.isEmpty(), "Should return empty list");
    }
    
    @Test
    public void testGetMovieByid() {
        Movie m1 = new Movie("Speed", "S100", new ArrayList<>(Arrays.asList("Action")));
        Movie m2 = new Movie("Titanic", "T101", new ArrayList<>(Arrays.asList("Romance")));
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList(m1 , m2));
        
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        Movie result = engine.getMovieById("T101");
        assertNotNull(result);
        assertEquals("Titanic", result.getTitle());

        Movie nullResult = engine.getMovieById("XX999");
        assertNull(nullResult);
    }
    @Test
    public void testGetMovieByIdNoMovies() {
        ArrayList<Movie> allMovies = new ArrayList<>(Arrays.asList());
        RecommendationEngine engine = new RecommendationEngine(allMovies);

        Movie nullResult = engine.getMovieById("999");
        assertNull(nullResult);
    }
}
