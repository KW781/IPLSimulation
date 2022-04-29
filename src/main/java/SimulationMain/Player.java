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

    //method to change the auction price when a new franchise bids
    public void newBid(double newPrice) {
        if (newPrice > this.auctionPrice) {
            this.auctionPrice = newPrice;
        } else {
            throw new IllegalArgumentException("New bid must be greater than current bid");
        }
    }

    //method to initialise the attributes to store data regarding player stats in a game currently being played
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

    //method to increment the current runs scored in the game for the player
    public void incrementCurrentRunsScored(int runsToAdd) {
        if (runsToAdd >= 0) {
            this.currentRunsScored += runsToAdd;
        } else {
            throw new RuntimeException("Runs to add was negative");
        }
    }

    //method to get the current runs scored, used when generating the scorecard
    public int getCurrentRunsScored() {
        return this.currentRunsScored;
    }


    //method to increment the current runs conceded in the game for the player
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

    //method to get the current runs conceded, used when generating the scorecard
    public int getCurrentRunsConceded() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentRunsConceded;
        } else {
            throw new RuntimeException("Can't get current runs conceded for a batsman or wicketkeeper");
        }
    }

    //method to increment the number of wickets taken in the game for the player
    public void incrementCurrentWickets() {
        //ensure that only the wickets for a bowler or all-rounder are incremented
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            this.currentWickets++;
        } else {
            throw new RuntimeException("Can't increment wickets for a batsman or wicketkeeper");
        }
    }

    //method to get the current wickets, used when generating the scorecard
    public int getCurrentWickets() {
        //ensure that only the wickets for a bowler or all-rounder are returned
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentWickets;
        } else {
            throw new RuntimeException("Can't return current wickets for a batsman or wicketkeeper");
        }
    }

    //method to increment the number of balls bowled in the game for the player
    public void incrementCurrentBallsBowled() {
        //ensure that only the balls bowled for a bowler or all-rounder are incremented
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            this.currentBallsBowled++;
        } else {
            throw new RuntimeException("Can't increment balls bowled for a batsman or wicketkeeper");
        }
    }

    //method to increment the number of balls faced in the game for the player
    public void incrementCurrentBallsFaced() {
        this.currentBallsFaced++;
    }

    //method to get the current bowls faced by the player, used when generating the scorecard
    public int getCurrentBallsFaced() {
        return this.currentBallsFaced;
    }

    //method to get the current balls bowled by the player
    public int getCurrentBallsBowled() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.currentBallsBowled;
        } else {
            throw new RuntimeException("Current balls bowled not available for a batsman or wicketkeeper");
        }
    }

    //method to dismiss the batsman i.e. set the 'out' attribute to true
    public void dismiss() {
        this.out = true;
    }

    //method to return whether the batsman was dismissed in the match or not, used when generating the scorecard
    public boolean wasDismissed() {
        return this.out;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.auctionPrice;
    }

    public boolean isOverseas() {
        return this.overseas;
    }

    public Role getRole() {
        return this.playerRole;
    }

    public double getStrikeRate() {
        return this.strikeRate;
    }

    public double getBattingAvg() {
        return this.battingAvg;
    }

    public double getEconomy() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.economy;
        } else {
            throw new RuntimeException("Bowling economy not available for a batsman or wicketkeeper");
        }
    }

    public double getBowlingAvg() {
        if ((this.playerRole == Role.BOWLER) || (this.playerRole == Role.ALL_ROUNDER)) {
            return this.bowlingAvg;
        } else {
            throw new RuntimeException("Bowling average not available for a batsman or wicketkeeper");
        }
    }


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

