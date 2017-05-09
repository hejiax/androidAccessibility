package com.shuiguo.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.shuiguo.utils.Logx;
import com.shuiguo.utils.Propers;
import com.shuiguo.utils.Utils;

import java.util.List;

import static com.shuiguo.app.Constant.Sp.KEY_IS_ACTIVITIED;
import static com.shuiguo.app.Constant.Sp.KEY_SOUND;

/**
 * Red envelope accessibility service.
 *
 * @author Vincent Cheung
 * @since  Feb. 10, 2015
 */
public class RedEnvelopeService extends AccessibilityService {
	private static final String TAG = RedEnvelopeService.class.getSimpleName();
	private static final long INTERVAL_TIME = 3;
	private static final int STEP_START = 1;
	private static final int STEP_NOTIFICATION = 2;
	private static final int STEP_CHAT_ITEM = 3;

	private PowerManager.WakeLock mWakeLock;
	private KeyguardManager.KeyguardLock mKeyLock;

	private Handler mHandler = new Handler();
	private long mStartReceiveTime;
	private int mSetp = STEP_START;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (!Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)) {
			return;
		}
		Log.e("4","4");
		sendNotification(event);

		switch (mSetp) {
		case STEP_NOTIFICATION:
			Log.e("4","4");
			clickRedEnvelope(event);
			break;

		case STEP_CHAT_ITEM:
			Log.e("3","3");
			openRedEnvelope(event);
			break;
		}
	}

	@Override
	public void onInterrupt() {

	}

	/** get current timestamp */
	private long currTime() {
		return System.currentTimeMillis() / 1000;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void exitWeiXinOrQQ() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					Utils.goBack();
					Utils.goBack();
				} else {
					performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
				}
				Logx.d("go back home");
			}
		}, 2000);
	}

	@SuppressWarnings("deprecation")
	private void brightAndUnlockScreen() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
				PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		mKeyLock = km.newKeyguardLock("unlock");

		mWakeLock.acquire();
		mKeyLock.disableKeyguard();

		Logx.d("bright and unlock screen");
	}

	private void offAndLockScreen() {
		if (mWakeLock != null) {
			mWakeLock.release();
		}

		if (mKeyLock != null) {
			mKeyLock.reenableKeyguard();
		}

		Logx.d("off and lock scrren");
	}

	/** reset the step */
	private void reset() {
		mSetp = STEP_START;
		exitWeiXinOrQQ();
		offAndLockScreen();
	}

	/* get the notification pendding intent and send */
	private void sendNotification(AccessibilityEvent event) {
		if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			return;
		}

		Notification notification = (Notification) event.getParcelableData();
		String notifiText = event.getText().toString();

		if (notifiText != null && !notifiText.contains("[微信红包]") &&
				!notifiText.contains("[QQ红包]")) {
			return;
		}

		brightAndUnlockScreen();

		try {
			if (Propers.with(this).load().getBoolean(KEY_SOUND, true)) {
				Utils.playRingTone(this);
			}

			mSetp = STEP_NOTIFICATION;
			mStartReceiveTime = currTime();
			notification.contentIntent.send();
		} catch (PendingIntent.CanceledException e) {
			Log.e(TAG, "open notification error: " + e.getMessage());
		}
	}

	/* to check if the chat item is red envelope */
	private void clickRedEnvelope(AccessibilityEvent event) {
		Log.e("2","2");


	}

	/* click red envelope to open it */
	private void openRedEnvelope(AccessibilityEvent event) {
		Log.e("1","1");
		reset();
	}
}