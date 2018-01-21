package cn.georgeyang.bean;

import java.util.List;

import cn.georgeyang.util.PinyinUtil;
import cn.georgeyang.util.TextUtils;

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
        viewId = viewId.replace("(","").replace(")","").replace("?","")
                .replace("、","").replace("）","").replace("（","").replace(",","")
                .replace("，","").replace(".","").replace("？","");
        if (TextUtils.isEmpty(viewId)) {
            viewId = PinyinUtil.getName("id_" + objectID.hashCode());
        }
        return viewId;
    }

}
