package FirebaseConnectivity;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    public static ArrayList<Map> getPlayerMapObjects() throws ExecutionException, InterruptedException {
        Firestore playerDatabase = FirestoreClient.getFirestore(); //create database object from firestore
        ApiFuture<QuerySnapshot> playersSnapshot = playerDatabase.collection("players").get(); //take snapshot of players collectioj
        List<QueryDocumentSnapshot> playerDocuments = playersSnapshot.get().getDocuments(); //retrieve documents from snapshot
        ArrayList<Map> playerMapObjects = new ArrayList<Map>();

        //for each document add the data to the array list
        for (QueryDocumentSnapshot document : playerDocuments) {
            playerMapObjects.add(document.getData());
        }

        return playerMapObjects;
    }

    public static Map getUser(String userName) throws ExecutionException, InterruptedException {
        Firestore playerDatabase = FirestoreClient.getFirestore();
        //retrieve the document corresponding to the username (which is the document ID)
        DocumentReference userRef = playerDatabase.collection("users").document(userName);

        //asynchronously retrieve user document
        ApiFuture<DocumentSnapshot> future = userRef.get();

        DocumentSnapshot user = future.get();

        if (user.exists()) {
            return user.getData();
        } else {
            throw new RuntimeException(); //throw exception if username doesn't exist in database
        }
    }

    public static ArrayList<String> getAllUsernames() throws ExecutionException, InterruptedException {
        Firestore playerDatabase = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> usersSnapshot = playerDatabase.collection("users").get();
        List<QueryDocumentSnapshot> userDocuments = usersSnapshot.get().getDocuments();
        ArrayList<String> usernames = new ArrayList<String>();

        //for each document add the document ID (user's username) to the array list
        for (QueryDocumentSnapshot document : userDocuments) {
            usernames.add(document.getId());
        }

        return usernames;
    }

    public static void updateUserDocument(String userName, Map userData) {
        Firestore playerDatabase = FirestoreClient.getFirestore();
        playerDatabase.collection("users").document(userName).set(userData, SetOptions.merge());
    }
}
