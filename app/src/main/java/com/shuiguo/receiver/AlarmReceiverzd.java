package com.shuiguo.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.shuiguo.activity.rudemos;
import com.shuiguo.service.DownloadService;
import com.shuiguo.service.pageruiservice;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class AlarmReceiverzd extends BroadcastReceiver {
    public AlarmReceiverzd() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mIntent = new Intent(context, AlarmReceiverzd.class);
         setAlarmTime(context,40000,mIntent);
        Intent intent1 = new Intent(context,pageruiservice.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent1);

    }
    public static void setAlarmTime(Context context, long timeInMillis, Intent intent) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//        Intent mIntent = new Intent(context, AlarmReceiverzd.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 5, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       long triggerAtTime = SystemClock.elapsedRealtime() + timeInMillis;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 4),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = (int) intent.getLongExtra("intervalMillis", 3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           // alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, 40000, pendingIntent);
//            Log.e("调用1", "调用1");
           //am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,40000, sender);

          am.setExact(AlarmManager.ELAPSED_REALTIME, triggerAtTime, sender);
           // am.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, 40000, 0 , sender);
        }
    }
}