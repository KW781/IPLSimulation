package SimulationMain;

import TypeWrappers.BooleanWrap;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserTeamFranchise extends TeamFranchise {
    public UserTeamFranchise(String franchiseName) {
        super(franchiseName);
        /*instantiate the CurrentPlayingEleven attribute for user controlled team, so that an appropriate check can be made
        before asking the user if they want to select the previous XI for a game*/
        this.currentPlayingEleven = new ArrayList<Player>();
    }

    /**
     * Method to bid for a player that's currently being auctioned. This method is for the USER controlled franchise
     * and so bids based on input from the user.
     * @param biddingPlayer The player currently being auctioned
     * @param continueBidding A BooleanWrap object, whose value indicates whether the user wants to continue bidding
     *                        for the player currently being auctioned. Set to false if the user indicates they want to
     *                        stop bidding
     * @return Boolean indicating whether the franchise bid for the player or not
     */
    public boolean bid(Player biddingPlayer, BooleanWrap continueBidding) {
        String playerName = biddingPlayer.getName();
        double currentBidPrice = biddingPlayer.getPrice();
        Scanner bidScan = new Scanner(System.in);
        double bidValue = -10; //initialise bidValue to an invalid value
        boolean valid = false;

        System.out.println("Enter the bid you would like to place for " + playerName + ".");
        System.out.println("If don't want to place a bid, enter 0.");
        System.out.print("If you no longer want to make any bids for this player, enter -1: ");
        //validation algorithm for the bid value entered by the user
        do {
            try {
                bidValue = bidScan.nextDouble();
            } catch (InputMismatchException error) {
                valid = false;
            }

            //check whether the bid value is a valid number and that it's greater than the current bid price
            if ((bidValue == -1) || ( bidValue >= 0)) {
                if (bidValue > currentBidPrice) {
                    valid = true;
                }
            }

            if (bidValue == -10) {
                System.out.print("Please enter a valid number for the bid: ");
            } else if (bidValue <= currentBidPrice) {
                System.out.print("Please enter a bid that's greater than the current bid: ");
            }
        } while (!valid);

        if (bidValue == 0) {
            return false;
        } else if (bidValue == -1) {
            continueBidding.value = false;
            return false;
        } else {
            biddingPlayer.newBid(bidValue);
            return true;
        }
    }


    /**
     * Selects the playing XI from the team franchise's squad. It does so by collecting user input for each player they
     * would like to play, and also validates the user input by checking they've selected a wicketkeeper and that
     * they've selected at least 5 bowlers/all-rounders. The user also has the option to select the same playing XI
     * as the previous game if they've already played at least one game.
     * @return An ArrayList of players, constituting the playing XI selected by the team franchise
     */
    public ArrayList<Player> selectPlayingEleven() {
        ArrayList<Player> playingEleven = new ArrayList<Player>();
        Scanner intScanner = new Scanner(System.in);
        int samePlayingEleven;
        int playerNum;
        ArrayList<Integer> playerNumArray = new ArrayList<Integer>();
        boolean wicketkeeperSelected;
        boolean samePlayer, playerInSquad, validPlayer;
        int overseasCount; //counter for number of overseas players in the XI
        int numBowlers; //number of players who can bowl (including all-rounders) in the XI

        //first ask the user if they want to play the same XI as the previous game, assuming it's not the first game
        samePlayingEleven = 0;
        if (this.currentPlayingEleven.size() != 0) { //check that it's not the first game
            //validation loop for the user entry
            do {
                System.out.println("Which option would you like to choose for playing XI for this game?");
                System.out.println("1. Choose same playing XI as previous game");
                System.out.println("2. Choose new playing XI");
                System.out.print("Enter the number: ");
                samePlayingEleven = intScanner.nextInt();
            } while ((samePlayingEleven != 1) && (samePlayingEleven != 2));
        }
        //if the user wants to play the same XI as last game, return the current playing XI and end the method
        if (samePlayingEleven == 2) {
            return this.currentPlayingEleven;
        }

        System.out.println("Time to select your playing XI! Here is your squad, choose your playing XI based on the " +
                "player numbers. Remember to select a wicketkeeper, and to ensure you have a maximum of 4 overseas" +
                "players.");
        for (int i = 0; i < this.squad.size(); i++) {
            System.out.print(Integer.toString(i + 1) + ". " + this.squad.get(i).getName()); //output the player name
            //add on the wicketkeeper tag if the player is a wicketkeeper
            if (this.squad.get(i).getRole() == Role.WICKETKEEPER) {
                System.out.print(" (wk)");
            }
            //add on the overseas tag if the player is overseas
            if (this.squad.get(i).isOverseas()) {
                System.out.print(" (O)");
            }
            System.out.println();
        }

        //enter all the player numbers from the user, ensuring that a wicketkeeper is selected through a validation loop
        wicketkeeperSelected = false;
        overseasCount = 0;
        numBowlers = 0;
        do {
            for (int i = 1; i <= 11; i++) {
                //use a validation loop to ensure the same player isn't already selected
                do {
                    validPlayer = true;
                    System.out.print("Enter player " + Integer.toString(i) + ": ");
                    playerNum = intScanner.nextInt() - 1;
                    playerInSquad = ((playerNum >= 1) && (playerNum <= this.squad.size())); //check whether the player number is a valid number
                    if (playerInSquad) {
                        samePlayer = false;
                        for (Integer num : playerNumArray) {
                            //if player already in playing XI, break, and inform the user
                            if (playerNum == num) {
                                samePlayer = true;
                                break;
                            }
                        }
                        if (samePlayer) {
                            System.out.println("Player already selected in the playing XI!");
                            validPlayer = false;
                        } else {
                            if (this.squad.get(playerNum).isOverseas() && (overseasCount == 4)) {
                                System.out.println("You aren't allowed any more overseas players!");
                                validPlayer = false;
                            }
                        }
                    } else {
                        validPlayer = false; //player is not valid if the number is not a valid squad number
                    }
                } while (!validPlayer);
                playerNumArray.add(playerNum); //add the player number to the array once validated
                //if a wicketkeeper was just selected, then set the validation flag for that to true
                if (this.squad.get(playerNum).getRole() == Role.WICKETKEEPER) {
                    wicketkeeperSelected = true;
                }
                //if an overseas player was just selected, then increment the overseas count
                if (this.squad.get(playerNum).isOverseas()) {
                    overseasCount++;
                }
                //if a bowler or all-rounder was selected, increment the count for it
                if ((this.squad.get(playerNum).getRole() == Role.BOWLER) || (this.squad.get(playerNum).getRole() == Role.ALL_ROUNDER)) {
                    numBowlers++;
                }
            }
            if (!wicketkeeperSelected) {
                System.out.println("You must select a wicketkeeper"); //inform the user if a wicketkeeper wasn't selected
                playerNumArray = new ArrayList<Integer>(); //reset the player num array
            }
            if (numBowlers < 5) {
                System.out.println("You must select at least 5 bowlers/all-rounders to bowl 20 overs");
                playerNumArray = new ArrayList<Integer>();
            }
        } while (!wicketkeeperSelected && (numBowlers < 5));

        //add all the selected players to the XI using the player numbers
        for (Integer num : playerNumArray) {
            playingEleven.add(this.squad.get(num)); //subtract one because the numbers in the array are 1-indexed
        }
        this.currentPlayingEleven = playingEleven;

        return playingEleven;
    }
}
