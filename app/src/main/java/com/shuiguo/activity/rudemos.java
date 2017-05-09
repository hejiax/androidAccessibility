package com.shuiguo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.shuiguo.redenvelope.R;


public class rudemos extends Activity {
    public  static rudemos rudemosins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rudemosins=rudemos.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rudemo);
        //startDownloadService();
    }

}
