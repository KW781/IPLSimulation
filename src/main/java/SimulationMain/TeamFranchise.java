package SimulationMain;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import TypeWrappers.*;

public class TeamFranchise {
    //fundamental team data
    private final boolean userControlled;
    private final String name;
    private double purse; //measured in lakhs
    private ArrayList<Player> squad;

    private ArrayList<Player> currentPlayingEleven; //used to track the playing XI played in each game

    //points table attributes
    private int points;
    private double netRunRate;

    /**
     * Creates a TeamFranchise object, which can be either user controlled or computer controlled.
     * @param franchiseName The name of the franchise
     * @param userControlled Boolean indicating whether the franchise is controlled by the user or not
     */
    public TeamFranchise(String franchiseName, boolean userControlled) {
        this.name = franchiseName;
        this.purse = 9000;
        this.userControlled = userControlled;
        this.netRunRate = 0;
        this.points = 0;
        this.squad = new ArrayList<Player>(); //instantiate the squad attribute
        /*instantiate the CurrentPlayingEleven attribute if the team is user controlled, so that an appropriate check can be made
        before asking the user if they want to select the previous XI for a game*/
        if (this.userControlled) {
            this.currentPlayingEleven = new ArrayList<Player>();
        }
    }

    /**
     * Method to bid for a player that's currently being auctioned. Uses an algorithm that takes into consideration the
     * player's stats, the current balance of the team's squad, and the current purse of the team franchise. This method
     * is for a franchise that is controlled by the COMPUTER.
     * @param biddingPlayer The player currently being auctioned
     * @return Boolean indicating whether the franchise bid for the player or not
     * @throws IllegalArgumentException If the team franchise is a user controlled franchise
     */
    public boolean bid(Player biddingPlayer) {
        //throw an exception if the second argument is not given and it's a user controlled franchise
        if (this.userControlled) {
            throw new IllegalArgumentException("Requires 'Continue Bidding' boolean wrap argument for the user controlled franchise");
        }

        Role biddingRole = biddingPlayer.getRole();
        double currentBidPrice = biddingPlayer.getPrice();
        double batsmanRatio, bowlerRatio, allRounderRatio, wicketkeeperRatio, overseasRatio;

        double maxPrice = 0; //the maximum price the franchise is willing to spend on current player being auctioned
        double battingMaxPrice, bowlingMaxPrice;

        //A random factor to create a form of unpredictability in the auction process
        final double RANDOM_FACTOR_MAGNITUDE = 0.4;
        double randomFactor = (RANDOM_FACTOR_MAGNITUDE * 2) * Math.random() + (1 - RANDOM_FACTOR_MAGNITUDE);

        switch (biddingRole) {
            case WICKETKEEPER:
                maxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg());
                if (this.squad.size() >= 10) { //will only start considering the ratio of roles in the squad once size >= 10
                    wicketkeeperRatio = ((double)this.countRoleInSquad(Role.WICKETKEEPER)) / this.squad.size();
                    if (wicketkeeperRatio < (1.0/11)) {
                        maxPrice *= 1.1; //increase the max price by 10% if the wicketkeeper ratio is too less
                    } else if (wicketkeeperRatio > (2.5/11)) {
                        maxPrice *= 0.9; //decrease the max price by 10% if the wicketkeeper ratio is too much
                    }
                }
            case BATSMAN:
                maxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg());
                if (this.squad.size() >= 10) { //will only start considering the ratio of roles in the squad once size >= 10
                    batsmanRatio = ((double)this.countRoleInSquad(Role.BATSMAN)) / this.squad.size();
                    if (batsmanRatio < (3.0/11)) {
                        maxPrice *= 1.2; //increase max price by 20% if batsman ratio is less than 3 for every 11
                    } else if (batsmanRatio < (5.0/11)) {
                        maxPrice *= 1.1; //increase max price by 10% if batsman ratio is less than 5 for every 11
                    } else if (batsmanRatio > (9.0/11)) {
                        maxPrice *= 0.8; //decrease max price by 20% if batsman ratio is more than 9 for every 11
                    } else if (batsmanRatio > (7.0/11)) {
                        maxPrice *= 0.9; //decrease max price by 10% if batsman ratio is more than 7 for every 11
                    }
                }
            case BOWLER:
                maxPrice = this.calculateBowlerStatsPrice(biddingPlayer.getEconomy(), biddingPlayer.getBowlingAvg());
                if (this.squad.size() >= 10) {
                    bowlerRatio = ((double)this.countRoleInSquad(Role.BOWLER)) / this.squad.size();
                    if (bowlerRatio < (3.0/11)) {
                        maxPrice *= 1.2; //increase max price by 20% if batsman ratio is less than 3 for every 11
                    } else if (bowlerRatio < (5.0/11)) {
                        maxPrice *= 1.1; //increase max price by 10% if bowler ratio is less than 5 for every 11
                    } else if (bowlerRatio > (9.0/11)) {
                        maxPrice *= 0.8; //decrease max price by 20% if bowler ratio is more than 9 for every 11
                    } else if (bowlerRatio > (7.0/11)) {
                        maxPrice *= 0.9; //decrease max price by 10% if bowler ratio is more than 7 for every 11
                    }
                }
            case ALL_ROUNDER:
                battingMaxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg());
                bowlingMaxPrice = this.calculateBowlerStatsPrice(biddingPlayer.getEconomy(), biddingPlayer.getBowlingAvg());
                maxPrice = (battingMaxPrice + bowlingMaxPrice) / 2; //calculate average of batting and bowling max prices
                if (this.squad.size() >= 10) {
                    allRounderRatio = ((double)this.countRoleInSquad(Role.ALL_ROUNDER)) / this.squad.size();
                    if (allRounderRatio < (1.0/11)) {
                        maxPrice *= 1.1; //increase max price by 10% if all-rounder ratio is too less
                    } else {
                        maxPrice *= 0.9; //decrease max price by 10% if all-rounder ratio is too much
                    }
                }
        }

        //account for overseas player ratio in squad for calculating max price
        if ((biddingPlayer.isOverseas()) && (this.squad.size() >= 10)) {
            overseasRatio = ((double)this.countOverseasInSquad()) / this.squad.size();
            if (overseasRatio >= (4.0/11)) {
                maxPrice *= 0.5;
            } else if (overseasRatio >= (3.5/11)) {
                maxPrice *= 0.6;
            } else if (overseasRatio >= (3.0/11)) {
                maxPrice *= 0.7;
            } else if (overseasRatio >= (2.5/11)) {
                maxPrice *= 0.8;
            } else if (overseasRatio >= (2.0/11)) {
                maxPrice *= 0.85;
            }
        }
        maxPrice *= randomFactor; //apply the random factor to the max price

        if (maxPrice > (currentBidPrice + 20)) {
            maxPrice = currentBidPrice + 20; //add on increment to current bid price if willing to pay more
            if (this.purse > maxPrice) {
                biddingPlayer.newBid(maxPrice);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Method to bid for a player that's currently being auctioned. This method is for the USER controlled franchise
     * and so bids based on input from the user.
     * @param biddingPlayer The player currently being auctioned
     * @param continueBidding A BooleanWrap object, whose value indicates whether the user wants to continue bidding
     *                        for the player currently being auctioned. Set to false if the user indicates they want to
     *                        stop bidding
     * @return Boolean indicating whether the franchise bid for the player or not
     * @throws IllegalArgumentException If the team franchise is a computer controlled franchise
     */
    public boolean bid(Player biddingPlayer, BooleanWrap continueBidding) {
        //throw an exception if the 'continueBidding' argument is passed for a computer based franchise
        if (!this.userControlled) {
            throw new IllegalArgumentException("Only one argument should be passed for a computer based franchise");
        }

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
     * Selects the playing XI from the team franchise's squad. If the team is user controlled, it does so by collecting
     * user input. If the team is computer controlled, it uses an algorithm that chooses an XI by randomly generating
     * pointers at players in the squad (one pointer for each role), then iterating the pointers for each role until an
     * XI is selected.
     * @return An ArrayList of players, constituting the playing XI selected by the team franchise
     * @throws RuntimeException If there are not enough players of a particular role to select a playing XI (e.g. not
     *                          enough batsmen)
     */
    public ArrayList<Player> selectPlayingEleven() {
        ArrayList<Player> playingEleven = new ArrayList<Player>();
        Scanner intScanner = new Scanner(System.in);
        int samePlayingEleven;
        int playerNum;
        ArrayList<Integer> playerNumArray = new ArrayList<Integer>();
        boolean wicketkeeperSelected, allRounderSelected;
        boolean samePlayer, playerInSquad, validPlayer;
        int overseasCount; //counter for number of overseas players in the XI

        int passes; //keep track of the number of passes made through the squad when selecting any role, to avoid infinite loop
        int batPointer, bowlPointer, wkPointer, allRoundPointer; //pointers to point to players in squad when automatically generating an XI, initially randomly generated
        int numBatsmen, numBowlers; //keep track of the number of batsmen and bowlers that have been selected in the XI, when automatically generating one
        int numAllRounders = this.countRoleInSquad(Role.ALL_ROUNDER); //extract the number of all-rounders in the squad, to determine how many to play in XI when automatically generating one
        int numBowlersToSelect; //number of bowlers that need to be selected, based on the number of all-rounders selected

        if (this.userControlled) {
            //run algorithm to get playing eleven from the user if user controlled

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
            do {
                for (int i = 1; i <= 11; i++) {
                    //use a validation loop to ensure the same player isn't already selected
                    do {
                        validPlayer = true;
                        System.out.print("Enter player " + Integer.toString(i) + ": ");
                        playerNum = intScanner.nextInt();
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
                                if (this.squad.get(playerNum - 1).isOverseas() && (overseasCount == 4)) {
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
                    if (this.squad.get(playerNum - 1).getRole() == Role.WICKETKEEPER) {
                        wicketkeeperSelected = true;
                    }
                    //if an overseas player was just selected, then increment the overseas count
                    if (this.squad.get(playerNum - 1).isOverseas()) {
                        overseasCount++;
                    }
                }
                if (!wicketkeeperSelected) {
                    System.out.println("You must select a wicketkeeper"); //inform the user if a wicketkeeper wasn't selected
                    playerNumArray = new ArrayList<Integer>(); //reset the player num array
                }
            } while (!wicketkeeperSelected);
        } else {
            //run algorithm to automatically generate eleven if computer based team

            overseasCount = 0; //keep track of the number of overseas players
            //generate the pointers randomly for each role
            batPointer = (int)(Math.random() * this.squad.size());
            bowlPointer = (int)(Math.random() * this.squad.size());
            wkPointer = (int)(Math.random() * this.squad.size());
            allRoundPointer = (int)(Math.random() * this.squad.size());

            //select the batsmen
            numBatsmen = 0;
            passes = 0;
            while (numBatsmen < 5) {
                if (this.squad.get(batPointer).getRole() == Role.BATSMAN) {
                    if (this.squad.get(batPointer).isOverseas()) {
                        if (overseasCount < 4) {
                            playerNumArray.add(batPointer + 1); //only add the overseas player if the overseas cap hasn't been met
                            overseasCount++;
                            numBatsmen++;
                        }
                    } else {
                        playerNumArray.add(batPointer + 1); //fine to add if player isn't overseas
                        numBatsmen++;
                    }
                }
                batPointer++;
                if (batPointer >= this.squad.size()) {
                    batPointer = 0;
                    passes++;
                }
                if (passes >= 2) {
                    throw new RuntimeException("Not enough batsmen in squad to generate XI");
                }
            }

            //select the wicketkeeper
            wicketkeeperSelected = false;
            passes = 0;
            while (!wicketkeeperSelected) {
                if (this.squad.get(wkPointer).getRole() == Role.WICKETKEEPER) {
                    if (this.squad.get(wkPointer).isOverseas()) {
                        if (overseasCount < 4) {
                            playerNumArray.add(wkPointer + 1); //only add the overseas player if the overseas cap hasn't been met
                            overseasCount++;
                            wicketkeeperSelected = true;
                        }
                    } else {
                        playerNumArray.add(wkPointer + 1); //fine to add if player isn't overseas
                        wicketkeeperSelected = true;
                    }
                }
                wkPointer++;
                if (wkPointer >= this.squad.size()) {
                    wkPointer = 0;
                    passes++;
                }
                if (passes >= 2) {
                    throw new RuntimeException("No wicketkeeper in squad, can't generate XI");
                }
            }

            //select an all-rounder if there is one
            passes = 0;
            if (numAllRounders > 0) {
                allRounderSelected = false;
                while (!allRounderSelected) {
                    if (this.squad.get(allRoundPointer).getRole() == Role.ALL_ROUNDER) {
                        if (this.squad.get(allRoundPointer).isOverseas()) {
                            if (overseasCount < 4) {
                                playerNumArray.add(allRoundPointer + 1); //only add the overseas player if the overseas cap hasn't been met
                                overseasCount++;
                                allRounderSelected = true;
                            }
                        } else {
                            playerNumArray.add(allRoundPointer + 1); //fine to add player if not overseas
                            allRounderSelected = true;
                        }
                    }
                    allRoundPointer++;
                    if (allRoundPointer >= this.squad.size()) {
                        allRoundPointer = 0;
                        passes++;
                    }
                    //break if we've searched the entire squad
                    if (passes >= 2) {
                        break;
                    }
                }
                if (!allRounderSelected) {
                    numBowlersToSelect = 5;
                } else {
                    numBowlersToSelect = 4;
                }
            } else {
                numBowlersToSelect = 5;
            }

            //select the bowlers
            numBowlers = 0;
            passes = 0;
            while (numBowlers < numBowlersToSelect) {
                if (this.squad.get(bowlPointer).getRole() == Role.ALL_ROUNDER) {
                    if (this.squad.get(bowlPointer).isOverseas()) {
                        if (overseasCount < 4) {
                            playerNumArray.add(bowlPointer + 1); //only add overseas player if overseas cap hasn't been met
                            overseasCount++;
                            numBowlers++;
                        }
                    } else {
                        playerNumArray.add(bowlPointer + 1); //fine to add player if not overseas
                        numBowlers++;
                    }
                }
                bowlPointer++;
                if (bowlPointer >= this.squad.size()) {
                    bowlPointer = 0;
                    passes++;
                }
                if (passes >= 2) {
                    throw new RuntimeException("Not enough bowlers in squad to generate XI");
                }
            }
        }

        //add all the selected players to the XI using the player numbers
        for (Integer num : playerNumArray) {
            playingEleven.add(this.squad.get(num - 1)); //subtract one because the numbers in the array are 1-indexed
        }
        //store the current playing XI if the team is user controlled, so that the user can use the same XI again later on
        if (this.userControlled) {
            this.currentPlayingEleven = playingEleven;
        }
        return playingEleven;
    }

    /**
     * Adjusts the points and NRR of the team franchise after a match.
     * @param pointsToAdd The number of points to add to the team's points table (either 0 or 2)
     * @param nrrToAdd The NRR to add to the team's overall NRR
     */
    public void adjustPointsTableData(int pointsToAdd, double nrrToAdd) {
        if (pointsToAdd < 0) {
            this.points += pointsToAdd;
        }
        this.netRunRate += nrrToAdd;
    }

    /**
     * Getter method for the current number of points of the team franchise.
     * @return Current number of points of the team franchise
     */
    public int getPoints() {return this.points;}

    /**
     * Getter method for the current NRR of the team franchise.
     * @return Current NRR of the team franchise.
     */
    public double getNRR() {return this.netRunRate;}

    /**
     * Adds a new player to the team franchise's squad, if the franchise has just won the bidding for the player in an
     * auction.
     * @param newPlayer The new plyaer to add to the squad
     * @throws ArithmeticException If the team franchise's purse is not enough to afford to auction price of the player
     */
    public void addPlayer(Player newPlayer) {
        if (this.purse >= newPlayer.getPrice()) {
            this.purse -= newPlayer.getPrice();
            this.squad.add(newPlayer);
        } else {
            throw new ArithmeticException("The franchise cannot afford the new player");
        }
    }

    /**
     * Getter method for the current purse of the team franchise.
     * @return The current purse of the team franchise
     */
    public double getPurse() {
        return this.purse;
    }

    /**
     * Returns whether or not the team franchise is controlled by the user or not (otherwise controlled by the
     * computer).
     * @return A boolean indicating whether the team franchise is controlled by the user
     */
    public boolean controlledByUser() {
        return this.userControlled;
    }

    /**
     * Getter method for the name of the team franchise.
     * @return The name of the team franchise
     */
    public String getName() {return this.name;}


    /**
     * Helper method to count the number of players in the squad with a particular role e.g. the number of batsmen in
     * the squad.
     * @param roleToCount The role to count (batsman, bowler, all-rounder, wicket-keeper)
     * @return The number of players in the squad with the role
     */
    private int countRoleInSquad(Role roleToCount) {
        int count = 0;
        for (Player player : this.squad) {
            if (player.getRole() == roleToCount) {
                count++;
            }
        }
        return count;
    }

    /**
     * Helper method to count the number of overseas players in the squad.
     * @return The number of overseas players in the squad
     */
    private int countOverseasInSquad() {
        int count = 0;
        for (Player player : this.squad) {
            if (player.isOverseas()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Helper method to calculate the initial auction price for a batsman based purely on stats, used in the bidding
     * algorithm for a computer controlled franchise.
     * @param strikeRate The strike rate of the batsman
     * @param battingAvg The batting average of the batsman
     * @return The stats based auction price of the batsman
     */
    private double calculateBatsmanStatsPrice(double strikeRate, double battingAvg) {
        double numerator = this.purse * Math.pow(strikeRate, 3) * Math.pow(battingAvg, 2);
        double denominator = 4 * Math.pow(10, 10);
        return numerator / denominator;
    }

    /**
     * Helper method to calculate the initial auction price for a batsman based purely on stats, used in the bidding
     * algorithm for a computer controlled franchise.
     * @param economy The economy of the bowler
     * @param bowlingAvg The bowling average of the bowler
     * @return The stats based auction price of the bowler
     */
    private double calculateBowlerStatsPrice(double economy, double bowlingAvg) {
        double numerator = this.purse * 3 * Math.pow(10, 4);
        double denominator = Math.pow(economy, 3) * Math.pow(bowlingAvg, 2);
        return numerator / denominator;
    }
}
