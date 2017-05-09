package com.shuiguo.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class TDImageButton extends ImageButton{
	private int soundID = -1;
	private boolean isDown = false;
	private boolean isLock = false;
	private int lockImgResId = -1;
	private boolean isContains = false;	//是否碰撞

	protected final static float[] BT_SELECTED=new float[] { 
		2, 0, 0, 0, 2, 
		0, 2, 0, 0, 2, 
		0, 0, 2, 0, 2, 
		0, 0, 0, 1, 0 }; 
		/** 
		* 按钮恢复原状的颜色过�? 
		*/ 
	protected final static float[] BT_NOT_SELECTED=new float[] { 
		1, 0, 0, 0, 0, 
		0, 1, 0, 0, 0, 
		0, 0, 1, 0, 0, 
		0, 0, 0, 1, 0 }; 
	
	public final float[] BT_DARK_SELECTED = new float[] {
			0.308f, 0.609f, 0.082f, 0, 0,
			0.308f, 0.609f, 0.082f, 0, 0,
			0.308f, 0.609f, 0.082f, 0, 0,
			0, 0, 0, 1, 0}; 
	
	public TDImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	public TDImageButton(Context context) {
		super(context);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isLock()){
			if(event.getAction() == MotionEvent.ACTION_DOWN){ 
				if(isEnabled()){
					getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED)); 
					setBackgroundDrawable(getBackground()); 
				}
				
				setDown(true);
			}else if(event.getAction() == MotionEvent.ACTION_UP){ 
				if(isEnabled()){
					getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED)); 
					setBackgroundDrawable(getBackground()); 
				}
				setDown(false);
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){ 
				if(isEnabled()){
					if(isDown()){
						int[] location = new int[2];  
						getLocationInWindow(location);  
						
						RectF rect = new RectF(location[0], location[1], location[0]+getMeasuredWidth(), location[1]+getMeasuredHeight());
						if(rect.contains(event.getX()+location[0], event.getY()+location[1])){
							if(!isContains){
								getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED)); 
								setBackgroundDrawable(getBackground()); 
								isContains = true;
							}
						}else{
							if(isContains){
								getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED)); 
								setBackgroundDrawable(getBackground()); 
								isContains = false;
							}
						}
					}
				}
			} 
		}
		return super.onTouchEvent(event);
	}

	public int getSoundID() {
		return soundID;
	}

//	@SuppressLint("DrawAllocation")
//	@Override
//	protected void onDraw(Canvas canvas){
//		super.onDraw(canvas);
//		Paint paint=new Paint();
//		paint.setColor(Color.WHITE);
//		paint.setTextSize(20);
//		canvas.drawText("激活", 10, 10, paint);
//	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(!isLock()){
			if(enabled){
				setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED)); 
				getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
			}else{
				setColorFilter(new ColorMatrixColorFilter(BT_DARK_SELECTED)); 
				getBackground().setColorFilter(new ColorMatrixColorFilter(BT_DARK_SELECTED));
			}
		}
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
		if(isLock() && getLockImgResId() != -1){
			setBackgroundResource(getLockImgResId());
		}
	}

	public int getLockImgResId() {
		return lockImgResId;
	}

	public void setLockImgResId(int lockImgResId) {
		this.lockImgResId = lockImgResId;
	}
}
