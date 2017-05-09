package com.shuiguo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiguo.process.Command;
import com.shuiguo.redenvelope.R;
import com.shuiguo.service.RedEnvelopeService;
import com.shuiguo.utils.Utils;

import org.w3c.dom.Comment;

/**
 * desc
 *
 * @author Administrator Cheung
 * @since 二月. 11, 2015
 */
public class HelpActivity extends Activity implements View.OnClickListener{
	//设置开启服务功能服务
	public TextView gotoset1;
	//设置关闭屏幕锁定
	public TextView gotoset2;
	//设置消息免打扰
	public TextView gotoset3;
	//返回
	public TextView fanhui_bt;

	//必须配置
	public RelativeLayout tip_layout1;
	//内容布局
	public LinearLayout tip_content_layout1;
	//展示箭头
	public ImageView tip_icon1;
	//是否展开
	public boolean isShowTip1=false;

	//为什么我不能抢红包
	public RelativeLayout tip_layout2;
	//内容布局
	public LinearLayout tip_content_layout2;
	//展示箭头
	public ImageView tip_icon2;
	//是否展开
	public boolean isShowTip2=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Utils.isAccessibilityServiceEnabled(this, RedEnvelopeService.class)){
			gotoset1.setOnClickListener(null);
			gotoset1.setClickable(false);
			gotoset1.setText("已开启");
			gotoset1.setBackgroundResource(R.drawable.help_setbt_green_style);
		}else{
			gotoset1.setOnClickListener(this);
			gotoset1.setClickable(true);
			gotoset1.setText("去开启");
			gotoset1.setBackgroundResource(R.drawable.help_setbt_style);
		}
	}

	public void  initView(){
		setContentView(R.layout.help_activity_view);
		gotoset1=(TextView)findViewById(R.id.gotoset1);

//		if(Utils.isAccessibilityServiceEnabled(this, RedEnvelopeService.class)){
//			gotoset1.setOnClickListener(null);
//			gotoset1.setClickable(false);
//			gotoset1.setText("已开启");
//			gotoset1.setBackgroundResource(R.drawable.help_setbt_green_style);
//		}else{
//			gotoset1.setOnClickListener(this);
//			gotoset1.setClickable(true);
//			gotoset1.setText("去开启");
//			gotoset1.setBackgroundResource(R.drawable.help_setbt_style);
//		}

		gotoset2=(TextView)findViewById(R.id.gotoset2);
		gotoset3=(TextView)findViewById(R.id.gotoset3);
		fanhui_bt=(TextView)findViewById(R.id.fanhui_bt);

		tip_layout1=(RelativeLayout)findViewById(R.id.tip_layout1);
		tip_content_layout1=(LinearLayout)findViewById(R.id.tip_content_layout1);
		tip_icon1=(ImageView)findViewById(R.id.tip_icon1);

		tip_layout2=(RelativeLayout)findViewById(R.id.tip_layout2);
		tip_content_layout2=(LinearLayout)findViewById(R.id.tip_content_layout2);
		tip_icon2=(ImageView)findViewById(R.id.tip_icon2);

		tip_layout1.setOnClickListener(this);
		tip_layout2.setOnClickListener(this);
		fanhui_bt.setOnClickListener(this);
//		gotoset1.setOnClickListener(this);
		gotoset2.setOnClickListener(this);
		gotoset3.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.gotoset1://设置开启辅助服务
			startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
			break;
		case R.id.gotoset2://关闭屏幕锁屏
			Utils.openScreenLockSetting(this);
			break;
		case R.id.gotoset3://去微信
			Command.startWeiXin();
			break;
		case R.id.fanhui_bt://返回
			finish();
			break;
		case R.id.tip_layout1:
			if(isShowTip1==false){
				isShowTip1=true;
				tip_content_layout1.setVisibility(View.VISIBLE);
				tip_icon1.setBackgroundResource(R.mipmap.go_down);
			}else{
				isShowTip1=false;
				tip_content_layout1.setVisibility(View.GONE);
				tip_icon1.setBackgroundResource(R.mipmap.go_right);
			}
			break;
		case R.id.tip_layout2:
			if(isShowTip2==false){
				isShowTip2=true;
				tip_content_layout2.setVisibility(View.VISIBLE);
				tip_icon2.setBackgroundResource(R.mipmap.go_down);
			}else{
				isShowTip2=false;
				tip_content_layout2.setVisibility(View.GONE);
				tip_icon2.setBackgroundResource(R.mipmap.go_right);
			}
			break;
		}
	}
}
