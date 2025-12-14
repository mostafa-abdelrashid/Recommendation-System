package com.system.UnitTest;
import com.system.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;

import org.junit.jupiter.api.*;

class OutputWriterTest {
	
	 private static Path tempFile;
	 private RecommendationEngine engine;
	 private User user1,user2,user3;
	 private 
	    
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Running @BeforeAll");
        tempFile = Files.createTempFile("rec_test_", ".txt");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		 System.out.println("Running @AfterAll");
	     Files.deleteIfExists(tempFile);
	}

	@BeforeEach
	void setUp() throws Exception {
		System.out.println("Running @BeforeEach");
        engine = mock(RecommendationEngine.class);
        
	}

	@AfterEach
	void tearDown() throws Exception {
		 System.out.println("Running @AfterEach");
	     Files.write(tempFile, new byte[0]);
	}

	@Test
	void Single_user_Scenario() throws IOException {
		user1 = new User("Jhon", "1",new ArrayList<>(Arrays.asList("M001")));
		
		when(engine.getRecommendationsForUser(user1))
        .thenReturn(Arrays.asList("Jhon Wick","Wanted"));
		
		OutputWriter.writeRecommendationsToFile(List.of(user1),engine,tempFile.toString());
		
		verify(engine, times(1)).getRecommendationsForUser(user1);
		
		List<String> lines = Files.readAllLines(tempFile);
		System.out.println("FILE CONTENT:");
		for (int i = 0; i < lines.size(); i++) {
		    System.out.println(i + ": [" + lines.get(i) + "]");
		}
        assertEquals("Jhon,1", lines.get(0));
        assertEquals("Jhon Wick,Wanted", lines.get(1));
	}
	
	@Test
	void Multiple_users_Scenario() throws IOException{
		user1 = new User("Jhon", "1",new ArrayList<>(Arrays.asList("M001")));
        user2 = new User("Alex", "2",new ArrayList<>(Arrays.asList("M002")));
        user3 = new User("Ahmed", "311",new ArrayList<>());
        
		when(engine.getRecommendationsForUser(user1))
        .thenReturn(Arrays.asList("Movie 1","Movie 2"));
		
		when(engine.getRecommendationsForUser(user2))
        .thenReturn(Arrays.asList("Movie X"));
		
		when(engine.getRecommendationsForUser(user3))
        .thenReturn(Collections.emptyList());
		
		List<User> users=Arrays.asList(user1,user2,user3);
		OutputWriter.writeRecommendationsToFile(users,engine,tempFile.toString());
		
		verify(engine, times(1)).getRecommendationsForUser(user1);
		verify(engine, times(1)).getRecommendationsForUser(user2);
		verify(engine, times(1)).getRecommendationsForUser(user3);
		
		List<String> lines = Files.readAllLines(tempFile);
		System.out.println("FILE CONTENT:");
		for (int i = 0; i < lines.size(); i++) {
		    System.out.println(i + ": [" + lines.get(i) + "]");
		}
        assertEquals("Jhon,1", lines.get(0));
        assertEquals("Movie 1,Movie 2", lines.get(1));
        
        assertEquals("Alex,2",lines.get(2));
        assertEquals("Movie X",lines.get(3));
        
        assertEquals("Ahmed,311",lines.get(4));
        assertEquals("No recommendations available",lines.get(5));
	}
	
	@Test
	void testFileOverwriteBehavior() throws IOException {
	    User u1 = new User("Alice", "1", new ArrayList<>());
	    User u2 = new User("Bob", "2", new ArrayList<>());

	    when(engine.getRecommendationsForUser(u1))
	            .thenReturn(Collections.singletonList("Movie1"));
	    when(engine.getRecommendationsForUser(u2))
	            .thenReturn(Collections.singletonList("Movie2"));
	    
	    
	    // First write
	    OutputWriter.writeRecommendationsToFile(List.of(u1), engine, tempFile.toString());
	    List<String> lines1 = Files.readAllLines(tempFile);
	    for (int i = 0; i < lines1.size(); i++) {
		    System.out.println(i + ": [" + lines1.get(i) + "]");
		}
	    assertEquals("Alice,1", lines1.get(0));
	    assertEquals("Movie1", lines1.get(1));

	    // Second write: u2 overwrites file
	    OutputWriter.writeRecommendationsToFile(List.of(u2), engine, tempFile.toString());
	    List<String> lines2 = Files.readAllLines(tempFile);
	    for (int i = 0; i < lines2.size(); i++) {
		    System.out.println(i + ": [" + lines2.get(i) + "]");
		}
	    assertEquals("Bob,2", lines2.get(0));
	    assertEquals("Movie2", lines2.get(1));
	}
	
	@Test
    void testNormalErrorMessage() throws IOException {
        String errorMsg = "Something went wrong!";
        OutputWriter.writeErrorToFile(errorMsg, tempFile.toString());


        String content = Files.readString(tempFile);
        assertEquals(errorMsg, content);
    }
	
	 @Test
	    void testNullErrorMessage() throws IOException {
	        String errorMsg = null;
	        OutputWriter.writeErrorToFile(errorMsg, tempFile.toString());

	        String content = Files.readString(tempFile);

	        assertEquals("null", content);
	    }
	 
	 @Test
	    void testEmptyErrorMessage() throws IOException {
	        String errorMsg = "";
	        OutputWriter.writeErrorToFile(errorMsg, tempFile.toString());

	        String content = Files.readString(tempFile);
	        assertEquals("", content);
	    }
	 
	 @Test
	    void testInvalidFilePath() {
	        String invalidPath = "/invalid_dir/error.txt";

	        assertDoesNotThrow(() ->
	                OutputWriter.writeErrorToFile("Error", invalidPath));
	    }
	 
}