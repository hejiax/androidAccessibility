package com.shuiguo.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class rednum extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(){
//            @Override
//            public void run(){
//                int renum=new filestart().getrednum();
//                int money=new filestart().getredmoney();
//                while(true){
//
//                    TextView textView = (TextView) View.findViewById(R.id.textView); textView.setText("");
//                }
//            }
//        };
        return super.onStartCommand(intent, flags, startId);
    }
}
