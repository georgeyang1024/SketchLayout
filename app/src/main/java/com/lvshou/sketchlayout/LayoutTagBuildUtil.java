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

    private static String buildViewBound(StArtboards artboards,StLayer layer,LayoutTag tag) {
        List list = new ArrayList<String>();

        if (tag.leftLayer == tag.rightLayer && Math.abs(tag.leftDis-tag.rightDis)<=2) {
            //左右居中
            list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.leftLayer}));
            list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.leftLayer}));
        } else if (tag.outLayer!=null) {
            double tempLeftDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.LEFT);
            double tempRightDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.RIGHT);
            if (tempLeftDis>tempRightDis) {
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.outLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"\n",new Object[]{(int)tempRightDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.outLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"\n",new Object[]{(int)tempLeftDis + "px"}));
            }
        } else {
            if (tag.leftDis>tag.rightDis) {
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.rightLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"\n",new Object[]{(int)tag.rightDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.leftLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"\n",new Object[]{(int)tag.leftDis + "px"}));
            }
        }


        if (tag.topLayer == tag.bottomLayer && Math.abs(tag.topDis-tag.bottomDis)<=2) {
            //上下居中
            list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.topLayer}));
            list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.bottomLayer}));
        } else if (tag.outLayer!=null) {
            double tempTopDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.TOP);
            double tempBottomDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.BOTTOM);
            if (tempTopDis>tempBottomDis) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.outLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)tempBottomDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.outLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"\n",new Object[]{(int)tempTopDis + "px"}));
            }
        } else {
            if (tag.topDis>tag.bottomDis) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.bottomLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)tag.bottomDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.topDis}));
                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tag.topDis + "px"}));
            }
        }


//
//
//        if (tag.outLayer!=null) {
//            if (tag.leftLayer != null &&tag.leftLayer==tag.rightLayer) {
//                //内嵌的布局，左右居中
//                list.add(tag.source.rect.x > tag.leftLayer.rect.x ? "app:layout_constraintLeft_toRightOf" : "app:layout_constraintLeft_toLeftOf");
//                list.add(tag.leftLayer.toString());
//
//                list.add(tag.source.rect.x + tag.source.rect.width > tag.rightLayer.rect.x + tag.rightLayer.rect.width ? "app:layout_constraintRight_toLeftOf" : "app:layout_constraintRight_toRightOf");
//                list.add(tag.rightLayer.toString());
//
//                list.add(tag.topLayer==null?"layout_constraintBottom_toBottomOf":"app:layout_constraintTop_toTopOf");
//                list.add((tag.topLayer==null?tag.bottomLayer:tag.topLayer).toString());
//
//                list.add(tag.topLayer==null?"android:layout_marginBottom":"android:layout_marginTop");
//                list.add((int)(tag.topLayer==null?tag.bottomDis:tag.topDis) +  "px");
//            } else if (tag.topLayer !=null && tag.topLayer == tag.bottomLayer) {
//                //内嵌的布局，上下居中
//                list.add(tag.source.rect.y > tag.topLayer.rect.y ? "app:layout_constraintTop_toBottomOf" : "app:layout_constraintTop_toTopOf");
//                list.add(tag.topLayer.toString());
//
//                list.add(tag.source.rect.y + tag.source.rect.height > tag.bottomLayer.rect.y + tag.bottomLayer.rect.height ? "app:??" : "app:layout_constraintBottom_toBottomOf");
//                list.add(tag.bottomLayer.toString());
//
//                list.add(tag.leftLayer==null?"layout_constraintRight_toRightOf":"app:layout_constraintLeft_toLeftOf");
//                list.add((tag.leftLayer==null?tag.rightLayer:tag.leftLayer).toString());
//
//                list.add(tag.leftLayer == null?"android:layout_marginRight":"android:layout_marginLeft");
//                list.add((int)(tag.leftLayer==null?tag.rightDis:tag.rightDis) +  "px");
//            } else {
//                //内嵌的布局，不居中,选上面一个或下面一个
//                if (tag.leftDis > tag.rightDis) {
//                    list.add("app:layout_constraintLeft_toLeftOf");
//                    list.add(tag.leftLayer.toString());
//                    list.add("android:layout_marginLeft");
//                    list.add((int)tag.leftDis+"px");
//                } else {
//                    list.add("app:layout_constraintRight_toRightOf");
//                    list.add(tag.rightLayer.toString());
//                    list.add("android:layout_marginRight");
//                    list.add((int)tag.rightDis+"px");
//                }
//                if (tag.topDis>tag.bottomDis) {
//
//                }
////
//            }
//        } else {
//
//            if (tag.leftLayer!=null) {
//                list.add("app:layout_constraintLeft_toLeftOf");
//                list.add(tag.leftLayer.toString());
//                list.add("android:layout_marginLeft");
//                list.add((int)tag.leftDis+"px");
//            }
//            if (tag.rightLayer!=null) {
//                list.add("app:layout_constraintRight_toRightOf");
//                list.add(tag.rightLayer.toString());
//                list.add("android:layout_marginRight");
//                list.add((int)tag.rightDis+"px");
//            }
//            if (tag.topLayer!=null) {
//                list.add("app:layout_constraintTop_toTopOf");
//                list.add(tag.topLayer.toString());
//                list.add("android:layout_marginTop");
//                list.add((int)tag.topDis+"px");
//            }
//            if (tag.bottomLayer!=null) {
//                list.add("app:layout_constraintBottom_toBottomOf");
//                list.add(tag.bottomLayer.toString());
//                list.add("android:layout_marginBottom");
//                list.add((int)tag.bottomDis+"px");
//            }
//            if (list.size()==0) {
//                list.add("app:layout_constraintLeft_toLeftOf=\"parent\"");
//                list.add("app:layout_constraintTop_toTopOf\"parent\"");
//            }
//        }
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
