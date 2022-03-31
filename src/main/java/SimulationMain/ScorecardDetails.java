package SimulationMain;

import java.util.ArrayList;

public class ScorecardDetails {
    private String winningTeamName;
    private String losingTeamName;
    private int firstInningsRuns;
    private int firstInningsBallsPlayed;
    private int firstInningsWicketsLost;
    private int secondInningsRuns;
    private int secondInningsBallsPlayed;
    private int secondInningsWicketsLost;
    private ArrayList<BatsmanPerformance> winningTopThreeBatsman;
    private ArrayList<BatsmanPerformance> losingTopThreeBatsman;
    private ArrayList<BowlerPerformance> winningTopThreeBowlers;
    private ArrayList<BowlerPerformance> losingTopThreeBowlers;

    public ScorecardDetails(ArrayList<Player> winningEleven, ArrayList<Player> losingEleven, String winningTeamName, String losingTeamName,
                            int firstInnsRuns, int firstInnsBallsPlayed, int firstInnsWicketsLost, int secondInnsRuns,
                            int secondInnsBallsPlayed, int secondInnsWicketsLost) {

        //use regular arrays rather than array lists to store the players so that a sorting algorithm can be applied to them
        //batting array used to determine top batsmen, bowling array used to determine top bowlers

        BatsmanPerformance[] battingWinningElevenArr = new BatsmanPerformance[11];
        BowlerPerformance[] bowlingWinningElevenArr = new BowlerPerformance[11];
        BatsmanPerformance[] battingLosingElevenArr = new BatsmanPerformance[11];
        BowlerPerformance[] bowlingLosingElevenArr = new BowlerPerformance[11];
        BatsmanPerformance tempBatsman; //used to temporarily store a batsman when performing a swap in the bubble sort algorithm
        BowlerPerformance tempBowler; //used to temporarily store a bowler when performing a swap in the bubble sort algorithm
        int losingBowlerCount, winningBowlerCount; //used to keep track of the number of bowlers/all-rounders in each XI, to ensure all non-void bowlers are the start of each array

        //variables to implement bubble sort
        boolean noMorePassesWinning;
        boolean noMorePassesLosing;
        boolean noMorePasses;
        int numElements;

        //assign to the attributes storing the names and run/wicket totals
        this.winningTeamName = winningTeamName;
        this.losingTeamName = losingTeamName;
        this.firstInningsRuns = firstInnsRuns;
        this.firstInningsBallsPlayed = firstInnsBallsPlayed;
        this.firstInningsWicketsLost = firstInnsWicketsLost;
        this.secondInningsRuns = secondInnsRuns;
        this.secondInningsBallsPlayed = secondInnsBallsPlayed;
        this.secondInningsWicketsLost = secondInnsWicketsLost;
        //instantiate the batsman/bowler performance array lists
        this.winningTopThreeBatsman = new ArrayList<BatsmanPerformance>();
        this.losingTopThreeBatsman = new ArrayList<BatsmanPerformance>();
        this.winningTopThreeBowlers = new ArrayList<BowlerPerformance>();
        this.losingTopThreeBowlers = new ArrayList<BowlerPerformance>();

        //extract all the game stats for both XIs into the arrays of performance objects
        winningBowlerCount = 0; //initialise to zero so that we can place all non-void bowlers at the start, same applies to losing
        losingBowlerCount = 0;
        for (int i = 0; i < 11; i++) {
            battingWinningElevenArr[i] = new BatsmanPerformance(winningEleven.get(i).getName(),
                    winningEleven.get(i).getCurrentRunsScored(),
                    winningEleven.get(i).getCurrentBallsFaced(),
                    winningEleven.get(i).wasDismissed());

            battingLosingElevenArr[i] = new BatsmanPerformance(losingEleven.get(i).getName(),
                    losingEleven.get(i).getCurrentRunsScored(),
                    losingEleven.get(i).getCurrentBallsFaced(),
                    losingEleven.get(i).wasDismissed());

            //only add the bowler to the array of bowler performance objects if the player is a bowler/all-rounder
            if ((winningEleven.get(i).getRole() == Role.BOWLER) || (winningEleven.get(i).getRole() == Role.ALL_ROUNDER)) {
                bowlingWinningElevenArr[winningBowlerCount] = new BowlerPerformance(winningEleven.get(i).getName(),
                        winningEleven.get(i).getCurrentRunsConceded(),
                        winningEleven.get(i).getCurrentBallsBowled(),
                        winningEleven.get(i).getCurrentWickets());
                winningBowlerCount++; //increment so that we can place the next bowler/all-rounder in the next subsequent null space in the array
            }

            //only add the bowler to the array of bowler performance objects if the player is a bowler/all-rounder
            if ((losingEleven.get(i).getRole() == Role.BOWLER) || (losingEleven.get(i).getRole() == Role.ALL_ROUNDER)) {
                bowlingLosingElevenArr[losingBowlerCount] = new BowlerPerformance(losingEleven.get(i).getName(),
                        losingEleven.get(i).getCurrentRunsConceded(),
                        losingEleven.get(i).getCurrentBallsBowled(),
                        losingEleven.get(i).getCurrentWickets());
                losingBowlerCount++; //increment so that we can place the next bowler/all-rounder in the next subsequent null space in the array
            }
        }

        //sort the batting XIs based upon performance (best to worst)
        noMorePassesWinning = false;
        noMorePassesLosing = false;
        numElements = 10;
        while ((!noMorePassesWinning) || (!noMorePassesLosing)) {
            //run the swapping for the winning batsmen
            if (!noMorePassesWinning) {
                noMorePassesWinning = true;
                for (int i = 0; i < numElements; i++) {
                    //swap if first batsman in array scored lesser runs than next batsman in array
                    if (battingWinningElevenArr[i].getRunsScored() < battingWinningElevenArr[i + 1].getRunsScored()) {
                        tempBatsman = battingWinningElevenArr[i];
                        battingWinningElevenArr[i] = battingWinningElevenArr[i + 1];
                        battingWinningElevenArr[i + 1] = tempBatsman;
                        noMorePassesWinning = false;
                        //if the runs scored are equal between the two batsmen, perform other checks to determine the order
                    } else if (battingWinningElevenArr[i].getRunsScored() == battingWinningElevenArr[i + 1].getRunsScored()) {
                        //swap if first batsman in array was out and next batsman was not out
                        if ((battingWinningElevenArr[i].wasOut()) && (!battingWinningElevenArr[i + 1].wasOut())) {
                            tempBatsman = battingWinningElevenArr[i];
                            battingWinningElevenArr[i] = battingWinningElevenArr[i + 1];
                            battingWinningElevenArr[i + 1] = tempBatsman;
                            noMorePassesWinning = false;
                            //if the dismissal status is the same between the two batsmen, perform other checks
                        } else if (battingWinningElevenArr[i].wasOut() == battingWinningElevenArr[i + 1].wasOut()) {
                            //swap if the strike rate of the first batsman is greater than the second batsman
                            if (battingWinningElevenArr[i].getStrikeRate() < battingWinningElevenArr[i + 1].getStrikeRate()) {
                                tempBatsman = battingWinningElevenArr[i];
                                battingWinningElevenArr[i] = battingWinningElevenArr[i + 1];
                                battingWinningElevenArr[i + 1] = tempBatsman;
                                noMorePassesWinning = false;
                            }
                        }
                    }
                }
            }

            //run the swapping for the losing batsmen
            if (!noMorePassesLosing) {
                noMorePassesLosing = true;
                for (int i = 0; i < numElements; i++) {
                    //swap if first batsman in array scored lesser runs than next batsman in array
                    if (battingLosingElevenArr[i].getRunsScored() < battingLosingElevenArr[i + 1].getRunsScored()) {
                        tempBatsman = battingLosingElevenArr[i];
                        battingLosingElevenArr[i] = battingLosingElevenArr[i + 1];
                        battingLosingElevenArr[i + 1] = tempBatsman;
                        noMorePassesLosing = false;
                        //if the runs scored are equal between the two batsmen, perform other checks to determine the order
                    } else if (battingLosingElevenArr[i].getRunsScored() == battingLosingElevenArr[i + 1].getRunsScored()) {
                        //swap if first batsman in array was out and next batsman was not out
                        if ((battingLosingElevenArr[i].wasOut()) && (!battingLosingElevenArr[i + 1].wasOut())) {
                            tempBatsman = battingLosingElevenArr[i];
                            battingLosingElevenArr[i] = battingLosingElevenArr[i + 1];
                            battingLosingElevenArr[i + 1] = tempBatsman;
                            noMorePassesLosing = false;
                            //if the dismissal status is the same between the two batsmen, determine other checks to determine the order
                        } else if (battingLosingElevenArr[i].wasOut() == battingLosingElevenArr[i + 1].wasOut()) {
                            //swap if the strike rate of the first batsman is higher than that of the second batsman
                            if (battingLosingElevenArr[i].getStrikeRate() < battingLosingElevenArr[i + 1].getStrikeRate()) {
                                tempBatsman = battingLosingElevenArr[i];
                                battingLosingElevenArr[i] = battingLosingElevenArr[i + 1];
                                battingLosingElevenArr[i + 1] = tempBatsman;
                            }
                        }
                    }
                }
            }
            numElements--; //decrement numElements for each pass completed
        }


        //sort the winning bowling XI based upon performance (best to worst)
        noMorePasses = false;
        numElements = winningBowlerCount;
        while (!noMorePasses) {
            noMorePasses = true;
            for (int i = 0; i < numElements; i++) {
                //swap if the wickets of the first bowler in the array is less than that of the next bowler
                if (bowlingWinningElevenArr[i].getWickets() < bowlingWinningElevenArr[i + 1].getWickets()) {
                    tempBowler = bowlingWinningElevenArr[i];
                    bowlingWinningElevenArr[i] = bowlingWinningElevenArr[i + 1];
                    bowlingWinningElevenArr[i + 1]= tempBowler;
                    noMorePasses = false;
                    //if the wickets is the same between the two bowlers then perform other checks to determine the order
                } else if (bowlingWinningElevenArr[i].getWickets() == bowlingLosingElevenArr[i + 1].getWickets()) {
                    //swap if the economy of the first bowler is greater than that of the next bowler in the array
                    if (bowlingWinningElevenArr[i].getEconomy() > bowlingWinningElevenArr[i + 1].getEconomy()) {
                        tempBowler = bowlingWinningElevenArr[i];
                        bowlingWinningElevenArr[i] = bowlingWinningElevenArr[i + 1];
                        bowlingWinningElevenArr[i + 1] = tempBowler;
                        noMorePasses = false;
                        //if the economy is the same between the two bowlers then perform other checks to determine the order
                    } else if (bowlingWinningElevenArr[i].getEconomy() == bowlingWinningElevenArr[i + 1].getEconomy()) {
                        //swap if the first bowler bowled lesser balls than the next bowler in the array
                        if (bowlingWinningElevenArr[i].getBallsBowled() < bowlingWinningElevenArr[i + 1].getBallsBowled()) {
                            tempBowler = bowlingWinningElevenArr[i];
                            bowlingWinningElevenArr[i] = bowlingWinningElevenArr[i + 1];
                            bowlingWinningElevenArr[i + 1] = tempBowler;
                            noMorePasses = false;
                        }
                    }
                }
            }
            numElements--;
        }


        //sort the losing bowling XI based upon performance
        noMorePasses = false;
        numElements = losingBowlerCount;
        while (!noMorePasses) {
            noMorePasses = true;
            for (int i = 0; i < numElements; i++) {
                //swap if the wickets of the first bowler in the array is less than that of the next bowler
                if (bowlingLosingElevenArr[i].getWickets() < bowlingLosingElevenArr[i + 1].getWickets()) {
                    tempBowler = bowlingLosingElevenArr[i];
                    bowlingLosingElevenArr[i] = bowlingLosingElevenArr[i + 1];
                    bowlingLosingElevenArr[i + 1] = tempBowler;
                    noMorePasses = false;
                    //if the wickets between the two bowlers is the same, then perform other checks to determine the order
                } else if (bowlingLosingElevenArr[i].getWickets() == bowlingLosingElevenArr[i + 1].getWickets()) {
                    //swap if the economy of the first bowler in the array is greater than that of the next bowler
                    if (bowlingLosingElevenArr[i].getEconomy() > bowlingLosingElevenArr[i + 1].getEconomy()) {
                        tempBowler = bowlingLosingElevenArr[i];
                        bowlingLosingElevenArr[i] = bowlingLosingElevenArr[i + 1];
                        bowlingLosingElevenArr[i + 1] = tempBowler;
                        noMorePasses = false;
                        //perform other checks if the economy between the two bowlers is the same, to determine the order
                    } else if (bowlingLosingElevenArr[i].getEconomy() == bowlingLosingElevenArr[i + 1].getEconomy()) {
                        //swap if the balls bowled by the first bowler in the array is less than that of the next bowler
                        tempBowler = bowlingLosingElevenArr[i];
                        bowlingLosingElevenArr[i] = bowlingLosingElevenArr[i + 1];
                        bowlingLosingElevenArr[i + 1] = tempBowler;
                        noMorePasses = false;
                    }
                }
            }
            numElements--;
        }


        //add the top players to the array list attributes, provided that batsmen have scored more than zero runs, and bowlers have bowled more than zero balls
        for (int i = 0; i < 3; i++) {
            if (battingWinningElevenArr[i].getRunsScored() > 0) {
                this.winningTopThreeBatsman.add(battingWinningElevenArr[i]);
            }

            if (battingLosingElevenArr[i].getRunsScored() > 0) {
                this.losingTopThreeBatsman.add(battingLosingElevenArr[i]);
            }

            if (bowlingWinningElevenArr[i].getBallsBowled() > 0) {
                this.winningTopThreeBowlers.add(bowlingWinningElevenArr[i]);
            }

            if (bowlingLosingElevenArr[i].getBallsBowled() > 0) {
                this.losingTopThreeBowlers.add(bowlingLosingElevenArr[i]);
            }
        }
    }


