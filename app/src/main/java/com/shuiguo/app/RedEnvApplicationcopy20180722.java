package com.shuiguo.app;

import android.app.Application;
import android.content.Intent;

import com.shuiguo.redenvelope.BuildConfig;
import com.shuiguo.service.RedEnvelopeService;
import com.shuiguo.utils.Logx;

/**
 * The application for this project.
 *
 * @since  Feb. 11, 2015
 */
public class RedEnvApplicationcopy20180722 extends Application {
	private static RedEnvApplicationcopy20180722 sInstance;

	private static final String LOG_TAG = "Red";

	@Override
	public void onCreate() {
		super.onCreate();

		Logx.setDebug(BuildConfig.DEBUG);
		Logx.setTag(LOG_TAG);
		startService(new Intent(this, RedEnvelopeService.class));

	}

	/**
	 * Get the global instance of application.
	 *
	 * @return the instance of application
	 */
	public static RedEnvApplicationcopy20180722 getInstance() {
		if (sInstance == null) {
			synchronized (RedEnvApplicationcopy20180722.class) {
				sInstance = new RedEnvApplicationcopy20180722();
			}
		}

		return sInstance;
	}


}
