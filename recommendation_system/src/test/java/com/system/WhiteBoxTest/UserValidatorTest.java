package com.system.WhiteBoxTest;

import com.system.User;
import com.system.UserValidator;
import java.util.ArrayList;
import com.system.DataValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    
    public UserValidatorTest() {
    }
    
    @Test
    public void testValidateUserId_ADUP_InvalidFormat() {
        UserValidator validator = new UserValidator();
        User invalidUser = new User("Alice", "123", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(invalidUser);
        });

        assertTrue(exception.getMessage().contains("User Id 123 is wrong"));
    }
    
    @Test
    public void testValidateUserId_ADUP_DuplicateId() {
        UserValidator validator = new UserValidator();
        
        User existingUser = new User("Bob", "12345678A", new ArrayList<>());
        validator.setUser(existingUser);

        User duplicateUser = new User("Charlie", "12345678A", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(duplicateUser);
        });

        assertTrue(exception.getMessage().contains("User Id 12345678A is wrong"));
    }
    
    @Test
    public void testValidateUserId_ADUP_ValidAndUnique() {
        UserValidator validator = new UserValidator();
        
        User existingUser = new User("Bob", "12345678A", new ArrayList<>());
        validator.setUser(existingUser);

        User newUser = new User("David", "87654321B", new ArrayList<>());

        assertDoesNotThrow(() -> {
            validator.validateUserId(newUser);
        });
    }
    
    @Test
    public void testValidateUserId_ADUP_InvalidLengthFormat() {
        UserValidator validator = new UserValidator();
        User invalidUser = new User("Alice", "12345678@", new ArrayList<>());

        Exception exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserId(invalidUser);
        });

        assertTrue(exception.getMessage().contains("User Id 12345678@ is wrong"));
    }
    
}