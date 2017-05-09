package com.shuiguo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.shuiguo.redenvelope.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/8/28 0028.
 */
public class picCheck {
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    URL myFileUrl = null;
    Bitmap bitmap = null;
    private static Context context;
    private static View view;
    private static ViewPager viewPage;
    private static Handler viewHandlers;
    int touchdown=2;
    int touchdup=2;
    int touchdmove=2;
    int downX;
    int downY;
    //      这里存放的是四张广告背景
    private static List<View> advPics = new ArrayList<View>();
    int flag=0;
    /*as*/
    public void initPic(Context context,View view,ViewPager viewPage,Handler viewHandlers){

        this.viewHandlers=viewHandlers;
        this.context=context;
        this.view=view;
        this.viewPage=viewPage;
        returnBitMap();
    }
    public void initViewPager() {
        // advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        advPager=viewPage;


//
//
//
//        ImageView img1 = new ImageView(context);
//        img1.setBackgroundResource(R.drawable.advertising_default_1);
////        webpic="http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg";
////        img1.setImageBitmap(returnBitMap("http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg"));
//        advPics.add(img1);
//
//        ImageView img2 = new ImageView(context);
//        img2.setBackgroundResource(R.drawable.advertising_default_2);
////        webpic="http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg";
////       img2.setImageBitmap(returnBitMap("http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg"));
//        advPics.add(img2);
//
//        ImageView img3 = new ImageView(context);
//        img3.setBackgroundResource(R.drawable.advertising_default_3);
////        webpic="http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg";
////         img3.setImageBitmap(returnBitMap("http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg"));
//        advPics.add(img3);
//
//        ImageView img4 = new ImageView(context);
////        webpic="http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg";
////        img4.setImageBitmap(returnBitMap("http://pic2.ooopic.com/12/90/09/28bOOOPICd4_1024.jpg"));
//        img4.setBackgroundResource(R.drawable.advertising_default);
//        advPics.add(img4);

//      对imageviews进行填充
        imageViews = new ImageView[advPics.size()];

//小图标
//        for (int i = 0; i < advPics.size(); i++) {
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
//            imageView.setPadding(5, 5, 5, 5);
//            imageViews[i] = imageView;
//            if (i == 0) {
//                imageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);
//            } else {
//                imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
//            }
//            group.addView(imageViews[i]);
//        }
        //advPager.removeAllViews();
//        if(advPager!=null){
//
//        }else {
//
//        }
        advPager.removeAllViews();

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                // do transformation here
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);
//                final float normalizedpositions = Math.abs(Math.abs(position) - 1);
//                page.setScaleX(normalizedpositions / 2 + 0.5f);
//                page.setScaleY(normalizedpositions / 2 + 0.5f);
            }
        });

        advPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.e("1", what.get() + "");
                        touchdown=1;
                        //https://appcdn.yiwen213.com/hm/kbplay03071.apk
                        downX=(int) event.getX();
                        downY=(int) event.getY();
                    case MotionEvent.ACTION_MOVE:
                        int thisX = (int) event.getX();
                        int thisY= (int) event.getY();
                        if((downX-thisX)==0 &&(downY-thisY)==0){
                            touchdmove =2;
                        }else {
                            touchdmove = 1;
                        }

                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        touchdup=1;
                        downloadapk( what.get());
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
    }

    public void  removeview(){
        ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
        ViewGroup groups=viewPage;
        groups.removeAllViews();
        group.removeAllViews();
    }
    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
//            for (int i = 0; i < imageViews.length; i++) {
//                imageViews[arg0]
//                        .setBackgroundResource(R.drawable.banner_dian_focus);
//                if (arg0 != i) {
//                    imageViews[i]
//                            .setBackgroundResource(R.drawable.banner_dian_blur);
//                }
//            }

        }

    }

    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }
    public void returnBitMap() {

        new Thread() {
            @Override
            public void run() {
                loadfile s = new loadfile();
                String result = s.getURLResponse(new Constant().bannerpush + "?uid=" + Udid.with(context).fetch() + "&qudao=" + new Constant().getdid() + "&isp=" + new filestart().getispcode() + "&city=" + new filestart().getispcode());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    int status = 21;
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        advPics.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            myFileUrl = null;
                            bitmap = null;

                            JSONObject jobj = (JSONObject) jsonArray.get(i);
                            String Appname = jobj.getString("Appname");
                            String Appid = jobj.getString("Appid");
                            String Appurl = jobj.getString("Appurl");
                            String Picurl = jobj.getString("picurl");
                            String Apppackagename = jobj.getString("Apppackagename");
                            myFileUrl = new URL(Picurl);
                            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            ImageView img1 = new ImageView(context);
                            img1.setImageBitmap(bitmap);
                            advPics.add(img1);
                            is.close();
                        }

                        Message msg1 = viewHandlers.obtainMessage(1);
                        msg1.obj = "This is task1";
                        viewHandlers.sendMessage(msg1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }
    private void downloadapk(int flags) {
        this.flag = flags;
        if (touchdmove != 1 && touchdown == 1 && touchdup == 1) {
            Toast.makeText(context, "开始下载如已安装不会重复下载", Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    loadfile s = new loadfile();
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    String result = s.getURLResponse(new Constant().bannerpush + "?uid=" + Udid.with(context).fetch() + "&qudao=" + new Constant().getdid() + "&isp=" + new filestart().getispcode() + "&city=" + new filestart().getispcode());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        int status = 21;
                        status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (flag < 1) {
                                flag = 0;
                            }
                            JSONObject jobj = (JSONObject) jsonArray.get(flag);
                            String Appname = jobj.getString("Appname");
                            String Appid = jobj.getString("Appid");
                            String Appurl = jobj.getString("Appurl");
                            String Picurl = jobj.getString("picurl");
                            String Apppackagename = jobj.getString("Apppackagename");
                            if(s.fileIsExists(sdpath + "Download/" + Appid + ".apk")) {

                            }else {
                                // Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
                                s.downLoadFileandInstall(Appurl, Appid, sdpath + Appid + ".apk", context);
                            }
                           new checkutil().getURLResponse(new Constant().BASEURL + "did=" + new Constant().getdid() + "&interface=logmmms" + "&result=startdownload_" + Appname);
                            if(!new Utils().haveinstall(Appname,context)){
                                s.install(context, sdpath + "Download/" + Appid + ".apk");
                                //Toast.makeText(context, "已经安装1", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(context, "已经安装", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //sleep(20000);
                        touchdown = 2;
                        touchdup = 2;
                        touchdmove = 2;
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            touchdown = 2;
            touchdup = 2;
            touchdmove = 2;
        }
    }

}
