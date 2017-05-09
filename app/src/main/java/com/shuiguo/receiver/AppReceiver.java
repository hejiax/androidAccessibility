package com.shuiguo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shuiguo.helper.Prefs;
import com.shuiguo.utils.AppUtils;

import java.util.Map;

/**
 * APP安装完成
 */
public class AppReceiver extends BroadcastReceiver {

    private static final int PACKAGE_NAME_START_INDEX = 8;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String data = intent.getDataString();
            if (data == null || data.length() <= PACKAGE_NAME_START_INDEX) {
                return;
            }

            String packageName = data.substring(PACKAGE_NAME_START_INDEX);


            /**
             * 桌面图标安装完成上报
             */
            Map<String,?> map = Prefs.with(context, "icon_install").getAll();
            for (Map.Entry<String,?> enty : map.entrySet()){
                String papa = (String)enty.getValue();
                if (papa.equals(packageName)){
                    String appName = AppUtils.getProgramNameByPackageName(context, packageName);
                    Prefs.with(context,"installed").save(appName,packageName);
                }
            }

        }
    }

}