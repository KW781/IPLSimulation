package SimulationMain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import TypeWrappers.*; //import all the wrapper classes from TypeWrappers
import FirebaseConnectivity.*;
import UserData.*;


public class IPLSimulation {
    //helper function to find the factorial of a number, recursive solution
    public static int factorial(int number) {
        if (number > 1) {
            number = number * factorial(number - 1);
        }
        return number;
    }

    //helper function to find the combination of two numbers (used for the scheduling)
    public static int combination(int num1, int num2) {
        return (factorial(num1)) / (factorial(num2) * factorial(num1 - num2));
    }

    //this function will use database connectivity from the FirebaseConnectivity package to create the auction pool of players
    public static ArrayList<Player> registerPlayersInAuction() throws ExecutionException, InterruptedException {
        ArrayList<Player> auctionPool = new ArrayList<Player>(); //the auction pool of players
        ArrayList<Map> playerMapObjects = FirebaseService.getPlayerMapObjects(); //player map objects collected from the FirebaseConnectivity package
        int[] playerStats = new int[7];
        String name;
        double basePrice;
        boolean isWicketkeeper;
        boolean isOverseas;
        Long tempStat; //temporarily stores java.lang.Long value for each player stat

        for (Map playerMapObject : playerMapObjects) {
            name = playerMapObject.get("name").toString();
            tempStat = (Long) playerMapObject.get("basePrice");
            basePrice = tempStat.doubleValue();
            isWicketkeeper = (boolean) (playerMapObject.get("isWicketkeeper"));
            isOverseas = (boolean)(playerMapObject.get("overseas"));

            tempStat = (Long) playerMapObject.get("numMatches");
            playerStats[0] = tempStat.intValue();
            tempStat = (Long) playerMapObject.get("careerRunsScored");
            playerStats[1] = tempStat.intValue();
            tempStat = (Long) playerMapObject.get("timesOut");
            playerStats[2] = tempStat.intValue();
            tempStat = (Long) playerMapObject.get("careerBallsFaced");
            playerStats[3] = tempStat.intValue();
            tempStat = (Long) playerMapObject.get("careerWickets");
            playerStats[4] = tempStat.intValue();
            tempStat = (Long) (playerMapObject.get("careerRunsConceded"));
            playerStats[5] = tempStat.intValue();
            tempStat = (Long) playerMapObject.get("careerBallsBowled");
            playerStats[6] = tempStat.intValue();

            //add the player to the auction pool
            auctionPool.add(new Player(name, basePrice, isWicketkeeper, isOverseas, playerStats));
        }
        return auctionPool;
    }

    public static void instantiateTeams(ArrayList<TeamFranchise> teams, String[] names) {
        for (int i = 0; i < names.length; i++) {
            teams.add(new TeamFranchise(names[i], false));
        }
    }

    public static void runAuction(ArrayList<TeamFranchise> teams, ArrayList<Player> auctionPool, User userPlaying) {
        boolean bidOccurred;
        BooleanWrap userContinueBidding = new BooleanWrap();
        int nextTeamToBid;
        int playerCurrentTeam = -1;
        int numTimesUnsold;

        for (Player currentPlayer : auctionPool) {
            System.out.println("Current player being auctioned: " + currentPlayer.getName());
            System.out.println("Base Price: " + Double.toString(currentPlayer.getPrice()));

            userContinueBidding.value = true;
            numTimesUnsold = 0;

            while (numTimesUnsold < 18) {
                bidOccurred = false;

                //first choose a team that is not the user's team or the team with the current highest bid, and let them bid
                do {
                    nextTeamToBid = (int) (Math.random() * (teams.size() - 1));
                } while (nextTeamToBid == playerCurrentTeam);

                if (teams.get(nextTeamToBid).bid(currentPlayer)) {
                    playerCurrentTeam = nextTeamToBid;
                    bidOccurred = true;
                }

                //next allow the user to bid, if they have not yet opted to stop bidding completely
                if ((userContinueBidding.value) && (teams.get(teams.size() - 1).bid(currentPlayer, userContinueBidding))) {
                    if (numTimesUnsold >= 17) {
                        System.out.println("If you choose not to bid again, bidding for " + currentPlayer.getName() + " will end.");
                    }
                    playerCurrentTeam = teams.size() - 1;
                    bidOccurred = true;
                }

                //if no team bidded for the player, then increment the number of times the player went unsold
                if (!bidOccurred) {
                    numTimesUnsold++;
                }
            }

            if (playerCurrentTeam != -1) {
                teams.get(playerCurrentTeam).addPlayer(currentPlayer);
                if (teams.get(playerCurrentTeam).controlledByUser()) {
                    System.out.println("Congratulations, you've got " + currentPlayer.getName() + " in your squad!");
                    userPlaying.playerBought(); //increment number players bought by user if they've bought a player
                }
            }
        }
    }

