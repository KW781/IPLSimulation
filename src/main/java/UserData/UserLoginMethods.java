package UserData;

import FirebaseConnectivity.FirebaseService;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UserLoginMethods {
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
        ArrayList<String> allUsernames;
        String[] loginDetails = new String[2];
        boolean nameValid = false;
        boolean usernameUnique;
        boolean passwordValid = false;

        //enter and validate username
        while (!nameValid) {
            System.out.print("Create a username, must be at least 6 characters long: ");
            userName = loginScanner.next();

            if (userName.length() < 6) {
                System.out.println("Username MUST be at least 6 characters long.");
            } else {
                allUsernames = FirebaseService.getAllUsernames(); //retrieve all usernames from firestore

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
}
