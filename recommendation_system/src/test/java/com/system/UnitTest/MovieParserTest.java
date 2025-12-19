package com.system.UnitTest;
import com.system.*;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MovieParserTest {

    private Path tmpFile;

    @BeforeEach
    public void setup() {
        tmpFile = null;
    }

    @AfterEach
    public void cleanup() throws Exception {
        if (tmpFile != null) {
            Files.deleteIfExists(tmpFile);
        }
    }

    @Test
    public void testParseMovies_normal() throws Exception {
        tmpFile = Files.createTempFile("movies_normal", ".txt");

        String content = "The Matrix,TM193\n" +
                "Action, Sci-Fi\n" +
                "Inception,I666\n" +
                "Action, Thriller, Sci-Fi\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(2, movies.size());

        Movie m1 = movies.get(0);
        assertEquals("The Matrix", m1.getTitle());
        assertEquals("TM193", m1.getMovieId());
        assertTrue(m1.getGenres().contains("Action"));
        assertTrue(m1.getGenres().contains("Sci-Fi"));
    }

    @Test
    public void testParseMovies_extraSpaces() throws Exception {
        tmpFile = Files.createTempFile("movies_trim", ".txt");

        String content = "  Parasite  ,  P668  \n" +
                "  Drama ,  Thriller  \n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());

        Movie m = movies.get(0);
        assertEquals("Parasite", m.getTitle());
        assertEquals("P668", m.getMovieId());
        assertTrue(m.getGenres().contains("Drama"));
        assertTrue(m.getGenres().contains("Thriller"));
    }

    @Test
    public void testParseMovies_missingGenreLine() throws Exception {
        tmpFile = Files.createTempFile("movies_odd", ".txt");

        String content = "Frozen,F111\n" +
                "Comedy\n" +
                "Moana,M222\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        assertThrows(DataValidationException.class, () -> Parsing.parseMovies());
    }

    @Test
    public void testParseMovies_fileNotFound() {
        Parsing.setMovieFile("path/to/not/exist.txt");

        try {
            ArrayList<Movie> movies = Parsing.parseMovies();
            assertNotNull(movies);
            assertEquals(0, movies.size());
        } catch (DataValidationException e) {
            fail("Exception should not be thrown for file not found");
        }
    }

    @Test
    public void testParseMovies_blankLineBetweenMovies() throws Exception {
        tmpFile = Files.createTempFile("movies_blank", ".txt");

        String content = "Frozen,F111\n" +
                "Action,Comedy\n" +
                "\n" +
                "Moana,M222\n" +
                "Drama,Thriller\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(2, movies.size(), "Parser skips blank lines and parses both movies");

        Movie m1 = movies.get(0);
        assertEquals("Frozen", m1.getTitle());
        assertEquals("F111", m1.getMovieId());
        
        Movie m2 = movies.get(1);
        assertEquals("Moana", m2.getTitle());
        assertEquals("M222", m2.getMovieId());
    }

}
