package styles.zonetech.net.styles.server.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

        @Override
        public void onTokenRefresh() {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
         //Displaying token on logcat
        }
    }

