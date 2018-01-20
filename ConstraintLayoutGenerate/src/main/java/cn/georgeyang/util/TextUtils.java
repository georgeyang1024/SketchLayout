package cn.georgeyang.util;

import java.util.List;

/**
 * Created by georgeyang1024 on 2018/1/20.
 */

public class TextUtils {
    public static boolean equals(String a,String b) {
        if (a==b) {
            return true;
        }
        if (a==null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public static boolean isEmpty (String a) {
        return a==null || "".equals(a);
    }

    public static String join(String str, List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            stringBuilder.append(string);
            if (i < strings.size()-1) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }
}
