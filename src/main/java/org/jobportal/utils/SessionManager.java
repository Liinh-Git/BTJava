package org.jobportal.utils;

import org.jobportal.dto.UserDTO;

public class SessionManager {
    private static UserDTO currentUser;

    public static UserDTO getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDTO user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
