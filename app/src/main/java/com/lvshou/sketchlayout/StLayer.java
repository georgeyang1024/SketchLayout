package com.lvshou.sketchlayout;


import java.util.List;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */

public class StLayer {
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
        return PinyinUtil.getPinyinName(name);
    }
}
