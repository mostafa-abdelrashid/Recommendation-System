package com.system.BlackBoxTest;
import static org.junit.jupiter.api.Assertions.*;
import com.system.*;

import java.io.*;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


public class Parsing_bbt {
    
    private final String MOVIE_FILE = "movies_test.txt";
    private final String USER_FILE  = "users_test.txt";

    // ---------- helper ----------
    private void writeToFile(String path, String content) throws IOException {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
        }
    }

    @AfterEach
    void tearDown() {
        new File(MOVIE_FILE).delete();
        new File(USER_FILE).delete();
    }

    // =================================================

    @Test
    void BVA_Movies_EmptyFile() throws Exception {
        writeToFile(MOVIE_FILE, "");
        Parsing.setMovieFile(MOVIE_FILE);

        ArrayList<Movie> movies = Parsing.parseMovies();
        assertEquals(0, movies.size());
    }

    @Test
    void BVA_Movies_MinimumValidInput() throws Exception {
        writeToFile(MOVIE_FILE,
                "Shutter Island, SI123\nThriller,Action\n");

        Parsing.setMovieFile(MOVIE_FILE);
        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());
        assertEquals("Shutter Island", movies.get(0).getTitle());
        assertEquals("SI123", movies.get(0).getMovieId());
    }
     @Test
    void BVA_Movies_Blankline() throws Exception {
        writeToFile(MOVIE_FILE,
                "Shutter Island, SI123\nThriller,Action\n\nConjuring,C456\nHorror ");

        Parsing.setMovieFile(MOVIE_FILE);
        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(2, movies.size());
        assertEquals("Shutter Island", movies.get(0).getTitle());
        assertEquals("SI123", movies.get(0).getMovieId());
        assertEquals("Thriller", movies.get(0).getGenres().get(0));
        assertEquals("Action", movies.get(0).getGenres().get(1));
        assertEquals("Conjuring", movies.get(1).getTitle());
        assertEquals("C456", movies.get(1).getMovieId());

    }
    @Test
    void BVA_Movies_multiple_movies() throws Exception {
        writeToFile(MOVIE_FILE,
                "Shutter Island, SI123\nThriller,Action\n Conjuring, C456\nHorror,Thriller\nWe Are The Millers, WATM789\nComedy,Adventure\n");

        Parsing.setMovieFile(MOVIE_FILE);
        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(3, movies.size());
        assertEquals("Shutter Island", movies.get(0).getTitle());
        assertEquals("SI123", movies.get(0).getMovieId());
        assertEquals("Thriller", movies.get(0).getGenres().get(0));
        assertEquals("Action", movies.get(0).getGenres().get(1));
        assertEquals("Conjuring", movies.get(1).getTitle());
        assertEquals("C456", movies.get(1).getMovieId());
        assertEquals("Horror", movies.get(1).getGenres().get(0));
        assertEquals("Thriller", movies.get(1).getGenres().get(1));
        assertEquals("We Are The Millers", movies.get(2).getTitle());
        assertEquals("WATM789", movies.get(2).getMovieId());
        assertEquals("Comedy", movies.get(2).getGenres().get(0));
        assertEquals("Adventure", movies.get(2).getGenres().get(1));
    }
    
    @Test
    void BVA_Users_empty_file() throws Exception {
        writeToFile(USER_FILE, "");
        Parsing.setUserFile(USER_FILE);
        ArrayList<User> users = Parsing.parseUsers();
        assertEquals(0, users.size());
    }
      @Test
    void BVA_Users_min_valid() throws Exception {
        writeToFile(USER_FILE, "Ahmed Ali, 12345678A\n TC123");
        Parsing.setUserFile(USER_FILE);
        ArrayList<User> users = Parsing.parseUsers();
        assertEquals(1, users.size());
        assertEquals( "Ahmed Ali", users.get(0).getUserName());
        assertEquals("12345678A", users.get(0).getUserId());
        assertEquals("TC123", users.get(0).getLikedMovieIds().get(0));
    }
     @Test
    void BVA_Users_blankline_lead() throws Exception {
        writeToFile(USER_FILE, "\nAhmed Ali, 12345678A\n TC123");
        Parsing.setUserFile(USER_FILE);
        ArrayList<User> users = Parsing.parseUsers();
        assertEquals(1, users.size());
        assertEquals( "Ahmed Ali", users.get(0).getUserName());
        assertEquals("12345678A", users.get(0).getUserId());
        assertEquals("TC123", users.get(0).getLikedMovieIds().get(0));
    }
  @Test
    void BVA_Users_blankline_multple_Users() throws Exception {
        writeToFile(USER_FILE, "Ahmed Ali, 12345678A\n TC123\n\nSara Khan, 87654321B\n JP456\n Mohamed Noor, 11223344C\n AC789");
        Parsing.setUserFile(USER_FILE);
        ArrayList<User> users = Parsing.parseUsers();
        assertEquals(3, users.size());
        assertEquals( "Ahmed Ali", users.get(0).getUserName());
        assertEquals("12345678A", users.get(0).getUserId());
        assertEquals("TC123", users.get(0).getLikedMovieIds().get(0));
        assertEquals( "Sara Khan", users.get(1).getUserName());
        assertEquals("87654321B", users.get(1).getUserId());
        assertEquals("JP456", users.get(1).getLikedMovieIds().get(0));
        assertEquals( "Mohamed Noor", users.get(2).getUserName());
        assertEquals("11223344C", users.get(2).getUserId());
        assertEquals("AC789", users.get(2).getLikedMovieIds().get(0));
    }
        //======================================

    @Test
    void EP_Movies_Valid_1_EquivalenceClass() throws Exception {
        writeToFile(MOVIE_FILE,
                "The Conjuring, TC123\nHorror\n");

        Parsing.setMovieFile(MOVIE_FILE);
        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());
        assertEquals("The Conjuring", movies.get(0).getTitle());
        assertEquals("TC123", movies.get(0).getMovieId());
        assertEquals("Horror", movies.get(0).getGenres().get(0));
    }
     @Test
    void EP_Movies_Valid_2_EquivalenceClass() throws Exception {
        writeToFile(MOVIE_FILE,
                "Jurassic Park The Final Act, JPTFA598\nAction,Adventure\n");

        Parsing.setMovieFile(MOVIE_FILE);
        ArrayList<Movie> movies = Parsing.parseMovies();

        assertEquals(1, movies.size());
        assertEquals("Jurassic Park The Final Act", movies.get(0).getTitle());
        assertEquals("JPTFA598", movies.get(0).getMovieId());
        assertEquals("Action", movies.get(0).getGenres().get(0));
        assertEquals("Adventure", movies.get(0).getGenres().get(1));
    }

    @Test
    void EP_Movies_InvalidMovieTitle() throws Exception {
        writeToFile(MOVIE_FILE,
                "the Conjuring, TC123\nHorror\n");

        Parsing.setMovieFile(MOVIE_FILE);

        assertThrows(DataValidationException.class,
                Parsing::parseMovies);
    }
    @Test
    void EP_Movies_InvalidMovieId() throws Exception {
        writeToFile(MOVIE_FILE,
                "The Conjuring, T123\nHorror\n");

        Parsing.setMovieFile(MOVIE_FILE);

        assertThrows(DataValidationException.class,
                Parsing::parseMovies);
    }
      @Test
    void EP_Movies_Invalidmissing_coma() throws Exception {
        writeToFile(MOVIE_FILE,
                "The Conjuring T123\nHorror\n");

        Parsing.setMovieFile(MOVIE_FILE);

        assertThrows(DataValidationException.class,
                Parsing::parseMovies);
    }
      @Test
    void EP_Movies_Invalid_missing_genre_list() throws Exception {
        writeToFile(MOVIE_FILE,
                "The Conjuring T123");

        Parsing.setMovieFile(MOVIE_FILE);

        assertThrows(DataValidationException.class,
                Parsing::parseMovies);
    }
       @Test
    void EP_Users_valid() throws Exception {
         writeToFile(USER_FILE, "Ahmed Ali, 12345678A\n TC123");
        Parsing.setUserFile(USER_FILE);
        ArrayList<User> users = Parsing.parseUsers();
        assertEquals(1, users.size());
        assertEquals( "Ahmed Ali", users.get(0).getUserName());
        assertEquals("12345678A", users.get(0).getUserId());
        assertEquals("TC123", users.get(0).getLikedMovieIds().get(0));
    }
      @Test
    void EP_Users_Invalid_ID() throws Exception {
         writeToFile(USER_FILE, "Ahmed Ali, 1234568A\n TC123");
        Parsing.setUserFile(USER_FILE);
    
        assertThrows(DataValidationException.class,
                Parsing::parseUsers);
        }
    @Test
    void EP_Users_Invalid_missing_liked_list() throws Exception {
         writeToFile(USER_FILE, "Ahmed Ali, 1234568A\n");
        Parsing.setUserFile(USER_FILE);
    
        assertThrows(DataValidationException.class,
                Parsing::parseUsers);
        }

    }


   

  