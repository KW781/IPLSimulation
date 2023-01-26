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
}
