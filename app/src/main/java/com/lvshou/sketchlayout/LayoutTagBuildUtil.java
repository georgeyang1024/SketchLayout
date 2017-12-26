package com.lvshou.sketchlayout;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 将Layer转成LayoutTag
 * Created by george.yang on 17/12/22.
 */

public class LayoutTagBuildUtil {
    static Random random = new Random();

    private static String templateTextView = "    <TextView\n" +
            "        android:id=\"@+id/%s\"\n" +
            "        android:layout_width=\"%s\"\n" +
            "        android:layout_height=\"%s\"\n" +
            "        android:text=\"%s\"\n";

    private static String buildViewBackground(StArtboards artboards,StLayer layer,LayoutTag tag) {
        int color = Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
        return String.format( "android:background=\"#%s\"\n",Integer.toHexString(color));
    }

    private static   StLayer parentLayer = new StLayer();
    private static String buildViewBound(StArtboards artboards,StLayer layer,LayoutTag tag) {
        List list = new ArrayList<String>();

        parentLayer.name = "parent";
        parentLayer.objectID = "id";
        parentLayer.rect = new StRect();
        parentLayer.rect.x = 0;
        parentLayer.rect.y = 0;
        parentLayer.rect.width = artboards.width;
        parentLayer.rect.height = artboards.height;

        if (tag.outLayer==null) {
            tag.outLayer = parentLayer;
        }

        if (tag.leftLayer == tag.rightLayer && Math.abs(tag.leftDis-tag.rightDis)<=2) {
            //左右居中
            list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.leftLayer}));
            list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.leftLayer}));
        } else {
                double tempParentLeftDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.LEFT);
                double tempParentRightDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.RIGHT);
                if (tempParentLeftDis < tag.leftDis && tempParentLeftDis < Math.min(tag.rightDis,tempParentRightDis)) {
                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{"parent"}));
                    list.add(String.format("android:layout_marginLeft=\"%s\"",new Object[]{(int)tempParentLeftDis + "px"}));
                } else if (tag.leftDis <= tempParentLeftDis && tag.leftDis < Math.min(tag.rightDis,tempParentRightDis)) {
                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.leftLayer}));
                    list.add(String.format("android:layout_marginLeft=\"%s\"",new Object[]{(int)tag.leftDis + "px"}));
                } else if (tempParentRightDis < tag.rightDis) {
                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{"parent"}));
                    list.add(String.format("android:layout_marginRight=\"%s\"",new Object[]{(int)tempParentRightDis + "px"}));
                } else {
                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.rightLayer}));
                    list.add(String.format("android:layout_marginRight=\"%s\"",new Object[]{(int)tag.rightDis + "px"}));
                }
        }


        if (tag.topLayer == tag.bottomLayer && Math.abs(tag.topDis-tag.bottomDis)<=2) {
            //上下居中
            list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.topLayer}));
            list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.bottomLayer}));
        } else {
            double tempParentTopDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.TOP);
            double tempParentBottomDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.BOTTOM);
            if (tempParentTopDis < tag.topDis && tempParentTopDis < Math.min(tag.bottomDis,tempParentBottomDis)) {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{"parent"}));
                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tempParentTopDis + "px"}));
            } else if (tag.topDis <= tempParentTopDis && tag.topDis < Math.min(tag.bottomDis,tempParentBottomDis)) {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.topLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tempParentTopDis + "px"}));
            } else if (tempParentBottomDis<tag.bottomDis) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.outLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)(tempParentBottomDis) + "px"}));
            } else {
                list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"",new Object[]{tag.bottomLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"\n",new Object[]{(int)(tag.bottomDis-layer.rect.height) + "px"}));
            }
        }
        return TextUtils.join("\n",list.toArray(new String[list.size()]));
    }

    private static String buildTextViewHeader(StArtboards artboards,StLayer layer,LayoutTag tag) {
        String id =  PinyinUtil.getPinyinName(layer.name);
        Object[] params = new Object[]{
                layer.getViewId(),
                (int)(layer.rect.width) + "px",
                (int)(layer.rect.height) + "px",
                TextUtils.isEmpty(layer.content)?id:layer.content
        };
        return String.format(templateTextView,params);
    }

    private static String buildView (StArtboards artboards,StLayer layer,LayoutTag tag) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(buildTextViewHeader(artboards,layer,tag));
        stringBuffer.append(buildViewBackground(artboards,layer,tag));
        stringBuffer.append(buildViewBound(artboards,layer,tag));
        stringBuffer.append("\n/>");
        return stringBuffer.toString();
    }


    /**
     * 定位好LayoutTag位置后，调用此方法生成背景，写入文字之类的操作
     */
    public static String generatedLayout(StArtboards artboards,StLayer layer,LayoutTag tag) {
        Log.d("test","====================generatedLayout====================");
        Log.d("test","layer:" + layer);
        Log.d("test","tag:" + tag);
        String xml = buildView(artboards,layer,tag);
        Log.d("test",xml);
        return xml;
    }
}
