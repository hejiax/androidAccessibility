package com.shuiguo.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shuiguo.service.DownloadService;
import com.shuiguo.utils.Constant;
import com.shuiguo.utils.Udid;
import com.shuiguo.utils.Utils;
import com.shuiguo.utils.checkutil;
import com.shuiguo.utils.filestart;
import com.shuiguo.utils.loadfile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class BootReceiver extends BroadcastReceiver {
    private Context mcontext;
    String packageName;
   private static List<String> s=new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        //���հ�װ�㲥
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            packageName = intent.getDataString();
            new Thread() {
                @Override
                public void run() {
                    String aa = new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=install" + "&uid=" + Udid.with(mcontext).fetch() + "&packageName=" + packageName);
                }
            }.start();
            int loadstat = new filestart().getloadstat();
            DownloadService.packnameList.add(packageName);
        }
    }



}