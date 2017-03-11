package slzjandroid.slzjapplication.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import slzjandroid.slzjapplication.dto.ChargeList;

/**
 * Created by xuyifei on 16/4/14.
 */
public class NumbersUtils {


    /**
     * 判断是否是手机号规则
     *
     * @param no
     * @return
     */
    public static boolean isCellphoneNo(String no) {
        Pattern p = Pattern.compile("^1[1|3|4|5|7|8][0-9]{9}$");
        Matcher m = p.matcher(no);
        return m.matches();
    }


    /**
     * 根据输入的字符和长度判断是否合法
     *
     * @param num
     * @param length
     * @return
     */
    public static boolean isNumberLenght(String num, int length) {
        if (!TextUtils.isEmpty(num)) {
            if (num.length() == length) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否合法的邮箱
     *
     * @param email
     * @return
     */

    public static boolean isEmail(String email) {

        // String check = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})";
        String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }


    public static boolean isIdNumber(String idNumber) {
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher matcher = idNumPattern.matcher(idNumber);
        return matcher.matches();
    }


    /**
     * 判断输入的是字符和汉字
     *
     * @param strs
     * @return
     */
    public static boolean isNum(String strs) {
        Pattern compile = Pattern.compile("^([a-zA-Z]|[\\u4e00-\\u9fa5]|[\\(\\)])+$");
        Matcher matcher = compile.matcher(strs);
        return matcher.matches();
    }

    public static boolean isZipCode(String strs) {
        Pattern compile = Pattern.compile("^[1-9]\\d{5}$");
        Matcher matcher = compile.matcher(strs);
        return matcher.matches();
    }


    public static double getAllNumber(ArrayList<ChargeList> chargeList) {
        double sum = 0;
        for (ChargeList data : chargeList) {
            String chargeAmount = data.getChargeAmount();
            sum = sum + Double.parseDouble(chargeAmount);
        }
        BigDecimal b = new BigDecimal(sum);
        sum = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return sum;
    }
}
