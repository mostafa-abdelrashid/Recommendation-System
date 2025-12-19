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
		user1 = new User("John Smith", "12345678A",new ArrayList<>(Arrays.asList("TM193")));
		
		when(engine.getRecommendationsForUser(user1))
        .thenReturn(Arrays.asList("John Wick","Wanted"));
		
		OutputWriter.writeRecommendationsToFile(List.of(user1),engine,tempFile.toString());
		
		verify(engine, times(1)).getRecommendationsForUser(user1);
		
		List<String> lines = Files.readAllLines(tempFile);
		System.out.println("FILE CONTENT:");
		for (int i = 0; i < lines.size(); i++) {
		    System.out.println(i + ": [" + lines.get(i) + "]");
		}
        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("John Wick,Wanted", lines.get(1));
	}
	
	@Test
	void Multiple_users_Scenario() throws IOException{
		user1 = new User("John Smith", "12345678A",new ArrayList<>(Arrays.asList("TM193")));
        user2 = new User("Alex Brown", "87654321B",new ArrayList<>(Arrays.asList("I666")));
        user3 = new User("Ahmed Hassan", "11223344C",new ArrayList<>());
        
		when(engine.getRecommendationsForUser(user1))
        .thenReturn(Arrays.asList("The Matrix","Inception"));
		
		when(engine.getRecommendationsForUser(user2))
        .thenReturn(Arrays.asList("Gladiator"));
		
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
        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("The Matrix,Inception", lines.get(1));
        
        assertEquals("Alex Brown,87654321B",lines.get(2));
        assertEquals("Gladiator",lines.get(3));
        
        assertEquals("Ahmed Hassan,11223344C",lines.get(4));
        assertEquals("No recommendations available",lines.get(5));
	}
	
	@Test
	void testFileOverwriteBehavior() throws IOException {
	    User u1 = new User("Alice Johnson", "12345678A", new ArrayList<>());
	    User u2 = new User("Bob Williams", "87654321B", new ArrayList<>());

	    when(engine.getRecommendationsForUser(u1))
	            .thenReturn(Collections.singletonList("The Matrix"));
	    when(engine.getRecommendationsForUser(u2))
	            .thenReturn(Collections.singletonList("Inception"));
	    
	    OutputWriter.writeRecommendationsToFile(List.of(u1), engine, tempFile.toString());
	    List<String> lines1 = Files.readAllLines(tempFile);
	    for (int i = 0; i < lines1.size(); i++) {
		    System.out.println(i + ": [" + lines1.get(i) + "]");
		}
	    assertEquals("Alice Johnson,12345678A", lines1.get(0));
	    assertEquals("The Matrix", lines1.get(1));

	    OutputWriter.writeRecommendationsToFile(List.of(u2), engine, tempFile.toString());
	    List<String> lines2 = Files.readAllLines(tempFile);
	    for (int i = 0; i < lines2.size(); i++) {
		    System.out.println(i + ": [" + lines2.get(i) + "]");
		}
	    assertEquals("Bob Williams,87654321B", lines2.get(0));
	    assertEquals("Inception", lines2.get(1));
	}
	
	@Test
    void testNormalErrorMessage() throws IOException {
        String errorMsg = "Something went wrong!";
        OutputWriter.writeErrorToFile(errorMsg, tempFile.toString());

        String content = Files.readString(tempFile);
        assertEquals(errorMsg, content);
    }
	
	 @Test
	    void testNullErrorMessage_ThrowsException() throws IOException {
	        String errorMsg = null;

	        assertThrows(NullPointerException.class, () -> 
	                OutputWriter.writeErrorToFile(errorMsg, tempFile.toString()));
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