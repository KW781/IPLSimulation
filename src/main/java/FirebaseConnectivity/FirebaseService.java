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
        Firestore playerDatabase = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> playersSnapshot = playerDatabase.collection("players").get();
        List<QueryDocumentSnapshot> playerDocuments = playersSnapshot.get().getDocuments();
        ArrayList<Map> playerMapObjects = new ArrayList<Map>();

        for (QueryDocumentSnapshot document : playerDocuments) {
            playerMapObjects.add(document.getData());
        }

        return playerMapObjects;
    }

    public static Map getUser(String userName) throws ExecutionException, InterruptedException {
        Firestore playerDatabase = FirestoreClient.getFirestore();
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

    public static void updateUserDocument(String userName, Map userData) {
        Firestore playerDatabase = FirestoreClient.getFirestore();
        playerDatabase.collection("users").document(userName).set(userData, SetOptions.merge());
    }
}
