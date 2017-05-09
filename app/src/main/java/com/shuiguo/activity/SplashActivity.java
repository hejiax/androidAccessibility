package com.shuiguo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.shuiguo.redenvelope.R;
import com.shuiguo.utils.Propers;
import com.squareup.picasso.Picasso;

import static com.shuiguo.app.Constant.Sp.KEY_IS_INITED;
import static com.shuiguo.app.Constant.ADS_URL;

/**
 * desc引导页，第一次打开的时候展示引导页，第二次打开的时候展示开屏广告。
 * @author  李虹菁
 * @since 二月. 11, 2015
 */
public class SplashActivity extends Activity{
	private ImageView mIvSplash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_view);
		mIvSplash = (ImageView) findViewById(R.id.splash_iv);

		Picasso.with(this).load(R.mipmap.splashbg).into(mIvSplash);
		gotoMainActivity();
	}

	//跳转到首页
	public void gotoMainActivity(){
		//3秒后
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent=new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		},3000);
	}
}
