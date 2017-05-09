package com.shuiguo.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;


import android.content.ComponentName;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.shuiguo.activity.rudemos;
import com.shuiguo.redenvelope.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some utils used in this app.
 *
 * @author Vincent Cheung
 * @since  Feb. 11, 2015
 */
public class Utils {

	private TelephonyManager telephonyManager;
	public static  final String  SDCardpath=Environment.getExternalStorageDirectory()+"/Download";


	/**
	 * To check if the specified accessibility service is enabled.
	 *
	 * @param   context   context
	 * @param   clazzName service class name
	 * @return            true if enabled, otherwise return false
	 */
	public static boolean isAccessibilityServiceEnabled(Context context, String clazzName) {
		final String enabledServicesSetting = Settings.Secure.getString(
				context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

		if (TextUtils.isEmpty(enabledServicesSetting)) {
			return false;
		}

		String[] enabledServices = enabledServicesSetting.split(":");
		for (String componentName : enabledServices) {
			ComponentName enabledService = ComponentName.unflattenFromString(componentName);
			if (enabledService.getClassName().equals(clazzName)) {
				return true;
			}
		}

		return false;
	}
	/**
	 * 获取手机服务商信息
	 */
	public void getProvidersName(Context context) {
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String ProvidersName = "";
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = telephonyManager.getSubscriberId();
		String opname = telephonyManager.getSimOperatorName();
		opname = getProvidersName(opname.toLowerCase());
		int isp = 1;
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI == null) {

			new filestart().setispcode(isp);
		}else {
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				ProvidersName = "cm";//中国移动
			} else if (IMSI.startsWith("46001")) {
				ProvidersName = "cu";//中国联通
			} else if (IMSI.startsWith("46003")) {
				ProvidersName = "cn";//中国电信
			} else {
				ProvidersName = opname;
			}

			if (ProvidersName.equals("cu")) {//中国联通
				isp = 2;
			} else if (ProvidersName.equals("cm")) {//中国移动
				isp = 3;
			} else if (ProvidersName.equals("cn")) {//中国电信
				isp = 1;
			}
			new filestart().setispcode(isp);
		}
		//Log.e("isp", "" + isp);
	}
	private String getProvidersName(String temp) {
		if (temp.contains("移动") || temp.startsWith("china mobile") || temp.contains("cmcc")) {
			return "cm";
		} else if (temp.contains("联通") || temp.contains("china unicom")) {
			return "cu";
		} else if (temp.contains("电信") || temp.contains("46003")) {
			return "cn";
		} else {
			return "cm";
		}
	}
	/*获取城市烈镖*/
	public void cityCode(Context context) {

		new Thread(){
			@Override
			public void run(){
				loadfile s=new loadfile();
				String result=s.getURLResponse(new Constant().CITYURL);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String city = jsonObject.getString("province");
					//Log.e("city",city);
					int cityCode = CityCode.getShortCityName(city);
					//Log.e("cityCode",cityCode+"");
					new filestart().setcitycode(cityCode);
					//Log.e("cityCode",""+cityCode);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}
	/**
	 * To check if the specified accessibility service is enabled.
	 *
	 * @param   context context
	 * @param   clazz   service class
	 * @return          true if enabled, otherwise return false
	 */
	public static boolean isAccessibilityServiceEnabled(Context context, Class<?> clazz) {
		return isAccessibilityServiceEnabled(context, clazz.getName());
	}

	/**
	 * Simulate click back.
	 */
	public static void goBack() {
		String cmd = "input keyevent 4";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {

		}
	}

	/**
	 * 播放提示声音。
	 *
	 * @param context context
	 */
	public static void playRingTone(Context context) {
		MediaPlayer player = MediaPlayer.create(context, R.raw.ringtone);
		player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});

		player.start();
	}

	/**
	 * Open screen lock settings.
	 *
	 * @param context context
	 */
	public static void openScreenLockSetting(Context context) {
		String model = Build.MODEL;
		ComponentName componentName;

		if (model != null && model.toUpperCase(Locale.US).contains("MI")) {
			componentName = new ComponentName("com.android.settings",
					"com.android.settings.MiuiSecurityChooseUnlock");
		} else {
			componentName = new ComponentName("com.android.settings",
					"com.android.settings.ChooseLockGeneric");
		}

		Intent intent = new Intent();
		intent.setComponent(componentName);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			context.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
		}
	}

    /**
     * scales the bitmap based on the display metrics
     * <p/>
     * this gives us the right size bitmap for the icon
     * <p/>
     * returns null on error
     *
     * @param original input bitmap
     * @param densityDpi  current system display metrics
     * @return scaled icon
     */
    public static Bitmap scaledIcon(Bitmap original, int densityDpi) {
        Bitmap scaledIcon = null;
        if (original != null) {
            int dstWidth, dstHeight;
            switch (densityDpi) {
                case 120: // DisplayMetrics.DENSITY_LOW
                    dstWidth = dstHeight = 36;
                    break;
                case 160: // DisplayMetrics.DENSITY_MEDIUM
                default:
                    dstWidth = dstHeight = 48;
                    break;
                case 240: // DisplayMetrics.DENSITY_HIGH
                    dstWidth = dstHeight = 72;
                    break;
                case 320: // DisplayMetrics.DENSITY_XHIGH
                    dstWidth = dstHeight = 96;
                    break;
                case 480: // DisplayMetrics.DENSITY_XXHIGH
                    dstWidth = dstHeight = 144;
                    break;
            }

            scaledIcon = Bitmap.createScaledBitmap(original, dstWidth, dstHeight, true);
        }

        return scaledIcon;
    }

    /**
     * Cuts rounded corners out of the canvas
     *
     * @param canvas that you wish to paint on
     * @param width  width of the canvas
     * @param height height of the canvas
     */
    public static void roundCorners(Canvas canvas, float width, float height) {
        // Construct a path from a round rect. This will allow drawing with
        // an inverse fill so we can punch a hole using the round rect.
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_WINDING);
        RectF rect = new RectF(0, 0, width, height);
        rect.inset(1, 1);
        path.addRoundRect(rect, 8f, 8f, Path.Direction.CW);

        // Construct a paint that clears the outside of the rectangle and
        // draw.
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPath(path, paint);
    }
	/*打开apk*/
	public void RunApp(String packageName,Context context) {
		packageName=packageName.replaceAll("package:","");
		packageName=packageName.replaceAll(" ","");
		PackageInfo pi;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.setPackage(pi.packageName);
			PackageManager pManager = context.getPackageManager();
			List apps = pManager.queryIntentActivities(
					resolveIntent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName cn = new ComponentName(packageName, className);
				intent.setComponent(cn);
				context.startActivity(intent);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

	}
	/*
	* 写入文件到SD卡
	* */
	public void saveToSDCard(String name, String content) {

		FileOutputStream fos = null;

		try{

			//Environment.getExternalStorageDirectory()。获取sd卡的路径
			File file = new File(SDCardpath,name);
			fos = new FileOutputStream(file);

			fos.write(content.getBytes());
		}catch(Exception e){

			e.printStackTrace();

		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	* 读取sdk卡文件
	* */
	public String readToSDCard(String filename){
			File file = new File(SDCardpath,filename);
			try {
				if(!file.isFile()){
					return null;
				}
				FileInputStream inputStream = new FileInputStream(file);
				byte[] b = new byte[inputStream.available()];
				inputStream.read(b);
				deleteSDCard(filename);
				return new String(b);
			} catch (Exception e) {
				return null;
			}
	}
	/*
	删除SD卡文件
	* */
	public void deleteSDCard(String filename){
			File file = new File(SDCardpath,filename);
			if(file.isFile()){
				file.delete();
			}
			file.exists();
	}
	/*
	* 清楚空格
	* */
	public  String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	/*保持屏幕高量*/
	public void unlockScreen(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("MyKeyguardLock");
		keyguardLock.disableKeyguard();

		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

		wakeLock.acquire();
	}
	/*保持屏幕高量*/
	public void unlockScreens(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("MyKeyguardLock");
		keyguardLock.disableKeyguard();

		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

		wakeLock.acquire();
	}
	/*排除已安装*/
	public boolean haveinstall(String pname,Context context){
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

		for(int i=0;i<packages.size();i++) {
			PackageInfo packageInfo = packages.get(i);
			if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0)
			{

				String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
				if(appName.indexOf(pname)!=-1){
					return true;
				}
			}
			else
			{
				String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
				if(appName.indexOf(pname)!=-1){
					return true;
				}
			}
		}
		return false;
	}
	/*占顶*/
	public  void startAct(Context context){

			String toname = isTopActivy(context);
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

			boolean isScreenOn = pm.isScreenOn();
		//!toname.equals("com.shuiguo.redenvelope") &&
			if (!toname.equals("com.shuiguo.redenvelope") &&new filestart().getifredemos()==1) {
				Intent it = new Intent(context, rudemos.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(it);
				// Log.e("现在占顶",tonames);
			}


	}
		/**
		 * 检测某ActivityUpdate是否在当前Task的栈顶
		 */
		public String isTopActivy(Context context) {

				ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
				ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
				String packageName = cn.getPackageName();

				if (packageName != null) {
					return packageName;
				}
				return null;

		}
	/*预先下载*/

}
