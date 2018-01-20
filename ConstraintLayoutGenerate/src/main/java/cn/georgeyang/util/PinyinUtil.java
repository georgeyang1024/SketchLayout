package cn.georgeyang.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georgeyang1024 on 2017/12/25.
 */

public class PinyinUtil {
    private static final HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper();
    public static String getPinyinName(String source) {
//        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(source);
//        StringBuffer stringBuffer = new StringBuffer();
//        for (HanziToPinyin.Token token : tokens) {
//            stringBuffer.append(token.target);
//        }
//        return getName(stringBuffer.toString());
        return getName(hanyuPinyinHelper.toHanyuPinyin(source));
    }

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;
        return flg;
    }

    public static String getName(String string) {
        return string.replace(" ","").replace("-","").replace(":","_").replace("\"","_").toLowerCase();
    }

}
