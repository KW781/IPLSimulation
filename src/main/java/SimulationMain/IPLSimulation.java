package SimulationMain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import TypeWrappers.*; //import all the wrapper classes from TypeWrappers
import FirebaseConnectivity.*;


public class IPLSimulation {
    //helper function to find the factorial of a number, recursive solution
    public static int Factorial(int number) {
        if (number > 1) {
            number = number * Factorial(number - 1);
        }
        return number;
    }

    //helper function to find the combination of two numbers (used for the scheduling)
    public static int Combination(int num1, int num2) {
        return (Factorial(num1)) / (Factorial(num2) * Factorial(num1 - num2));
    }

    //this function will use database connectivity from the FirebaseConnectivity package to create the auction pool of players
    public static ArrayList<Player> RegisterPlayersInAuction() throws ExecutionException, InterruptedException {
        ArrayList<Player> auctionPool = new ArrayList<Player>(); //the auction pool of players
        ArrayList<Map> playerMapObjects = FirebaseService.getPlayerMapObjects(); //player map objects collected from the FirebaseConnectivity package
        int[] playerStats = new int[7];
        String name;
        double basePrice;
        boolean isWicketkeeper;

        for (Map playerMapObject : playerMapObjects) {
            playerStats[0] = (int)(playerMapObject.get("numMatches"));
            playerStats[1] = (int)(playerMapObject.get("careerRunsScored"));
            playerStats[2] = (int)(playerMapObject.get("timesOut"));
            playerStats[3] = (int)(playerMapObject.get("careerBallsFaced"));
            playerStats[4] = (int)(playerMapObject.get("careerWickets"));
            playerStats[5] = (int)(playerMapObject.get("careerRunsConceded"));
            playerStats[6] = (int)(playerMapObject.get("careerBallsBowled"));

            name = playerMapObject.get("name").toString();
            basePrice = (double)(playerMapObject.get("basePrice"));
            isWicketkeeper = (boolean)(playerMapObject.get("isWicketkeeper"));

            auctionPool.add(new Player(name, basePrice, isWicketkeeper, playerStats)); //add the player to the auction pool
        }
        return auctionPool;
    }

    public static void InstantiateTeams(ArrayList<TeamFranchise> teams, String[] names) {
        for (int i = 0; i < names.length; i++) {
            teams.add(new TeamFranchise(names[i], false));
        }
    }

    public static void RunAuction(ArrayList<TeamFranchise> teams, ArrayList<Player> auctionPool) {
        boolean bidOccurred;
        BooleanWrap userContinueBidding = new BooleanWrap();
        Player currentPlayer;
        int nextTeamToBid;
        int playerCurrentTeam = -1;
        int numTimesUnsold;

        for (int i = 0; i < auctionPool.size(); i++) {
            currentPlayer = auctionPool.get(i); //extract the current player from the auction pool
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
                }
            }
        }
    }

    public static ArrayList<Match> GenerateTournamentSchedule(ArrayList<TeamFranchise> teams) {
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
        numLeagueMatches = Combination(numTeams, 2);
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

    public static void runTournament(ArrayList<Match> schedule, ArrayList<TeamFranchise> teams) {
        TeamFranchise tempTeam; //used for swapping when sorting the teams according to the points table
        boolean noMorePasses;
        int numElements;
        ArrayList<Match> playoffs = new ArrayList<Match>(); //array list of all the playoff matches (4 in total)
        int qualifier1WinnerPointer; //pointer to the position of the team that won qualifier 1 in the array
        int qualifier1LoserPointer; //pointer to the position of the team that lost qualifier 1 in the array
        int eliminatorWinnerPointer; //pointer to the position of the team that won the eliminator in the array
        int qualifier2WinnerPointer; //pointer to the position of the team that won
        int IPLWinnerPointer; //pointer to the team that won the final i.e. won the IPL

        //run all the league matches
        for (int i = 0; i < schedule.size(); i++) {
            schedule.get(i).playMatch();
        }

        //re order the teams array based on NRR
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
        //account for tied matches by taking the higher position in the points table
        if (qualifier1WinnerPointer == -1) {
            qualifier1WinnerPointer = 0;
            qualifier1LoserPointer = 1;
        }
        if (eliminatorWinnerPointer == -1) {
            eliminatorWinnerPointer = 2;
        }

        playoffs.add(new Match(teams.get(qualifier1LoserPointer), teams.get(eliminatorWinnerPointer))); //create the second qualifier
        playoffs.get(2).playMatch(); //run the second qualifier
        qualifier2WinnerPointer = playoffs.get(2).getWinningPointer(teams); //get the pointer of the winning team
        //account for the match being tied by taking the team that lost the first qualifier, since that team would've finished higher in the points table
        if (qualifier2WinnerPointer == -1) {
            qualifier2WinnerPointer = qualifier1LoserPointer;
        }

        playoffs.add(new Match(teams.get(qualifier1WinnerPointer), teams.get(qualifier2WinnerPointer))); //create the final
        playoffs.get(3).playMatch(); //run the final
        IPLWinnerPointer = playoffs.get(3).getWinningPointer(teams); //get the pointer of the winning team
        if (IPLWinnerPointer == -1) {
            if (qualifier1WinnerPointer > qualifier2WinnerPointer) {
                IPLWinnerPointer = qualifier1WinnerPointer; //make the qualifier 1 winner the IPL winner if they finished higher on the points table
            } else {
                IPLWinnerPointer = qualifier2WinnerPointer; //make the qualifier 2 winner the IPL winner if they finished higher on the points table
            }
        }

        //output the winner to the user
        if (teams.get(IPLWinnerPointer).controlledByUser()) {
            System.out.println("Congratulations! Your team, " + teams.get(IPLWinnerPointer).getName() + ", has won the IPL!");
        } else {
            System.out.println("Congratulations to " + teams.get(IPLWinnerPointer).getName() + " for winning the IPL");
        }
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        /*
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

        ArrayList<Player> auctionPool = RegisterPlayersInAuction();
        InstantiateTeams(teams, standardNames);
        */
        FirebaseInitialise.initialise();
    }
}

