package slzjandroid.slzjapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferenceHelper {
    public static final String KEY_TOKEN = "TOKEN";


    /**
     * 初始化SharePreferences
     *
     * @param context
     * @param key
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context, String key) {
        return context.getApplicationContext().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    /**
     * 初始化 editor
     *
     * @param context
     * @param key
     * @return
     */
    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context, String key) {
        return getSharedPreferences(context, key).edit();
    }
}
