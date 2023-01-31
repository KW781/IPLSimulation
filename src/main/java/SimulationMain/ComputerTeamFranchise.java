package SimulationMain;

import java.util.ArrayList;

public class ComputerTeamFranchise extends TeamFranchise {
    public ComputerTeamFranchise(String franchiseName) {
        super(franchiseName);
    }

    /**
     * Method to bid for a player that's currently being auctioned. Uses an algorithm that takes into consideration the
     * player's stats, the current balance of the team's squad, and the current purse of the team franchise. This method
     * is for a franchise that is controlled by the COMPUTER.
     * @param biddingPlayer The player currently being auctioned
     * @return Boolean indicating whether the franchise bid for the player or not
     */
    public boolean bid(Player biddingPlayer) {
        Role biddingRole = biddingPlayer.getRole();
        double currentBidPrice = biddingPlayer.getPrice();
        double batsmanRatio, bowlerRatio, allRounderRatio, wicketkeeperRatio, overseasRatio;

        double maxPrice = 0; //the maximum price the franchise is willing to spend on current player being auctioned
        double battingMaxPrice, bowlingMaxPrice;

        //A random factor to create a form of unpredictability in the auction process
        final double RANDOM_FACTOR_MAGNITUDE = 0.4;
        double randomFactor = (RANDOM_FACTOR_MAGNITUDE * 2) * Math.random() + (1 - RANDOM_FACTOR_MAGNITUDE);

        //return if the team franchise can't afford the player
        if (this.purse < currentBidPrice + 20) {
            return false;
        }

        switch (biddingRole) {
            case WICKETKEEPER:
                maxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg(),
                        biddingPlayer.getNumMatches());
                if (this.squad.size() >= 10) { //will only start considering the ratio of roles in the squad once size >= 10
                    wicketkeeperRatio = ((double)this.countRoleInSquad(Role.WICKETKEEPER)) / this.squad.size();
                    if (wicketkeeperRatio < (1.0/11)) {
                        maxPrice *= 1.1; //increase the max price by 10% if the wicketkeeper ratio is too less
                    } else if (wicketkeeperRatio > (2.5/11)) {
                        maxPrice *= 0.9; //decrease the max price by 10% if the wicketkeeper ratio is too much
                    }
                }
                break;
            case BATSMAN:
                maxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg(),
                        biddingPlayer.getNumMatches());
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
                break;
            case BOWLER:
                maxPrice = this.calculateBowlerStatsPrice(biddingPlayer.getEconomy(), biddingPlayer.getBowlingAvg(),
                        biddingPlayer.getNumMatches());
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
                break;
            case ALL_ROUNDER:
                battingMaxPrice = this.calculateBatsmanStatsPrice(biddingPlayer.getStrikeRate(), biddingPlayer.getBattingAvg(),
                        biddingPlayer.getNumMatches());
                bowlingMaxPrice = this.calculateBowlerStatsPrice(biddingPlayer.getEconomy(), biddingPlayer.getBowlingAvg(),
                        biddingPlayer.getNumMatches());
                maxPrice = (battingMaxPrice + bowlingMaxPrice) / 2; //calculate average of batting and bowling max prices
                if (this.squad.size() >= 10) {
                    allRounderRatio = ((double)this.countRoleInSquad(Role.ALL_ROUNDER)) / this.squad.size();
                    if (allRounderRatio < (1.0/11)) {
                        maxPrice *= 1.1; //increase max price by 10% if all-rounder ratio is too less
                    } else {
                        maxPrice *= 0.9; //decrease max price by 10% if all-rounder ratio is too much
                    }
                }
                break;
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

