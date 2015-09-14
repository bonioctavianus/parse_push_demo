package com.bonioctavianus.parsepushdemo.helper;

import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bonioctavianus.parsepushdemo.R;
import com.bonioctavianus.parsepushdemo.config.PushConfig;

public class NotificationHelper {
	private static final String TAG = NotificationHelper.class.getSimpleName();
	private Context mContext;

	// konstruktor
	public NotificationHelper() {

	}

	// konstruktor
	public NotificationHelper(Context context) {
		this.mContext = context;
	}

	// method untuk menampilkan notifikasi di system tray jika aplikasi sedang tidak dibuka (not on background)
	public void showNotification(String title, String message, Intent intent) {
		if (!TextUtils.isEmpty(title) & !TextUtils.isEmpty(message)) {
			if (isAppIsInBackground(mContext)) {
			Log.d(TAG, "Processing Notification...");
				int icon = R.drawable.ic_launcher;
				int mNotificationId = PushConfig.NOTIFICATION_ID;
				PendingIntent resultPendingIntent = PendingIntent.getActivity(
						mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

				NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						mContext);
				Notification notification = mBuilder
						.setSmallIcon(icon)
						.setTicker(title)
						.setWhen(0)
						.setAutoCancel(true)
						.setContentTitle(title)
						.setStyle(inboxStyle)
						.setContentIntent(resultPendingIntent)
						.setSound(
								RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
						.setContentText(message).build();

				NotificationManager notificationManager = (NotificationManager) mContext
						.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(mNotificationId, notification);
				
				// intent ke main activity jika aplikasi sedang dibuka (on background)
			} else {
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            mContext.startActivity(intent);
			  }
		} else {
			return;
		}
	}
	
	public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
 
        return isInBackground;
    }
}