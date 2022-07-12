package UserData;

import FirebaseConnectivity.FirebaseService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class User {
    //account credential data
    private String username;
    private String password;

    //game statistics
    private int numMatchWins;
    private int numMatchLosses;
    private int numMatchesTied;
    private int numCompWins;
    private int numPlayersBought;
    private int numMatchesPlayed;
    private int highestRanking;

    public User(String user, String pass, boolean newUser) throws ExecutionException, InterruptedException {
        if (newUser) {
            this.username = user;
            this.password = pass;
            this.numMatchWins = 0;
            this.numMatchLosses = 0;
            this.numMatchesTied = 0;
            this.numCompWins = 0;
            this.numPlayersBought = 0;
            this.numMatchesPlayed = 0;
            this.highestRanking = 0;
        } else {
            this.fetchUser(user, pass); //throws exception if either username doesn't exist or password is incorrect
        }
    }

    public void winMatch() {

    }

    public void updateSelfToDatabase() {
        Map<String, Object> selfData = new HashMap<>();

        //place all data inside the map, to be placed as a firestore document
        selfData.put("password", this.password);
        selfData.put("matchWins", this.numMatchWins);
        selfData.put("matchLosses", this.numMatchLosses);
        selfData.put("matchTies", this.numMatchesTied);
        selfData.put("compWins", this.numCompWins);
        selfData.put("playersBought", this.numPlayersBought);
        selfData.put("matchesPlayed", this.numMatchesPlayed);
        selfData.put("highestRank", this.highestRanking);

        FirebaseService.updateUserDocument(this.username, selfData); //update user data in firestore
    }

    private void fetchUser(String user, String pass) throws ExecutionException, InterruptedException {
        Long tempStat; //used to temporarily store firestore document values, since they're of type 'Long'
        Map userData = FirebaseService.getUser(user); //throws exception if user doesn't exist in firestore
        String docPassword = userData.get("password").toString();

        if (pass.equals(docPassword)) {
            //set username and password once validated
            this.username = user;
            this.password = pass;

            //set attributes to game statistics collected from document map
            tempStat = (Long) userData.get("matchWins");
            this.numMatchWins = tempStat.intValue();
            tempStat = (Long) userData.get("matchLosses");
            this.numMatchLosses = tempStat.intValue();
            tempStat = (Long) userData.get("matchTies");
            this.numMatchesTied = tempStat.intValue();
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
