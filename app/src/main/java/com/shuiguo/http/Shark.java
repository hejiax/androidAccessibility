package com.shuiguo.http;

/**
 * 网络请求类, 可以使用这个类开心地向服务器请求数据。
 */
public class Shark {
	private static final String TAG = Shark.class.getSimpleName();
	private static final int METHOD_EAT = 1;
	private static final int METHOD_SPIT = 2;
	public static boolean sDebug = true;
	
	private static Shark sInstance;
	/**
	 * 网络请求回调接口。
	 */
	public interface OnPreyListener {
		void onPreyOk(String body);
		void OnPreyFail();
	}
	/**
	 * 开启或关闭调试。
	 * 
	 * @param debug true of false
	 */
	public static void setDebug(boolean debug) {
		sDebug = debug;
	}
	
	/**
	 * 获取网络请求类单实例。
	 * 
	 * @return 请求单实例
	 */
	public static Shark ready() {
		synchronized (Shark.class) {
			if (sInstance == null) {
				sInstance = new Shark();
			}
		}
		return sInstance;
	}


}
