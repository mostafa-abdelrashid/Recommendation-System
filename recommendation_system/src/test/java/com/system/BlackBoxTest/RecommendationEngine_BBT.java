package com.system.BlackBoxTesting;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.system.Movie;
import com.system.RecommendationEngine;
import com.system.*;

class RecommendationEngine_BBT {
	private ArrayList<Movie> allMovies;
    private RecommendationEngine recommendationEngine;

	@BeforeEach
	void setUp() throws Exception {
		this.allMovies = new ArrayList<>();
        this.recommendationEngine = new RecommendationEngine(this.allMovies);

	}
	private <T> ArrayList<T> arrayListOf(T... elements) {
        return new ArrayList(Arrays.asList(elements));
    }

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	@DisplayName("(BVA) no Movie list")
	void No_movie_list() {
		User user=new User("Bobby","12345678A",this.arrayListOf("TSR101","TM101"));
		Movie existing_Movie = new Movie("The Shawshank Redemption", "TSR101", this.arrayListOf("Drama", "Romance"));
		Movie existing_Movie2 =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		allMovies.add(existing_Movie);
		allMovies.add(existing_Movie2);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.isEmpty());
	}
	
	@Test
	@DisplayName("(BVA) Liked all Movies")
	void Liked_all_movies() {
		User user=new User("Bobby","12345678A",this.arrayListOf("I101"));
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.isEmpty());
	}
	
	
	@Test
	@DisplayName("(ECP) Liking Existing films")
	void Liking_existing_films() {
		User user=new User("Mike Tyson","12345678A",this.arrayListOf("I101"));
		Movie existing_movie =new Movie("Inception","I101",this.arrayListOf("Drama","Sci_Fi"));
		Movie recommended_Movie = new Movie("The Shawshank Redemption", "TSR201", this.arrayListOf("Drama", "Romance"));
		Movie unrelated_movie = new Movie("Titanic","T301",this.arrayListOf("Romance"));
		
		allMovies.add(existing_movie);
		allMovies.add(recommended_Movie);
		allMovies.add(unrelated_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.contains("The Shawshank Redemption"));
	}
	
	@Test
	@DisplayName("(ECP) Liking Non-Existing films")
	void Liking_non_existing_films() {
		User user=new User("Carl Jhonson","12345678A",this.arrayListOf("XX101"));
		Movie existing_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		Movie recommended_Movie = new Movie("Interstellar", "I201", this.arrayListOf("Adventure","Sci-Fi"));
		Movie unrelated_movie = new Movie("Titanic","T301",this.arrayListOf("Romance"));
		
		allMovies.add(existing_movie);
		allMovies.add(recommended_Movie);
		allMovies.add(unrelated_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.isEmpty());
	}
	
	@Test
	@DisplayName("(ECP) Liking Mixed films")
	void Liking_Mixed_films() {
		User user=new User("Carl Max","12345678A",this.arrayListOf("XX523","TSR201","I201"));
		Movie Liked_Sci_fi_Movie = new Movie("Interstellar", "I201", this.arrayListOf("Adventure","Sci-Fi"));
		Movie recommended_Sci_fi_movie =new Movie("Inception","I101",this.arrayListOf("Sci-Fi"));
		Movie Liked_Romance_Movie = new Movie("The Shawshank Redemption", "TSR201", this.arrayListOf("Drama", "Romance"));
		Movie recommended_Romance_movie = new Movie("Titanic","T301",this.arrayListOf("Romance"));
		Movie unrelated_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		
		allMovies.add(Liked_Sci_fi_Movie);
		allMovies.add(recommended_Sci_fi_movie);
		allMovies.add(Liked_Romance_Movie);
		allMovies.add(recommended_Romance_movie);
		allMovies.add(unrelated_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.contains("Inception"));
		assertTrue(recommendations.contains("Titanic"));
	}
	
	@Test
	@DisplayName("State Transtion Testing liked genre added later")
	void State_Transtion_Testing_Liked_genre_added_later() {
		User user=new User("Ahmed salim","12345678A",this.arrayListOf("TM101"));
		Movie Sci_fi_Movie = new Movie("Interstellar", "I201", this.arrayListOf("Adventure","Sci-Fi"));
		Movie Romance_Movie = new Movie("The Shawshank Redemption", "TSR201", this.arrayListOf("Drama", "Romance"));
		Movie Liked_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		
		allMovies.add(Sci_fi_Movie);
		allMovies.add(Romance_Movie);
		allMovies.add(Liked_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.isEmpty());
		
		Movie Added_later_Movie=new Movie("Tenet","T302",this.arrayListOf("Action","Sci-Fi"));
		allMovies.add(Added_later_Movie);
		
		List<String> recommendations_updated = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations_updated.contains("Tenet"));
	}
	
	@Test
	@DisplayName("State Transtion Testing like all movies")
	void State_Transtion_Testing() {
		User user=new User("Ibrahim salim","12345678A",this.arrayListOf("I201","TSR201","TM101"));
		Movie Sci_fi_Movie = new Movie("Interstellar", "I201", this.arrayListOf("Adventure","Sci-Fi"));
		Movie Romance_Movie = new Movie("The Shawshank Redemption", "TSR201", this.arrayListOf("Drama", "Romance"));
		Movie Liked_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		allMovies.add(Sci_fi_Movie);
		allMovies.add(Romance_Movie);
		allMovies.add(Liked_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertTrue(recommendations.isEmpty());
	}
	
	@Test
	@DisplayName("(DTT & CEG) Liked and Shares Genre")
	void Liked_and_Shares_Genre() {
		User user=new User("Johan Cruyf","12345678A",this.arrayListOf("TSR101"));
		Movie Liked_Movie = new Movie("The Shawshank Redemption", "TSR101", this.arrayListOf("Drama", "Romance"));
		Movie Shared_genre_Movie = new Movie("Interstellar","I102",this.arrayListOf("Drama","Sci-Fi"));
		allMovies.add(Liked_Movie);
		allMovies.add(Shared_genre_Movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertFalse(recommendations.contains("The Shawshank Redemption"));
		assertTrue(recommendations.contains("Interstellar"));
	}
	
	@Test
	@DisplayName("(DTT & CEG) Liked and Doesn't Shares Genre")
	void Liked_and_Doesnt_Shares_Genre() {
		User user=new User("Ahmed Gamal","12345678A",this.arrayListOf("TSR101"));
		Movie Liked_Movie = new Movie("The Shawshank Redemption", "TSR101", this.arrayListOf("Drama", "Romance"));
		Movie Un_related_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));
		
		allMovies.add(Liked_Movie);
		allMovies.add(Un_related_movie);
		
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertFalse(recommendations.contains("The Shawshank Redemption"));
		assertFalse(recommendations.contains("The Matrix"));
	}
	
	@Test
	@DisplayName("(DTT CEG) Not liked and Shares Genre")
	void Not_Liked_and_Shares_Genre() {
		User user=new User("emad","12345678A",this.arrayListOf());
		Movie Liked_Movie = new Movie("The Shawshank Redemption", "TSR101", this.arrayListOf("Drama", "Romance"));
		Movie Shared_genre_Movie = new Movie("Interstellar","I102",this.arrayListOf("Drama","Sci-Fi"));
		
		allMovies.add(Liked_Movie);
		allMovies.add(Shared_genre_Movie);
	
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertFalse(recommendations.contains("The Shawshank Redemption"));
		assertFalse(recommendations.contains("Interstellar"));
	}
	
	@Test
	@DisplayName("(DTT) Not liked and Doesn't shares Genre")
	void Not_Liked_and_Deosnt_shares_Genre() {
		User user=new User("Lenny","12345678A",this.arrayListOf());
		Movie Liked_Movie = new Movie("The Shawshank Redemption", "TSR101", this.arrayListOf("Drama", "Romance"));
		Movie Un_related_movie =new Movie("The Matrix","TM101",this.arrayListOf("Action"));

		allMovies.add(Liked_Movie);
		allMovies.add(Un_related_movie);
	
		List<String> recommendations = this.recommendationEngine.getRecommendationsForUser(user);
		assertFalse(recommendations.contains("The Shawshank Redemption"));
		assertFalse(recommendations.contains("The Matrix"));
		}
	
}