package SimulationMain;

public class BowlerPerformance {
    private String Name;
    private int RunsConceded;
    private int BallsBowled;
    private int Wickets;
    private double Economy;

    public BowlerPerformance(String name, int runsConceded, int ballsBowled, int wickets) {
        this.Name = name;
        this.RunsConceded = runsConceded;
        this.BallsBowled = ballsBowled;
        this.Wickets = wickets;
        this.Economy = ((double)(RunsConceded)) / (this.BallsBowled / 6);
    }

    public String getName() {return this.Name;}

    public int getRunsConceded() {
        return this.RunsConceded;
    }

    public int getBallsBowled() {
        return this.BallsBowled;
    }

    public int getWickets() {
        return this.Wickets;
    }

    public double getEconomy() {
        return this.Economy;
    }
}

