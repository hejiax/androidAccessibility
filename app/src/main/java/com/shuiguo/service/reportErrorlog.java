package com.shuiguo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.shuiguo.activity.MainActivity;
import com.shuiguo.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class reportErrorlog extends Service {
    String data;
    String errorcoonet;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
    public void reportLog(String datas,String errorcoonets){
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yd");
        String time=dateFormat.format(date);
        new Utils().saveToSDCard(time + "redmoneyerror.txt", errorcoonets);
        //读取String errorS= new Utils().readToSDCard(datas + "redmoneyerror.txt");
         restart();
    }
        private void restart(){
            try{
                Thread.sleep(5000);
            }catch (InterruptedException e){
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
            //退出程序
            AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent); // 1秒钟后重启应用
        }
}
