package com.shuiguo.utils;

import android.media.AudioManager;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class voice {
    private  AudioManager s;
    public  voice(AudioManager s){
        this.s=s;
    }
    public int getInitring()
    {
        //取得手机的初始音量，并初始化进度条
        int volume=s.getStreamVolume(AudioManager.STREAM_RING);  //取得初始音量
        //取得初始模式，并分别设置图标
        int mode=s.getRingerMode();  //取得初始模式
        return mode;
    }
    /*铃声*/
    public void ring() {
        s.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
    }
    /*生意加震动*/
    public  void ringAndVibrate() {
        s.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
    }
    /*只是震动*/
    public void vibrate() {
        s.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
    }
    /*无声模式*/
    public void noRingAndVibrate() {
        s.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
        s.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
    }
    public void  resetvo(int p){
        if(p==1){
            this.ring();
        }else if(p==2){
            this.ringAndVibrate();
        }else if(p==3){
            this.vibrate();
        }else{
            this.noRingAndVibrate();
        }
    }
}
