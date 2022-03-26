package FirebaseConnectivity;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.annotation.PostConstruct;

public class FirebaseInitialise {

    @PostConstruct
    public static void initialise()  throws IOException {
        FileInputStream serviceAccount;
        FirebaseOptions options;

        serviceAccount = new FileInputStream("./playerDatabase.json");

        options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

}
