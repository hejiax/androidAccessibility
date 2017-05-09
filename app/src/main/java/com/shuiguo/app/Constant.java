package com.shuiguo.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhengdelong on 15/2/11.
 */
public class Constant {

    public static final String BASE_URL = "http://sgstatis.haoghost.com/index.php";
    public static final String ICON_URL = "http://sgstatis.haoghost.com/app.php";
	public static final String ADS_URL = "http://sgstatis.haoghost.com/images/splash.jpg";
	/** 更新app */
	public static final String UPDATE_URL = "http://sgstatis.haoghost.com/index.php";
    public static final String DOWN_DIR = Environment.getExternalStoragePublicDirectory(
		    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static final String DOWN_PATH = DOWN_DIR + File.separator ;

	public class Sp {
		public static final String KEY_SOUND = "sound";
		public static final String KEY_IS_INITED = "is_inited";
		public static final String KEY_IS_ACTIVITIED = "is_activited";
		public static final String KEY_UPDATE_TIME = "update_time";
	}

	/**Intent传值key*/
	public class Extras {
		public static final String DOWN_URL = "com.shuiguo.DOWN_URL";
	}
}
