package br.com.ufop.daniel.d2dwifidirect.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.activity.NotificationActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TITLE_NOTIFICATION_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TITLE_NOTIFICATION_RECEIVED_ERROR;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TITLE_NOTIFICATION_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TITLE_NOTIFICATION_SEND_ERROR;

/**
 * Created by daniel on 03/12/17.
 */

public class Notification {

    public static void showNotification(Context context, String desc, String title) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, NotificationActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("WiFiDirectD2D");
        builder.setContentTitle(title);

        switch (title){
            case TITLE_NOTIFICATION_RECEIVED: builder.setSmallIcon(R.drawable.stat_sys_download_anim0); break;
            case TITLE_NOTIFICATION_RECEIVED_ERROR: builder.setSmallIcon(R.drawable.stat_notify_error); break;
            case TITLE_NOTIFICATION_SEND: builder.setSmallIcon(R.drawable.stat_sys_upload_anim0); break;
            case TITLE_NOTIFICATION_SEND_ERROR: builder.setSmallIcon(R.drawable.stat_notify_error); break;
        }

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher1));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.addLine(desc);
        builder.setStyle(style);

        android.app.Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.ic_launcher1, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {

        }
    }

    public static void showNotification(Context context, String[] descs, String title) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, NotificationActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("WiFiDirectD2D");
        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.ic_download);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher1));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (int i = 0; i < descs.length; i++) {
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        android.app.Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.ic_launcher1, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {

        }
    }
}

