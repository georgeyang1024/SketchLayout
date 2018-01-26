package cn.georgeyang.bean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.georgeyang.util.PinyinUtil;
import cn.georgeyang.util.TextUtils;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */

public class StLayer {
    private static final Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");

    public String objectID;
    public String type;
    public String name;
    public StRect rect;
    public String content;
    public int rotation;
    public int radius;
    public List<String> css;
    public String fontSize;
    public String fontFace;
    public String textAlign;
    public float letterSpacing;
    public List<StBorders> borders;
    public StColor color;

    @Override
    public String toString() {
        return TextUtils.equals(objectID,"parent") ? "parent" : "@+id/" + getViewId();
    }


    public String getViewId() {
        String viewId;
        if (PinyinUtil.isChinese(name)) {
            viewId =  PinyinUtil.getPinyinName(name);
        } else {
            viewId =  PinyinUtil.getName(name);
        }
        try {
            Integer.valueOf(viewId.substring(0,1));
            viewId = "id" + viewId;
        } catch (Exception e) {

        }
        if (TextUtils.isEmpty(viewId)) {
            viewId = PinyinUtil.getName("id_" + objectID.hashCode());
        }

        Matcher matcher = pattern.matcher(viewId);
        if (matcher.find()) {
            viewId = matcher.group();
        }
        return viewId;
    }

}
