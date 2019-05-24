package unimet.deli;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by IAFIGLIOLA on 18/08/2017.
 */

public class mFirebaseCloudMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d("Origen",from);

        if(remoteMessage.getNotification()!=null){
            Log.d("Notificacion",remoteMessage.getNotification().getBody());
            notificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        }

    }

    private void notificacion(String title, String body) {

        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nM.notify(0,notificationBuilder.build());

    }
}
