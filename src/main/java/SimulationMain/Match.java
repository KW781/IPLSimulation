package SimulationMain;
import java.util.ArrayList;
import java.util.Scanner;
import TypeWrappers.*;

public class Match {
    private TeamFranchise team1;
    private ArrayList<Player> playingEleven1;
    private TeamFranchise team2;
    private ArrayList<Player> playingEleven2;
    private ScorecardDetails matchScorecard; //stores the scorecard for this match

    public Match(TeamFranchise firstTeam, TeamFranchise secondTeam) {
        this.team1 = firstTeam;
        this.team2 = secondTeam;
    }

    public void playMatch() {
        int tossNum; //number randomly generated to simulate coin toss
        Scanner batBowlChoiceInput = new Scanner(System.in);
        int batBowlChoice;
        IntWrap pointsToAdd = new IntWrap(); //represents the points to be added to team 1's points
        DoubleWrap nrrToAdd = new DoubleWrap(); //represents the NRR to be added to team 1

        //select playing elevens for both the teams
        this.playingEleven1 = this.team1.selectPlayingEleven();
        this.playingEleven2 = this.team2.selectPlayingEleven();

        tossNum = (int)((Math.random() * 2) + 1); //run coin toss
        if (tossNum == 1) { //team 1 won the toss
            if (this.team1.controlledByUser()) { //if team winning toss is user controlled, ask them to choose
                System.out.println("You won the toss! Bat or bowl?");
                System.out.println("1. Bat");
                System.out.println("2. Bowl");
                System.out.print("Please enter a number: ");
                batBowlChoice = batBowlChoiceInput.nextInt();
                //keep asking for user input until valid value is entered
                while ((batBowlChoice != 1) && (batBowlChoice != 2)) {
                    System.out.print("Please enter a valid number: ");
                    batBowlChoice = batBowlChoiceInput.nextInt();
                }

                //based on choice input, run what the user wants
                if (batBowlChoice == 1) {
                    this.runUserBattingFirst(pointsToAdd, nrrToAdd);
                } else if (batBowlChoice == 2) {
                    this.runUserBowlingFirst(pointsToAdd, nrrToAdd);
                }
            } else {
                if (this.team2.controlledByUser()) { //if team losing the toss is controlled by the user, then inform them of the decision
                    batBowlChoice = (int) ((Math.random() * 2) + 1); //randomise whether the computer wants to bat or bowl
                    if (batBowlChoice == 1) { //if 1 is generated, the team bats first
                        System.out.println("You lost the toss, " + this.team1.getName() + " opts to bat first");
                        this.runUserBowlingFirst(pointsToAdd, nrrToAdd);
                    } else if (batBowlChoice == 2) { //if 2 is generated, the team bowls first
                        System.out.println("You lost the toss, " + this.team1.getName() + " opts to bowl first");
                        this.runUserBattingFirst(pointsToAdd, nrrToAdd);
                    }
                } else {
                    this.runComputerFullGame(1, pointsToAdd, nrrToAdd);
                }
            }
        } else if (tossNum == 2) { //team 2 won the toss
            if (this.team2.controlledByUser()) { //if team winning toss is user controlled, ask them to choose
                System.out.println("You won the toss! Bat or bowl?");
                System.out.println("1. Bat");
                System.out.println("2. Bowl");
                System.out.print("Please enter a number: ");
                batBowlChoice = batBowlChoiceInput.nextInt();
                //keep asking for user input until valid value is entered
                while ((batBowlChoice != 1) && (batBowlChoice != 2)) {
                    System.out.print("Please enter a valid number: ");
                    batBowlChoice = batBowlChoiceInput.nextInt();
                }

                //based on choice input, run what the user wants
                if (batBowlChoice == 1) {
                    this.runUserBattingFirst(pointsToAdd, nrrToAdd);
                } else if (batBowlChoice == 2) {
                    this.runUserBowlingFirst(pointsToAdd, nrrToAdd);
                }
            } else {
                if (this.team1.controlledByUser()) {
                    batBowlChoice = (int) ((Math.random() * 2) + 1); //randomise whether the opposition wants to bat or bowl
                    if (batBowlChoice == 1) { //if 1 is generated, the team bats first
                        System.out.println("You lost the toss, " + this.team2.getName() + " opts to bat first");
                        this.runUserBowlingFirst(pointsToAdd, nrrToAdd);
                    } else if (batBowlChoice == 2) { //if 2 is generated, the team bowls first
                        System.out.println("You lost the toss, " + this.team2.getName() + " opts to bowl first");
                        this.runUserBattingFirst(pointsToAdd, nrrToAdd);
                    }
                } else {
                    this.runComputerFullGame(2, pointsToAdd, nrrToAdd);
                }
            }
        }

        //adjust the points table of the teams
        if (pointsToAdd.value == 1) {
            this.team1.adjustPointsTableData(1, 0);
            this.team2.adjustPointsTableData(1, 0);
        } else {
            this.team1.adjustPointsTableData(pointsToAdd.value, nrrToAdd.value);
            this.team2.adjustPointsTableData(-1 * pointsToAdd.value, -1 * nrrToAdd.value);
        }
    }

