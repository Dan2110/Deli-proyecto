package unimet.deli;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by IAFIGLIOLA on 18/08/2017.
 */

public class mFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "Token";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,token);
    }

}