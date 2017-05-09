package com.shuiguo.utils;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/3 0003.
 */
public class filestart {
    private static  int rednum=10000;
    private static  int redmoney=10000;
    private static  int loadstat=1;
    private static  int loadover=1;
    private static  int ifredemos=2;
    private static  int isDownstart=2;
    private static  int stoptime=600001;
    private static  int citycode=1;
    private static  int ispcode=1;
    private static  int isload=1;
    public static List<String> apkpagenameList=new ArrayList<>();
    public int getisload(){return isload;}
    public void setisload(int s){
        isload=s;
    }
    public int getispcode(){return ispcode;}
    public void setispcode(int s){
        ispcode=s;
    }
    public int getcitycode(){return citycode;}
    public void setcitycode(int s){
        citycode=s;
    }
    public int getisDownstart(){
        return isDownstart;
    }
    public void setisDownstart(int s){
        filestart.isDownstart=s;
    }
    public int getstoptime(){
        return stoptime;
    }
    public void setstoptime(int s){
        filestart.stoptime=s;
    }
    public int getifredemos(){
        return ifredemos;
    }
    public void setifredemos(int s){
        filestart.ifredemos=s;
    }
    public int getloadstat(){
        return loadstat;
    }
    public void setloadstat(int s){
        filestart.loadstat=s;
    }
    public void setloadover(int s){
        filestart.loadover=s;
    }
    public int getloadover(){
        return loadover;
    }
    public int getrednum(){
        return rednum;
    }
    public void setrednum(int s){
        filestart.rednum=s;
    }
    public int getredmoney(){
        return redmoney;
    }
    public void setredmoney(int s){
        filestart.redmoney=s;
    }
    public static  void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}
