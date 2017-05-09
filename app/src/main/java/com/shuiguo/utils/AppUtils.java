package com.shuiguo.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AppUtils {

	private static final String APK_MANIFEST = "AndroidManifest.xml";

    /**
     * 通过文件路径获取
     *
     * @param context
     * @param apkpath
     * @return
     */
    public static String getPackageNameByPath(Context context, String apkpath) {
    	if (apkpath.startsWith("file://")) {
    		apkpath = apkpath.replace("file://", "");
    	}
    	
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkpath, 0);
        String packageName = "";

        if (info != null) {
            // 获取这个就都可以获取了
            ApplicationInfo appInfo = info.applicationInfo;
            packageName = appInfo.packageName; // 得到安装包名称
        }
        return packageName;
    }

	/**
	 * To check if the apk file is available to install.
	 *
	 * @param apkFilePath the apk file path
	 * @return            true if available, otherwise return false
	 */
	public static boolean isApkAvailable(String apkFilePath) {
		File apkFile = new File(apkFilePath);
		if (!apkFile.exists()) {
			return false;
		}
		try {
			ZipFile zipFile = new ZipFile(apkFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				if (APK_MANIFEST.equals(zipEntry.getName())) {
					zipFile.close();
					return true;
				}
			}
			zipFile.close();
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * To check whether the apk file has installed.
	 *
	 * @param  context     the context
	 * @param  apkFilePath the apk file
	 * @return             true if installed, otherwise return false
	 */
	public static boolean isApkInstalled(Context context, String apkFilePath) {
		if (context == null || TextUtils.isEmpty(apkFilePath)) {
			return false;
		}

		File apkFile = new File(apkFilePath);
		if (!apkFile.exists()) {
			return false;
		}

		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPkgs = packageManager.getInstalledPackages(0);
		PackageInfo pkgInfo = packageManager.getPackageArchiveInfo(
				apkFilePath, PackageManager.GET_ACTIVITIES);
		if (pkgInfo != null) {
			String pkgName = pkgInfo.packageName;

			for (PackageInfo info : installedPkgs) {
				if (pkgName.equals(info.packageName)) {
					return true;
				}
			}
		}

		return false;
	}
    
    /**
     * 安装应用。
     * 
     * @param context  context
     * @param filePath the path of file
     */
    public static void installApk(Context context, String filePath) {
	    if (!filePath.startsWith("file://")) {
		    filePath = "file://" + filePath;
	    }

    	Intent intentInstall = new Intent(Intent.ACTION_VIEW);
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setDataAndType(Uri.parse(filePath), 
        		"application/vnd.android.package-archive");
        context.startActivity(intentInstall);
    }

	/**
	 * 打开应用程序
	 *
	 * @param context
	 * @param packageName
	 * @throws Exception
	 */
	public static void openApp(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo;
		try {
			pkgInfo = pm.getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return;
		}

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pkgInfo.packageName);
		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String pkg = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName(pkg, className);
			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}

    /**
     * 通过包名获取应用程序的名称。
     * @param context
     *            Context对象。
     * @param packageName
     *            包名。
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context,String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    //根据apk文件获取包名
    public synchronized String getPackgeNameByApkFile(Context context,String apkFilePath) throws Exception{
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = null;
        if (info != null) {
            appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;
            return packageName;
        }
        return null;
    }
}
