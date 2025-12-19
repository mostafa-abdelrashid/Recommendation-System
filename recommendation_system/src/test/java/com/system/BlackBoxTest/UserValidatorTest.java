package com.system.BlackBoxTest;

import com.system.User;
import com.system.UserValidator;
import com.system.DataValidationException;

import java.util.ArrayList; // <--- ADDED THIS IMPORT

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UserValidator();
    }

    // --- 1. EQUIVALENCE PARTITIONING ---

    @Test
    @DisplayName("EP: Valid User Name (Alphabets + Space)")
    void testEp_ValidUserName() {
        // Added 'new ArrayList<String>()' as the 3rd argument
        User user = new User("John Doe", "12345678A", new ArrayList<String>());
        assertDoesNotThrow(() -> validator.validateUserName(user));
    }

    @Test
    @DisplayName("EP: Invalid User Name (Contains Numbers)")
    void testEp_InvalidUserName_Numbers() {
        User user = new User("John1", "12345678A", new ArrayList<String>());
        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
            validator.validateUserName(user);
        });
        assertTrue(exception.getMessage().contains("User Name"));
    }

    @Test
    @DisplayName("EP: Invalid User Name (Special Characters)")
    void testEp_InvalidUserName_SpecialChars() {
        User user = new User("John@Doe", "12345678A", new ArrayList<String>());
        assertThrows(DataValidationException.class, () -> validator.validateUserName(user));
    }

    @Test
    @DisplayName("EP: Valid User ID (8 Digits + 1 Letter)")
    void testEp_ValidUserId() {
        User user = new User("John", "12345678A", new ArrayList<String>());
        assertDoesNotThrow(() -> validator.validateUserId(user));
    }

    // --- 2. BOUNDARY VALUE ANALYSIS ---

    @Test
    @DisplayName("BVA: User ID Length Lower Boundary (8 chars) -> Fail")
    void testBva_UserId_LengthMinusOne() {
        User user = new User("John", "12345678", new ArrayList<String>()); // 8 chars
        assertThrows(DataValidationException.class, () -> validator.validateUserId(user));
    }

    @Test
    @DisplayName("BVA: User ID Length Exact Boundary (9 chars) -> Pass")
    void testBva_UserId_LengthExact() {
        User user = new User("John", "123456789", new ArrayList<String>()); // 9 chars
        assertDoesNotThrow(() -> validator.validateUserId(user));
    }

    @Test
    @DisplayName("BVA: User ID Length Upper Boundary (10 chars) -> Fail")
    void testBva_UserId_LengthPlusOne() {
        User user = new User("John", "1234567890", new ArrayList<String>()); // 10 chars
        assertThrows(DataValidationException.class, () -> validator.validateUserId(user));
    }

    // --- 3. STATE TRANSITION ---

    @Test
    @DisplayName("State Transition: ID Valid initially, Invalid after adding (Duplicate)")
    void testStateTransition_DuplicateId() throws DataValidationException {
        String testId = "99999999X";
        // Passing empty lists for both users
        User user1 = new User("Alice", testId, new ArrayList<String>());
        User user2 = new User("Bob", testId, new ArrayList<String>());

        // State 1: List is empty. ID is unique. Validation passes.
        assertDoesNotThrow(() -> validator.validateUserId(user1));

        // TRANSITION: Add user to the system
        validator.setUser(user1);

        // State 2: List contains ID. ID is NOT unique. Validation fails.
        assertThrows(DataValidationException.class, () -> validator.validateUserId(user2));
    }

    // --- 4. DECISION TABLE ---

    @Test
    @DisplayName("Decision Table: Format Wrong (Regex Fail)")
    void testDecisionTable_Rule1_FormatWrong() {
        User user = new User("John", "ABCDEFGH", new ArrayList<String>()); // Letters at start (Wrong format)
        assertThrows(DataValidationException.class, () -> validator.validateUserId(user));
    }

    @Test
    @DisplayName("Decision Table: Format OK, Unique False")
    void testDecisionTable_Rule2_NotUnique() {
        User existingUser = new User("Existing", "11111111A", new ArrayList<String>());
        validator.setUser(existingUser); // Set up state

        User newUser = new User("New", "11111111A", new ArrayList<String>()); // Same ID
        assertThrows(DataValidationException.class, () -> validator.validateUserId(newUser));
    }

    @Test
    @DisplayName("Decision Table: Format OK, Unique True")
    void testDecisionTable_Rule3_HappyPath() {
        User user = new User("John", "22222222B", new ArrayList<String>());
        assertDoesNotThrow(() -> validator.validateUserId(user));
    }

    // --- 5. CAUSE-EFFECT ---

    @Test
    @DisplayName("Cause-Effect: Cause (Invalid Name) -> Effect (Exception Name Error)")
    void testCauseEffect_InvalidName() {
        User user = new User("John1", "12345678A", new ArrayList<String>());
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateUserName(user));
        assertTrue(ex.getMessage().contains("User Name"));
    }

    @Test
    @DisplayName("Cause-Effect: Cause (Invalid ID) -> Effect (Exception ID Error)")
    void testCauseEffect_InvalidId() {
        User user = new User("John", "12345", new ArrayList<String>());
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateUserId(user));
        assertTrue(ex.getMessage().contains("User Id"));
    }

    @Test
    @DisplayName("Cause-Effect: Cause (Valid Inputs) -> Effect (No Exception)")
    void testCauseEffect_Success() {
        User user = new User("John", "55555555Z", new ArrayList<String>());
        assertDoesNotThrow(() -> {
            validator.validateUserName(user);
            validator.validateUserId(user);
        });
    }
}