    //method to output the scorecard of the match to the console window
    public void viewScorecard() {
        int firstInnsOversPlayed = this.firstInningsRuns / 6; //stores the overs played in the first innings; integer division so it will only account FULL overs bowled
        int firstInnsBallsRemaining = this.firstInningsRuns % 6; //stores the balls remaining i.e. balls bowled in an over that wasn't completed e.g. 18.3 overs
        int secondInnsOversPlayed = this.secondInningsRuns / 6;
        int secondInnsBallsRemaining = this.secondInningsRuns % 6;

        int bowlerOversBowled; //stores the overs bowled by a bowler; integer to division so it will only account for FULL overs bowled
        int bowlerBallsRemaining; //stores the bowler's balls remaining i.e. balls bowled in an over that wasn't completed e.g. 2.3 overs

        //check whether team batting or bowling first won to determine which team total to output first
        if (this.firstInningsRuns > this.secondInningsRuns) {
            System.out.println(this.winningTeamName + " beat " + this.losingTeamName + " by " +
                    Integer.toString(this.firstInningsRuns - this.secondInningsRuns) + " runs");
            System.out.println();
            System.out.println();

            //output first innings data
            System.out.println(this.winningTeamName + ": " + Integer.toString(this.firstInningsRuns) + "/" + Integer.toString(this.firstInningsWicketsLost)
                    + " " + Integer.toString(firstInnsOversPlayed) + "." + Integer.toString(firstInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores for the winning team
            for (int i = 0; i < this.winningTopThreeBatsman.size(); i++) {
                System.out.print(this.winningTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.winningTopThreeBatsman.get(i).getRunsScored()));
                if (!this.winningTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*"); //add on asterisk if batsman was not out
                }
                System.out.println(" (" + Integer.toString(this.winningTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            System.out.println();
            //output the top bowler figures for the losing team
            for (int i = 0; i < this.losingTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.losingTopThreeBowlers.get(i).getBallsBowled() / 6;
                bowlerBallsRemaining = this.losingTopThreeBowlers.get(i).getBallsBowled() % 6;
                System.out.print(this.losingTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.losingTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.losingTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining) + ")");
                }
            }

            System.out.println();
            System.out.println();

            //output second innings data
            System.out.println(this.losingTeamName + ": " + Integer.toString(this.secondInningsRuns) + "/" + Integer.toString(this.secondInningsWicketsLost)
                    + " " + Integer.toString(secondInnsOversPlayed) + "." + Integer.toString(secondInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores for the losing team
            for (int i = 0; i < this.losingTopThreeBatsman.size(); i++) {
                System.out.print(this.losingTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.losingTopThreeBatsman.get(i).getRunsScored()));
                if (!this.losingTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*"); //add on asterisk if batsman was not out
                }
                System.out.println(" (" + Integer.toString(this.losingTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            //output the top bowler figures for the winning team
            for (int i = 0; i < this.winningTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.winningTopThreeBowlers.get(i).getBallsBowled() / 6;
                bowlerBallsRemaining = this.winningTopThreeBowlers.get(i).getBallsBowled() % 6;
                System.out.print(this.winningTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.winningTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.winningTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining));
                }
                System.out.println(")");
            }
        } else if (this.secondInningsRuns > this.firstInningsRuns) {
            System.out.println(this.winningTeamName + " beat " + this.losingTeamName + " by " + Integer.toString(10 - this.secondInningsWicketsLost)
                    + " wickets");
            System.out.println();
            System.out.println();

            //output first innings data
            System.out.println(this.losingTeamName + ": " + Integer.toString(this.firstInningsRuns) + "/" + Integer.toString(this.firstInningsWicketsLost)
                    + " " + Integer.toString(firstInnsOversPlayed) + "." + Integer.toString(firstInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores for the losing team
            for (int i = 0; i < this.losingTopThreeBatsman.size(); i++) {
                System.out.print(this.losingTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.losingTopThreeBatsman.get(i).getRunsScored()));
                if (!this.losingTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*");
                }
                System.out.println(" (" + Integer.toString(this.losingTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            System.out.println();
            //output the top bowler figures from the winning team
            for (int i = 0; i < this.winningTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.winningTopThreeBowlers.get(i).getRunsConceded() / 6;
                bowlerBallsRemaining = this.winningTopThreeBowlers.get(i).getRunsConceded() % 6;
                System.out.print(this.winningTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.winningTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.winningTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining));
                }
                System.out.println(")");
            }

            System.out.println();
            System.out.println();

            //output second innings data
            System.out.println(this.winningTeamName + ": " + Integer.toString(this.secondInningsRuns) + "/" + Integer.toString(this.secondInningsWicketsLost)
                    + " " + Integer.toString(secondInnsOversPlayed) + "." + Integer.toString(secondInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores from the winning team
            for (int i = 0; i < this.winningTopThreeBatsman.size(); i++) {
                System.out.print(this.winningTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.winningTopThreeBatsman.get(i).getRunsScored()));
                if (!this.losingTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*"); //print the asterisk if the batsman was not out
                }
                System.out.println(" (" + Integer.toString(this.winningTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            System.out.println();
            //output the top bowling figures from the losing team
            for (int i = 0; i < this.losingTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.losingTopThreeBowlers.get(i).getBallsBowled() / 6;
                bowlerBallsRemaining = this.losingTopThreeBowlers.get(i).getBallsBowled() % 6;
                System.out.print(this.losingTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.losingTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.losingTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining));
                }
                System.out.println(")");
            }
        } else {
            System.out.println("Match tied");
            System.out.println();
            System.out.println();

            //output first innings data
            System.out.println(this.winningTeamName + ": " + Integer.toString(this.firstInningsRuns) + "/" + Integer.toString(this.firstInningsWicketsLost)
                    + " " + Integer.toString(firstInnsOversPlayed) + "." + Integer.toString(firstInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores for the winning team
            for (int i = 0; i < this.winningTopThreeBatsman.size(); i++) {
                System.out.print(this.winningTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.winningTopThreeBatsman.get(i).getRunsScored()));
                if (!this.winningTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*"); //add on asterisk if batsman was not out
                }
                System.out.println(" (" + Integer.toString(this.winningTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            System.out.println();
            //output the top bowler figures for the losing team
            for (int i = 0; i < this.losingTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.losingTopThreeBowlers.get(i).getBallsBowled() / 6;
                bowlerBallsRemaining = this.losingTopThreeBowlers.get(i).getBallsBowled() % 6;
                System.out.print(this.losingTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.losingTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.losingTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining) + ")");
                }
            }

            System.out.println();
            System.out.println();

            //output second innings data
            System.out.println(this.losingTeamName + ": " + Integer.toString(this.secondInningsRuns) + "/" + Integer.toString(this.secondInningsWicketsLost)
                    + " " + Integer.toString(secondInnsOversPlayed) + "." + Integer.toString(secondInnsBallsRemaining) + " overs");
            System.out.println();
            //output the top batsman scores for the losing team
            for (int i = 0; i < this.losingTopThreeBatsman.size(); i++) {
                System.out.print(this.losingTopThreeBatsman.get(i).getName() + " " + Integer.toString(this.losingTopThreeBatsman.get(i).getRunsScored()));
                if (!this.losingTopThreeBatsman.get(i).wasOut()) {
                    System.out.print("*"); //add on asterisk if batsman was not out
                }
                System.out.println(" (" + Integer.toString(this.losingTopThreeBatsman.get(i).getBallsFaced()) + ")");
            }
            //output the top bowler figures for the winning team
            for (int i = 0; i < this.winningTopThreeBowlers.size(); i++) {
                bowlerOversBowled = this.winningTopThreeBowlers.get(i).getBallsBowled() / 6;
                bowlerBallsRemaining = this.winningTopThreeBowlers.get(i).getBallsBowled() % 6;
                System.out.print(this.winningTopThreeBowlers.get(i).getName() + " " + Integer.toString(this.winningTopThreeBowlers.get(i).getWickets()) + "-"
                        + Integer.toString(this.winningTopThreeBowlers.get(i).getRunsConceded()) + " (" + Integer.toString(bowlerOversBowled));
                if (bowlerBallsRemaining != 0) {
                    System.out.print("." + Integer.toString(bowlerBallsRemaining));
                }
                System.out.println(")");
            }
        }
    }

    //method to return the winning team name, used to find the winner pointer in the main class
    public String getWinningTeamName(){return this.winningTeamName;}

    //method to find the losing team name, used to find the loser pointer in the main class
    public String getLosingTeamName() {return this.losingTeamName;}

    //method to return the victory margin of the game, used to determine whether the match was actually a tie or not in Match class
    public int getVictoryMargin() {
        if (this.firstInningsRuns > this.secondInningsRuns) {
            return this.firstInningsRuns - this.secondInningsRuns; //return runs margin if team batting first won
        } else if (this.secondInningsRuns > this.firstInningsRuns) {
            return 10 - this.secondInningsWicketsLost; //return wickets margin if team bowling first won
        } else {
            return 0; //return 0 if tied
        }
    }
}

