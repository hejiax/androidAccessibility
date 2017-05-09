package com.shuiguo.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.shuiguo.redenvelope.BuildConfig;
import com.shuiguo.service.RedEnvelopeService;
import com.shuiguo.service.reportErrorlog;
import com.shuiguo.utils.Logx;

/**
 * The application for this project.
 *
 * @since  Feb. 11, 2015
 */
public class RedEnvApplication extends Application {
	private static RedEnvApplication sInstance;

	private static final String LOG_TAG = "Red";

	@Override
	public void onCreate() {
		super.onCreate();
//		Logx.setDebug(BuildConfig.DEBUG);
//		Logx.setTag(LOG_TAG);
//		startService(new Intent(this, RedEnvelopeService.class));
		reportErrorlog myService = new reportErrorlog();
		MyCrashHandler handler = MyCrashHandler.getInstance();
		handler.init(getApplicationContext(),myService);
		Thread.setDefaultUncaughtExceptionHandler(handler);

	}

	/**
	 * Get the global instance of application.
	 *
	 * @return the instance of application
	 */
	public static RedEnvApplication getInstance() {
		if (sInstance == null) {
			synchronized (RedEnvApplication.class) {
				sInstance = new RedEnvApplication();
			}
		}

		return sInstance;
	}


}
