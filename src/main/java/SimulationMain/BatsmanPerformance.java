package SimulationMain;

public class BatsmanPerformance {
    private String name;
    private int runsScored;
    private int ballsFaced;
    private boolean out;
    private double strikeRate;

    public BatsmanPerformance(String name, int runsScored, int ballsFaced, boolean out) {
        this.name = name;
        this.runsScored = runsScored;
        this.ballsFaced = ballsFaced;
        this.out = out;
        this.strikeRate = (((double)(this.runsScored)) / this.ballsFaced) * 100;
    }

    public String getName() {return this.name;}

    public int getRunsScored() {
        return this.runsScored;
    }

    public int getBallsFaced() {
        return this.ballsFaced;
    }

    public boolean wasOut() {
        return this.out;
    }

    public double getStrikeRate() {
        return this.strikeRate;
    }
}

