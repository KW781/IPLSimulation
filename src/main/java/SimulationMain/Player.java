package SimulationMain;

public class Player {
    private final String name;
    private final Role playerRole;
    private double auctionPrice; //measured in lakhs
    private int numMatches;
    private boolean overseas;

    //attributes storing career batting stats
    private int totalRuns;
    private int numTimesOut;
    private int totalBallsFaced;
    private double battingAvg;
    private double strikeRate;

    //attributes storing career bowling stats
    private int totalWickets;
    private int totalRunsConceded;
    private int totalBallsBowled;
    private double economy;
    private double bowlingAvg;

    //attributes storing current game stats
    private int currentRunsScored;
    private int currentWickets;
    private int currentBallsFaced;
    private int currentRunsConceded;
    private int currentBallsBowled;
    private boolean out;

    /**
     * Creates and returns a player object based on the following parameters. Calculates the player's statistics e.g.
     * average and strike rate. Determines the player's role based on its statistics (batsman, bowler, all-rounder,
     * wicket-keeper).
     * @param playerName Player's name e.g. 'Virat Kohli'
     * @param basePrice The base price of the player (in lakhs)
     * @param isWicketkeeper Whether player can keep wickets
     * @param foreign Whether the player is an overseas player
     * @param playerStats An array of integers storing all the player's stats in the following order: Number of matches,
     *                    career runs, number of times out in career, number of balls faced in career, career wickets,
     *                    career runs conceded, number of balls bowled in career
     */
    public Player(String playerName, double basePrice, boolean isWicketkeeper, boolean foreign, int[] playerStats) {
        double avgOversPerMatch; //stores the average number of overs bowled per match, used to determine the player's role

        //set all the statistics to the statistics input
        this.name = playerName;
        this.auctionPrice = basePrice;
        this.overseas = foreign;
        this.numMatches = playerStats[0];
        this.totalRuns = playerStats[1];
        this.numTimesOut = playerStats[2];
        this.totalBallsFaced = playerStats[3];
        this.totalWickets = playerStats[4];
        this.totalRunsConceded = playerStats[5];
        this.totalBallsBowled = playerStats[6];

        //calculate the remaining statistics from the set statistics, invoking CalculateStats()
        this.calculateStats();

        //determine the role of the player based on the statistics
        avgOversPerMatch = (((double)this.totalBallsBowled) / 6) / this.numMatches; //calculate average overs bowled per match
        if (isWicketkeeper) {
            this.playerRole = Role.WICKETKEEPER;
        } else {
            if (avgOversPerMatch >= 2.75) {
                //all rounder if strike rate greater than 120 and batting avg greater than 15
                if ((this.strikeRate >= 120) && (this.battingAvg >= 15)) {
                    this.playerRole = Role.ALL_ROUNDER;
                //also all rounder if strike rate greater than 150 and batting avg greater than 10
                } else if ((this.strikeRate >= 150) && (this.battingAvg >= 10)) {
                    this.playerRole = Role.ALL_ROUNDER;
                } else {
                    this.playerRole = Role.BOWLER; //otherwise bowler
                }
            //batsman if they bowl less than 1 over per match
            } else if (avgOversPerMatch < 1) {
                this.playerRole = Role.BATSMAN;
            } else {
                this.playerRole = Role.ALL_ROUNDER; //otherwise all rounder
            }
        }
    }

    /**
     * Method to change the player's current auction price (i.e. place a new bid for the player).
     * @param newPrice The new price of the player
     * @throws IllegalArgumentException If the new price is less than the player's current auction price
     */
    public void newBid(double newPrice) {
        if (newPrice > this.auctionPrice) {
            this.auctionPrice = newPrice;
        } else {
            throw new IllegalArgumentException("New bid must be greater than current bid");
        }
    }

