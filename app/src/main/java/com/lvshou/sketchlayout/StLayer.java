package com.lvshou.sketchlayout;


import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */

public class StLayer {
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
    public StColor color;

    @Override
    public String toString() {
        return "@+id/" + getViewId();
    }

    private String viewId;
    public String getViewId() {
        if (TextUtils.equals(name,"parent")) return name;
        if (TextUtils.isEmpty(viewId)) {
            if (PinyinUtil.isChinese(name)) {
                viewId = PinyinUtil.getPinyinName(name);
            } else {
                viewId = PinyinUtil.getName(name);
            }
        }
        if (TextUtils.isEmpty(viewId)) {
            viewId = PinyinUtil.getName("id_" + objectID.hashCode());
        }
        return viewId;
    }

}
