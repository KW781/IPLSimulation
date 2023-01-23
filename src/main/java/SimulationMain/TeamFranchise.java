package SimulationMain;
import java.util.ArrayList;

public abstract class TeamFranchise {
    //fundamental team data
    private final String name;
    protected double purse; //measured in lakhs
    protected ArrayList<Player> squad;

    protected ArrayList<Player> currentPlayingEleven; //used to track the playing XI played in each game

    //points table attributes
    private int points;
    private double totalRunRate;
    private int numMatches;

    /**
     * Creates a TeamFranchise object, which can be either user controlled or computer controlled.
     * @param franchiseName The name of the franchise
     */
    public TeamFranchise(String franchiseName) {
        this.name = franchiseName;
        this.purse = 9000;
        this.totalRunRate = 0;
        this.points = 0;
        this.numMatches = 0;
        this.squad = new ArrayList<Player>(); //instantiate the squad attribute
    }

    /**
     * Adjusts the points and NRR of the team franchise after a match.
     * @param pointsToAdd The number of points to add to the team's points table (either 0, 1, or 2)
     * @param nrrToAdd The NRR to add to the team's overall NRR
     */
    public void adjustPointsTableData(int pointsToAdd, double nrrToAdd) {
        if ((pointsToAdd == 0) && (nrrToAdd >= 0)) {
            throw new RuntimeException("Points is zero (team lost) but NRR to add is somehow positive");
        } else if ((pointsToAdd == 2) && (nrrToAdd < 0)) {
            throw new RuntimeException("Points is two (team won) but NRR to add is somehow negative");
        } else if ((pointsToAdd == 1) && (nrrToAdd != 0)) {
            throw new RuntimeException("Points is one (match tied) but NRR is somehow non-zero");
        }

        this.points += pointsToAdd;
        this.totalRunRate += nrrToAdd;
        this.numMatches++;
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
    public double getNRR() {return this.totalRunRate / (double) this.numMatches;}

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
     * Method to determine whether this team franchise is user controlled or team controlled.
     * @return Boolean that is true if the team is user controlled, false otherwise
     */
    public boolean controlledByUser() {
        return this.getClass() == UserTeamFranchise.class;
    }

    /**
     * Getter method for the current purse of the team franchise.
     * @return The current purse of the team franchise
     */
    public double getPurse() {
        return this.purse;
    }

    /**
     * Getter method for the name of the team franchise.
     * @return The name of the team franchise
     */
    public String getName() {return this.name;}

    /**
     * Abstract method to select a playing XI from the squad of a team, which will be implemented differently
     * depending on whether it's a user controlled franchise or a computer controlled franchise.
     * @return An array list of players (of size 11) representing the playing XI
     */
    public abstract ArrayList<Player> selectPlayingEleven();

    /**
     * Helper method to count the number of players in the squad with a particular role e.g. the number of batsmen in
     * the squad.
     * @param roleToCount The role to count (batsman, bowler, all-rounder, wicket-keeper)
     * @return The number of players in the squad with the role
     */
    protected int countRoleInSquad(Role roleToCount) {
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
    protected int countOverseasInSquad() {
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
    protected double calculateBatsmanStatsPrice(double strikeRate, double battingAvg) {
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
    protected double calculateBowlerStatsPrice(double economy, double bowlingAvg) {
        double numerator = this.purse * 3 * Math.pow(10, 4);
        double denominator = Math.pow(economy, 3) * Math.pow(bowlingAvg, 2);
        return numerator / denominator;
    }
}
