package com.lvshou.sketchlayout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */
public class SketchLayout extends ConstraintLayout {
    public SketchLayout(Context context) {
        super(context);
    }

    public SketchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SketchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int showIndex = 1;//登录页
    public void show() {
        try {
            //3.0设计/登录注册/index.html#artboard9
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("test.json"));
            JsonReader reader = new JsonReader(inputReader);
            reader.beginObject();
            while(reader.hasNext()){
                String keyName = reader.nextName();
                Log.d("test",keyName);
                if (TextUtils.equals(keyName,"artboards")) {
                    reader.beginArray();
                    int index = 0;
                    while (reader.hasNext()) {
                        reader.beginObject();
                        if (index==showIndex) {
                            StArtboards stArtboards = JsonReaderUtil.paresObject(reader, StArtboards.class);
                            Log.d("test","name:" + stArtboards.name);
                            if (stArtboards.height*1f/stArtboards.width<=16f/9) {
                                //大于6比9，是很长的图，不适合一屏展示，从上面开始布局

                            } else {
                                //一屏幕能展示完，边缘开始布局
                                doLater(stArtboards);
                            }
                        }
                        while (reader.hasNext()) {
                            reader.nextName();
                            reader.skipValue();
                        }
                        reader.endObject();
                        index++;
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLater(StArtboards artboards) throws Exception {
        ArrayList<StLayer> filterLayer = filterLayer(artboards);
        Log.d("test","filterLayer:" + Arrays.asList(filterLayer));
        orderFindList(artboards,filterLayer);
        Log.d("test","orderFindList:" + Arrays.asList(filterLayer));
        List<LayoutTag> layoutTagList = parseLayoutTags(artboards,filterLayer);
        Log.d("test","layoutTagList:" + Arrays.asList(layoutTagList));
    }

    /**
     * 按照元素离屏幕原点或最远点进行由近到远排序
     * @param effectList
     */
    private void orderFindList(final StArtboards artboards,List<StLayer> effectList) {
        Collections.sort(effectList, new Comparator<StLayer>() {
            @Override
            public int compare(StLayer layer, StLayer t1) {
                double disL = DisUtil.checkZeroEndDis(artboards,layer);
                double disR = DisUtil.checkZeroEndDis(artboards,t1);
                if (disL>disR) {
                    return 1;
                } else if (disL==disR) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    private ArrayList<StLayer> filterLayer(StArtboards artboards) throws Exception {
        ArrayList<StLayer> layers = new ArrayList<>();
        for (StLayer layer:artboards.layers) {
            if (!LayerFilterUtil.filter(artboards,layer)) {
                layers.add(layer);
            }
        }
        return layers;
    }

    /**
     * 定义位置
     * @param artboards
     * @param orderEffectList
     * @return
     * @throws Exception
     */
    private List<LayoutTag> parseLayoutTags(StArtboards artboards,ArrayList<StLayer> orderEffectList) throws Exception {
        List<LayoutTag> layoutTags = new ArrayList<>();//已找到位置的Layer列表
        List<String> names = new ArrayList<>();
        StringBuffer xmlBuffer = new StringBuffer();

        StLayer rootLayer = new StLayer();
        rootLayer.name = "parent";
        rootLayer.objectID = "parent";
        rootLayer.rect = new StRect();
        rootLayer.rect.x = 0;
        rootLayer.rect.y = 0;
        rootLayer.rect.width = artboards.width;
        rootLayer.rect.height = artboards.height;

        orderEffectList.add(0,rootLayer);
        for (int i = 1; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer tagLayer = orderEffectList.get(i);
            StLayer leftToLeftLayer = null,leftToRightLayer = null,rightToRightLayer = null,rightToLeftLayer = null;
            StLayer topToTopLayer = null,topToBottomLayer = null,bottomToBottomLayer = null,bottomToTopLayer = null;
            StLayer outerLayer = null;
            double leftToLeftDis = 0,leftToRightDis = 0,rightToRightDis = 0,rightToLeftDis = 0;
            double topToTopDis = 0,topToBottomDis = 0,bottomToBottomDis = 0,bottomToTopDis = 0;
            double tempDis;
//            Log.d("test","i:" + i);
            for (int j = i-1;j >= i-6 && j >= 0 && j < orderEffectList.size(); j--) {//往外层搜最多5个
                StLayer findLayer = orderEffectList.get(j);//参考目标

                //左居左
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.LEFT,findLayer,Gravity.LEFT);
                if (leftToLeftLayer==null) {
                    leftToLeftLayer = findLayer;
                    leftToLeftDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(leftToLeftDis)) {
                    leftToLeftLayer = findLayer;
                    leftToLeftDis = tempDis;
                }

                //左居某右
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.LEFT,findLayer,Gravity.RIGHT);
                if (leftToRightLayer==null) {
                    leftToRightLayer = findLayer;
                    leftToRightDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(leftToRightDis)) {
                    leftToRightLayer = findLayer;
                    leftToRightDis = tempDis;
                }

                //右居某右
                tempDis =  DisUtil.checkDisDirection(tagLayer,Gravity.RIGHT,findLayer,Gravity.RIGHT);
                if (rightToRightLayer==null) {
                    rightToRightLayer = findLayer;
                    rightToRightDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(rightToRightDis)) {
                    rightToRightLayer = findLayer;
                    rightToRightDis = tempDis;
                }

                //右居某左
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.RIGHT,findLayer,Gravity.LEFT);
                if (rightToLeftLayer==null) {
                    rightToLeftLayer = findLayer;
                    rightToLeftDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(rightToLeftDis)) {
                    rightToLeftLayer = findLayer;
                    rightToLeftDis = tempDis;
                }

                //上居某上
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.TOP,findLayer,Gravity.TOP);
                if (topToTopLayer==null) {
                    topToTopLayer = findLayer;
                    topToTopDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(topToTopDis)) {
                    topToTopLayer = findLayer;
                    topToTopDis = tempDis;
                }

                //上居某下
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.TOP,findLayer,Gravity.BOTTOM);
                if (topToBottomLayer==null) {
                    topToBottomLayer = findLayer;
                    topToBottomDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(topToBottomDis)) {
                    topToBottomLayer = findLayer;
                    topToBottomDis = tempDis;
                }

                //下居某下
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.BOTTOM,findLayer,Gravity.BOTTOM);
                if (bottomToBottomLayer==null) {
                    bottomToBottomLayer = findLayer;
                    bottomToBottomDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(bottomToBottomDis)) {
                    bottomToBottomLayer = findLayer;
                    bottomToBottomDis = tempDis;
                }

                //下居某上
                tempDis = DisUtil.checkDisDirection(tagLayer,Gravity.BOTTOM,findLayer,Gravity.TOP);
                if (bottomToTopLayer==null) {
                    bottomToTopLayer = findLayer;
                    bottomToTopDis = tempDis;
                } else if (Math.abs(tempDis) < Math.abs(bottomToTopDis)) {
                    bottomToTopLayer = findLayer;
                    bottomToTopDis = tempDis;
                }

                //只找一个最近的外部元素
                if (outerLayer==null) {
                    if (DisUtil.isInner(tagLayer,findLayer)) {
                        outerLayer = findLayer;
                    }
                }
            }
            //往外层检索完成....

            //更名防止重复
            String rename = PinyinUtil.getName(tagLayer.name);
            boolean finded = names.contains(rename);
            if (finded) {
                tagLayer.name = PinyinUtil.getName(tagLayer.name + tagLayer.objectID.hashCode());
            } else {
                tagLayer.name = rename;
            }
            names.add(tagLayer.name);

            LayoutTag layoutTag = new LayoutTag();
            layoutTag.source = tagLayer;
            layoutTag.leftToLeftLayer = leftToLeftLayer;
            layoutTag.leftToLeftDis = leftToLeftDis;

            layoutTag.leftToRightLayer = leftToRightLayer;
            layoutTag.leftToRightDis = leftToRightDis;

            layoutTag.rightToRightLayer = rightToRightLayer;
            layoutTag.rightToRightDis = rightToRightDis;

            layoutTag.rightToLeftLayer = rightToLeftLayer;
            layoutTag.rightToLeftDis = rightToLeftDis;

            layoutTag.topToTopLayer = topToTopLayer;
            layoutTag.topToTopDis = topToTopDis;

            layoutTag.topToBottomLayer = topToBottomLayer;
            layoutTag.topToBottomDis = topToBottomDis;

            layoutTag.bottomToBottomLayer = bottomToBottomLayer;
            layoutTag.bottomToBottomDis = bottomToBottomDis;

            layoutTag.bottomToTopLayer = bottomToTopLayer;
            layoutTag.bottomToTopDis = bottomToTopDis;

            layoutTag.outLayer = outerLayer;

            //左右最小距离的差值
            double leftRightDis = Math.min(Math.abs(leftToLeftDis),Math.abs(leftToRightDis))-Math.min(Math.abs(rightToRightDis),Math.abs(rightToLeftDis));
            if (Math.abs(leftRightDis)<=2) {
                layoutTag.leftRightEq = true;
            } else if (leftRightDis>0) {
                layoutTag.rightMinThanLeft = true;
            } else {
                layoutTag.leftMinThanRight = true;
            }

            double topBottomDis = Math.min(Math.abs(topToTopDis),Math.abs(topToBottomDis))-Math.min(Math.abs(bottomToTopDis),Math.abs(bottomToBottomDis));
            if (Math.abs(topBottomDis)<=2) {
                layoutTag.topBottomEq = true;
            } else if (topBottomDis>0) {
                layoutTag.bottomMinThanTop = true;
            } else {
                layoutTag.topMinThanBottom = true;
            }

            //最后一步定义真正需要的依赖元素
            if (layoutTag.leftRightEq) {
                if (Math.abs(leftToLeftDis)<=Math.abs(leftToRightDis)) {
                    layoutTag.useLeftLayer = leftToLeftLayer;
                } else {
                    layoutTag.useLeftLayer = rightToLeftLayer;
                }
                if (Math.abs(rightToRightDis)<=Math.abs(rightToLeftDis)) {
                    layoutTag.useRightLayer = rightToRightLayer;
                } else {
                    layoutTag.useRightLayer = rightToLeftLayer;
                }
            } else if (layoutTag.leftMinThanRight) {
                //通过测试
                if (Math.abs(leftToLeftDis)<=Math.abs(leftToRightDis)) {
                    layoutTag.useLeftLayer = leftToLeftLayer;
                } else {
                    layoutTag.useLeftLayer = leftToRightLayer;
                }
            } else if (layoutTag.rightMinThanLeft) {
                if (Math.abs(rightToRightDis)<=Math.abs(rightToLeftDis)) {
                    layoutTag.useRightLayer = rightToRightLayer;
                } else {
                    layoutTag.useRightLayer = rightToLeftLayer;
                }
            }

            if (layoutTag.topBottomEq) {
                if (Math.abs(topToTopDis)<=Math.abs(topToBottomDis)) {
                    layoutTag.useTopLayer = topToTopLayer;
                } else {
                    layoutTag.useTopLayer = topToBottomLayer;
                }
                if (Math.abs(bottomToBottomDis)<=Math.abs(bottomToTopDis)) {
                    layoutTag.useBottomLayer = bottomToBottomLayer;
                } else {
                    layoutTag.useBottomLayer = bottomToTopLayer;
                }
            } else if (layoutTag.topMinThanBottom) {
                if (Math.abs(topToTopDis)<=Math.abs(topToBottomDis)) {
                    layoutTag.useTopLayer = topToTopLayer;
                } else {
                    layoutTag.useTopLayer = topToBottomLayer;
                }
            } else {
                if (Math.abs(bottomToBottomDis)<=Math.abs(bottomToTopDis)) {
                    layoutTag.useBottomLayer = bottomToBottomLayer;
                } else {
                    layoutTag.useBottomLayer = bottomToTopLayer;
                }
            }

            String xml = LayoutTagBuildUtil.generatedLayout(artboards,rootLayer,tagLayer,layoutTag);
            xmlBuffer.append(xml+"\n");

            layoutTags.add(layoutTag);
        }
        Log.d("test",xmlBuffer.toString());
        return layoutTags;
    }


}
