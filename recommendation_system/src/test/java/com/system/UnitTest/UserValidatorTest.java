package com.system.UnitTest;
import com.system.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    UserValidator v;

    @AfterEach
    void tearDown() {
        v = null;
        System.out.println("TEARDOWN DONE");
    }

    @Test
    void test_UserName_0() {
        User user = new User("Ahmed Ali", "12345678A", new ArrayList<>());

        v = new UserValidator();

        assertDoesNotThrow(() -> v.validateUserName(user));
    }

    @Test
    void test_UserName_1() {
        User user = new User("A", "12345678A", new ArrayList<>());

        v = new UserValidator();

        assertDoesNotThrow(() -> v.validateUserName(user));
    }

    @Test
    void test_UserName_2() {
        String name = "Ahmed123";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_3() {
        String name = " Ahmed";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_4() {
        String name = "Ahmed@Mah";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_5() {
        String name = "Ahmed  Ali";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_6() {
        String name = "Ahmed ";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_7() {
        String name = "";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_8() {
        String name = "Ahmed-Mah";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserName_9() {
        String name = "O'Ahmed";
        User user = new User(name, "12345678A", new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserName(user));

        assertEquals("ERROR: User Name " + name + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_0() {
        User user = new User("Ahmed", "123456789", new ArrayList<>());

        v = new UserValidator();

        assertDoesNotThrow(() -> v.validateUserId(user));
    }

    @Test
    void test_UserId_1() {
        User user = new User("Ahmed", "12345678A", new ArrayList<>());

        v = new UserValidator();

        assertDoesNotThrow(() -> v.validateUserId(user));
    }

    @Test
    void test_UserId_2() {
        String id = "12345A";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_3() {
        String id = "1234567890";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_4() {
        String id = "1234567$A";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_5() {
        String id = "1234567AB";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_6() {
        String id = "123DE678C";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_7() {
        String id = "A23456789";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_8() {
        String id = "12345678A";
        User user1 = new User("Ahmed", id, new ArrayList<>());
        User user2 = new User("Ali", id, new ArrayList<>());

        v = new UserValidator();
        v.setUser(user1);

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user2));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void test_UserId_9() {
        String id = "";
        User user = new User("Ahmed", id, new ArrayList<>());

        v = new UserValidator();

        DataValidationException ex =
                assertThrows(DataValidationException.class, () -> v.validateUserId(user));

        assertEquals("ERROR: User Id " + id + " is wrong", ex.getMessage());
    }

    @Test
    void isUserIdUnique_Unique_ReturnsTrue() {
        v = new UserValidator();
        User user = new User("Alice", "12345678A", new ArrayList<>());
        v.setUser(user);
        assertTrue(v.isUserIdUnique("12345678B"));
    }

    @Test
    void isUserIdUnique_Duplicate_ReturnsFalse() {
        v = new UserValidator();
        User user = new User("Bob", "12345678A", new ArrayList<>());
        v.setUser(user);
        assertFalse(v.isUserIdUnique("12345678A"));
    }

    @Test
    void isUserIdUnique_EmptyList_ReturnsTrue() {
        v = new UserValidator();
        assertTrue(v.isUserIdUnique("newUserId"));
    }

    @Test
    void isUserIdUnique_NullId_ReturnsTrue() {
        v = new UserValidator();
        assertTrue(v.isUserIdUnique(null));
    }
}
