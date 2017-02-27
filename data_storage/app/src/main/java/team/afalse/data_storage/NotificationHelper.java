package team.afalse.data_storage;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Kyle on 2/27/2017.
 */

public class NotificationHelper {
    public static void showNotification(Context context, String msg) {
        makeNotification(context, msg);
    }

    private static void makeNotification(Context context, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Task Planner");
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        builder.setContentText(msg);
        pushNotification(context, builder);
    }

    private static void pushNotification(Context context, NotificationCompat.Builder builder) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
