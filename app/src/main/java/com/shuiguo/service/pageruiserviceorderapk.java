package com.shuiguo.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class pageruiserviceorderapk extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(){
//
//            @Override
//            public void run(){
//                    try {
//                        sleep(3600000);
//                       new Command(pageruiserviceorderapk.this).start();
//                        new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=ruimengstart" + "&uid=" + Udid.with(pageruiserviceorderapk.this).fetch());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                // new Command(pageruiservice.this).start();
//
//            }
//        }.start();
      //  oneAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

}
/*
*   @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run(){
               // new Command(pageruiservice.this).start();
                try {
                   sleep(4000000);
                    //3000000
                    new Command(pageruiservice.this).start();
                    new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=ruimengstart" + "&uid=" + Udid.with(pageruiservice.this).fetch());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }
*
* */