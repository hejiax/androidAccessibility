package com.shuiguo.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Misc {
    private static final String[] SAFE_ARR = {
        /* 360安全卫士 */
            "com.qihoo360.mobilesafe",
		/* 360极客版 */
            "com.qihoo.antivirus",
		/* LBE安全大师 */
            "com.lbe.security",
		/* 百度手机卫士 */
            "cn.opda.a.phonoalbumshoushou",
		/* 乐安全 */
            "com.lenovo.safecenter",
		/* 腾讯手机管家 */
            "com.tencent.qqpimsecure",
		/* 金山手机卫士 */
            "com.ijinshan.guard",
		/* 金山毒霸 */
            "com.ijinshan.duba"
    };

    private static final List<String> SAFE_LIST = Arrays.asList(SAFE_ARR);

    private static final String MODEL = "MODEL";

    /**
     * 获取设备IMEI码。
     *
     * @param context context
     * @return imei
     */
    public static String imei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager == null ? "" : telephonyManager.getDeviceId();
    }

    /**
     * 获取mac地址。
     *
     * @param context context
     * @return mac地址
     */
    public static String mac(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager == null) {
            return "";
        }

        WifiInfo info = manager.getConnectionInfo();

        return info == null ? "" : info.getMacAddress();
    }

    /**
     * 获取啊怕怕安装列表。
     *
     * @param context context
     * @return 啊怕怕列表<包名，名称>
     */
    public static List<Map<String, String>> getAppList(Context context) {
        List<Map<String, String>> appList = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
			/* 非系统应用 */
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                Map<String, String> res = new HashMap<>();
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                res.put(appInfo.packageName, (String) pm.getApplicationLabel(appInfo));
                appList.add(res);
            }
        }

        return appList;
    }

    /**
	 * 检测服务是否存在。
	 *
	 * @param  context   context
	 * @param  className 服务类名
     * @return           如果活着返回true, 死了返回false
	 */
	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (info.service.getClassName().equals(className)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检测服务是否存在。
	 *
	 * @param  context context
	 * @param  clazz   服务类
	 * @return         如果活着返回true, 死了返回false
	 */
	public static boolean isServiceRunning(Context context, Class<?> clazz) {
		return isServiceRunning(context, clazz.getName());
	}

    /**
     * 检测App是否打开过(大部分情况可用)。
     *
     * @param context     context
     * @param packageName 应用包名
     * @return 打开过则返回true, 否则false
     */
    public static boolean isAppActivated(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo == null) {
                return false;
            }

            ApplicationInfo appInfo = packageInfo.applicationInfo;
            File file = new File(appInfo.dataDir);

			/* if the /data/data/<packagename> is not existed, return false */
            if (file == null || !file.exists()) {
                return false;
            }

            long lastUpdateTime = packageInfo.lastUpdateTime;
            long lastModifiedTime = file.lastModified();
			
			/* 如果差值大于1.5s(除非这人手速很快, 机子又特别牛x), 则认为激活过 */
            if (Math.abs(lastModifiedTime - lastUpdateTime) >= 1500) {
                return true;
            }

            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
    
    /**
     * 检查apk是否已经安装
     * 
     * @param  packageName 啊怕怕包名
     * @return             true, 木有安装返回false
     */
    public static boolean isApkInstalled(Context context, String packageName) {
    	if (TextUtils.isEmpty(packageName)) {
    		return false;
    	}
    	
    	PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
			/* 非系统应用 */
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                if (packageName.equals(appInfo.packageName)) {
                	return true;
                }
            }
        }
        
        return false;
    }

	/**
	 * 显示短时间toast。
	 *
	 * @param context context
	 * @param text    要显示的内容
	 */
	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取位于栈顶app的包名。
	 *
	 * @param  context context
	 * @return app包名
	 */
	public static String getTopPkgName(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfos = manager.getRunningTasks(1);
		if (taskInfos != null && !taskInfos.isEmpty()) {
			ComponentName componentName = taskInfos.get(0).topActivity;
			return componentName.getPackageName();
		}

		return null;
	}

	/**
	 * 通过url获取文件名。
	 *
	 * @param  url 文件下载url
	 * @return     文件名
	 */
	public static String getFilenameFromUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}

		return url.substring(url.lastIndexOf("/"));
	}

    /**
     * 是否安装安全软件。
     *
     * @param context context
     * @return 安全软件列表，如果没有则返回空
     */
    public static List<Integer> isSafe(Context context) {
        List<Integer> safeList = new ArrayList<>();
        List<Map<String, String>> appList = getAppList(context);

        for (Map<String, String> app : appList) {
            for (Entry<String, String> entry : app.entrySet()) {
                String key = entry.getKey();
                if (SAFE_LIST.contains(key)) {
                    safeList.add(SAFE_LIST.indexOf(key) + 1);
                }
            }
        }

        return safeList;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String model() {
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (MODEL.equals(name)) {
                    String model = field.get(null).toString();
                    return model == null ? "" : model;
                }
            }
        } catch (Exception e) {
			/* ignore */
        }

        return "";
    }

    /**
     * 获取本应用的版本信息。
     *
     * @return 版本信息
     */
    public static String ver(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info != null ? info.versionName : "0";
        } catch (NameNotFoundException e) {
			/* ignore */
        }
        return "0";
    }

    /**
     * 判断是否有SD卡。
     *
     * @return true则有，false木有
     */
    public static boolean sdcardAvailable() {
        return Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState());
    }
    
    /**
	 * Get the width of current screen.
	 * 
	 * @param  contex the contex
	 * @return the width
	 */
	public static int getScreenWidth(Context contex) {
		DisplayMetrics dm = contex.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	/**
	 * Get the height of current screen.
	 * 
	 * @param  contex the context
	 * @return the height
	 */
	public static int getScreenHeight(Context contex) {
		DisplayMetrics dm = contex.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * Get the random number according to the cardinal number.
	 * 
	 * @param  cardinal the cardinal number, the max random number will be (cardinal-1)
	 * @return          the random number
	 */
	public static int random(int cardinal) {
		Random random = new Random();
		return cardinal == 0 ? 0 : Math.abs(random.nextInt()) % cardinal;
	}

	/**
	 * 获取当前日期(天)
	 *
	 * @return
	 */
	public static int getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前时间戳(s).
	 *
	 * @return 时间戳
	 */
	public static long curTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 测试url是否可用.
	 *
	 * @param  url 要测试的url
	 * @return     true if available, otherwise return false
	 */
	public static boolean isUrlOK(String url) {
		HttpURLConnection conn = null;

		try {
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setInstanceFollowRedirects(false);
			conn.setUseCaches(false);
			int statusCode = conn.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				return false;
			}
		} catch (IOException e) {
			return false;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return true;
	}
}
