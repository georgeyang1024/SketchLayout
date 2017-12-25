package com.lvshou.sketchlayout;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 将Layer转成LayoutTag
 * Created by george.yang on 17/12/22.
 */

public class LayoutTagBuildUtil {
    private static String templateBound3 = "%s=\"@+id/%s\"\n" +
            "%s=@id/%s\n" +
            "%s=@id/%s\n" +
            "%s=\"%s\"\n";
    private static String templateBound2 = "%s=\"@+id/%s\"\n" +
            "%s=\"%s\"\n" +
            "%s=@id/%s\n" +
            "%s=\"%s\"\n";

    private static String templateTextView = "    <TextView\n" +
            "        android:id=\"@+id/%s\"\n" +
            "        android:layout_width=\"%s\"\n" +
            "        android:layout_height=\"%s\"\n" +
            "        android:text=\"%s\"\n" +
            "        %s" + //边框
            "        />";


    private static String buildBound(StArtboards artboards,StLayer layer,LayoutTag tag) {
        if (tag.outLayer!=null) {
            if (tag.leftLayer != null &&tag.leftLayer==tag.rightLayer) {
                Object[] params = new Object[]{
                        tag.source.rect.x > tag.leftLayer.rect.x ? "app:layout_constraintLeft_toRightOf" : "app:layout_constraintLeft_toLeftOf",
                        tag.leftLayer.toString(),
                        tag.source.rect.x + tag.source.rect.width > tag.rightLayer.rect.x + tag.rightLayer.rect.width ? "app:layout_constraintRight_toLeftOf" : "app:layout_constraintRight_toRightOf",
                        tag.rightLayer.toString(),
                        tag.topLayer==null?"layout_constraintBottom_toBottomOf":"app:layout_constraintTop_toTopOf",
                        (tag.topLayer==null?tag.bottomLayer:tag.topLayer).toString(),
                        (int)(tag.topLayer==null?tag.bottomDis:tag.topDis) +  "px"
                };
                return String.format(templateBound3,params);
            } else if (tag.topLayer !=null && tag.topLayer == tag.bottomLayer) {
                Object[] params = new Object[]{
                        tag.source.rect.y > tag.topLayer.rect.y ? "app:layout_constraintTop_toBottomOf" : "app:layout_constraintTop_toTopOf",
                        tag.topLayer.toString(),
                        tag.source.rect.y + tag.source.rect.height > tag.bottomLayer.rect.y + tag.bottomLayer.rect.height ? "app:??" : "app:layout_constraintBottom_toBottomOf",
                        tag.bottomLayer.toString(),
                        tag.leftLayer==null?"layout_constraintRight_toRightOf":"app:layout_constraintLeft_toLeftOf",
                        (tag.leftLayer==null?tag.rightLayer:tag.leftLayer).toString(),
                        (int)(tag.leftLayer==null?tag.rightDis:tag.rightDis) +  "px"
                };
                return String.format(templateBound3,params);
            } else {
//                throw new RuntimeException("Center element inconsistency!");
            }
        } else {
            List list = new ArrayList<String>();
            if (tag.leftLayer!=null) {
                list.add("app:layout_constraintLeft_toLeftOf");
                list.add(tag.leftLayer.toString());
                list.add("android:layout_marginLeft");
                list.add((int)tag.leftDis+"px");
            }
            if (tag.rightLayer!=null) {
                list.add("app:layout_constraintRight_toRightOf");
                list.add(tag.rightLayer.toString());
                list.add("android:layout_marginRight");
                list.add((int)tag.rightDis+"px");
            }
            if (tag.topLayer!=null) {
                list.add("app:layout_constraintTop_toTopOf");
                list.add(tag.topLayer.toString());
                list.add("android:layout_marginTop");
                list.add((int)tag.topDis+"px");
            }
            if (tag.bottomLayer!=null) {
                list.add("app:layout_constraintBottom_toBottomOf");
                list.add(tag.bottomLayer.toString());
                list.add("android:layout_marginBottom");
                list.add((int)tag.bottomDis+"px");
            }
            if (list.size()==0) {
                list.add("app:layout_constraintLeft_toLeftOf=\"parent\"");
                list.add("app:layout_constraintTop_toTopOf\"parent\"");
                return TextUtils.join("\n",list.toArray(new String[list.size()]));
            } else {
                return String.format(templateBound2,list.toArray(new String[list.size()]));
            }

        }
        return "error";
    }

    private static String buildTextView(StArtboards artboards,StLayer layer,LayoutTag tag) {
        String id =  PinyinUtil.getPinyinName(layer.name);
        Object[] params = new Object[]{
                id,
                layer.rect.width + "px",
                layer.rect.height + "px",
                TextUtils.isEmpty(layer.content)?id:layer.content,
                buildBound(artboards,layer,tag)
        };
        return String.format(templateTextView,params);
    }

    /**
     * 定位好LayoutTag位置后，调用此方法生成背景，写入文字之类的操作
     */
    public static void generatedLayout(StArtboards artboards,StLayer layer,LayoutTag tag) {
        Log.d("test","====================generatedLayout====================");
        Log.d("test","layer:" + layer);
        Log.d("test","tag:" + tag);
        Log.d("test",buildTextView(artboards,layer,tag));
    }
}
