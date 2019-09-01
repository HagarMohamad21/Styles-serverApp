package styles.zonetech.net.styles.server.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import styles.zonetech.net.styles.server.Activities.OrdersActivity;
import styles.zonetech.net.styles.server.Helpers.NotificationHelper;
import styles.zonetech.net.styles.server.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FireBaseMessagingServic";
    private static int count = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());
        //Displaying data in log
        //It is optional


//Calling method to generate notification
        String type=remoteMessage.getData().get("type");

//Calling method to generate notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotificationAPI26(type);
        } else {
            sendNotification(type);

        }

    }
    //This method is only generating push notification

    private void sendNotification(String  type) {
       Intent notificationIntent = new Intent(this, OrdersActivity.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.new_order))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }




    private void sendNotificationAPI26(String type) {
        String content = "";
        Intent notificationIntent = null;

                notificationIntent = new Intent(this, OrdersActivity.class);
                content = getString(R.string.new_order);


        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper helper;
        Notification.Builder builder;
        helper = new NotificationHelper(this,pendingIntent);
        builder = helper.getBuilder(getString(R.string.app_name), content, defaultSound);
        NotificationManager manager = helper.getNotificationManager();
        manager.notify(new Random().nextInt(), builder.build());



    }


}
