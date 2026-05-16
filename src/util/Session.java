package util;

public class Session {
    private static String currentUser;

    public static void setUser(String username) { currentUser = username; }
    public static String getUser() { 
        return (currentUser == null) ? "GuestPlayer" : currentUser; 
    }
}