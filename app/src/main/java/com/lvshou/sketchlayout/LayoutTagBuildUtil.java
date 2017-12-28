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
        if (tag.betterLeftLayer!=null) {
            if (tag.betterLeftDis==0) {
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.betterLeftLayer}));
            } else if (tag.betterLeftDis>=0) {
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.betterLeftLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.betterLeftDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintLeft_toRight=\"%s\"", new Object[]{tag.betterLeftLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.betterLeftDis + "px"}));
            }
        }

        if (tag.betterRightLayer!=null) {
            if (tag.betterRightDis==0) {
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.betterRightLayer}));
            } else if (tag.betterRightDis>=0) {
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.betterRightLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.betterRightDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.betterRightLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.betterRightDis + "px"}));
            }
        }

        if (tag.betterTopLayer!=null) {
            if (tag.betterTopDis==0) {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.betterTopLayer}));
            } else if (tag.betterTopDis>=0) {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.betterTopLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.betterTopDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"", new Object[]{tag.betterTopLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"", new Object[]{(int) tag.betterTopDis + "px"}));
            }
        }

        if (tag.betterBottomLayer!=null) {
            if (tag.betterBottomDis==0) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
            } else if (tag.betterBottomDis>=0) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"", new Object[]{(int) tag.betterBottomDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintBottom_toTopOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.betterBottomDis + "px"}));
            }
        }


