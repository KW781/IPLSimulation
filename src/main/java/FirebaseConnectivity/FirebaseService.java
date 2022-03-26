package FirebaseConnectivity;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    public static ArrayList<Map> getPlayerMapObjects() throws ExecutionException, InterruptedException {
        Firestore playerCollection = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> playersSnapshot = playerCollection.collection("players").get();
        List<QueryDocumentSnapshot> playerDocuments = playersSnapshot.get().getDocuments();
        ArrayList<Map> playerMapObjects = new ArrayList<Map>();

        for (QueryDocumentSnapshot document : playerDocuments) {
            playerMapObjects.add(document.getData());
        }

        return playerMapObjects;
    }
}
