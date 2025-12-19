package com.system.BlackBoxTest;

import com.system.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class OutputWriterTest {

    private static Path tempFile;
    private RecommendationEngine engine;
    private User user1, user2, user3;


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        tempFile = Files.createTempFile("rec_test_", ".txt");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    @BeforeEach
    void setUp() {
        engine = mock(RecommendationEngine.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.write(tempFile, new byte[0]);
    }

    // writeRecommendationsToFile


    @Test
    void singleUserScenario() throws IOException {
        user1 = new User("John Smith", "12345678A",
                new ArrayList<>(Arrays.asList("TM093")));

        when(engine.getRecommendationsForUser(user1))
                .thenReturn(Arrays.asList("John Wick", "Wanted"));

        OutputWriter.writeRecommendationsToFile(
                List.of(user1), engine, tempFile.toString());

        verify(engine, times(1)).getRecommendationsForUser(user1);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("John Wick,Wanted", lines.get(1));
    }


    @Test
    void multipleUsersScenario() throws IOException {
        user1 = new User("John Smith", "12345678A",
                new ArrayList<>(Arrays.asList("TM093")));
        user2 = new User("Alex Brown", "87654321B",
                new ArrayList<>(Arrays.asList("I666")));
        user3 = new User("Ahmed Hassan", "11223344C",
                new ArrayList<>());

        when(engine.getRecommendationsForUser(user1))
                .thenReturn(Arrays.asList("The Matrix", "Inception"));
        when(engine.getRecommendationsForUser(user2))
                .thenReturn(Collections.singletonList("Gladiator"));
        when(engine.getRecommendationsForUser(user3))
                .thenReturn(Collections.emptyList());

        OutputWriter.writeRecommendationsToFile(
                Arrays.asList(user1, user2, user3),
                engine, tempFile.toString());

        verify(engine, times(1)).getRecommendationsForUser(user1);
        verify(engine, times(1)).getRecommendationsForUser(user2);
        verify(engine, times(1)).getRecommendationsForUser(user3);

        List<String> lines = Files.readAllLines(tempFile);

        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("The Matrix,Inception", lines.get(1));

        assertEquals("Alex Brown,87654321B", lines.get(2));
        assertEquals("Gladiator", lines.get(3));

        assertEquals("Ahmed Hassan,11223344C", lines.get(4));
        assertEquals("No recommendations available", lines.get(5));
    }


    @Test
    void testFileOverwriteBehavior() throws IOException {
        User u1 = new User("Alice Johnson", "12345678A", new ArrayList<>());
        User u2 = new User("Bob Williams", "87654321B", new ArrayList<>());

        when(engine.getRecommendationsForUser(u1))
                .thenReturn(Collections.singletonList("The Matrix"));
        when(engine.getRecommendationsForUser(u2))
                .thenReturn(Collections.singletonList("Inception"));

        OutputWriter.writeRecommendationsToFile(
                List.of(u1), engine, tempFile.toString());

        OutputWriter.writeRecommendationsToFile(
                List.of(u2), engine, tempFile.toString());

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals("Bob Williams,87654321B", lines.get(0));
        assertEquals("Inception", lines.get(1));
    }


    @Test
    void testEmptyUsersList() throws IOException {
        OutputWriter.writeRecommendationsToFile(
                Collections.emptyList(), engine, tempFile.toString());

        List<String> lines = Files.readAllLines(tempFile);
        assertTrue(lines.isEmpty());

        verify(engine, never()).getRecommendationsForUser(any());
    }


    @Test
    void testNullUsersList_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                OutputWriter.writeRecommendationsToFile(
                        null, engine, tempFile.toString()));
    }


    @Test
    void testNullRecommendationEngine_ThrowsException() {
        user1 = new User("John Smith", "12345678A", new ArrayList<>());

        assertThrows(NullPointerException.class, () ->
                OutputWriter.writeRecommendationsToFile(
                        List.of(user1), null, tempFile.toString()));
    }

    // writeErrorToFile


    @Test
    void testNormalErrorMessage() throws IOException {
        String errorMsg = "Something went wrong!";
        OutputWriter.writeErrorToFile(errorMsg, tempFile.toString());

        String content = Files.readString(tempFile);
        assertEquals(errorMsg, content);
    }


    @Test
    void testEmptyErrorMessage() throws IOException {
        OutputWriter.writeErrorToFile("", tempFile.toString());

        String content = Files.readString(tempFile);
        assertEquals("", content);
    }

    @Test
    void testNullErrorMessage_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
                OutputWriter.writeErrorToFile(null, tempFile.toString()));
    }


    void testInvalidFilePath() {
        String invalidPath = "/invalid_dir/error.txt";

        assertDoesNotThrow(() ->
                OutputWriter.writeErrorToFile("Error", invalidPath));
    }
}
