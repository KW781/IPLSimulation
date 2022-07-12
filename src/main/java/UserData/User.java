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

    public User(String user, String pass, boolean newUser) {
        if (newUser) {
            this.username = user;
            this.password = pass;
            this.numMatchWins = 0;
            this.numCompWins = 0;
            this.numPlayersBought = 0;
            this.numMatchesPlayed = 0;
            this.highestRanking = 0;

            this.addSelf(); //add user to firestore database
        } else {
            if (!this.fetchUser(user, pass)) {
                throw new RuntimeException();
            }
        }
    }

    private void addSelf() {

    }

    private boolean fetchUser(String user, String pass) {
        return true;
    }
}
