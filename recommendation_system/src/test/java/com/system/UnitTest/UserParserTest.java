package com.system.UnitTest;
import com.system.*;
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
                "Alice Johnson,12345678A\n" +
                "TM193,I666\n" +
                "Bob Smith,87654321B\n" +
                "MO111,MT222\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(2, users.size());

        User u1 = users.get(0);
        assertEquals("Alice Johnson", u1.getUserName());
        assertEquals("12345678A", u1.getUserId());
        assertTrue(u1.getLikedMovieIds().contains("TM193"));
        assertTrue(u1.getLikedMovieIds().contains("I666"));
    }

    @Test
    public void testParseUsers_extraSpacesAndTrailingComma_trimmed() throws Exception {
        tmpFile = Files.createTempFile("users_trim", ".txt");

        String content =
                "  Karim Ahmed  ,  12345678X  \n" +
                "  TM193 ,  I666  \n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(1, users.size());

        User u = users.get(0);
        assertEquals("Karim Ahmed", u.getUserName());
        assertEquals("12345678X", u.getUserId());
        assertTrue(u.getLikedMovieIds().contains("TM193"));
        assertTrue(u.getLikedMovieIds().contains("I666"));
    }

    @Test
    public void testParseUsers_malformedNameLine_throwsException() throws Exception {
        tmpFile = Files.createTempFile("users_malformed", ".txt");

        String content =
                "BadLineWithoutComma\n" +
                "TM193,I666\n" +
                "David Miller,12345678G\n" +
                "MO111\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        assertThrows(DataValidationException.class, () -> Parsing.parseUsers());
    }

    @Test
    public void testParseUsers_oddNumberOfLines_ignoresLastIncompleteRecord() throws Exception {
        tmpFile = Files.createTempFile("users_odd", ".txt");

        String content =
                "Emma Watson,12345678A\n" +
                "TM193\n" +
                "Chris Evans,87654321B\n"; 

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        assertThrows(DataValidationException.class, () -> Parsing.parseUsers());
    }

    @Test
    public void testParseUsers_blankLineBetweenUsers() throws Exception {
        tmpFile = Files.createTempFile("users_blank", ".txt");

        String content =
                "Tom Holland,12345678A\n" +
                "TM193,I666\n" +
                "\n" +                     
                "Zendaya Coleman,87654321B\n" +
                "MO111,MT222\n";

        Files.writeString(tmpFile, content);

        Parsing.setUserFile(tmpFile.toString());

        ArrayList<User> users = Parsing.parseUsers();

        assertEquals(2, users.size(), "Parser skips blank lines and parses both users");

        assertEquals("Tom Holland", users.get(0).getUserName());
        assertEquals("12345678A", users.get(0).getUserId());
        
        assertEquals("Zendaya Coleman", users.get(1).getUserName());
        assertEquals("87654321B", users.get(1).getUserId());
    }

    @Test
    public void testParseUsers_fileNotFound_returnsEmptyList() {
        Parsing.setUserFile("path/to/not_exist_123.txt");
        try{
            ArrayList<User> users = Parsing.parseUsers();
            assertNotNull(users);
            assertEquals(0, users.size());
        }
        catch (DataValidationException e) {
            fail("Exception should not be thrown for file not found");
        }

    }
}
