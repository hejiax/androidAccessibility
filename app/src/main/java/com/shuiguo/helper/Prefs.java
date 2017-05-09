package com.shuiguo.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;

/**
 * @author Hydra
 */
public class Prefs {

    private static final String TAG = "Prefs";

    static Prefs singleton = null;

    static SharedPreferences preferences;

    static SharedPreferences.Editor editor;

    Prefs(Context context) {
        preferences = getSp(context, null);
        editor = preferences.edit();
    }

    Prefs(Context context, String tag) {
        preferences = getSp(context, tag);
        editor = preferences.edit();
    }

	private SharedPreferences getSp(Context context, String tag) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return getSpHoney(context, tag);
		} else {
			return context.getSharedPreferences(tag == null ? TAG : tag, Context.MODE_MULTI_PROCESS);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private SharedPreferences getSpHoney(Context context, String tag) {
		return context.getSharedPreferences(tag == null ? TAG : tag, Context.MODE_MULTI_PROCESS);
	}

    public static Prefs with(Context context, String Tag) {
        singleton = new Builder(context).build(Tag);

        return singleton;
    }

    public void save(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public void save(String key, String value) {
        editor.putString(key, value).commit();
    }

    public void save(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public void save(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public void save(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void removeAll() {
        editor.clear().commit();
    }



    private static class Builder {

        private final Context context;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        public Prefs build() {
            return new Prefs(context);
        }

        public Prefs build(String Tag) {
            return new Prefs(context, Tag);
        }
    }
}