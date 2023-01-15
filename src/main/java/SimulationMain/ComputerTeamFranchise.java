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

        int passes; //keep track of the number of passes made through the squad when selecting any role, to avoid infinite loop
        int batPointer, bowlPointer, wkPointer, allRoundPointer; //pointers to point to players in squad when automatically generating an XI, initially randomly generated
        int numBatsmen, numBowlers; //keep track of the number of batsmen and bowlers that have been selected in the XI, when automatically generating one
        int numAllRounders = this.countRoleInSquad(Role.ALL_ROUNDER); //extract the number of all-rounders in the squad, to determine how many to play in XI when automatically generating one
        int numBowlersToSelect; //number of bowlers that need to be selected, based on the number of all-rounders selected

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

        //add all the selected players to the XI using the player numbers
        for (Integer num : playerNumArray) {
            playingEleven.add(this.squad.get(num - 1)); //subtract one because the numbers in the array are 1-indexed
        }

        return playingEleven;
    }
}
