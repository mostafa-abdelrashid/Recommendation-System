/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.system.WhiteBoxTest;

import com.system.User;
import com.system.UserValidator;
import java.util.ArrayList;
import com.system.DataValidationException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author genzoo
 */
public class UserValidatorTest {
    
    public UserValidatorTest() {
    }
    
    /**
     * ADUP Test Case 1: Format Failure Path
     */
    @Test
    public void testValidateUserId_ADUP_InvalidFormat() {
        UserValidator validator = new UserValidator();
        // Setup: User with ID "123" (Too short, fails Regex)
        User invalidUser = new User("Alice", "123", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(invalidUser);
        });

        // Verify we hit the first exception block
        assertTrue(exception.getMessage().contains("User Id 123 is wrong"));
    }
    
    /**
     * ADUP Test Case 2: user already exists
     */
    @Test
    public void testValidateUserId_ADUP_DuplicateId() {
        UserValidator validator = new UserValidator();
        
        // Setup: Add an existing user to the system
        // Valid ID format: 8 digits + 1 char (e.g., "12345678A")
        User existingUser = new User("Bob", "12345678A", new ArrayList<>());
        validator.setUser(existingUser);

        // Create a NEW user with the SAME ID
        User duplicateUser = new User("Charlie", "12345678A", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(duplicateUser);
        });

        // Verify we hit the second exception block
        assertTrue(exception.getMessage().contains("User Id 12345678A is wrong"));
    }
    
    
    /**
    * ADUP Test Case 3: Success Path
    */
    @Test
    public void testValidateUserId_ADUP_ValidAndUnique() {
        UserValidator validator = new UserValidator();
        
        // Setup: System has a different user
        User existingUser = new User("Bob", "12345678A", new ArrayList<>());
        validator.setUser(existingUser);

        // Create a NEW user with a UNIQUE and VALID ID
        User newUser = new User("David", "87654321B", new ArrayList<>());

        // Execute: Should NOT throw exception
        assertDoesNotThrow(() -> {
            validator.validateUserId(newUser);
        });
    }
    
     /**
     * ADUP Test Case 4: matcher fails
     */
    @Test
    public void testValidateUserId_ADUP_InvalidLengthFormat() {
        UserValidator validator = new UserValidator();
        // Setup: User with ID "123" (Too short, fails Regex)
        User invalidUser = new User("Alice", "12345678@", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(invalidUser);
        });

        // Verify we hit the first exception block
        assertTrue(exception.getMessage().contains("User Id 12345678@ is wrong"));
    }
    
}
