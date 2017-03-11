package slzjandroid.slzjapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hdb on 2016/3/30.
 */
public class DateUtil {

    public static final String YMDHMS_STYLE_ONE = "yyyyMMddHHmmss";

    public static final String YMDHMS_STYLE_TOW = "yyyy-MM-dd HH:mm:ss";

    public static final String YEARS_MOTHES_DAY = "yyyy-MM-dd";

    public static final String MHS_STYLE_THREE = "HH:mm:ss";


    /**
     * 获取系统当前时间
     *
     * @param formatterstr 传递的要格式化的时间类型
     * @return
     */
    public static String getCurrentTime(String formatterstr) {
        return new SimpleDateFormat(formatterstr).format(new Date(System.currentTimeMillis()));
    }

    /**
     *
     */
    public static String getYearMothesDay(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMDHMS_STYLE_TOW);
            Date mdate = simpleDateFormat.parse(date);
            simpleDateFormat = new SimpleDateFormat(YEARS_MOTHES_DAY);
            String formattedTime = simpleDateFormat.format(mdate);
            return formattedTime;
        } catch (Exception e) {
        }
        return null;
    }
}