    public static ArrayList<Match> generateTournamentSchedule(ArrayList<TeamFranchise> teams) {
        ArrayList<Match> schedule = new ArrayList<Match>();
        int numLeagueMatches; //number of league matches in the whole schedule (not including playoffs)
        int numTeams = teams.size();
        int[] numMatchesPerTeam = new int[numTeams]; //int array that stores the number of matches currently allocated to each team
        int randTeamNumber; //integer to store the randomly generated team number for each allocation instance
        int teamNum1, teamNum2; //stores the team numbers that will play the match for each allocation instance

        //initialise the number of matches currently allocated to each team all to zero
        for (int i = 0; i < numTeams; i++) {
            numMatchesPerTeam[i] = 0;
        }

        //allocate the teams to matches with each other, thereby generating a schedule
        numLeagueMatches = combination(numTeams, 2);
        for (int i = 0; i < numLeagueMatches; i++) {
            //find the first team to play the match
            randTeamNumber = (int)(Math.random() * numTeams); //generate random number to find a team to put in the match
            //find another team if the current team has already been scheduled all of its matches
            while (numMatchesPerTeam[randTeamNumber] >= numTeams - 1) {
                //wrap over to the start of the array if at the end
                if (randTeamNumber >= numTeams - 1) {
                    randTeamNumber = 0;
                } else {
                    randTeamNumber++;
                }
            }
            teamNum1 = randTeamNumber;

            //find the second team to play the match
            randTeamNumber = (int)(Math.random() * numTeams);
            //find another team if either the current team has already been scheduled all its matches, or if it's the first team that's playing the match
            while ((numMatchesPerTeam[randTeamNumber] >= numTeams - 1) || (randTeamNumber == teamNum1)) {
                //wrap over to the start of the array if at the end
                if (randTeamNumber >= numTeams - 1) {
                    randTeamNumber = 0;
                } else {
                    randTeamNumber++;
                }
            }
            teamNum2 = randTeamNumber;

            schedule.add(new Match(teams.get(teamNum1), teams.get(teamNum2))); //add the match to the schedule
            //increment the number of matches scheduled for the respective teams that were just allocated
            numMatchesPerTeam[teamNum1]++;
            numMatchesPerTeam[teamNum2]++;
        }
        return schedule;
    }

