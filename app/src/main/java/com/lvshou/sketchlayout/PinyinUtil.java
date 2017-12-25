package com.lvshou.sketchlayout;

import java.util.ArrayList;

/**
 * Created by georgeyang1024 on 2017/12/25.
 */

public class PinyinUtil {
    public static String getPinyinName(String source) {
        ArrayList<HanziToPinyin.Token> tokens =  HanziToPinyin.getInstance().get(source);
        StringBuffer stringBuffer = new StringBuffer();
        for (HanziToPinyin.Token token:tokens) {
            stringBuffer.append(token.target);
        }
        return stringBuffer.toString().replace(" ","").replace("-","_").replace(":","_").replace("\"","_");
    }
}
