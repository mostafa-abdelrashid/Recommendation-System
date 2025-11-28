package com.system;

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

        String content = "The Matrix,tt0133093\n" +
                "Action, Sci-Fi\n" +
                "Inception,tt1375666\n" +
                "Action, Thriller, Sci-Fi\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(2, movies.size());

        Movie m1 = movies.get(0);
        assertEquals("The Matrix", m1.getTitle());
        assertEquals("tt0133093", m1.getMovieId());
        assertTrue(m1.getGenres().contains("Action"));
        assertTrue(m1.getGenres().contains("Sci-Fi"));
    }

    @Test
    public void testParseMovies_extraSpaces() throws Exception {
        tmpFile = Files.createTempFile("movies_trim", ".txt");

        String content = "  Parasite  ,  tt6751668  \n" +
                "  Drama ,  Thriller  ,  \n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());

        Movie m = movies.get(0);
        assertEquals("Parasite", m.getTitle());
        assertEquals("tt6751668", m.getMovieId());
        assertTrue(m.getGenres().contains("Drama"));
        assertTrue(m.getGenres().contains("Thriller"));
    }

    @Test
    public void testParseMovies_missingGenreLine() throws Exception {
        tmpFile = Files.createTempFile("movies_odd", ".txt");

        String content = "Movie One,tt1111111\n" +
                "Comedy\n" +
                "Movie Two,tt2222222\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());
        assertEquals("Movie One", movies.get(0).getTitle());
    }

    @Test
    public void testParseMovies_fileNotFound() {
        Parsing.setMovieFile("path/to/not/exist.txt");

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertNotNull(movies);
        assertEquals(0, movies.size());
    }

    @Test
    public void testParseMovies_blankLineBetweenMovies() throws Exception {
        tmpFile = Files.createTempFile("movies_blank", ".txt");

        String content = "Movie One,tt111\n" +
                "Action,Comedy\n" +
                "\n" + 
                "Movie Two,tt222\n" +
                "Drama,Thriller\n";

        Files.writeString(tmpFile, content);

        Parsing.setMovieFile(tmpFile.toString());

        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(2, movies.size(), "Expected 2 movies even with blank line");

        Movie m1 = movies.get(0);
        assertEquals("Movie One", m1.getTitle());
        assertEquals("tt111", m1.getMovieId());

        Movie m2 = movies.get(1);
        assertEquals("Movie Two", m2.getTitle());
        assertEquals("tt222", m2.getMovieId());
    }

}
