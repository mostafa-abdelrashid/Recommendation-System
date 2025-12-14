
package com.system;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private ArrayList<User> allUsers;
    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[A-Za-z]+(?: [A-Za-z]+)*$");

    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[0-9]{8}[A-Za-z0-9]$");

    public UserValidator() {
        allUsers = new ArrayList<User>();
    }

    public void setUser(User user) {
        allUsers.add(user);
    }

    public void validateUserName(User user) throws DataValidationException {
        Matcher matcher = USER_NAME_PATTERN.matcher(user.getUserName());
        if (!matcher.matches()) {
            throw new DataValidationException("ERROR: User Name " + user.getUserName() + " is wrong");
        }
    }

    public void validateUserId(User user) throws DataValidationException {
        Matcher matcher = USER_ID_PATTERN.matcher(user.getUserId());
        if (!matcher.matches() || user.getUserId().length() != 9) {
            throw new DataValidationException("ERROR: User Id " + user.getUserId() + " is wrong");
        }
        if (!isUserIdUnique(user.getUserId())) {
            throw new DataValidationException("ERROR: User Id " + user.getUserId() + " is wrong");
        }
    }

    public boolean isUserIdUnique(String userId) {
        for (User user : allUsers) {
            if (user.getUserId().equals(userId)) {
                return false;
            }
        }
        return true;
    }
}