package es.unex.geoapp.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.unex.geoapp.MainActivity;
import es.unex.geoapp.R;
import es.unex.geoapp.locationmanager.LocationManager;
import es.unex.geoapp.messagemanager.LocationMessage;
import es.unex.geoapp.messagemanager.NotificationHelper;
import es.unex.geoapp.messagemanager.NotificationKind;
import es.unex.geoapp.messagemanager.RequestLocationMessage;
import es.unex.geoapp.messagemanager.SendLocationsMessage;
import es.unex.geoapp.model.LocationFrequency;
import es.unex.geoapp.retrofit.NotificationFirebase;

public class FirebaseService extends FirebaseMessagingService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    public FirebaseService() {
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKEN FIREBASE", s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String TAG = "FirebaseService: ";
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("msg"));

            Map<String, String> data = remoteMessage.getData();

            NotificationFirebase notification = gson.fromJson(String.valueOf(data), NotificationFirebase.class);

            if (notification.getRequest() != null) {
                processRequest(notification);
                Log.e("HEATMAP", "Locations received from sender.");

            } else {
                if (notification.getReply() != null) {
                    Log.e("HEATMAP", "Reply received.");
                    processReply(notification);


                } else {
                    Log.e("HEATMAP", "Another kind of message: ");
                }

            }

        }

    }

    private void processReply(NotificationFirebase notification) {
        LocationManager.storeLocations(notification.getReply().getLocationList());
    }

    private void processRequest(NotificationFirebase notification) {
        List<LocationFrequency> locations = LocationManager.getLocationHistory(notification.getRequest().getBeginDate(), notification.getRequest().getEndDate(), notification.getRequest().getLatitude(), notification.getRequest().getLongitude(), notification.getRequest().getRadius());
        List<List<LocationFrequency>> locationLists = partition(locations, 25);

        for (List<LocationFrequency> list : locationLists) {
            NotificationHelper.sendLocationsMessage(list, notification.getRequest().getSenderId());
        }
    }

    private static <T> List<List<T>> partition(List<T> input, int size) {
        List<List<T>> lists = new ArrayList<List<T>>();
        for (int i = 0; i < input.size(); i += size) {
            lists.add(input.subList(i, Math.min(input.size(), i + size)));
        }
        return lists;
    }

    private void showNotification(String title, String body) {

        //Intent to open APP when click in the notification.
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "1";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Android Server");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setContentIntent(resultPendingIntent).setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.common_full_open_on_phone).setContentTitle(title).setContentText(body);
        notificationManager.notify((new Random().nextInt()), notificationBuilder.build());
    }
}