    /**
     * Initialises the game statistics for the player before playing a game. Sets the runs scored, balls faced, etc.,
     * to zero.
     */
    public void initialiseGameStats() {
        //initialise batting stats
        this.currentRunsScored = 0;
        this.currentBallsFaced = 0;
        this.out = false;
        //only initialise the bowling stats if the player is a bowler or an all-rounder
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            this.currentWickets = 0;
            this.currentBallsBowled = 0;
            this.currentRunsConceded = 0;
        }
    }

    /**
     * Increments the current number of runs scored for the player (i.e. for a particular game). This means the player
     * played some shot and added to their individual total.
     * @param runsToAdd The new runs to add to their total (0, 1, 2, 3, 4, 6)
     * @throws RuntimeException If the new runs to add is negative
     */
    public void incrementCurrentRunsScored(int runsToAdd) {
        if (runsToAdd >= 0) {
            this.currentRunsScored += runsToAdd;
        } else {
            throw new RuntimeException("Runs to add was negative");
        }
    }

    /**
     * Getter method for the number of runs currently scored by the player in a particular game
     * @return The current number of runs
     */
    public int getCurrentRunsScored() {
        return this.currentRunsScored;
    }


    /**
     * Increments the number of runs currently conceded in a game for a bowler or all-rounder
     * @param runsToAdd The new runs to add to their total (0, 1, 2, 3, 4, 6)
     * @throws RuntimeException Either if the runs to add is negative, or if the player is a batsman or wicket-keeper (
     *                          batsmen and wicket-keepers don't bowl)
     */
    public void incrementCurrentRunsConceded(int runsToAdd) {
        //ensure that only the runs for a bowler or all-rounder are incremented
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            if (runsToAdd >= 0) {
                this.currentRunsConceded += runsToAdd;
            } else {
                throw new RuntimeException("Runs to add was negative");
            }
        } else {
            throw new RuntimeException("Can't increment runs conceded for a batsman or wicketkeeper");
        }
    }

    /**
     * Getter method for the number of runs currently conceded by the player in the current game (for a bowler or
     * all-rounder).
     * @return The number of runs currently conceded
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl)
     */
    public int getCurrentRunsConceded() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentRunsConceded;
        } else {
            throw new RuntimeException("Can't get current runs conceded for a batsman or wicketkeeper");
        }
    }

    /**
     * Increments the number of wickets taken by the player currently in a game, if the player has taken a wicket (for
     * a bowler or all-rounder).
     * @throws RuntimeException If the method is called for a batsman or wicket-keepr (batsmen and wicket-keepers don't
     *                          bowl)
     */
    public void incrementCurrentWickets() {
        //ensure that only the wickets for a bowler or all-rounder are incremented
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            this.currentWickets++;
        } else {
            throw new RuntimeException("Can't increment wickets for a batsman or wicketkeeper");
        }
    }

    /**
     * Getter method for the current number of wickets taken by the player currently in a game, for a bowler or
     * all-rounder. Used when generating the scorecard.
     * @return The current number of wickets
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl).
     */
    public int getCurrentWickets() {
        //ensure that only the wickets for a bowler or all-rounder are returned
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentWickets;
        } else {
            throw new RuntimeException("Can't return current wickets for a batsman or wicketkeeper");
        }
    }

    /**
     * Increments the number of balls currently bowled by the player in a game, for a bowler or all-rounder. Used when
     * the player has just bowled a ball.
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl)
     */
    public void incrementCurrentBallsBowled() {
        //ensure that only the balls bowled for a bowler or all-rounder are incremented
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            this.currentBallsBowled++;
        } else {
            throw new RuntimeException("Can't increment balls bowled for a batsman or wicketkeeper");
        }
    }

    /**
     * Increments the number of balls currently faced by the player in a game. Used when the player has just faced a
     * ball.
     */
    public void incrementCurrentBallsFaced() {
        this.currentBallsFaced++;
    }

    /**
     * Getter method for the number of balls currently faced by the player in a game. Used when generating the
     * scorecard.
     * @return The current number of balls faced
     */
    public int getCurrentBallsFaced() {
        return this.currentBallsFaced;
    }

    /**
     * Getter method for the current number of balls bowled by a player in a game, for a bowler or all-rounder. Used
     * when generating the scorecard.
     * @return The current number of balls bowled.
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl)
     */
    public int getCurrentBallsBowled() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentBallsBowled;
        } else {
            throw new RuntimeException("Current balls bowled not available for a batsman or wicketkeeper");
        }
    }

    /**
     * Dismisses the batsman in a game by setting the 'out' attribute to true.
     */
    public void dismiss() {
        this.out = true;
    }

    /**
     * A getter method for whether the batsman has been dismissed or not in a current game. Used when generating the
     * scorecard.
     * @return Whether or not the batsman has been dismissed
     */
    public boolean wasDismissed() {
        return this.out;
    }

    /**
     * Getter method for the player's name
     * @return The player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for the player's current auction price
     * @return The player's auction price
     */
    public double getPrice() {
        return this.auctionPrice;
    }

    /**
     * Returns whether the player is an overseas player or not.
     * @return Whether player is an overseas player or not
     */
    public boolean isOverseas() {
        return this.overseas;
    }

    /**
     * Getter method for the player's role (batsman, bowler, all-rounder, wicket-keeper).
     * @return The player's role
     */
    public Role getRole() {
        return this.playerRole;
    }

    /**
     * Getter method for the player's batting strike rate.
     * @return The player's strike rate
     */
    public double getStrikeRate() {
        return this.strikeRate;
    }

    /**
     * Getter method for the player's batting average.
     * @return The player's batting average
     */
    public double getBattingAvg() {
        return this.battingAvg;
    }

    /**
     * Getter method for the player's economy, for a bowler or all-rounder.
     * @return The player's economy
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl).
     */
    public double getEconomy() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.economy;
        } else {
            throw new RuntimeException("Bowling economy not available for a batsman or wicketkeeper");
        }
    }

    /**
     * Getter method for the player's bowling average, for a bowler or all-rounder.
     * @return The player's bowling average.
     * @throws RuntimeException If the method is called for a batsman or wicket-keeper (batsmen and wicket-keepers
     *                          don't bowl).
     */
    public double getBowlingAvg() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.bowlingAvg;
        } else {
            throw new RuntimeException("Bowling average not available for a batsman or wicketkeeper");
        }
    }

    /**
     * Helper method to calculate all the stats for the player, based on the raw stats stored in the database.
     * Calculates the batting average, strike rate, bowling average, and economy.
     */
    private void calculateStats() {
        if (this.numTimesOut != 0) {
            this.battingAvg = ((double) this.totalRuns) / this.numTimesOut;
        } else {
            this.battingAvg = -1; //make the batting average negative if batsman was never dismissed, to indicate its invalid
        }

        if (this.totalBallsFaced != 0) {
            this.strikeRate = (((double) this.totalRuns) / this.totalBallsFaced) * 100;
        } else {
            this.strikeRate = -1; //make the strike rate negative if the batsman never faced any balls, to indicate its invalid
        }

        if (this.totalBallsBowled != 0) {
            this.economy = (((double) this.totalRunsConceded) / (((double)this.totalBallsBowled) / 6));
        } else {
            this.economy = -1; //make the economy negative if the bowler never bowled any balls, to indicate it's invalid
        }

        if (this.totalRunsConceded != 0) {
            this.bowlingAvg = ((double) this.totalRunsConceded) / this.totalWickets;
        } else {
            this.bowlingAvg = -1; //make the bowling average negative if the bowler never took any wickets, to indicate it's invalid
        }
    }
}

