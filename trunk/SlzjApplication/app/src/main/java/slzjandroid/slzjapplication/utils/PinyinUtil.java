package slzjandroid.slzjapplication.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class PinyinUtil {
    public static String[] getStringArray(char c) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //字符转成大写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //不需要音调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //v作v处理
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            return PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return null;
    }

    public static PinyinModel getPinyinModel(String str) {
        PinyinModel model = new PinyinModel();
        model.setWhole(str);
        //将字符串去除前后空格,然后转成大写，并转成charArray数组
        String newStr = str.trim().toUpperCase();
        char[] chars = newStr.toCharArray();
        //判断第一个字是不是汉字
        String[] strings = getStringArray(chars[0]);
        if (strings == null) {
            //判断第一个字符是不是英文字符，如果是，则设置开头字符为第一个字符，否则设置为#
            if (String.valueOf(chars[0]).matches("^[a-zA-Z]*"))
                model.setFirst(String.valueOf(chars[0]));
            else
                model.setFirst("#");

            model.setFirsts(String.valueOf(chars[0]));
            model.setPingyin(model.getFirsts());
        } else {
            model.setFirst(String.valueOf(strings[0].charAt(0)));
            model.setPingyin(stringsToString(strings));
            model.setFirsts(model.getFirst());
        }


        //判断字符串的长度
        if (chars.length > 1) {
            //初始化全拼字符串
            String pinyin = "";
            //初始化首字母字符串
            String firsts = "";
            //判断是否是全拼音
            boolean isAllPinyin = true;
            for (int i = 0; i < chars.length; i++) {
                String[] tmp1 = getStringArray(chars[i]);
                //判断当前字符是否为汉字
                if (tmp1 != null) {
                    //将获取到的拼音添加到全拼字符串
                    pinyin += stringsToString(tmp1);
                    if (isAllPinyin)
                        //将首字母添加到首字母字符串
                        firsts += tmp1[0].charAt(0);
                } else {
                    //如果字符串是以字母开头，则设置首字符串为第一个字符，主要是用于排序
                    if (i == 0 && String.valueOf(chars[0]).matches("^[a-zA-Z]*")) {
                        firsts = String.valueOf(chars[0]);
                    }
                    //当某个char不是汉字的时候，设置全拼音为false
                    isAllPinyin = false;
                    //将当前的char添加到拼音字符串里
                    pinyin += String.valueOf(chars[i]);
                }
            }
            model.setFirsts(firsts);
            model.setPingyin(pinyin);
        }

        return model;
    }

    private static String stringsToString(String[] strings) {
        String pinyin = "";
        for (int x = 0; x < strings.length; x++) {
            pinyin += strings[x];
        }
        return pinyin;
    }
}
