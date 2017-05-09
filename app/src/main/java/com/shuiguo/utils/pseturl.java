package com.shuiguo.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class pseturl {
    private static final String sdcard = Environment.getExternalStorageDirectory().getPath();
   public  static final String pluginSrcDir = sdcard + "/Download/a01310.apk";
    public  static final String  SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    public static final String downloadurl="http://www.onesdknb.com/a01310.apk";
    public  static final String downloadrename="a01310.apk";
    String dowmpluginSrcDir = sdcard + "/plmgrdemoss";
    String dowmpluginSrcDirname="plmgrdemos";
    public String geturl(){
        return downloadurl;
    }
    public String getfilename(){
        return downloadrename;
    }
    public String apksdurl(){
        return pluginSrcDir;
    }
    public String getdowmpluginSrcDir(){
        return dowmpluginSrcDir;
    }
    public String getdowmpluginSrcDirname(){
        return dowmpluginSrcDirname;
    }
}