        if (maxPrice >= (currentBidPrice + 20)) {
            maxPrice = currentBidPrice + 20; //add on increment to current bid price if willing to pay more
            biddingPlayer.newBid(maxPrice);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Selects the playing XI from the team franchise's squad. It does so by using an algorithm that chooses an XI by
     * randomly generating pointers at players in the squad (one pointer for each role),vthen iterating the pointers for
     * each role until an XI is selected.
     * @return An ArrayList of players, constituting the playing XI selected by the team franchise
     * @throws RuntimeException If there are not enough players of a particular role to select a playing XI (e.g. not
     *                          enough batsmen)
     */
    public ArrayList<Player> selectPlayingEleven() {
        ArrayList<Player> playingEleven = new ArrayList<Player>();
        ArrayList<Integer> playerNumArray = new ArrayList<Integer>();
        boolean wicketkeeperSelected, allRounderSelected;
        int overseasCount; //counter for number of overseas players in the XI

        //pointers to point to players in squad when automatically generating an XI, initially randomly generated
        int batPointer, bowlPointer, wkPointer, allRoundPointer;
        //pointers to store original pointer values, so as not to check same player twice
        int originalBatPointer, originalBowlPointer, originalWkPointer, originalAllRoundPointer;
        //keep track of the number of batsmen, bowlers and all-rounders that have been selected in the XI, when automatically generating one
        int numBatsmen, numBowlers, numAllRounders;
        int numAllRoundersNeeded = 0;
        int numBowlersNeeded;

        overseasCount = 0; //keep track of the number of overseas players
        //generate the pointers randomly for each role
        batPointer = (int)(Math.random() * this.squad.size());
        originalBatPointer = batPointer;
        bowlPointer = (int)(Math.random() * this.squad.size());
        originalBowlPointer = bowlPointer;
        wkPointer = (int)(Math.random() * this.squad.size());
        originalWkPointer = wkPointer;
        allRoundPointer = (int)(Math.random() * this.squad.size());
        originalAllRoundPointer = allRoundPointer;

        //select the batsmen
        numBatsmen = 0;
        while (numBatsmen < 5) {
            if (this.squad.get(batPointer).getRole() == Role.BATSMAN) {
                if (this.squad.get(batPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(batPointer); //only add the overseas player if the overseas cap hasn't been met
                        overseasCount++;
                        numBatsmen++;
                    }
                } else {
                    playerNumArray.add(batPointer); //fine to add if player isn't overseas
                    numBatsmen++;
                }
            }
            batPointer++;
            if (batPointer >= this.squad.size()) {
                batPointer = 0;
            }
            //if we've run out of specialist batsmen, indicate that we need to select all-rounders instead
            if (batPointer == originalBatPointer) {
                numAllRoundersNeeded = 5 - numBatsmen;
                break;
            }
        }

        //select all-rounders to replaced specialist batsmen if not enough specialist batsmen were there
        numAllRounders = 0;
        while (numAllRounders < numAllRoundersNeeded) {
            if (this.squad.get(allRoundPointer).getRole() == Role.ALL_ROUNDER) {
                if (this.squad.get(allRoundPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(allRoundPointer);
                        overseasCount++;
                        numAllRounders++;
                    }
                } else {
                    playerNumArray.add(allRoundPointer);
                    numAllRounders++;
                }
            }
            allRoundPointer++;
            if (allRoundPointer >= this.squad.size()) {
                allRoundPointer = 0;
            }
            if ((allRoundPointer == originalAllRoundPointer) && (numAllRounders < numAllRoundersNeeded)) {
                throw new RuntimeException("Not enough batsmen in squad to generate XI");
            }
        }

        //select the wicketkeeper
        wicketkeeperSelected = false;
        while (!wicketkeeperSelected) {
            if (this.squad.get(wkPointer).getRole() == Role.WICKETKEEPER) {
                if (this.squad.get(wkPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(wkPointer); //only add the overseas player if the overseas cap hasn't been met
                        overseasCount++;
                        wicketkeeperSelected = true;
                    }
                } else {
                    playerNumArray.add(wkPointer); //fine to add if player isn't overseas
                    wicketkeeperSelected = true;
                }
            }
            wkPointer++;
            if (wkPointer >= this.squad.size()) {
                wkPointer = 0;
            }
            if ((wkPointer == originalWkPointer) && !wicketkeeperSelected) {
                throw new RuntimeException("No wicketkeeper in squad, can't generate XI");
            }
        }

        //select an all-rounder if we have not already selected one
        while (numAllRounders == 0) {
            if (this.squad.get(allRoundPointer).getRole() == Role.ALL_ROUNDER) {
                if (this.squad.get(allRoundPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(allRoundPointer);
                        overseasCount++;
                        numAllRounders++;
                    }
                } else {
                    playerNumArray.add(allRoundPointer);
                    numAllRounders++;
                }
            }
            allRoundPointer++;
            if (allRoundPointer >= this.squad.size()) {
                allRoundPointer = 0;
            }
            //exit if we don't have an all-rounder left
            if (allRoundPointer == originalAllRoundPointer) {
                break;
            }
        }

        //select the bowlers
        numBowlers = 0;
        numBowlersNeeded = 11 - playerNumArray.size();
        while (numBowlers < numBowlersNeeded) {
            if (this.squad.get(bowlPointer).getRole() == Role.BOWLER) {
                if (this.squad.get(bowlPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(bowlPointer); //only add overseas player if overseas cap hasn't been met
                        overseasCount++;
                        numBowlers++;
                    }
                } else {
                    playerNumArray.add(bowlPointer); //fine to add player if not overseas
                    numBowlers++;
                }
            }
            bowlPointer++;
            if (bowlPointer >= this.squad.size()) {
                bowlPointer = 0;
            }
            if (bowlPointer == originalBowlPointer) {
                numAllRoundersNeeded = numAllRounders + numBowlersNeeded - numBowlers;
                break;
            }
        }

        //select additional all-rounders if not enough bowlers
        while (numAllRounders < numAllRoundersNeeded) {
            if ((allRoundPointer == originalAllRoundPointer) && (numAllRounders < numAllRoundersNeeded)) {
                throw new RuntimeException("Not enough bowlers in squad to generate XI");
            }
            if (this.squad.get(allRoundPointer).getRole() == Role.ALL_ROUNDER) {
                if (this.squad.get(allRoundPointer).isOverseas()) {
                    if (overseasCount < 4) {
                        playerNumArray.add(allRoundPointer);
                        overseasCount++;
                        numAllRounders++;
                    }
                } else {
                    playerNumArray.add(allRoundPointer);
                    numAllRounders++;
                }
            }
            allRoundPointer++;
            if (allRoundPointer >= this.squad.size()) {
                allRoundPointer = 0;
            }
        }

        //add all the selected players to the XI using the player numbers
        for (Integer num : playerNumArray) {
            playingEleven.add(this.squad.get(num));
        }

        return playingEleven;
    }

    /**
     * Helper method to calculate the initial auction price for a batsman based purely on stats, used in the bidding
     * algorithm for a computer controlled franchise.
     * @param strikeRate The strike rate of the batsman
     * @param battingAvg The batting average of the batsman
     * @return The stats based auction price of the batsman
     */
    private double calculateBatsmanStatsPrice(double strikeRate, double battingAvg, int numMatches) {
        double numerator = 9 * Math.pow(strikeRate, 3) * Math.pow(battingAvg, 2);
        double denominator = 4 * Math.pow(10, 7);
        double price = numerator / denominator;
        return adjustPriceForNumberOfMatches(price, numMatches);
    }

    /**
     * Helper method to calculate the initial auction price for a batsman based purely on stats, used in the bidding
     * algorithm for a computer controlled franchise.
     * @param economy The economy of the bowler
     * @param bowlingAvg The bowling average of the bowler
     * @return The stats based auction price of the bowler
     */
    private double calculateBowlerStatsPrice(double economy, double bowlingAvg, int numMatches) {
        double numerator = 27 * Math.pow(10, 7);
        double denominator = Math.pow(economy, 3) * Math.pow(bowlingAvg, 2);
        double price = numerator / denominator;
        return adjustPriceForNumberOfMatches(price, numMatches);
    }

    /**
     * Helper method to scale the price up/down if need be depending on the number of matches, because good stats may
     * not be accurate representation of a player's ability if they've played few matches. Conversely, good stats
     * are an even better representation of a player's ability if they've played a lot of matches.
     * @param price The current price of the player, purely based on statistics
     * @param numMatches The number of matches played by the player
     * @return The new price of the player adjusted for experience (number of matches they've played)
     */
    private double adjustPriceForNumberOfMatches(double price, int numMatches) {
        if (price >= 1100) {
            if (numMatches >= 150) {
                return price * 1.3;
            } else if (numMatches >= 80) {
                return price * 1.15;
            } else if (numMatches <= 15) {
                return price * 0.5;
            } else if (numMatches <= 40) {
                return price * 0.75;
            }
        }
        return price;
    }
}
