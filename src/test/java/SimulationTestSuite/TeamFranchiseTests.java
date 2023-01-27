package SimulationTestSuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import SimulationMain.Player;
import org.junit.jupiter.api.Test;

import SimulationMain.TeamFranchise;
import SimulationMain.UserTeamFranchise;
import SimulationMain.ComputerTeamFranchise;

import java.util.ArrayList;

public class TeamFranchiseTests {
    public static class TestSuperMethods {
        @Test
        public void testControlledByUser() {
            TeamFranchise computerTeam = new ComputerTeamFranchise("computer team");
            TeamFranchise userTeam = new UserTeamFranchise("user team");
            assertTrue(userTeam.controlledByUser());
            assertFalse(computerTeam.controlledByUser());
        }

        @Test
        public void testConstructorInitialisation() {
            TeamFranchise myTeam = new ComputerTeamFranchise("England");
            assertEquals("England", myTeam.getName());
            assertEquals(9000, myTeam.getPurse());
            assertEquals(0, myTeam.getPoints());
            assertEquals(0, myTeam.getNRR());
            assertEquals(0, myTeam.getNumMatches());
        }

        @Test
        public void testAddingAffordablePlayer() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            TeamFranchise myTeam = new UserTeamFranchise("India");

            viratKohli.newBid(1500);
            myTeam.addPlayer(viratKohli);
            assertEquals(7500, myTeam.getPurse());
        }

        @Test
        public void testAddingUnaffordablePlayer() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            TeamFranchise myTeam = new UserTeamFranchise("India");

