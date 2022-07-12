package UserData;

import FirebaseConnectivity.FirebaseService;

import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    public User(String user, String pass, boolean newUser) throws ExecutionException, InterruptedException {
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
            this.fetchUser(user, pass); //throws exception if either username doesn't exist or password is incorrect
        }
    }

    private void fetchUser(String user, String pass) throws ExecutionException, InterruptedException {
        Long tempStat; //used to temporarily store firestore document values, since they're of type 'Long'
        Map userData = FirebaseService.getUser(user);
        String docPassword = userData.get("password").toString();

        if (pass.equals(docPassword)) {
            //set username and password once validated
            this.username = user;
            this.password = pass;

            //set attributes to game statistics collected from document map
            tempStat = (Long) userData.get("matchWins");
            this.numMatchWins = tempStat.intValue();
            tempStat = (Long) userData.get("compWins");
            this.numCompWins = tempStat.intValue();
            tempStat = (Long) userData.get("playersBought");
            this.numPlayersBought = tempStat.intValue();
            tempStat = (Long) userData.get("matchesPlayed");
            this.numMatchesPlayed = tempStat.intValue();
            tempStat = (Long) userData.get("highestRank");
            this.highestRanking = tempStat.intValue();
        } else {
            throw new RuntimeException(); //exception thrown if user entered password incorrectly
        }
    }
}
