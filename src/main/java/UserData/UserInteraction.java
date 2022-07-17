package UserData;

import FirebaseConnectivity.FirebaseService;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UserInteraction {
    /**
     * Method that registers a new user by making them create a new username and password and validating the username
     * and password. Both must be at least 6 characters long and the username must not already exist.
     * @return 2 element string array, first element is username and second element is password
     * @throws ExecutionException Firebase error
     * @throws InterruptedException Firebase error
     */
    public static String[] registerUser() throws ExecutionException, InterruptedException {
        String userName = "";
        String password = "";
        Scanner loginScanner = new Scanner(System.in);
        ArrayList<String> allUsernames = FirebaseService.getAllUsernames(); //retrieve all usernames from firestore
        String[] loginDetails = new String[2];
        boolean nameValid = false;
        boolean usernameUnique;
        boolean passwordValid = false;

        System.out.println("Please remember the username and password that you create, as there is currently no" +
                " 'forgot password' feature.");
        //enter and validate username
        while (!nameValid) {
            System.out.print("Create a username, must be at least 6 characters long: ");
            userName = loginScanner.next();

            if (userName.length() < 6) {
                System.out.println("Username MUST be at least 6 characters long.");
            } else {
                //check that username isn't already taken
                usernameUnique = true;
                for (String otherUsername : allUsernames) {
                    if (otherUsername.equals(userName)) {
                        System.out.println("Sorry, that username is already taken.");
                        usernameUnique = false;
                        break;
                    }
                }

                if (usernameUnique) {
                    nameValid = true;
                }
            }
        }

        //enter and validate password
        while (!passwordValid) {
            System.out.print("Create a password, must be at least 6 characters long: ");
            password = loginScanner.next();
            if (password.length() < 6) {
                System.out.println("Password MUST be at least 6 characters long.");
            } else {
                passwordValid = true;
            }
        }

        loginDetails[0] = userName;
        loginDetails[1] = password;

        return loginDetails;
    }


    /**
     * Method that logs in a user by inputting the username and password, and then returning them.
     * @return 2 element string array, first element is username and second element is password
     */
    public static String[] loginUser() {
        Scanner loginScanner = new Scanner(System.in);
        String userName, password;
        String loginDetails[] = new String[2];

        System.out.println("Enter your username: ");
        userName = loginScanner.next();
        System.out.println("Enter your password: ");
        password = loginScanner.next();

        loginDetails[0] = userName;
        loginDetails[1] = password;

        return loginDetails;
    }

    /**
     * Method to collect input from the user regarding whether they would like to create a new account or login using
     * an existing account. Returns the choice as a boolean value.
     * @return True if user wants to create new account, false is user wants to login with existing account
     */
    public static boolean newAccountWanted() {
        Scanner choiceInput = new Scanner(System.in);
        int choiceInt;

        System.out.println("1. Create new account");
        System.out.println("2. Login with existing account");
        System.out.print("Enter a number for which you would like to do: ");
        choiceInt = choiceInput.nextInt();

        return choiceInt == 1;
    }

    /**
     * Method to collect input from the user regarding whether they would like to play just a single match or play a
     * full IPL tournament. Returns the choice as a boolean value.
     * @return True if the user wants to play a full tournament, false if the user just wants a single match
     */
    public static boolean tournamentWanted() {
        Scanner choiceInput = new Scanner(System.in);
        int choiceInt;

        System.out.println("1. Play single match (randomised players)");
        System.out.println("2. Play full IPL tournament");
        System.out.print("Enter a number for which you would like to do: ");
        choiceInt = choiceInput.nextInt();

        return choiceInt == 2;
    }

    /**
     * Method to input from the user how many teams they want to play with (including themselves) in the IPL tournament.
     * Must be between 5 and 8 inclusive.
     * @return The number of teams in the tournament
     */
    public static int getNumTeams() {
        Scanner choiceInput = new Scanner(System.in);
        int choiceInt;

        System.out.println("Select how many teams you want to play with in the tournament (including your own). Please"
            + " note that the minimum is 5 and the maximum is 8.");

        //input and validate number of teams wanted from user
        do {
            System.out.print("Enter the number of teams: ");
            choiceInt = choiceInput.nextInt();
        } while ((choiceInt < 5) || (choiceInt > 8));

        return choiceInt;
    }

    /**
     * Method to input from the user whether they would like to play with a full auction or play the tournament with
     * players randomly allocated to the teams. Returns the choice as a boolean value.
     * @return True if the user wants an auction, false if they want random player allocation
     */
    public static boolean auctionWanted() {
        Scanner choiceInput = new Scanner(System.in);
        int choiceInt;

        System.out.println("Select whether you want to play a full auction or with randomised players. A full auction "
            + "means that you will participate in an auction alongside the other teams and purchase players for your "
            + "franchise. Playing with randomised players means that there will be no auction and the players will be "
            + "randomly allocated to the teams.");

        System.out.println("1. Play with full auction");
        System.out.println("2. Play with randomised players");
        System.out.print("Enter a number for which you would like to do: ");
        choiceInt = choiceInput.nextInt();

        return choiceInt == 1;
    }
}
