package com.bonioctavianus.parsepushdemo.helper;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bonioctavianus.parsepushdemo.database.DBAdapter;
import com.parse.ParsePushBroadcastReceiver;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
	private final String TAG = CustomPushReceiver.class.getSimpleName();
	private NotificationHelper notifHelper;
	private Intent parseIntent;
	private Context mContext;
	private DBAdapter db;

	// konstruktor
	public CustomPushReceiver() {
		super();
	}

	// method yang dicall saat notifikasi push diterima
	@Override
	public void onPushReceive(Context context, Intent intent) {
		super.onPushReceive(context, intent);
		Log.d(TAG, "onPushReceive() called...");

		if (intent != null) {
			this.mContext = context;
			this.parseIntent = intent;
			try {
				JSONObject json = new JSONObject(parseIntent.getExtras()
						.getString("com.parse.Data")); // key default JSON dari parse -> "com.parse.Data"
				Log.d(TAG, "Push Received : " + json.toString());
				parsePushJson(json);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// method untuk ekstrak JSON
	private void parsePushJson(JSONObject json) {
		try {
			// jika "isBackground" == TRUE -> notifikasi tidak akan ditampilkan
			boolean isBackground = json.getBoolean("is_background"); 
			JSONObject data = json.getJSONObject("data");
			String title = data.getString("title");
			String message = data.getString("message");
			
			// untuk mengambil waktu pada saat ini
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String received_at = DateToDay.setDateNotif(cal);
			
			// save data ke database
			db = new DBAdapter(mContext);
			long result = db.insertNotif(received_at, title, message);
			
			if (result != -1) {
				Log.d(TAG, "Save received push on DB success !");
			} else {
				Log.d(TAG, "Failed save received push on DB :(");
			}

			if (!isBackground) {
				Intent resultIntent = new Intent(mContext,
						com.bonioctavianus.parsepushdemo.MainActivity.class);
				showNotificationMessage(title, message, resultIntent);
			}

		} catch (JSONException e) {
			Log.e(TAG, "Push message json exception: " + e.getMessage());
		}
	}

	// fire notifikasi
	private void showNotificationMessage(String title, String message,
			Intent intent) {

		notifHelper = new NotificationHelper(mContext);
		intent.putExtras(parseIntent.getExtras());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		notifHelper.showNotification(title, message, intent);
	}
}