package SimulationTestSuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import SimulationMain.Player;
import SimulationMain.Role;


public class PlayerTests {
    public static class TestPlayerRoles {
        @Test
        public void testBatsmen() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            Player kaneWilliamson = new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats);

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            Player abDeVilliers = new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats);

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            Player rohitSharma = new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats);

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            Player suryakumarYadav = new Player("Suryakumar Yadav", 150, false, false,
                    suryakumarYadavStats);

            assertEquals(Role.BATSMAN, viratKohli.getRole());
            assertEquals(Role.BATSMAN, kaneWilliamson.getRole());
            assertEquals(Role.BATSMAN, abDeVilliers.getRole());
            assertEquals(Role.BATSMAN, rohitSharma.getRole());
            assertEquals(Role.BATSMAN, suryakumarYadav.getRole());
        }

        @Test
        public void testBowlers() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            Player lasithMalinga = new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats);

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            Player rashidKhan = new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats);

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            Player adamZampa = new Player("Adam Zampa", 100, false, true,
                    adamZampaStats);

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            Player trentBoult = new Player("Trent Boult", 150, false, true,
                    trentBoultStats);

            assertEquals(Role.BOWLER, jaspritBumrah.getRole());
            assertEquals(Role.BOWLER, lasithMalinga.getRole());
            assertEquals(Role.BOWLER, rashidKhan.getRole());
            assertEquals(Role.BOWLER, adamZampa.getRole());
            assertEquals(Role.BOWLER, trentBoult.getRole());
        }

        @Test
        public void testWicketKeepers() {
            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);

            int[] rishabhPantStats = {84, 2498, 71, 1694, 0, 0, 0};
            Player rishabhPant = new Player("Rishabh Pant", 200, true, false,
                    rishabhPantStats);

            int[] josButtlerStats = {65, 1968, 56, 1312, 0, 0, 0};
            Player josButtler = new Player("Jos Buttler", 150, true, true,
                    josButtlerStats);

            int[] jonnyBairstowStats = {28, 1038, 25, 730, 0, 0, 0};
            Player jonnyBairstow = new Player("Jonny Bairstow", 100, true, true,
                    jonnyBairstowStats);

            int[] nicholasPooranStats = {33, 606, 27, 391, 0, 0, 0};
            Player nicholasPooran = new Player("Nicholas Pooran", 100, true, true,
                    nicholasPooranStats);

            assertEquals(Role.WICKETKEEPER, msDhoni.getRole());
            assertEquals(Role.WICKETKEEPER, rishabhPant.getRole());
            assertEquals(Role.WICKETKEEPER, josButtler.getRole());
            assertEquals(Role.WICKETKEEPER, jonnyBairstow.getRole());
            assertEquals(Role.WICKETKEEPER, nicholasPooran.getRole());
        }

        @Test
        public void testAllRounders() {
            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            int[] shaneWatsonStats = {145, 3874, 125, 2809, 92, 2682, 2029};
            Player shaneWatson = new Player("Shane Watson", 150, false, true,
                    shaneWatsonStats);

            int[] benStokesStats = {43, 920, 36, 684, 28, 974, 683};
            Player benStokes = new Player("Ben Stokes", 150, false, true,
                    benStokesStats);

            int[] mohammadNabiStats = {17, 180, 12, 119, 13, 408, 343};
            Player mohammadNabi = new Player("Mohammad Nabi", 100, false, true,
                    mohammadNabiStats);

            int[] shakibAlHasanStats = {71, 793, 40, 637, 63, 1839, 1484};
            Player shakibAlHasan = new Player("Shakib Al Hasan", 200, false, true,
                    shakibAlHasanStats);

            assertEquals(Role.ALL_ROUNDER, hardikPandya.getRole());
            assertEquals(Role.ALL_ROUNDER, shaneWatson.getRole());
            assertEquals(Role.ALL_ROUNDER, benStokes.getRole());
            assertEquals(Role.ALL_ROUNDER, mohammadNabi.getRole());
            assertEquals(Role.ALL_ROUNDER, shakibAlHasan.getRole());
        }
    }

    public static class TestPlayerStats {
        @Test
        public void testBattingAverage() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            Player kaneWilliamson = new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats);

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            Player abDeVilliers = new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats);

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            Player rohitSharma = new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats);

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            Player suryakumarYadav = new Player("Suryakumar Yadav", 150, false, false,
                    suryakumarYadavStats);

            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            Player lasithMalinga = new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats);

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            Player rashidKhan = new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats);

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            Player adamZampa = new Player("Adam Zampa", 100, false, true,
                    adamZampaStats);

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            Player trentBoult = new Player("Trent Boult", 150, false, true,
                    trentBoultStats);

            assertTrue((viratKohli.getBattingAvg() > 37.395) && (viratKohli.getBattingAvg() < 37.405));
            assertTrue((kaneWilliamson.getBattingAvg() > 40.105) && (kaneWilliamson.getBattingAvg() < 40.115));
            assertTrue((abDeVilliers.getBattingAvg() > 39.705) && (abDeVilliers.getBattingAvg() < 39.715));
            assertTrue((rohitSharma.getBattingAvg() > 31.165) && (rohitSharma.getBattingAvg() < 31.175));
            assertTrue((suryakumarYadav.getBattingAvg() > 28.895) && (suryakumarYadav.getBattingAvg() < 28.905));
            assertTrue((jaspritBumrah.getBattingAvg() > 11.195) && (jaspritBumrah.getBattingAvg() < 11.205));
            assertTrue((lasithMalinga.getBattingAvg() > 5.495) && (lasithMalinga.getBattingAvg() < 5.505));
            assertTrue((rashidKhan.getBattingAvg() > 9.245) && (rashidKhan.getBattingAvg() < 9.255));
            assertTrue((adamZampa.getBattingAvg() > 2.495) && (adamZampa.getBattingAvg() < 2.505));
            assertTrue((trentBoult.getBattingAvg() > 4.325) && (trentBoult.getBattingAvg() < 4.335));
        }


        @Test
        public void testStrikeRate() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            Player kaneWilliamson = new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats);

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            Player abDeVilliers = new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats);

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            Player rohitSharma = new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats);

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            Player suryakumarYadav = new Player("Suryakumar Yadav", 150, false, false,
                    suryakumarYadavStats);

            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            Player lasithMalinga = new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats);

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            Player rashidKhan = new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats);

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            Player adamZampa = new Player("Adam Zampa", 100, false, true,
                    adamZampaStats);

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            Player trentBoult = new Player("Trent Boult", 150, false, true,
                    trentBoultStats);

            assertTrue((viratKohli.getStrikeRate() > 129.945) && (viratKohli.getStrikeRate() < 129.955));
            assertTrue((kaneWilliamson.getStrikeRate() > 131.265) && (kaneWilliamson.getStrikeRate() < 131.275));
            assertTrue((abDeVilliers.getStrikeRate() > 151.685) && (abDeVilliers.getStrikeRate() < 151.695));
            assertTrue((rohitSharma.getStrikeRate() > 130.395) && (rohitSharma.getStrikeRate() < 130.405));
            assertTrue((suryakumarYadav.getStrikeRate() > 135.705) && (suryakumarYadav.getStrikeRate() < 135.715));
            assertTrue((jaspritBumrah.getStrikeRate() > 96.545) && (jaspritBumrah.getStrikeRate() < 96.555));
            assertTrue((lasithMalinga.getStrikeRate() > 88.885) && (lasithMalinga.getStrikeRate() < 88.895));
            assertTrue((rashidKhan.getStrikeRate() > 137.035) && (rashidKhan.getStrikeRate() < 137.045));
            assertTrue((adamZampa.getStrikeRate() > 62.495) && (adamZampa.getStrikeRate() < 62.505));
            assertTrue((trentBoult.getStrikeRate() > 68.415) && (trentBoult.getStrikeRate() < 68.425));
        }


        @Test
        public void testBowlingAverage() {
            boolean exceptThrown = false;

            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            Player kaneWilliamson = new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats);

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            Player abDeVilliers = new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats);

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            Player rohitSharma = new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats);

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            Player suryakumarYadav = new Player("Suryakumar Yadav", 150, false, true,
                    suryakumarYadavStats);

            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            Player lasithMalinga = new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats);

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            Player rashidKhan = new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats);

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            Player adamZampa = new Player("Adam Zampa", 100, false, true,
                    adamZampaStats);

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            Player trentBoult = new Player("Trent Boult", 150, false, true,
                    trentBoultStats);

            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);

            try {
                viratKohli.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                kaneWilliamson.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                abDeVilliers.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                rohitSharma.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                suryakumarYadav.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.getBowlingAvg();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            assertTrue((jaspritBumrah.getBowlingAvg() > 23.045) && (jaspritBumrah.getBowlingAvg() < 23.055));
            assertTrue((lasithMalinga.getBowlingAvg() > 19.785) && (lasithMalinga.getBowlingAvg() < 19.795));
            assertTrue((rashidKhan.getBowlingAvg() > 19.595) && (rashidKhan.getBowlingAvg() < 20.605));
            assertTrue((adamZampa.getBowlingAvg() > 17.615) && (adamZampa.getBowlingAvg() < 17.625));
            assertTrue((trentBoult.getBowlingAvg() > 26.085) && (trentBoult.getBowlingAvg() < 26.095));
            assertTrue((hardikPandya.getBowlingAvg() > 31.255) && (hardikPandya.getBowlingAvg() < 31.265));
        }


        @Test
        public void testEconomy() {
            boolean exceptThrown = false;

            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            int[] kaneWilliamsonStats = {63, 1885, 47, 1436, 0, 31, 18};
            Player kaneWilliamson = new Player("Kane Williamson", 150, false, true,
                    kaneWilliamsonStats);

            int[] abDeVilliersStats = {184, 5162, 130, 3403, 0, 0, 0};
            Player abDeVilliers = new Player("AB de Villiers", 200, false, true,
                    abDeVilliersStats);

            int[] rohitSharmaStats = {213, 5611, 180, 4303, 15, 453, 339};
            Player rohitSharma = new Player("Rohit Sharma", 200, false, false,
                    rohitSharmaStats);

            int[] suryakumarYadavStats = {115, 2341, 81, 1725, 0, 8, 6};
            Player suryakumarYadav = new Player("Suryakumar Yadav", 150, false, false,
                    suryakumarYadavStats);

            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            int[] lasithMalingaStats = {122, 88, 16, 99, 170, 3365, 2827};
            Player lasithMalinga = new Player("Lasith Malinga", 200, false, true,
                    lasithMalingaStats);

            int[] rashidKhanStats = {76, 222, 24, 162, 93, 1912, 1812};
            Player rashidKhan = new Player("Rashid Khan", 200, false, true,
                    rashidKhanStats);

            int[] adamZampaStats = {14, 5, 2, 8, 21, 370, 287};
            Player adamZampa = new Player("Adam Zampa", 100, false, true,
                    adamZampaStats);

            int[] trentBoultStats = {62, 13, 3, 19, 76, 1983, 1417};
            Player trentBoult = new Player("Trent Boult", 150, false, true,
                    trentBoultStats);

            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);

            try {
                viratKohli.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                kaneWilliamson.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                abDeVilliers.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                rohitSharma.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                suryakumarYadav.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.getEconomy();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            assertTrue((jaspritBumrah.getEconomy() > 7.415) && (jaspritBumrah.getEconomy() < 7.425));
            assertTrue((lasithMalinga.getEconomy() > 7.135) && (lasithMalinga.getEconomy() < 7.145));
            assertTrue((rashidKhan.getEconomy() > 6.325) && (rashidKhan.getEconomy() < 6.335));
            assertTrue((adamZampa.getEconomy() > 7.735) && (adamZampa.getEconomy() < 7.745));
            assertTrue((trentBoult.getEconomy() > 8.395) && (trentBoult.getEconomy() < 8.405));
            assertTrue((hardikPandya.getEconomy() > 9.065) && (hardikPandya.getEconomy() < 9.075));
        }
    }

    public static class TestAuctionMethod {
        @Test
        public void testStandardBid() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            viratKohli.newBid(210);
            assertEquals(210, viratKohli.getPrice());
        }

        @Test
        public void testIllegalBid() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            boolean illegalBid = false;

            try {
                viratKohli.newBid(180);
            } catch(RuntimeException e) {
                illegalBid = true;
            }

            assertTrue(illegalBid);
        }
    }

    public static class TestCurrentGameMethods {
        @Test
        public void testValidRunsScored() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);

            viratKohli.initialiseGameStats();
            assertEquals(0, viratKohli.getCurrentRunsScored());
            viratKohli.incrementCurrentRunsScored(4);
            assertEquals(4, viratKohli.getCurrentRunsScored());
            viratKohli.incrementCurrentRunsScored(2);
            assertEquals(6, viratKohli.getCurrentRunsScored());
        }

        @Test
        public void testInvalidRunsScored() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);
            boolean exceptThrown = false;

            jaspritBumrah.initialiseGameStats();

            try {
                jaspritBumrah.incrementCurrentRunsScored(-2);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            jaspritBumrah.incrementCurrentRunsScored(3);
            assertEquals(3, jaspritBumrah.getCurrentRunsScored());

            try {
                jaspritBumrah.incrementCurrentRunsScored(-1);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
        }

        @Test
        public void testValidRunsConceded() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);
            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            jaspritBumrah.initialiseGameStats();
            hardikPandya.initialiseGameStats();

            assertEquals(0, jaspritBumrah.getCurrentRunsConceded());
            jaspritBumrah.incrementCurrentRunsConceded(6);
            assertEquals(6, jaspritBumrah.getCurrentRunsConceded());
            jaspritBumrah.incrementCurrentRunsConceded(3);
            assertEquals(9, jaspritBumrah.getCurrentRunsConceded());

            assertEquals(0, hardikPandya.getCurrentRunsConceded());
            hardikPandya.incrementCurrentRunsConceded(6);
            assertEquals(6, hardikPandya.getCurrentRunsConceded());
            hardikPandya.incrementCurrentRunsConceded(3);
            assertEquals(9, jaspritBumrah.getCurrentRunsConceded());
        }

        @Test
        public void testInvalidRunsConceded() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);
            boolean exceptThrown = false;

            jaspritBumrah.initialiseGameStats();
            viratKohli.initialiseGameStats();
            msDhoni.initialiseGameStats();

            try {
                viratKohli.incrementCurrentRunsConceded(4);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                viratKohli.getCurrentRunsConceded();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.incrementCurrentRunsConceded(4);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.getCurrentRunsConceded();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                jaspritBumrah.incrementCurrentRunsConceded(-2);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            jaspritBumrah.incrementCurrentRunsConceded(4);
            assertEquals(4, jaspritBumrah.getCurrentRunsConceded());

            try {
                jaspritBumrah.incrementCurrentRunsConceded(-1);
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
        }

        @Test
        public void testValidWickets() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);
            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            jaspritBumrah.initialiseGameStats();
            hardikPandya.initialiseGameStats();

            assertEquals(0, jaspritBumrah.getCurrentWickets());
            jaspritBumrah.incrementCurrentWickets();
            assertEquals(1, jaspritBumrah.getCurrentWickets());
            jaspritBumrah.incrementCurrentWickets();
            assertEquals(2, jaspritBumrah.getCurrentWickets());

            assertEquals(0, hardikPandya.getCurrentWickets());
            hardikPandya.incrementCurrentWickets();
            assertEquals(1, hardikPandya.getCurrentWickets());
            hardikPandya.incrementCurrentWickets();
            assertEquals(2, hardikPandya.getCurrentWickets());
        }

        @Test
        public void testInvalidWickets() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);
            boolean exceptThrown = false;

            viratKohli.initialiseGameStats();
            msDhoni.initialiseGameStats();

            try {
                viratKohli.incrementCurrentWickets();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                viratKohli.getCurrentWickets();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.incrementCurrentWickets();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.getCurrentWickets();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;
        }

        @Test
        public void testBallsFaced() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            viratKohli.initialiseGameStats();
            jaspritBumrah.initialiseGameStats();

            assertEquals(0, viratKohli.getCurrentBallsFaced());
            viratKohli.incrementCurrentBallsFaced();
            assertEquals(1, viratKohli.getCurrentBallsFaced());
            viratKohli.incrementCurrentBallsFaced();
            assertEquals(2, viratKohli.getCurrentBallsFaced());

            assertEquals(0, jaspritBumrah.getCurrentBallsFaced());
            jaspritBumrah.incrementCurrentBallsFaced();
            assertEquals(1, jaspritBumrah.getCurrentBallsFaced());
            jaspritBumrah.incrementCurrentBallsFaced();
            assertEquals(2, jaspritBumrah.getCurrentBallsFaced());
        }

        @Test
        public void testValidBallsBowled() {
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);
            int[] hardikPandyaStats = {92, 1476, 54, 959, 42, 1313, 869};
            Player hardikPandya = new Player("Hardik Pandya", 150, false, false,
                    hardikPandyaStats);

            jaspritBumrah.initialiseGameStats();
            hardikPandya.initialiseGameStats();

            assertEquals(0, jaspritBumrah.getCurrentBallsBowled());
            jaspritBumrah.incrementCurrentBallsBowled();
            assertEquals(1, jaspritBumrah.getCurrentBallsBowled());
            jaspritBumrah.incrementCurrentBallsBowled();
            assertEquals(2, jaspritBumrah.getCurrentBallsBowled());

            assertEquals(0, hardikPandya.getCurrentBallsBowled());
            hardikPandya.incrementCurrentBallsBowled();
            assertEquals(1, hardikPandya.getCurrentBallsBowled());
            hardikPandya.incrementCurrentBallsBowled();
            assertEquals(2, hardikPandya.getCurrentBallsBowled());
        }

        @Test
        public void testInvalidBallsBowled() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            int[] msDhoniStats = {220, 4746, 120, 3494, 0, 0, 0};
            Player msDhoni = new Player("MS Dhoni", 200, true, false,
                    msDhoniStats);
            boolean exceptThrown = false;

            viratKohli.initialiseGameStats();
            msDhoni.initialiseGameStats();

            try {
                viratKohli.incrementCurrentBallsBowled();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                viratKohli.getCurrentBallsBowled();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.incrementCurrentBallsBowled();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;

            try {
                msDhoni.getCurrentBallsBowled();
            } catch(RuntimeException e) {
                exceptThrown = true;
            }
            assertTrue(exceptThrown);
            exceptThrown = false;
        }

        @Test
        public void testDismissal() {
            int[] viratKohliStats = {207, 6283, 168, 4835, 4, 368, 251};
            Player viratKohli = new Player("Virat Kohli", 200, false, false,
                    viratKohliStats);
            int[] jaspritBumrahStats = {106, 56, 5, 58, 130, 2997, 2422};
            Player jaspritBumrah = new Player("Jasprit Bumrah", 200, false, false,
                    jaspritBumrahStats);

            viratKohli.initialiseGameStats();
            jaspritBumrah.initialiseGameStats();

            assertTrue(!viratKohli.wasDismissed());
            viratKohli.dismiss();
            assertTrue(viratKohli.wasDismissed());

            assertTrue(!jaspritBumrah.wasDismissed());
            jaspritBumrah.dismiss();
            assertTrue(jaspritBumrah.wasDismissed());
        }
    }
}