    private void runUserBattingFirst(IntWrap pointsToAdd, DoubleWrap nrrToAdd) {
        Scanner intScanner = new Scanner(System.in);
        ArrayList<Player> battingFirstEleven, bowlingFirstEleven;
        int firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsBallsPlayed, secondInnsWicketsLost;
        int firstInnsRuns, secondInnsRuns;
        int bowlerSelection, prevBowlerSelection; //number to determine which bowler is selected to bowl a given over, initially randomly generated
        int batsman1, batsman2, tempBat; //integers pointing to the batsmen in the XI that are at the crease, batsman1 representing batsman on strike
        int ballResult; //stores the result of every ball
        int bowlerChoiceNum; //used when outputting the bowler names for the user to choose from
        ArrayList<Integer> bowlerChoicePointers = new ArrayList<Integer>(); //list to store pointers to the bowlers in the user's XI, corresponding to the choice numbers output to them
        int bowlerChoiceInput; //stores the user's choice for the bowler that they want to bowl for a given over

        String winningTeamName, losingTeamName; //stores the names of the winning and losing teams in order to generate the scorecard
        ArrayList<Player> winningEleven, losingEleven; //stores the winning and losing elevens, used when generating the scorecard

        //assign the correct XIs based on which one is user controlled
        if (this.team1.controlledByUser()) {
            battingFirstEleven = this.playingEleven1;
            bowlingFirstEleven = this.playingEleven2;
        } else {
            battingFirstEleven = this.playingEleven2;
            bowlingFirstEleven = this.playingEleven1;
        }

        //initialise the game stats for each of the players in both teams
        for (int i = 0; i < 11; i++) {
            battingFirstEleven.get(i).initialiseGameStats();
            bowlingFirstEleven.get(i).initialiseGameStats();
        }


        //run the first innings
        firstInnsBallsPlayed = 0;
        firstInnsWicketsLost = 0;
        firstInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to -1 so that any bowler can be selected for the first over
        //set the opening batsman
        batsman1 = 0;
        batsman2 = 1;
        while ((firstInnsBallsPlayed < 120) && (firstInnsWicketsLost < 10)) {
            //select a bowler to bowl the current over
            bowlerSelection = (int)(Math.random() * 5 + 6);
            while ((bowlerSelection == prevBowlerSelection) || (bowlingFirstEleven.get(bowlerSelection).getCurrentBallsBowled() >= 24)) {
                bowlerSelection++;
                //wrap over to the next bowler if at the end of the playing XI
                if (bowlerSelection >= 11) {
                    bowlerSelection = 6;
                }
            }
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(battingFirstEleven.get(batsman1), bowlingFirstEleven.get(bowlerSelection)); //find the result of the ball
                //call the new batsman to the crease if the batsman is dismissed
                if (ballResult == -1) {
                    battingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++; //increment batsman1 to get the next batsman, if they are later in the lineup relative to batsman2
                    } else {
                        batsman1 = batsman2 + 1; //make the on-strike batsman one more than batsman 2, if batsman2 is later in the lineup
                    }
                    //increment both the wickets in the innings and the wickets taken by the bowler
                    firstInnsWicketsLost++;
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //break out of the over early if all 10 wickets are taken
                    if (firstInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add the runs scored to the batsman's tally, runs conceded to the bowlers tally and overall team total
                    firstInnsRuns += ballResult;
                    battingFirstEleven.get(batsman1).incrementCurrentRunsScored(ballResult);
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls bowled by the bowler and the balls faced by the batsman
                firstInnsBallsPlayed++;
                bowlingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();
                battingFirstEleven.get(batsman1).incrementCurrentBallsFaced();

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1 = batsman2;
            batsman2 = tempBat;
        }


        //run the second innings
        secondInnsBallsPlayed = 0;
        secondInnsWicketsLost = 0;
        secondInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to -1 so that any bowler can be selected for the first over
        //set the opening batsmen
        batsman1 = 0;
        batsman2 = 1;
        while ((secondInnsBallsPlayed < 120) && (secondInnsWicketsLost < 10)) {
            //user is now bowling, let the user select the bowler that they want to choose
            System.out.println("Choose the bowler you want to bowl for over number " + Integer.toString((secondInnsBallsPlayed / 6) + 1));
            bowlerChoiceNum = 0;
            //output all the bowler names
            for (int i = 0; i < battingFirstEleven.size(); i++) {
                //first check if the player is a bowler or all-rounder
                if ((battingFirstEleven.get(i).getRole() == Role.BOWLER) || (battingFirstEleven.get(i).getRole() == Role.ALL_ROUNDER)) {
                    //next check if they are eligible to bowl the over (not the previous bowler, and also bowled less than 4 overs)
                    if ((i != prevBowlerSelection) && (battingFirstEleven.get(i).getCurrentBallsBowled() < 24)) {
                        //output the bowler choice if they are a valid choice to bowl the over
                        bowlerChoiceNum++;
                        System.out.println(Integer.toString(bowlerChoiceNum) + ". " + battingFirstEleven.get(i).getName());
                        bowlerChoicePointers.add(i);
                    }
                }
            }
            //forfeit the game for the user if there's no valid choice of bowler
            if (bowlerChoiceNum == 0) {
                System.out.println("You have no bowlers that are suitable to bowl this over. This means you did not select an XI correctly, and therefore forfeit the game.");
                /* This is a placeholder for code that should be executed to indicate that the user's team has lost the game*/
                break;
            }
            //select a bowler choice from the user, with the use of a validation routine
            do {
                System.out.print("Enter a number to choose a bowler you want to bowl: ");
                bowlerChoiceInput = intScanner.nextInt();
                if ((bowlerChoiceInput < 1) || (bowlerChoiceInput > bowlerChoiceNum)) {
                    System.out.println("Please enter a valid number.");
                }
            } while ((bowlerChoiceInput < 1) || (bowlerChoiceInput > bowlerChoiceNum));
            //assign the bowler to bowl the over
            bowlerSelection = bowlerChoicePointers.get(bowlerChoiceInput - 1);
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(bowlingFirstEleven.get(batsman1), battingFirstEleven.get(bowlerSelection));
                //call the new batsman to the crease if the batsman on strike is dismissed
                if (ballResult == -1) {
                    bowlingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++;
                    } else {
                        batsman1 = batsman2 + 1;
                    }
                    //increment the wickets in the game and the wickets taken for the player
                    secondInnsWicketsLost++;
                    battingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //end the over early if all 10 wickets are taken
                    if (secondInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add the runs scored to the batsman's tally, the bowler's runs conceded and the innings total
                    secondInnsRuns += ballResult;
                    bowlingFirstEleven.get(batsman1).incrementCurrentRunsScored(ballResult);
                    battingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls bowled by the bowler and the balls faced by the batsman
                secondInnsBallsPlayed++;
                battingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();
                bowlingFirstEleven.get(batsman1).incrementCurrentBallsFaced();

                //end the over early if the first innings total has been chased down
                if (secondInnsRuns > firstInnsRuns) {
                    break;
                }

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1= batsman2;
            batsman2 = tempBat;
        }


        //evaluate the outcome of the match, and generate the scorecard for the match
        if (firstInnsRuns > secondInnsRuns) { //if this is true, then that means the user won the match
            if (this.team1.controlledByUser()) {
                //if team 1 is the team that's user controlled, then assign them to be the winning team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            } else {
                //if team 2 is the team that's user controlled, then assign them to be the winning team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            }
            //assign the team batting first to be the winning eleven for the scorecard
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        } else if (secondInnsRuns > firstInnsRuns){ //if this is true, then that means the user lost the match
            if (this.team1.controlledByUser()) {
                //if team 1 is the team that's user controlled, then assign them to be the losing team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            } else {
                //if team 2 is the team that's user controlled, then assign them to be the losing team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            }
            //assign the team bowling first to be the winning eleven for the scorecard
            winningEleven = bowlingFirstEleven;
            losingEleven = battingFirstEleven;
        } else { //if the 'else' statement runs, then that means that the match is a tie
            //the fact that 'winning' and 'losing' are used is irrelevant; the match is tied
            winningTeamName = this.team1.getName();
            losingTeamName = this.team2.getName();
            //return the points table data via the type wrappers
            pointsToAdd.value = 1;
            nrrToAdd.value = 0;
            //assign the team batting first to be the winning eleven for the scorecard; even tho it's arbitrary since it's a tie
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        }
        //generate the match scorecard
        this.matchScorecard = new ScorecardDetails(winningEleven, losingEleven, winningTeamName, losingTeamName, firstInnsRuns,
                firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsRuns, secondInnsBallsPlayed,
                secondInnsWicketsLost);
    }

    private void runUserBowlingFirst(IntWrap pointsToAdd, DoubleWrap nrrToAdd) {
        Scanner intScanner = new Scanner(System.in);
        ArrayList<Player> battingFirstEleven, bowlingFirstEleven;
        int firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsBallsPlayed, secondInnsWicketsLost;
        int firstInnsRuns, secondInnsRuns;
        int bowlerSelection, prevBowlerSelection; //number to determine which bowler is selected to bowl a given over, initially randomly generated
        int batsman1, batsman2, tempBat; //integers pointing to the batsmen in the XI that are at the crease, batsman1 representing batsman on strike
        int ballResult; //stores the result of every ball
        int bowlerChoiceNum; //used when outputting the bowler names for the user to choose from
        ArrayList<Integer> bowlerChoicePointers = new ArrayList<Integer>(); //list to store pointers to the bowlers in the user's XI, corresponding to the choice numbers output to them
        int bowlerChoiceInput; //stores the user's choice for the bowler that they want to bowl for a given over

        String winningTeamName, losingTeamName; //stores the names of the winning and losing teams in order to generate the scorecard
        ArrayList<Player> winningEleven, losingEleven; //stores the winning and losing elevens used for generating the scorecard


        //assign the correct XIs based on which one is user controlled
        if (this.team1.controlledByUser()) {
            bowlingFirstEleven = this.playingEleven1;
            battingFirstEleven = this.playingEleven2;
        } else {
            bowlingFirstEleven = this.playingEleven2;
            battingFirstEleven = this.playingEleven1;
        }

        //initialise the game stats for each of the players in both teams
        for (int i = 0; i < 11; i++) {
            battingFirstEleven.get(i).initialiseGameStats();
            bowlingFirstEleven.get(i).initialiseGameStats();
        }

        //run the first innings
        firstInnsBallsPlayed = 0;
        firstInnsWicketsLost = 0;
        firstInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to an invalid value to indicate that all bowlers can bowl
        //set the opening batsmen
        batsman1 = 0;
        batsman2 = 1;
        while ((firstInnsBallsPlayed < 120) && (firstInnsWicketsLost < 10)) {
            //user is now bowling, let the user select the bowler that they want to bowl
            System.out.println("Choose the bowler that you want to bowl for over number " + Integer.toString((firstInnsBallsPlayed / 6) + 1));
            bowlerChoiceNum = 0;
            //output the names of the bowlers that can bowl
            for (int i = 0; i < bowlingFirstEleven.size(); i++) {
                //first check that the player is a bowler or an all-rounder
                if ((bowlingFirstEleven.get(i).getRole() == Role.BOWLER) || (bowlingFirstEleven.get(i).getRole() == Role.ALL_ROUNDER)) {
                    //next check whether the bowler is not the same as the bowler that bowled the previous bowler, and that they still have overs left
                    if ((i != prevBowlerSelection) && (bowlingFirstEleven.get(i).getCurrentBallsBowled() < 24)) {
                        //output the bowler's name if they are a valid choice to bowl the over
                        bowlerChoiceNum++;
                        System.out.println(Integer.toString(bowlerChoiceNum) + ". " + bowlingFirstEleven.get(i).getName());
                        bowlerChoicePointers.add(i);
                    }
                }
            }
            //forfeit the game for the user if there's no valid choice of bowler
            if (bowlerChoiceNum == 0) {
                System.out.println("You have no bowlers that are suitable to bowl this over. This means you did not select an XI correctly, and therefore forfeit the game.");
                /* This is a placeholder for code that should be executed to indicate that the user's team has lost the game*/
                break;
            }
            //input the bowler choice from the user, with the use of a validation routine
            do {
                System.out.print("Enter a number to choose a bowler you want to bowl: ");
                bowlerChoiceInput = intScanner.nextInt();
                if ((bowlerChoiceInput < 1) || (bowlerChoiceInput > bowlerChoiceNum)) {
                    System.out.println("Please enter a valid number.");
                }
            } while ((bowlerChoiceInput < 1) || (bowlerChoiceInput > bowlerChoiceNum));
            //assign the bowler to bowl the over
            bowlerSelection = bowlerChoicePointers.get(bowlerChoiceInput - 1);
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(battingFirstEleven.get(batsman1), bowlingFirstEleven.get(bowlerSelection));
                //call the new batsman to the crease if the batsman on strike is dismissed
                if (ballResult == -1) {
                    battingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++;
                    } else {
                        batsman1 = batsman2 + 1;
                    }
                    //increment the wickets taken for the innings and also for the bowler
                    firstInnsWicketsLost++;
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //end the over early if all 10 wickets are taken
                    if (firstInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add the runs scored to the innings total, the batsman's tally, and the bowler's runs conceded
                    firstInnsRuns += ballResult;
                    battingFirstEleven.get(batsman1).incrementCurrentRunsConceded(ballResult);
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls faced by the batsman and the balls bowled by the bowler
                firstInnsBallsPlayed++;
                battingFirstEleven.get(batsman1).incrementCurrentBallsFaced();
                bowlingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1 = batsman2;
            batsman2 = tempBat;
        }


        //run the second innings
        secondInnsBallsPlayed = 0;
        secondInnsWicketsLost = 0;
        secondInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to an invalid value to indicate that any bowler can bowl a given over
        //set up the opening batsmen
        batsman1 = 0;
        batsman2 = 1;
        while ((secondInnsBallsPlayed < 120) && (secondInnsWicketsLost < 10)) {
            //select a bowler to bowl the current over
            bowlerSelection = (int)(Math.random() * 5 + 6);
            while ((bowlerSelection == prevBowlerSelection) || (battingFirstEleven.get(bowlerSelection).getCurrentBallsBowled() >= 24)) {
                bowlerSelection++;
                //wrap over to the next bowler if at the end of the playing XI
                if (bowlerSelection >= 11) {
                    bowlerSelection = 6;
                }
            }
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(bowlingFirstEleven.get(batsman1), battingFirstEleven.get(bowlerSelection));
                //call the new batsman to the crease if the batsman on strike is dismissed
                if (ballResult == -1) {
                    bowlingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++;
                    } else {
                        batsman1 = batsman2 + 1;
                    }
                    //increment the wickets for the innings, and the wickets taken by the bowler
                    secondInnsWicketsLost++;
                    battingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //end the over early if all 10 wickets are gone
                    if (secondInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add the runs scored to the innings total, the batsman's tally and the bowler's total runs conceded
                    secondInnsRuns += ballResult;
                    bowlingFirstEleven.get(batsman1).incrementCurrentRunsScored(ballResult);
                    battingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls faced by the batsman and the balls bowled by the bowler
                secondInnsBallsPlayed++;
                bowlingFirstEleven.get(batsman1).incrementCurrentBallsFaced();
                battingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();

                //end the over early if the first innings total is chased down
                if (secondInnsRuns > firstInnsRuns) {
                    break;
                }

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1 = batsman2;
            batsman2 = tempBat;
        }


        //evaluate the outcome of the match, and generate the scorecard for the match
        if (firstInnsRuns > secondInnsRuns) { //if this condition is true, then that means the user lost the match
            if (this.team1.controlledByUser()) {
                //if team 1 is the one that's user controlled then assign them to be the losing team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            } else {
                //if team 2 is the one that's user controlled, then assign them to be the losing team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            }
            //assign the team batting first to be the winning eleven
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        } else if (secondInnsRuns > firstInnsRuns) { //if this condition is true, then that means the user won the match
            if (this.team1.controlledByUser()) {
                //if team 1 is the one that's user controlled then assign them to be the winning team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            } else {
                //if team 2 is the one that's user controlled, then assign them to be the winning team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            }
            //assign the team bowling first to be the winning eleven
            winningEleven = bowlingFirstEleven;
            losingEleven = battingFirstEleven;
        } else { //if the 'else' part of the if statement is run, then that means the match is tied
            //the fact that they are called 'winning' and 'losing' is irrelevant; the match is tied
            winningTeamName = this.team1.getName();
            losingTeamName = this.team2.getName();
            //return the points table data via the type wrappers
            pointsToAdd.value = 1;
            nrrToAdd.value = 0;
            //assign the team batting first to be the winning eleven, even though it's arbitrary since the match is tied
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        }
        //generate the match scorecard
        this.matchScorecard = new ScorecardDetails(winningEleven, losingEleven, winningTeamName, losingTeamName, firstInnsRuns,
                firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsRuns, secondInnsBallsPlayed,
                secondInnsWicketsLost);
    }

    private void runComputerFullGame(int numBatFirst, IntWrap pointsToAdd, DoubleWrap nrrToAdd) {
        ArrayList<Player> battingFirstEleven, bowlingFirstEleven;
        int firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsBallsPlayed, secondInnsWicketsLost;
        int firstInnsRuns, secondInnsRuns;
        int bowlerSelection, prevBowlerSelection; //number to determine which bowler is selected to bowl a given over, initially randomly generated
        int batsman1, batsman2, tempBat; //integers pointing to the batsmen in the XI that are at the crease, batsman1 representing batsman on strike
        int ballResult; //stores the result of every ball

        String winningTeamName, losingTeamName; //stores the names of the winning and losing teams in order to generate the scorecard
        ArrayList<Player> winningEleven, losingEleven; //stores the winning and losing elevens, used for generating the scorecard

        //assign the correct XIs based on the number parameter, indicating the team batting first
        if (numBatFirst == 1) {
            battingFirstEleven = this.playingEleven1;
            bowlingFirstEleven = this.playingEleven2;
        } else {
            battingFirstEleven = this.playingEleven2;
            bowlingFirstEleven = this.playingEleven1;
        }

        //initialise the game stats for each of the players in both teams
        for (int i = 0; i < 11; i++) {
            battingFirstEleven.get(i).initialiseGameStats();
            bowlingFirstEleven.get(i).initialiseGameStats();
        }

        //run the first innings
        firstInnsBallsPlayed = 0;
        firstInnsWicketsLost = 0;
        firstInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to an invalid value to indicate that any bowler can bowl a given over
        //set up the opening batsmen
        batsman1 = 0;
        batsman2 = 1;
        while ((firstInnsBallsPlayed < 120) && (firstInnsWicketsLost < 10)) {
            //select a bowler to bowl the current over
            bowlerSelection = (int)(Math.random() * 5 + 6);
            while ((bowlerSelection == prevBowlerSelection) || (bowlingFirstEleven.get(bowlerSelection).getCurrentBallsBowled() >= 24)) {
                bowlerSelection++;
                //wrap over to the next bowler if at the end of the playing XI
                if (bowlerSelection >= 11) {
                    bowlerSelection = 6;
                }
            }
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(battingFirstEleven.get(batsman1), bowlingFirstEleven.get(bowlerSelection));
                //call the new batsman to the crease if the batsman on strike is dismissed
                if (ballResult == -1) {
                    battingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++;
                    } else {
                        batsman1 = batsman2 + 1;
                    }
                    //increment the wickets taken in the innings and the wickets taken by the bowler
                    firstInnsWicketsLost++;
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //end the over early if all 10 wickets are gone
                    if (firstInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add the runs scored to the total runs for the innings, the batsman's tally and the bowler's runs conceded
                    firstInnsRuns += ballResult;
                    battingFirstEleven.get(batsman1).incrementCurrentRunsScored(ballResult);
                    bowlingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls faced by the batsman and the bowler's balls bowled
                firstInnsBallsPlayed++;
                battingFirstEleven.get(batsman1).incrementCurrentBallsFaced();
                bowlingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1 = batsman2;
            batsman2 = tempBat;
        }


        //run the second innings
        secondInnsBallsPlayed = 0;
        secondInnsWicketsLost = 0;
        secondInnsRuns = 0;
        prevBowlerSelection = -1; //initialised to an invalid value to indicate that any bowler can bowl the given over
        //set up the opening batsmen
        batsman1 = 0;
        batsman2 = 1;
        while ((secondInnsBallsPlayed < 120) && (secondInnsWicketsLost < 10)) {
            //select a bowler to bowl the current over
            bowlerSelection = (int)(Math.random() * 5 + 6);
            while ((bowlerSelection == prevBowlerSelection) || (battingFirstEleven.get(bowlerSelection).getCurrentBallsBowled() >= 24)) {
                bowlerSelection++;
                //wrap over to the next bowler if at the end of the playing XI
                if (bowlerSelection >= 11) {
                    bowlerSelection = 6;
                }
            }
            prevBowlerSelection = bowlerSelection;

            //run the over
            for (int i = 1; i <= 6; i++) {
                ballResult = this.determineBallResult(bowlingFirstEleven.get(batsman1), battingFirstEleven.get(bowlerSelection));
                //call the new batsman to the crease if the batsman on strike is dismissed
                if (ballResult == -1) {
                    bowlingFirstEleven.get(batsman1).dismiss();
                    if (batsman1 > batsman2) {
                        batsman1++;
                    } else {
                        batsman1 = batsman2 + 1;
                    }
                    //increment the wickets taken in the innings and the wickets taken by the bowler
                    secondInnsWicketsLost++;
                    battingFirstEleven.get(bowlerSelection).incrementCurrentWickets();
                    //end the over early if all 10 wickets are gone
                    if (secondInnsWicketsLost >= 10) {
                        break;
                    }
                } else {
                    //if it's not a wicket, add on the runs scored to the innings total, the batsman's tally and the bowler's runs conceded
                    secondInnsRuns += ballResult;
                    bowlingFirstEleven.get(batsman1).incrementCurrentRunsScored(ballResult);
                    battingFirstEleven.get(bowlerSelection).incrementCurrentRunsConceded(ballResult);
                }

                //increment the balls bowled in the innings, the balls faced by the batsman and the balls bowled by the bowler
                secondInnsBallsPlayed++;
                bowlingFirstEleven.get(batsman1).incrementCurrentBallsFaced();
                battingFirstEleven.get(bowlerSelection).incrementCurrentBallsBowled();

                //if the first innings total has been chased down, end the over early
                if (secondInnsRuns > firstInnsRuns) {
                    break;
                }

                //switch the strike if an odd number of runs was scored
                if (ballResult % 2 == 1) {
                    tempBat = batsman1;
                    batsman1 = batsman2;
                    batsman2 = tempBat;
                }
            }

            //swap the strike at the end of the over
            tempBat = batsman1;
            batsman1 = batsman2;
            batsman2 = tempBat;
        }


        //evaluate the outcome of the match
        if (firstInnsRuns > secondInnsRuns) { //if this is true, then the team batting first won
            if (numBatFirst == 1) {
                //if the team batting first is team number 1, assign team number 1 to be the winning team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            } else {
                //if the team batting first is team number 2, assign team number 2 to be the winning team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            }
            //assign the team batting first to be the winning eleven
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        } else if (secondInnsRuns > firstInnsRuns) { //if this is true, then the team batting first lost
            if (numBatFirst == 1) {
                //if the team batting first is team number 1, assign team number 1 to be the losing team
                losingTeamName = this.team1.getName();
                winningTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = -2;
                nrrToAdd.value = (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6)) - (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6));
            } else {
                //if the team batting first is team number 2, assign team number 2 to be the losing team
                winningTeamName = this.team1.getName();
                losingTeamName = this.team2.getName();
                //return the points table data via the type wrappers
                pointsToAdd.value = 2;
                nrrToAdd.value = (((double)(secondInnsRuns)) / (((double)(secondInnsBallsPlayed)) / 6)) - (((double)(firstInnsRuns)) / (((double)(firstInnsBallsPlayed)) / 6));
            }
            //assign the team bowling first to be the winning eleven
            winningEleven = bowlingFirstEleven;
            losingEleven = battingFirstEleven;
        } else { //if the 'else' statement runs then that means the match is tied
            //the fact that 'winning' and 'losing' are used is irrelevant; the match is tied
            winningTeamName = this.team1.getName();
            losingTeamName = this.team2.getName();
            //return the points table data via the type wrappers
            pointsToAdd.value = 1;
            nrrToAdd.value = 0;
            //assign the team batting first to be the winning eleven, even though it's arbitrary since the match is tied
            winningEleven = battingFirstEleven;
            losingEleven = bowlingFirstEleven;
        }
        //generate the match scorecard
        this.matchScorecard = new ScorecardDetails(winningEleven, losingEleven, winningTeamName, losingTeamName, firstInnsRuns,
                firstInnsBallsPlayed, firstInnsWicketsLost, secondInnsRuns, secondInnsBallsPlayed,
                secondInnsWicketsLost);
    }

    //helper function to determine the result of a ball being bowled, returns -1 for a wicket
    private int determineBallResult(Player batsman, Player bowler) {
        ArrayList<Double> baseProbabilities = new ArrayList<Double>(); //stores the base probabilities of each ball outcome
        double baseProbabilitiesSum, cumulativeSum, newProbability;
        ArrayList<Double> newCumulativeProbabilities = new ArrayList<Double>();
        double bowlerRunsPerBall, batsmanRunsPerBall, avgRunsPerBall;
        double bowlerWktsPerBall, batsmanWktsPerBall, avgWktsPerBall;
        double maxProbabilityIncreaseFactor = 2.0; //the maximum factor by which the base probability is allowed to be multiplied via the player stats
        double randNum; //number randomly generated to determine the ball outcome

        bowlerRunsPerBall = bowler.getEconomy() / 6; //calculate the runs conceded per ball by the bowler
        batsmanRunsPerBall = batsman.getStrikeRate() / 100; //calculate the runs scored per ball by the batsman
        avgRunsPerBall = (bowlerRunsPerBall + batsmanRunsPerBall) / 2; //calculate the average

        bowlerWktsPerBall = bowlerRunsPerBall / bowler.getBowlingAvg(); //calculate the wickets taken per ball by the bowler
        batsmanWktsPerBall = batsmanRunsPerBall / batsman.getBattingAvg(); //calculate the number of times out per ball by the batsman
        avgWktsPerBall = (bowlerWktsPerBall + batsmanWktsPerBall) / 2; //calculate the average

        //add the base probabilities to the array list; representing the initial probability of each ball outcome regardless of player stats
        baseProbabilities.add(0.33); //probability of a dot ball
        baseProbabilities.add(0.34); //probability of a single
        baseProbabilities.add(0.12); //probability of a double
        baseProbabilities.add(0.01); //probability of a three
        baseProbabilities.add(0.08); //probability of a four
        baseProbabilities.add(0.07); //probability of a six
        baseProbabilities.add(0.05); //probability of a wicket

        //recalculate the probabilities based on the player statistics, for the run outcomes
        baseProbabilitiesSum = 0;
        for (int i = 0; i < 6; i++) {
            //calculate the new probability based on player stats using the formula, ensuring that 6 is used for the last element
            if (i == 5) {
                //ensure that the factor by which the probability increases does not exceed the max probability factor
                if (1 / Math.abs(avgRunsPerBall - 6) < maxProbabilityIncreaseFactor) {
                    newProbability = baseProbabilities.get(i) * (1 / Math.abs(avgRunsPerBall - 6));
                } else {
                    newProbability = baseProbabilities.get(i) * 2;
                }
            } else {
                //ensure that the factor by which the probability increases does not exceed the max probability factor
                if (1 / Math.abs(avgRunsPerBall - i) < maxProbabilityIncreaseFactor) {
                    newProbability = baseProbabilities.get(i) * (1 / Math.abs(avgRunsPerBall - i));
                } else {
                    newProbability = baseProbabilities.get(i) * 2;
                }
            }
            baseProbabilities.set(i, newProbability);
            baseProbabilitiesSum += newProbability; //add on to the sum for weighted probabilities
        }
        newProbability = baseProbabilities.get(6) * (0.7 + (10 * avgWktsPerBall));
        baseProbabilities.set(6, newProbability); //calculate wicket probability based on wicket stats
        baseProbabilitiesSum += newProbability; //add on to the sum ofr weighted probabilities

        //calculate the weighted, cumulative probabilities, which will be the final probabilities
        cumulativeSum = 0.0;
        newCumulativeProbabilities.add(0.0);
        for (int i = 0; i < 7; i++) {
            newCumulativeProbabilities.add((baseProbabilities.get(i) / baseProbabilitiesSum) + cumulativeSum);
            cumulativeSum += (baseProbabilities.get(i) / baseProbabilitiesSum);
        }

        //generate the ball result
        randNum = Math.random(); //generate random floating point between zero and one
        for (int i = 0; i < 7; i++) {
            //loop through the cumulative probabilities, checking if the numbers is between the current element and the subsequent element
            if ((randNum > newCumulativeProbabilities.get(i)) && (randNum < newCumulativeProbabilities.get(i + 1))) {
                switch (i) {
                    case 0, 1, 2, 3, 4:
                        return i;
                    case 5:
                        return 6;
                    case 6:
                        return -1; //return a wicket
                }
            }
        }

        throw new RuntimeException("Error generating ball result");
    }


    //method to get the pointer of the winning team in the teams array in the main class, for generating the playoff matches
    public int getWinningPointer(ArrayList<TeamFranchise> teams) {
        String winningTeamName = this.matchScorecard.getWinningTeamName();

        if (this.matchScorecard.getVictoryMargin() == 0) {
            return -1; //return -1 to indicate that the match was a tie
        }

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(winningTeamName)) {
                return i;
            }
        }

        throw new RuntimeException("Can't find winning team");
    }


    //method to get the pointer of the losing team in the teams array in the main class, for generating the playoff matches
    public int getLosingPointer(ArrayList<TeamFranchise> teams) {
        String losingTeamName = this.matchScorecard.getLosingTeamName();

        if (this.matchScorecard.getVictoryMargin() == 0) {
            return -1; //return -1 to indicate that the match was a tie
        }

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(losingTeamName)) {
                return i;
            }
        }

        throw new RuntimeException("Can't find losing team");
    }
}

