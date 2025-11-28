package com.system;

import org.junit.jupiter.api.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserParserTest {

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
    public void testParseUsers_normal() throws Exception {
        tmpFile = Files.createTempFile("users_normal", ".txt");

        String content =
                "Alice,u001\n" +
                "tt0133093,tt1375666\n" +
                "Bob,u002\n" +
                "tt1111111,tt2222222\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(2, users.size());

        User u1 = users.get(0);
        assertEquals("Alice", u1.getUserName());
        assertEquals("u001", u1.getUserId());
        assertTrue(u1.getLikedMovieIds().contains("tt0133093"));
        assertTrue(u1.getLikedMovieIds().contains("tt1375666"));
    }

    @Test
    public void testParseUsers_extraSpacesAndTrailingComma_trimmed() throws Exception {
        tmpFile = Files.createTempFile("users_trim", ".txt");

        String content =
                "  Karim  ,  u123  \n" +
                "  tt6751668 ,  tt9999999  ,  \n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(1, users.size());

        User u = users.get(0);
        assertEquals("Karim", u.getUserName());
        assertEquals("u123", u.getUserId());
        assertTrue(u.getLikedMovieIds().contains("tt6751668"));
        assertTrue(u.getLikedMovieIds().contains("tt9999999"));
        assertFalse(u.getLikedMovieIds().contains(""));
    }

    @Test
    public void testParseUsers_malformedNameLine_skipsMalformed() throws Exception {
        tmpFile = Files.createTempFile("users_malformed", ".txt");

        String content =
                "BadLineWithoutComma\n" +
                "tt0133093,tt1375666\n" +
                "GoodUser,u010\n" +
                "tt0000001\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertTrue(users.size() >= 1);

        boolean found = false;
        for (User u : users) {
            if ("u010".equals(u.getUserId()) && "GoodUser".equals(u.getUserName())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Valid user record should be parsed even if earlier lines are malformed.");
    }

    @Test
    public void testParseUsers_oddNumberOfLines_ignoresLastIncompleteRecord() throws Exception {
        tmpFile = Files.createTempFile("users_odd", ".txt");

        String content =
                "User One,u111\n" +
                "tt1234567\n" +
                "User Two,u222\n"; 

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(1, users.size());
        assertEquals("User One", users.get(0).getUserName());
    }

    @Test
    public void testParseUsers_blankLineBetweenUsers() throws Exception {
        tmpFile = Files.createTempFile("users_blank", ".txt");

        String content =
                "User A,ua1\n" +
                "tt111,tt222\n" +
                "\n" +                     
                "User B,ub2\n" +
                "tt333,tt444\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(2, users.size(), "Expected two users even with blank line");

        assertEquals("User A", users.get(0).getUserName());
        assertEquals("ua1", users.get(0).getUserId());
        assertEquals("User B", users.get(1).getUserName());
        assertEquals("ub2", users.get(1).getUserId());
    }

    @Test
    public void testParseUsers_fileNotFound_returnsEmptyList() {
        Parsing.setUserFile("path/to/not_exist_123.txt");

        ArrayList<User> users = Parsing.parseUsers();

        assertNotNull(users);
        assertEquals(0, users.size());
    }
}
