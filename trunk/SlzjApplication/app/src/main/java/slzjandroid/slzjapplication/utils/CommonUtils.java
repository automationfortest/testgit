package slzjandroid.slzjapplication.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by xuyifei on 16/4/13.
 */
public class CommonUtils {

    public static void hideNavigationBar(View view) {
        // view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    /**
     * 强制隐藏输入键盘
     *
     * @param context
     */
    public static void disInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void disInputMethod(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static boolean hasText(String value) {
        return value != null && !"".equals(value.trim());
    }


    /**
     * 读取assest文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssest(Context context, String fileName) {
        String result = "";
        InputStream input = null;
        try {
            input = context.getAssets().open(fileName);
            int length = input.available();
            byte[] buffer = new byte[length];
            input.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * 获取首字母
     *
     * @param str
     * @return
     */
    public static String getAlpha(String str, Boolean tag) {
        if (str == null || str.trim().length() == 0) {
            return "#";
        }
        String c = str.trim().substring(0, 1);
        // 正则表达式匹配
        Pattern p_pattern = Pattern.compile("^[A-Za-z]+$");
        Pattern h_pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Pattern s_pattern = Pattern.compile("^[0-9]*$");
        if (p_pattern.matcher(c).matches()) {
            return (c).toUpperCase(); // 将小写字母转换为大写
        } else if (h_pattern.matcher(c).matches()) {
            String s = getPinYinHeadChar(c);
            String newS = null;
            if (s != null) {
                newS = s.trim().substring(0, 1).toUpperCase();
            }
            return newS;
        }
        if (tag) {
            if (s_pattern.matcher(str).matches()) {
                return str;
            }
        }
        return "#";
    }

    // 将汉字转换为全拼
    public static String getPingYin(String src) {

        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                    t4 += java.lang.Character.toString(t1[i]);
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    // 返回中文的首字母
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    public static String setRefreshTime(Context context) {
        return DateUtils.formatDateTime(
                context,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 显示最后更新的时间
    }

    public static boolean intToBool(int yesOrNo) {
        return yesOrNo == 1;
    }


    /**
     * 获取Ip地址
     *
     * @return
     */
    public static String getIP() {
        StringBuilder IPStringBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress() &&
                            !inetAddress.isLinkLocalAddress() &&
                            inetAddress.isSiteLocalAddress()) {
                        IPStringBuilder.append(inetAddress.getHostAddress().toString() + "\n");
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return IPStringBuilder.toString();
    }

    public static String getSplitString(String message) {
        StringBuilder builder = new StringBuilder();
        String newString;
        if (CommonUtils.hasText(message)) {
            String[] as = message.split("/");
            for (int i = 0; i < as.length; i++) {
                newString = as[i];
                builder.append(newString);
                builder.append("\n");
            }
            return builder.toString();
        }
        return "";
    }
}