    public static void runTournament(ArrayList<Match> schedule, ArrayList<TeamFranchise> teams, User userPlaying) {
        TeamFranchise tempTeam; //used for swapping when sorting the teams according to the points table
        boolean noMorePasses;
        int numElements;
        ArrayList<Match> playoffs = new ArrayList<Match>(); //array list of all the playoff matches (4 in total)
        int qualifier1WinnerPointer; //pointer to the position of the team that won qualifier 1 in the array
        int qualifier1LoserPointer; //pointer to the position of the team that lost qualifier 1 in the array
        int eliminatorWinnerPointer; //pointer to the position of the team that won the eliminator in the array
        int eliminatorLoserPointer; //pointer to the position of the team that lost the eliminator in the array
        int qualifier2WinnerPointer; //pointer to the position of the team that won qualifier 2 in the array
        int qualifier2LoserPointer; //pointer to the position of the team that lost qualifier 2 in the array
        int IPLWinnerPointer; //pointer to the team that won the final in the array i.e. won the IPL
        int runnerUpPointer; //pointer to the team that lost the final in the array i.e. runners up to the IPL

        //run all the league matches
        for (Match currentMatch : schedule) {
            currentMatch.playMatch();
        }

        //re order the teams array based on points and NRR
        noMorePasses = false;
        numElements = teams.size() - 1;
        while (!noMorePasses) {
            noMorePasses = true;
            for (int i = 0; i < numElements; i++) {
                if (teams.get(i).getPoints() < teams.get(i + 1).getPoints()) {
                    //swap the teams if the first team has lesser points than the next team
                    tempTeam = teams.get(i);
                    teams.set(i, teams.get(i + 1));
                    teams.set(i + 1, tempTeam);
                    noMorePasses = false;
                    //otherwise if the points are equal do a check on the NRR
                } else if (teams.get(i).getPoints() == teams.get(i + 1).getPoints()) {
                    if (teams.get(i).getNRR() < teams.get(i + 1).getNRR()) {
                        //swap the teams if the first team has a lesser NRR than the next team
                        tempTeam = teams.get(i);
                        teams.set(i, teams.get(i + 1));
                        teams.set(i + 1, tempTeam);
                        noMorePasses = false;
                    }
                }
            }
            numElements--;
        }

        //create the first qualifier and eliminator
        playoffs.add(new Match(teams.get(0), teams.get(1))); //first qualifier
        playoffs.add(new Match(teams.get(2), teams.get(3))); //eliminator

        playoffs.get(0).playMatch(); //run the first qualifier
        playoffs.get(1).playMatch(); //run the eliminator

        //get the pointers of the winning and losing teams
        qualifier1WinnerPointer = playoffs.get(0).getWinningPointer(teams);
        qualifier1LoserPointer = playoffs.get(0).getLosingPointer(teams);
        eliminatorWinnerPointer = playoffs.get(1).getWinningPointer(teams);
        eliminatorLoserPointer = playoffs.get(1).getLosingPointer(teams);
        //account for tied matches by taking the higher position in the points table
        if (qualifier1WinnerPointer == -1) {
            qualifier1WinnerPointer = 0;
            qualifier1LoserPointer = 1;
        }
        if (eliminatorWinnerPointer == -1) {
            eliminatorWinnerPointer = 2;
            eliminatorLoserPointer = 3;
        }

        playoffs.add(new Match(teams.get(qualifier1LoserPointer), teams.get(eliminatorWinnerPointer))); //create the second qualifier
        playoffs.get(2).playMatch(); //run the second qualifier
        qualifier2WinnerPointer = playoffs.get(2).getWinningPointer(teams); //get the pointer of the winning team
        qualifier2LoserPointer = playoffs.get(2).getLosingPointer(teams);
        //account for the match being tied by taking the team that lost the first qualifier, since that team would've finished higher in the points table
        if (qualifier2WinnerPointer == -1) {
            qualifier2WinnerPointer = qualifier1LoserPointer;
            qualifier2LoserPointer = eliminatorWinnerPointer;
        }

        playoffs.add(new Match(teams.get(qualifier1WinnerPointer), teams.get(qualifier2WinnerPointer))); //create the final
        playoffs.get(3).playMatch(); //run the final
        IPLWinnerPointer = playoffs.get(3).getWinningPointer(teams); //get the pointer of the winning team
        runnerUpPointer = playoffs.get(3).getLosingPointer(teams);
        if (IPLWinnerPointer == -1) {
            if (qualifier1WinnerPointer > qualifier2WinnerPointer) {
                //make the qualifier 1 winner the IPL winner if they finished higher on the points table
                IPLWinnerPointer = qualifier1WinnerPointer;
                runnerUpPointer = qualifier2WinnerPointer;
            } else {
                //make the qualifier 2 winner the IPL winner if they finished higher on the points table
                IPLWinnerPointer = qualifier2WinnerPointer;
                runnerUpPointer = qualifier1WinnerPointer;
            }
        }

        //sort the teams new rankings based on playoffs
        noMorePasses = false;
        while (!noMorePasses) {
            noMorePasses = true;
            for (int i = 0; i < 4; i++) { //only rankings of top 4 teams will change
                //place IPL winner into first position in the array
                if ((i == IPLWinnerPointer) && (i != 0)) {
                    tempTeam = teams.get(0);
                    teams.set(0, teams.get(IPLWinnerPointer));
                    teams.set(IPLWinnerPointer, tempTeam);
                    IPLWinnerPointer = 0;
                    noMorePasses = false;
                }

                //place runners up of IPL into second position in array
                if ((i == runnerUpPointer) && (i != 1)) {
                    tempTeam = teams.get(1);
                    teams.set(1, teams.get(runnerUpPointer));
                    teams.set(runnerUpPointer, tempTeam);
                    runnerUpPointer = 1;
                    noMorePasses = false;
                }

                //place losers of the second qualifier (third ranked team) into third position in array
                if ((i == qualifier2LoserPointer) && (i != 2)) {
                    tempTeam = teams.get(2);
                    teams.set(2, teams.get(qualifier2LoserPointer));
                    teams.set(qualifier2LoserPointer, tempTeam);
                    qualifier2LoserPointer = 2;
                    noMorePasses = false;
                }

                //place losers of the eliminator (fourth ranked team) into fourth position in array
                if ((i == eliminatorLoserPointer) && (i != 3)) {
                    tempTeam = teams.get(3);
                    teams.set(3, teams.get(eliminatorLoserPointer));
                    teams.set(eliminatorLoserPointer, tempTeam);
                    eliminatorLoserPointer = 3;
                    noMorePasses = false;
                }
            }
        }

        //output the winner to the user
        if (teams.get(0).controlledByUser()) {
            System.out.println("Congratulations! Your team, " + teams.get(0).getName() + ", has won the IPL!");
            userPlaying.winTournament(); //increment user's number of tournament wins if they won
        } else {
            System.out.println("Congratulations to " + teams.get(0).getName() + " for winning the IPL");
        }
        userPlaying.playedTournament(); //increment user's number of tournaments played

        //display final team rankings upon completion of tournament
        System.out.println("Here are the final team rankings at the end of the IPL: ");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println(Integer.toString(i) + ". " + teams.get(i).getName());
        }
    }

    public static void configureGame(User userPlaying) throws ExecutionException, InterruptedException {
        boolean newUser;
        boolean loginValid;
        int numTeams;
        boolean auctionWanted;
        String[] loginDetails; //stores username and password of the user (first and second elements respectively)
        String teamName;

        System.out.println("Welcome to the IPL Simulation! Here you can play as a team that competes against the other"
                + " franchises in the auction and tournament in order to win the IPL! First you must login or register"
                + "in order to play.");
        System.out.println();

        newUser = UserInteraction.newAccountWanted();
        if (newUser) {
            loginDetails = UserInteraction.registerUser();
            userPlaying = new User(loginDetails[0], loginDetails[1], true);
        } else {
            //keep requesting the login details from the user until they are a valid set of login details
            loginValid = false;
            while (!loginValid) {
                try {
                    loginDetails = UserInteraction.loginUser();
                    userPlaying = new User(loginDetails[0], loginDetails[1], false); //throws exception if login is invalid
                    loginValid = true;
                } catch (RuntimeException e) {
                    System.out.println("Incorrect username or password.");
                    loginValid= false;
                }
            }
        }

        System.out.println("Welcome to IPL Simulation, " + userPlaying.getUsername() + "!");
        System.out.println();
        if (UserInteraction.tournamentWanted()) {
            numTeams = UserInteraction.getNumTeams();
            auctionWanted = UserInteraction.auctionWanted();
        }
        teamName = UserInteraction.chooseTeamName();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String[] standardNames = {"Mumbai Indians",
                "Chennai Super Kings",
                "Punjab Kings",
                "Sunrisers Hyderabad",
                "Delhi Capitals",
                "Royal Challengers Bangalore",
                "Rajasthan Royals",
                "Kolkata Knight Riders",
                "Lucknow Super Giants",
                "Gujarat Titans"};
        ArrayList<TeamFranchise> teams = new ArrayList<TeamFranchise>();

        ArrayList<Player> auctionPool = registerPlayersInAuction();
        instantiateTeams(teams, standardNames);
        FirebaseInitialise.initialise();


    }
}

