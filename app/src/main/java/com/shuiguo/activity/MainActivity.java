package com.shuiguo.activity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.shuiguo.helper.Prefs;
import com.shuiguo.redenvelope.R;
import com.shuiguo.service.MonitorService;
import com.shuiguo.service.pageruiserviceorderapk;
import com.shuiguo.utils.Constant;
import com.shuiguo.utils.Propers;
import com.shuiguo.utils.Udid;
import com.shuiguo.utils.Utils;
import com.shuiguo.utils.checkutil;
import com.shuiguo.utils.filestart;
import com.shuiguo.utils.picCheck;
import com.shuiguo.view.TDImageButton;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static com.shuiguo.app.Constant.Sp.KEY_IS_ACTIVITIED;
import static com.shuiguo.app.Constant.Sp.KEY_SOUND;

public class  MainActivity extends Activity implements View.OnClickListener{
	private Context mContext;
	private TDImageButton mBtnStart;                   //开抢按钮
	private TDImageButton mBtnDonate;                  //爱心赞助按钮
	private TDImageButton mBtnShare;                  //分享按钮
	private TDImageButton mBtnSound;                  //声音开关按钮
	private TDImageButton mBtnHelp;                   //帮助按钮
	private static final Intent sSettingsIntent =
			new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
	AlertDialog dialog;
	private static int isopen=2;
	ViewPager advPager;
	String errorlog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_view);
		mContext=MainActivity.this;
		Date date=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yd");
		String time=dateFormat.format(date);
		errorlog=new Utils().readToSDCard(time + "redmoneyerror.txt");
		if(errorlog!=null){
			new Thread(){
				@Override
				public void run(){
					errorlog=new Utils().replaceBlank(errorlog);
					new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=errorLogs" + "&uid=" + Udid.with(MainActivity.this).fetch() + "&errorcoonet=" + errorlog);
					errorlog=null;
				}
			}.start();
		}
		initView();
        initData();
		registerObserver();
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

	private final Handler viewsetAlpha = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int s=(int)msg.obj;
			RelativeLayout ss = (RelativeLayout) findViewById(R.id.adbanner);
			ss.setAlpha((0.0f+s)/10);
		}

	};
	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			new picCheck().initViewPager();
			super.handleMessage(msg);
		}

	};
	@Override
	protected void onResume() {
		super.onResume();
		//this.showCha();//插屏广告
		if (Utils.isAccessibilityServiceEnabled(this, MonitorService.class)) {
			Propers.with(this).load().save(KEY_IS_ACTIVITIED, true);
			mBtnStart.setBackgroundResource(R.mipmap.stop);
			mBtnDonate.setBackgroundResource(R.mipmap.lovetwo);
		} else {
			Propers.with(this).load().save(KEY_IS_ACTIVITIED, false);
			mBtnStart.setBackgroundResource(R.mipmap.start);
		}
		new Thread(){
			@Override
			public void run(){
				String isopens=new checkutil().getURLResponse(new Constant().isopen );
				if(isopens.indexOf("1")!=-1){
					//isopen=1;
					MobclickAgent.onResume(MainActivity.this);
				}else{
				}
			}
		}.start();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(isopen==1) {
			MobclickAgent.onPause(this);
		}else{
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mHomeKeyEventReceiver);
	}

	private void registerObserver() {
		ContentObserver observer = new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				if (Utils.isAccessibilityServiceEnabled(mContext, MonitorService.class)) {
					mContext.startActivity(new Intent(mContext, MainActivity.class));
				}
			}
		};

		Uri accessUri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
		getContentResolver().registerContentObserver(accessUri, false, observer);
	}

    /**
     * 监听是否点击了home键
     */
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台

                    boolean isFirst = Prefs.with(MainActivity.this, "app").getBoolean("isfirsts", true);
                    if (isFirst) {
                        /*初次启动*/
                        Prefs.with(MainActivity.this, "app").save("isfirsts", false);
						/*初始化Download休眠时间*/


						//unregisterReceiver(mHomeKeyEventReceiver);
						MainActivity.this.finish();
                    } else {
						MainActivity.this.finish();
                        /*二次启动*/
                    }

                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

	/**初始化页面按钮*/
	public void initView(){
		mBtnStart = (TDImageButton) findViewById(R.id.star_bt);
		mBtnDonate = (TDImageButton) findViewById(R.id.love_bt);
		mBtnShare = (TDImageButton) findViewById(R.id.share_bt);
		mBtnSound = (TDImageButton) findViewById(R.id.sound_bt);
		mBtnHelp = (TDImageButton) findViewById(R.id.help_bt);
		if (Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)) {
			mBtnStart.setBackgroundResource(R.mipmap.stop);
			mBtnDonate.setBackgroundResource(R.mipmap.lovetwo);
		} else {
			mBtnStart.setBackgroundResource(R.mipmap.start);
			if(Utils.isAccessibilityServiceEnabled(this, MonitorService.class)){
				mBtnDonate.setBackgroundResource(R.mipmap.loves);
			}

		}



		if(Propers.with(this).load().getBoolean(KEY_SOUND, true)) {
			mBtnSound.setBackgroundResource(R.mipmap.sound_on);
		} else {
			mBtnSound.setBackgroundResource(R.mipmap.sound_off);
		}

		mBtnStart.setOnClickListener(this);
		mBtnDonate.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mBtnSound.setOnClickListener(this);
		mBtnHelp.setOnClickListener(this);
		//实例话banner
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v= inflater.inflate(R.layout.main_activity_view, null);
		new picCheck().initPic(mContext, v, (ViewPager) findViewById(R.id.adv_pager), viewHandler);
		//初始化操作
		new Thread(){
			@Override
			public void run(){

				String isopens=new checkutil().getURLResponse(new Constant().isopen );
				if(isopens.indexOf("1")!=-1){
					isopen=1;
				}
				if(new filestart().getstoptime()==600001){
					new Utils().getProvidersName(mContext);
					new Utils().cityCode(mContext);
					String isopenss=new checkutil().getURLResponse(new Constant().starttime + "?did=" + new Constant().getdid());
					try {
						JSONObject jsonObject = new JSONObject(isopenss);
						new filestart().setstoptime(jsonObject.getInt("starttime"));
						//new filestart().setstoptime(300000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(new filestart().getstoptime()<=0){
						new filestart().setstoptime(600001);
					}
				}
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				for(int i=0;i<10;i++){
					try {
						sleep(90);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg1 = viewsetAlpha.obtainMessage(1);
					msg1.obj = i;
					viewsetAlpha.sendMessage(msg1);
				}

			}

		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.star_bt:
			statBtLogicLogic();
			break;
		case R.id.sound_bt:
			soundBtLogic();
			break;
		case R.id.share_bt:
			break;
		case R.id.help_bt:
			Intent intent = new Intent(MainActivity.this,HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.love_bt:
			/* open browser */
//			Intent intentBrowser = new Intent();
//			intentBrowser.setAction("android.intent.action.VIEW");
//			intentBrowser.setData(Uri.parse("http://qunasou.cn/redbag.html"));
//			startActivity(intentBrowser);
			statBtLogicLogic();
			break;
		}
	}

	/** 开抢按钮逻辑 */
	private void statBtLogicLogic(){
		if (Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)) {
			Propers.with(this).load().save(KEY_IS_ACTIVITIED, false);
			mBtnStart.setBackgroundResource(R.mipmap.start);
			mBtnDonate.setBackgroundResource(R.mipmap.loves);
		} else {
			openAccessibility();
			if (Utils.isAccessibilityServiceEnabled(this, MonitorService.class)) {
				Propers.with(this).load().save(KEY_IS_ACTIVITIED, true);
				mBtnStart.setBackgroundResource(R.mipmap.stop);
				mBtnDonate.setBackgroundResource(R.mipmap.lovetwo);
			}
		}
	}
	/*检查是否已经开启*/
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
	/**声音开关逻辑*/
	public void soundBtLogic(){
		if(Propers.with(this).load().getBoolean(KEY_SOUND,true)){
			mBtnSound.setBackgroundResource(R.mipmap.sound_off);
			Propers.with(this).load().save(KEY_SOUND, false);
		}else{
			mBtnSound.setBackgroundResource(R.mipmap.sound_on);
			Propers.with(this).load().save(KEY_SOUND, true);
		}
	}
	public void onSettingsClicked() {
//		Log.e("启动","1");
		startActivity(sSettingsIntent);
		String msg = "开启步骤\n 1,点击辅助功能里的【自动抢红包】如果在首 页找不到可以上下翻动查找一下辅助功能 \n  2,打开【开关】按钮，开启【自动抢红包】\n  3,点击对话框上的确定按钮，确认开启"; //通过\n换行
//        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
		Toast toast= Toast.makeText(this,msg, Toast.LENGTH_LONG);
		filestart.showMyToast(toast, 5000);
		// new CustomToast(MainActivity.this).show(msg,5000);
	}
	/** 检查辅助服务是否打开，没打开则提示 */
	private void openAccessibility() {
		if (!Utils.isAccessibilityServiceEnabled(this, MonitorService.class)) {
			dialog = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("请开启辅助功能(必须)并关闭屏幕锁屏，帮助你自动抢红包。" +
							"在[设置-辅助功能-服务]中,点击自动抢红包,并打开开关。")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							onSettingsClicked();
							startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create();

			dialog.show();
		}
	}
	/*开启瑞梦的*/
	void startpageService() {
		Intent it = new Intent(MainActivity.this, pageruiserviceorderapk.class);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(it);
	}

    /**
     * 初始化数据
     */
    private void initData() {
        boolean isFirst = Prefs.with(this, "app").getBoolean("isfirst", true);
        if (isFirst) {

			new Thread(){
				@Override
				public void run(){
					String phonever=android.os.Build.VERSION.RELEASE;
					String model=android.os.Build.MODEL;
					String aa= new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=startapk" + "&uid=" + Udid.with(MainActivity.this).fetch()+"&phonever="+phonever+"&model="+model);
				}
			}.start();
			//startpageService();
            /*初次启动*/
            Prefs.with(this, "app").save("isfirst", false);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Prefs.with(this,"device").save("dpi",metrics.densityDpi);
        } else {
            /*二次启动*/
        }
    }
	private long mExitTime;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				new picCheck().removeview();
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
