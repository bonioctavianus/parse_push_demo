package com.bonioctavianus.parsepushdemo.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bonioctavianus.parsepushdemo.config.PushConfig;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ParseHelper {
	private static final String TAG = ParseHelper.class.getSimpleName();

	// method cek application ID dan client key
	public static void verifyParseConfiguration(Context context) {
		if (TextUtils.isEmpty(PushConfig.PARSE_APPLICATION_ID)
				|| TextUtils.isEmpty(PushConfig.PARSE_CLIENT_KEY)) {
			Toast.makeText(
					context,
					"Isi Application ID dan client Key di PushConfig.java",
					Toast.LENGTH_LONG).show();
			((Activity) context).finish();
		}
	}

	// method untuk register aplikasi dengan parse
	public static void registerParse(Context context) {
		Parse.initialize(context, PushConfig.PARSE_APPLICATION_ID,
				PushConfig.PARSE_CLIENT_KEY);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		ParsePush.subscribeInBackground(PushConfig.PARSE_CHANNEL,
				new SaveCallback() {
					@Override
					public void done(ParseException e) {
						Log.d(TAG, "Register Parse sukses !");
					}
				});
	}
}