package singh.saurabh.iscfresnostate.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import singh.saurabh.iscfresnostate.R;
import singh.saurabh.iscfresnostate.view.JobDescription;
import singh.saurabh.iscfresnostate.view.MenuScreenActivity;
import singh.saurabh.iscfresnostate.view.PostDescription;

/**
 * Created by ${SAURBAH} on ${10/29/14}.
 */
public class CustomPushNotificationReceiver extends ParsePushBroadcastReceiver {

    private static int numMessages = 1;
    private static int NOTIFICATION_ID = 1;
    private static Intent mIntent;
    private static Context mContext;
    private static final String TAG = CustomPushNotificationReceiver.class.getSimpleName();
    private static NotificationCompat.Builder mBuilder;
    private static Intent resultIntent;

    private static String mObjectID;
    private static String mAlert; // This is the message string that is sent from push console
    private static String mSection = "0";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        mContext = context;
        mIntent = intent;
        Log.d(TAG, "onPushReceive");

        try {
            if (intent == null)
                Log.d(TAG, "Receiver intent null");
            else {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                mObjectID = json.getString("objectId");
                mAlert = json.getString("alert");
                mSection = json.getString("section");
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

        Uri notifySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.push_icon);
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setContentText(mAlert);
        mBuilder.setTicker(mAlert);
//        mBuilder.setNumber(numMessages);
        mBuilder.setSound(notifySound);
        mBuilder.setAutoCancel(true);

        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.parse.push.intent.OPEN"), 0x8000000);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
//        super.onPushOpen(context, intent);
        ParseAnalytics.trackAppOpened(intent);

        if (mSection.equals("1"))
            resultIntent = new Intent(context, PostDescription.class);
        else if (mSection.equals("4"))
            resultIntent = new Intent(context, JobDescription.class);
        else
            resultIntent = new Intent(context, MenuScreenActivity.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("objectId", mObjectID);
        context.startActivity(resultIntent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        NOTIFICATION_ID++;
        return null;
    }
}