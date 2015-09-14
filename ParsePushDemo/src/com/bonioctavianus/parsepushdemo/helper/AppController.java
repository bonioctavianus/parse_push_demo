package com.bonioctavianus.parsepushdemo.helper;

import android.app.Application;

public class AppController extends Application {
	
	// objek singleton untuk register parse
	private static AppController mParseController;

	@Override
	public void onCreate() {
		super.onCreate();
		mParseController = this;
		
		// register with parse
        ParseHelper.registerParse(mParseController);

	}

	public static synchronized AppController getInstance() {
		return mParseController;
	}
}