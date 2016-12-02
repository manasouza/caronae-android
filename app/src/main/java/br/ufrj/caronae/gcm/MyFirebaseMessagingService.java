package br.ufrj.caronae.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.ufrj.caronae.App;
import br.ufrj.caronae.R;
import br.ufrj.caronae.acts.MainAct;

/**
 * Created by Luis-DELL on 10/28/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // TODO: HANDLE MENSSAGES
        Log.v("MSG", "Message Received");
        createNotification("chat", "Luis", remoteMessage.toString(), "");
        super.onMessageReceived(remoteMessage);
    }

//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Map data = remoteMessage.getData();
//        String message = data.getString("message");
//        String msgType = data.getString("msgType");
//        String senderName = data.getString("senderName");
//        String rideId = data.getString("rideId");
//
//        Log.i("onMessageReceived", message);
//
//        if (msgType != null && msgType.equals("chat")) {
//            String senderId = data.getString("senderId");
//            //noinspection ConstantConditions
//            if (senderId.equals(App.getUser().getDbId() + "")) {
//                return;
//            }
//
//            String time = data.getString("time");
//            ChatMessageReceived cmr = new ChatMessageReceived(senderName, senderId, message, rideId, time);
//            cmr.save();
//            App.getBus().post(cmr);
//            new NewChatMsgIndicator(Integer.valueOf(rideId)).save();
//        }
//
//        if (msgType != null && msgType.equals("joinRequest")) {
//            new RideRequestReceived(Integer.valueOf(rideId)).save();
//        }
//
//        if (msgType != null && msgType.equals("finished")) {
//            new UnsubGcmTopic(getApplicationContext(), rideId).execute();
//            App.getBus().post(new RideEndedEvent(rideId));
//            ActiveRide.deleteAll(ActiveRide.class, "db_id = ?", rideId);
//        }
//
//        if (msgType != null && msgType.equals("cancelled")) {
//            new UnsubGcmTopic(getApplicationContext(), rideId).execute();
//            App.getBus().post(new RideEndedEvent(rideId));
//            ActiveRide.deleteAll(ActiveRide.class, "db_id = ?", rideId);
//        }
//
//        if (msgType != null && msgType.equals("accepted")) {
//            new CheckSubGcmTopic().execute(rideId);
//            //new DeleteConflictingRequests().execute(rideId);
//        }
//
//        if (SharedPref.getNotifPref().equals("true"))
//            createNotification(msgType, senderName, message, rideId);
//    }

    private void createNotification(String msgType, String senderName, String message, String rideId) {
        String title;
        Intent resultIntent;
        if (msgType.equals("chat")) {
            title = "Nova mensagem";
            message = senderName + ": " + message;

//            List<ChatAssets> l = ChatAssets.find(ChatAssets.class, "ride_id = ?", rideId);
//            if (l != null && !l.isEmpty()) {
//                resultIntent = new Intent(this, ChatAct.class);
//                resultIntent.putExtra("rideId", rideId);
//            } else {
//                resultIntent = new Intent(this, MainAct.class);
//            }
            resultIntent = new Intent(this, MainAct.class);

        } else {
            title = "Aviso de carona";
            resultIntent = new Intent(this, MainAct.class);
        }

        resultIntent.putExtra("msgType", msgType);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Context context = App.inst();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}