            viratKohli.newBid(100000);
            try {
                myTeam.addPlayer(viratKohli);
                fail();
            } catch (ArithmeticException e) {

            }
        }

        @Test
        public void testAddPointsBelowZero() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(-1, -0.567);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void testAddPointsAboveTwo() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(3, 0.567);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void testTeamWinsWithNegativeNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(2, -0.245);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void teamLosesWithPositiveNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(0, 0.81);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void teamWinsWithZeroNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(2, 0);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void teamLosesWithZeroNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(0, 0);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void teamTiesWithPositiveNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(1, 0.693);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void teamTiesWithNegativeNRR() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");
            try {
                myTeam.adjustPointsTableData(1, -0.693);
                fail();
            } catch (RuntimeException e) {

            }
        }

        @Test
        public void testCorrectNRRCalculations() {
            TeamFranchise myTeam = new ComputerTeamFranchise("India");

            myTeam.adjustPointsTableData(2, 0.5);
            assertEquals(2, myTeam.getPoints());
            assertEquals(0.5, myTeam.getNRR());

            myTeam.adjustPointsTableData(2, 0.75);
            assertEquals(4, myTeam.getPoints());
            assertEquals(0.625, myTeam.getNRR());

            myTeam.adjustPointsTableData(0, -0.35);
            assertEquals(4, myTeam.getPoints());
            assertEquals(0.3, myTeam.getNRR());

            myTeam.adjustPointsTableData(2, 0.3);
            assertEquals(6, myTeam.getPoints());
            assertEquals(0.3, myTeam.getNRR());

            myTeam.adjustPointsTableData(0, -1.4);
            assertEquals(6, myTeam.getPoints());
            assertEquals(-0.04, myTeam.getNRR());

            myTeam.adjustPointsTableData(1, 0);
            assertEquals(7, myTeam.getPoints());
            assertEquals(-0.033, myTeam.getNRR());

            myTeam.adjustPointsTableData(1, 0);
            assertEquals(8, myTeam.getPoints());
            assertEquals(-0.029, myTeam.getNRR());

            myTeam.adjustPointsTableData(2, 2.4);
            assertEquals(10, myTeam.getPoints());
            assertEquals(0.275, myTeam.getNRR());
        }
    }

    public static class TestComputerFranchise {
        @Test
        public void testRealisticBids() {
            ComputerTeamFranchise myTeam = new ComputerTeamFranchise("RCB");
            ArrayList<Player> miniAuctionPool = this.getTestPlayerList();

            for (Player currentPlayer : miniAuctionPool) {
                for (int i = 1; i <= 15; i++) {
                    myTeam.bid(currentPlayer);
                    assertTrue(currentPlayer.getPrice() < 3000);
                }
            }
        }

        @Test
        public void testValidPlayingXI() {
            TeamFranchise myTeam = new ComputerTeamFranchise("RCB");
            ArrayList<Player> squad = this.getTestPlayerList();
            ArrayList<Player> playingXI;
            int batsmanCount = 0;
            int bowlerCount = 0;
            int allRounderCount = 0;
            int wicketKeeperCount = 0;

            for (Player currentPlayer : squad) {
                myTeam.addPlayer(currentPlayer);
            }

            playingXI = myTeam.selectPlayingEleven();
            for (Player currentPLayer : playingXI) {
                switch (currentPLayer.getRole()) {
                    case BATSMAN:
                        batsmanCount++;
                        break;
                    case BOWLER:
                        bowlerCount++;
                        break;
                    case ALL_ROUNDER:
                        allRounderCount++;
                        break;
                    case WICKETKEEPER:
                        wicketKeeperCount++;
                        break;
                }
            }

            assertEquals(5, batsmanCount);
            assertEquals(4, bowlerCount);
            assertEquals(1, wicketKeeperCount);
            assertEquals(1, allRounderCount);
        }

        public ArrayList<Player> getTestPlayerList() {
            ArrayList<Player> testPlayerList = new ArrayList<>();
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            testPlayerList.add(new Player("Virat Kohli", 200, false, false,
                    viratKohliStats));

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            testPlayerList.add(new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats));

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            testPlayerList.add(new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats));

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            testPlayerList.add(new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats));

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            testPlayerList.add(new Player("Suryakumar Yadav", 150, false, false,
                    suryakumarYadavStats));

            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            testPlayerList.add(new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats));

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            testPlayerList.add(new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats));

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            testPlayerList.add(new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats));

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            testPlayerList.add(new Player("Adam Zampa", 100, false, true,
                    adamZampaStats));

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            testPlayerList.add(new Player("Trent Boult", 150, false, true,
                    trentBoultStats));

            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            testPlayerList.add(new Player("MS Dhoni", 200, true, false,
                    msDhoniStats));

            int[] rishabhPantStats = {84, 2498, 71, 1694, 0, 0, 0};
            testPlayerList.add(new Player("Rishabh Pant", 200, true, false,
                    rishabhPantStats));

            int[] josButtlerStats = {65, 1968, 56, 1312, 0, 0, 0};
            testPlayerList.add(new Player("Jos Buttler", 150, true, true,
                    josButtlerStats));

            int[] jonnyBairstowStats = {28, 1038, 25, 730, 0, 0, 0};
            testPlayerList.add(new Player("Jonny Bairstow", 100, true, true,
                    jonnyBairstowStats));

            int[] nicholasPooranStats = {33, 606, 27, 391, 0, 0, 0};
            testPlayerList.add(new Player("Nicholas Pooran", 100, true, true,
                    nicholasPooranStats));

            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            testPlayerList.add(new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats));

            int[] shaneWatsonStats = {145, 3874, 125, 2809, 92, 2682, 2029};
            testPlayerList.add(new Player("Shane Watson", 150, false, true,
                    shaneWatsonStats));

            int[] benStokesStats = {43, 920, 36, 684, 28, 974, 683};
            testPlayerList.add(new Player("Ben Stokes", 150, false, true,
                    benStokesStats));

            int[] mohammadNabiStats = {17, 180, 12, 119, 13, 408, 343};
            testPlayerList.add(new Player("Mohammad Nabi", 100, false, true,
                    mohammadNabiStats));

            int[] shakibAlHasanStats = {71, 793, 40, 637, 63, 1839, 1484};
            testPlayerList.add(new Player("Shakib Al Hasan", 200, false, true,
                    shakibAlHasanStats));

            int[] aveshKhanStats = {25, 9, 1, 6, 29, 749, 546};
            testPlayerList.add(new Player("Avesh Khan", 75, false, false,
                    aveshKhanStats));

            int[] babarAzamStats = {73, 2620, 58, 2029, 0, 0, 0};
            testPlayerList.add(new Player("Babar Azam", 200, false, true,
                    babarAzamStats));

            int[] brendonMcCullumStats = {109, 2880, 104, 2186, 0, 0, 0};
            testPlayerList.add(new Player("Brendon McCullum", 200, true, true,
                    brendonMcCullumStats));

            int[] dwayneBravoStats = {151, 1537, 67, 1180, 167, 4061, 2913};
            testPlayerList.add(new Player("Dwayne Bravo", 150, false, true,
                    dwayneBravoStats));

            int[] glennMaxwellStats = {97, 2018, 80, 1329, 22, 915, 642};
            testPlayerList.add(new Player("Glenn Maxwell", 200, false, true,
                    glennMaxwellStats));

            return testPlayerList;
        }
    }
}
