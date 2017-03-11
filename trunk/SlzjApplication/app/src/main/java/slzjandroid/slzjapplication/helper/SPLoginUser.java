package slzjandroid.slzjapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;

import slzjandroid.slzjapplication.dto.PicDomin;

/**
 * Created by xuyifei on 16/4/18.
 */
public class SPLoginUser {

    /**
     * 保存第一次登陆的状态
     *
     * @param context
     * @param v
     */
    public static void setTag(Context context, String v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "tag");
        editor.clear();
        editor.putString("flag", v);
        editor.commit();
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static String getTag(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "tag");
        return spf.getString("flag", "0");
    }


    public static void setOrderId(Context context, String v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "orderId");
        editor.clear();
        editor.putString("orderIdV", v);
        editor.commit();
    }

    public static String getOrderId(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "orderId");
        return spf.getString("orderIdV", "0");
    }

    public static void clearOrderId(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "orderId");
        editor.clear();
        editor.commit();
    }

    public static void setReasonName(Context context, String v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "reasonname");
        editor.clear();
        editor.putString("reasonnameV", v);
        editor.commit();
    }

    public static String getReasonName(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "reasonname");
        return spf.getString("reasonnameV", "0");
    }

    public static void clearReasonName(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "reasonname");
        editor.clear();
        editor.commit();
    }

    /**
     * 保持token
     *
     * @param context
     * @param token
     */
    public static void saveTokenTo(Context context, String token) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, SharePreferenceHelper.KEY_TOKEN);
        editor.clear();
        editor.putString("token", token);
        editor.commit();
    }


    public static void clearToken(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, SharePreferenceHelper.KEY_TOKEN);
        editor.clear();
        editor.commit();
    }

    /**
     * 获取toke
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, SharePreferenceHelper.KEY_TOKEN);
        return spf.getString("token", "0");
    }


    public static void saveLable(Context context, String v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "lable");
        editor.clear();
        editor.putString("sign", v);
        editor.commit();
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static String getfinshTag(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "finshTag");
        return spf.getString("tag", "0");
    }


    public static void setfinshTag(Context context, String v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "finshTag");
        editor.clear();
        editor.putString("tag", v);
        editor.commit();
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static void clearfinshTag(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "finshTag");
        editor.clear();
        editor.commit();
    }


    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static String getLable(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "lable");
        return spf.getString("sign", "0");
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static void clearLable(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "lable");
        editor.remove("sign");
        editor.commit();
    }


    public static void saveRenZheng(Context context, boolean v) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "renzhengTag");
        editor.clear();
        editor.putBoolean("renzheng", v);
        editor.commit();
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static boolean getRenZheng(Context context) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, "renzhengTag");
        return spf.getBoolean("renzheng", false);
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static void clearRenzheng(Context context) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, "renzhengTag");
        editor.remove("renzheng");
        editor.commit();
    }


    public static void savePicUri(Context context, PicDomin picDomin, String key) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, key);
        editor.clear();
        editor.putString("uri", picDomin.getPicUri());
        editor.putString("tag", picDomin.getTag());
        editor.commit();
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static PicDomin getPicUri(Context context, String key) {
        SharedPreferences spf = SharePreferenceHelper.getSharedPreferences(context, key);
        PicDomin picDomin = new PicDomin();
        picDomin.setPicUri(spf.getString("uri", ""));
        picDomin.setTag(spf.getString("tag", ""));
        return picDomin;
    }

    /**
     * 过去第一次登陆的状态
     *
     * @param context
     * @return
     */
    public static void clearPicUri(Context context, String key) {
        SharedPreferences.Editor editor = SharePreferenceHelper.getSharedPreferencesEditor(context, key);
        editor.clear();
        editor.commit();
    }

}

