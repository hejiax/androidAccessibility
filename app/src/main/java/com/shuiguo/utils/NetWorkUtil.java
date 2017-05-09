package com.shuiguo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 网络连接类
 * Created by zhengdelong on 14/12/30.
 */
public class NetWorkUtil {
	public static final int NETWORK_TYPE_WAP = 2;
	public static final int NETWORK_TYPE_INVALID = 0;
	public static final int NETWORK_TYPE_WIFI = 1;
	public static final int NETWORK_TYPE_2G = 2;
	public static final int NETWORK_TYPE_3G = 3;

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

	/* to check mobile net type */
	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephonyManager.getNetworkType();
		int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion <= 11) {
			switch (type) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_IDEN:
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;

			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true;

			default:
				return false;
			}
		} else {
			switch (type) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_IDEN:
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;

			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true;

			default:
				return false;
			}
		}
	}

	/**
	 * Get the type of network.
	 *
	 * @param  context context
	 * @return network type
	 */
	@SuppressWarnings("deprecation")
	public static int getNetWorkType(Context context) {
		int netType = NETWORK_TYPE_INVALID;

		if (context == null) {
			return NETWORK_TYPE_INVALID;
		}

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			int type = networkInfo.getType();
			switch (type) {
			case ConnectivityManager.TYPE_MOBILE:
				String proxyHost = android.net.Proxy.getDefaultHost();
				netType = TextUtils.isEmpty(proxyHost) ?
						(isFastMobileNetwork(context) ? NETWORK_TYPE_3G
								: NETWORK_TYPE_2G) : NETWORK_TYPE_WAP;
				break;
			case ConnectivityManager.TYPE_WIFI:
				netType = NETWORK_TYPE_WIFI;
				break;
			}
		} else {
			netType = NETWORK_TYPE_INVALID;
		}

		return netType;
	}
}
