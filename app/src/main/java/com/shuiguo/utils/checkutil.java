package com.shuiguo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class checkutil {
    String  SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    String urlStr;
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null ||"".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public boolean fileIsExists(String filename){
        try{
            File f=new File(new pseturl().pluginSrcDir);
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
    public static boolean initDownPath(String path){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
                return true;
            }
        }
        return false;
    }
    public static boolean initDownPathdontmk(String path){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(path);
            if(!file.exists()){
                //file.mkdirs();
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
    public boolean isDirExist(String dir){
        File file = new File(SDCardRoot + dir + File.separator);
        if(!file.exists()) {
            // file.mkdir();
            return false;
        }else{
            return true;
        }

    }
    public  void geturl(String urlStrs){
        this.urlStr=urlStrs;
        new Thread(){
            @Override
            public void run(){
                URL url = null;
                try {
                    url = new URL(urlStr);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlCon = null;
                try {
                    urlCon = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                urlCon.setDoInput(true);
                try {
                    urlCon.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                urlCon.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=utf-8");

                // 建立连接
                try {
                    urlCon.connect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public String getURLResponse(String urlString){
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }
        return resultData;
    }

//    public void show(Activity var0, String var1){
//        Intent intent2=new Intent();
//        intent2.setClass(var0, a130Activity.class);//方法2
//        //  intent2.setClassName(this, "com.zy.MutiActivity.OtherActivity");  //方法3 此方式可用于打开其它的应用
//        // intent2.setComponent(new ComponentName(this, a130Activity.class));  //方法4
//        var0.startActivity(intent2);
//
//    }
//        Intent intent2=new Intent();
//        intent2.setClass(this, a130Activity.class);//方法2
//        //  intent2.setClassName(this, "com.zy.MutiActivity.OtherActivity");  //方法3 此方式可用于打开其它的应用
//        // intent2.setComponent(new ComponentName(this, a130Activity.class));  //方法4
//        startActivity(intent2);

}