//        if (tag.outLayer==null) {
//            tag.outLayer = parentLayer;
//        }
//
//
//        if (tag.leftLayer==null && tag.rightLayer==null) {
//            //左右都没找到最近元素，使用父类
//            double tempParentLeftDis = DisUtil.checkDis(tag.source, parentLayer, Gravity.LEFT);
//            double tempParentRightDis = DisUtil.checkDis(layer, tag.outLayer, Gravity.RIGHT);
//            if (tempParentLeftDis <= tempParentRightDis) {
//                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{parentLayer}));
//                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tempParentLeftDis + "px"}));
//            } else {
//                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{parentLayer}));
//                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tempParentRightDis + "px"}));
//            }
//        } else if (tag.leftLayer == tag.rightLayer && Math.abs(tag.leftDis-tag.rightDis)<=2) {
//            //左右居中，允许误差2px
//            list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftLayer}));
//            list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.leftLayer}));
//        } else {
//                double tempParentLeftDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.LEFT);
//                double tempParentRightDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.RIGHT);
//                if (tempParentLeftDis < tag.leftDis && tempParentLeftDis < Math.min(tag.rightDis,tempParentRightDis)) {
//                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{"parent"}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"",new Object[]{(int)tempParentLeftDis + "px"}));
//                } else if (tag.leftDis <= tempParentLeftDis && tag.leftDis < Math.min(tag.rightDis,tempParentRightDis)) {
//                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"",new Object[]{tag.leftLayer}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"",new Object[]{(int)tag.leftDis + "px"}));
//                } else if (tempParentRightDis < tag.rightDis) {
//                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{"parent"}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"",new Object[]{(int)tempParentRightDis + "px"}));
//                } else {
//                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"",new Object[]{tag.rightLayer}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"",new Object[]{(int)tag.rightDis + "px"}));
//                }
//        }
//
//        if (tag.topLayer == null && tag.bottomLayer==null) {
//            //上下都没找到最近元素，使用父类
//            double tempParentTopDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.TOP);
//            double tempParentBottomDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.BOTTOM);
//            if (tempParentTopDis<tempParentBottomDis) {
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{parentLayer}));
//                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tempParentTopDis + "px"}));
//            } else {
//                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{parentLayer}));
//                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)(tempParentBottomDis) + "px"}));
//            }
//        } else if (tag.topLayer !=null && tag.topLayer == tag.bottomLayer && Math.abs(tag.topDis-tag.bottomDis)<=2) {
//            //上下居中,误差2px
//            list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.topLayer}));
//            list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.bottomLayer}));
//        } else {
//            double tempParentTopDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.TOP);
//            double tempParentBottomDis = DisUtil.checkDis(layer,tag.outLayer, Gravity.BOTTOM);
//            if (tempParentTopDis < tag.topDis && tempParentTopDis < Math.min(tag.bottomDis,tempParentBottomDis)) {
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{"parent"}));
//                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tempParentTopDis + "px"}));
//            } else if (tag.topDis <= tempParentTopDis && tag.topDis < Math.min(tag.bottomDis,tempParentBottomDis)) {
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"",new Object[]{tag.topLayer}));
//                list.add(String.format("android:layout_marginTop=\"%s\"",new Object[]{(int)tempParentTopDis + "px"}));
//            } else if (tempParentBottomDis<tag.bottomDis) {
//                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.outLayer}));
//                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)(tempParentBottomDis) + "px"}));
//            } else {
//                list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"",new Object[]{tag.bottomLayer}));
//                list.add(String.format("android:layout_marginTop=\"%s\"\n",new Object[]{(int)(tag.bottomDis-layer.rect.height) + "px"}));
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
     * 二次定位，所有情况
     * 通用定位
     * @param artboards
     * @param layer
     * @param tag
     */
    private static void alignmentLayout(StArtboards artboards,StLayer rootLayer,StLayer layer,LayoutTag tag) {
        //左右定位
        if (tag.leftLayer==null && tag.rightLayer==null) {
            //左右都没找到最近元素，使用父类
            double tempParentLeftDis = DisUtil.checkDisDirection(tag.source, rootLayer, Gravity.LEFT);
            double tempParentRightDis = DisUtil.checkDisDirection(layer, rootLayer, Gravity.RIGHT);
            if (Math.abs(tempParentLeftDis) <= Math.abs(tempParentRightDis)) {
                tag.betterLeftLayer = rootLayer;
                tag.betterLeftDis = tempParentLeftDis;
            } else {
                tag.betterRightLayer = rootLayer;
                tag.betterRightDis = tempParentRightDis;
            }
        } else if (tag.leftLayer == tag.rightLayer && Math.abs(tag.leftDis)-Math.abs(tag.rightDis)<=2) {
            //左右距离一样，且，都是同一个元素，即是居中
            tag.betterLeftLayer = tag.leftLayer;
            tag.betterRightLayer = tag.rightLayer;
        } else if (tag.leftLayer == tag.rightLayer) {
            //左右元素一样，但是不居中,选一个最佳
            if (tag.outLayer==null) {
                double tempLeftDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.LEFT);
                double tempRightDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.RIGHT);
                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
                    tag.betterLeftLayer = rootLayer;
                    tag.betterLeftDis = tempLeftDis;
                } else {
                    tag.betterRightLayer = rootLayer;
                    tag.betterRightDis = tempRightDis;
                }
            } else {
                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
                    tag.betterLeftLayer = tag.outLayer;
                    tag.betterLeftDis = tempLeftDis;
                } else {
                    tag.betterRightLayer = tag.outLayer;
                    tag.betterRightDis = tempRightDis;
                }
            }
        } else if (tag.leftLayer != null && tag.rightLayer==null) {
            //左右只有一个左元素
            if (tag.outLayer==null) {
                double tempLeftDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.LEFT);
                tag.betterLeftLayer = tag.outLayer;
                tag.betterLeftDis = tempLeftDis;
            } else {
                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
                if (Math.abs(tag.leftDis)<=Math.abs(tempLeftDis)) {
                    tag.betterLeftLayer = tag.leftLayer;
                    tag.betterLeftDis = tag.leftDis;
                } else {
                    tag.betterLeftLayer = tag.outLayer;
                    tag.betterLeftDis = tempLeftDis;
                }
            }
        } else if (tag.rightLayer != null && tag.leftLayer==null) {
            //左右只有一个右元素
            if (tag.outLayer==null) {
                double tempRightDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.RIGHT);
                tag.betterRightLayer = tag.outLayer;
                tag.betterRightDis = tempRightDis;
            } else {
                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
                if (Math.abs(tag.rightDis)<=Math.abs(tempRightDis)) {
                    tag.betterRightLayer = tag.rightLayer;
                    tag.betterRightDis = tag.rightDis;
                } else {
                    tag.betterRightLayer = tag.outLayer;
                    tag.betterRightDis = tempRightDis;
                }
            }
        } else if (tag.leftLayer!=tag.rightLayer){
            //最后一种情况，左右均不为空，且不相同
            //此时，如果有外围元素，参考外围元素最近的点，没有的话，取找到的最近的点
            if (tag.outLayer==null) {
                if (Math.abs(tag.leftDis)<=Math.abs(tag.rightDis)) {
                    tag.betterLeftLayer = tag.leftLayer;
                    tag.betterLeftDis = tag.leftDis;
                } else {
                    tag.betterRightLayer = tag.rightLayer;
                    tag.betterRightDis = tag.rightDis;
                }
            } else {
                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
                    tag.betterLeftLayer = tag.outLayer;
                    tag.betterLeftDis = tempLeftDis;
                } else {
                    tag.betterRightLayer = tag.outLayer;
                    tag.betterRightDis = tempRightDis;
                }
            }
        } else {
            Log.d("test","error");
        }


        //上下定位
        if (tag.topLayer==null && tag.bottomLayer==null) {
            //左右都没找到最近元素，使用父类
            double tempParentTopDis = DisUtil.checkDisDirection(tag.source, rootLayer, Gravity.TOP);
            double tempParentBottomDis = DisUtil.checkDisDirection(layer, rootLayer, Gravity.BOTTOM);
            if (Math.abs(tempParentTopDis) <= Math.abs(tempParentBottomDis)) {
                tag.betterTopLayer = rootLayer;
                tag.betterTopDis = tempParentTopDis;
            } else {
                tag.betterBottomLayer = rootLayer;
                tag.betterBottomDis = tempParentBottomDis;
            }
        } else if (tag.topLayer == tag.bottomLayer && Math.abs(tag.topDis)-Math.abs(tag.bottomDis)<=2) {
            //上下距离一样，且，都是同一个元素，即是居中
            tag.betterTopLayer = tag.topLayer;
            tag.betterBottomLayer = tag.bottomLayer;
        } else if (tag.topLayer == tag.bottomLayer) {
            //左右元素一样，但是不居中,选一个最佳
            if (tag.outLayer==null) {
                double tempTopDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.TOP);
                double tempBottomDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.BOTTOM);
                if (Math.abs(tempTopDis) <= Math.abs(tempBottomDis)) {
                    tag.betterTopLayer = rootLayer;
                    tag.betterTopDis = tempTopDis;
                } else {
                    tag.betterBottomLayer = rootLayer;
                    tag.betterBottomDis = tempBottomDis;
                }
            } else {
                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
                if (Math.abs(tempTopDis)<=Math.abs(tempBottomDis)) {
                    tag.betterTopLayer = tag.outLayer;
                    tag.betterTopDis = tempTopDis;
                } else {
                    tag.betterBottomLayer = tag.outLayer;
                    tag.betterBottomDis = tempBottomDis;
                }
            }
        } else if (tag.topLayer != null && tag.bottomLayer==null) {
            //左右只有一个左元素
            if (tag.outLayer==null) {
                double tempTopDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.TOP);
                tag.betterTopLayer = rootLayer;
                tag.betterTopDis = tempTopDis;
            } else {
                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
                if (Math.abs(tag.topDis)<=Math.abs(tempTopDis)) {
                    tag.betterTopLayer = tag.topLayer;
                    tag.betterTopDis = tag.topDis;
                } else {
                    tag.betterTopLayer = tag.outLayer;
                    tag.betterTopDis = tempTopDis;
                }
            }
        } else if (tag.bottomLayer != null && tag.topLayer==null) {
            //左右只有一个右元素
            if (tag.outLayer==null) {
                double tempBottomDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.BOTTOM);
                tag.betterBottomLayer = tag.outLayer;
                tag.betterBottomDis = tempBottomDis;
            } else {
                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
                if (Math.abs(tag.bottomDis)<=Math.abs(tempBottomDis)) {
                    tag.betterBottomLayer = tag.bottomLayer;
                    tag.betterBottomDis = tag.bottomDis;
                } else {
                    tag.betterBottomLayer = tag.outLayer;
                    tag.betterBottomDis = tempBottomDis;
                }
            }
        } else if (tag.topLayer!=tag.bottomLayer){
            //最后一种情况，左右均不为空，且不相同
            //此时，如果有外围元素，参考外围元素最近的点，没有的话，取找到的最近的点
            if (tag.outLayer==null) {
                if (Math.abs(tag.topDis)<=Math.abs(tag.bottomDis)) {
                    tag.betterTopLayer = tag.topLayer;
                    tag.betterTopDis = tag.topDis;
                } else {
                    tag.betterBottomLayer = tag.bottomLayer;
                    tag.betterBottomDis = tag.bottomDis;
                }
            } else {
                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
                if (Math.abs(tempTopDis)<=Math.abs(tempBottomDis)) {
                    tag.betterTopLayer = tag.outLayer;
                    tag.betterTopDis = tempTopDis;
                } else {
                    tag.betterBottomLayer = tag.outLayer;
                    tag.betterBottomDis = tempBottomDis;
                }
            }
        } else {
            Log.d("test","error");
        }
    }

    /**
     * 一次定位好LayoutTag位置后，调用此方法生成背景，写入文字之类的操作
     */
    public static String generatedLayout(StArtboards artboards,StLayer rootLayer,StLayer layer,LayoutTag tag) {
        Log.d("test","====================generatedLayout====================");
        Log.d("test","layer:" + layer);
        Log.d("test","tag:" + tag);
        alignmentLayout(artboards,rootLayer,layer,tag);
        Log.d("test","alignmentLayout:"  + tag);
        String xml = buildView(artboards,layer,tag);
        Log.d("test",xml);


        return xml;
    }
}
