package com.shuiguo.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.shuiguo.service.DownloadService;
import com.shuiguo.utils.filestart;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, AlarmReceiver.class);
        setAlarmTime(context, new filestart().getstoptime(),mIntent);
        Intent intent1 = new Intent(context,DownloadService.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent1);
    }
    public static void setAlarmTime(Context context, long timeInMillis, Intent intent) {
        long triggerAtTime = SystemClock.elapsedRealtime() + timeInMillis;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id",5),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = (int) intent.getLongExtra("intervalMillis", 3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//             Log.e("aaa", "aaa");
             am.setExact(AlarmManager.ELAPSED_REALTIME, triggerAtTime, sender);
           // am.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, timeInMillis , sender);
        }
    }
}