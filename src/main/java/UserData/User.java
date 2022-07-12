package UserData;

public class User {
    //account credential data
    private String username;
    private String password;

    //game statistics
    private int numMatchWins;
    private int numCompWins;
    private int numPlayersBought;
    private int numMatchesPlayed;
    private int highestRanking;

    public User(String user, String pass, int[] stats) {

    }

    /*
    public static User createUser(String user, String pass, boolean newUser) {
        int[] stats = new int[5];

        if (newUser) {
            for (Integer stat : stats) {
                stat = 0;
            }
        }
    }
     */
}
