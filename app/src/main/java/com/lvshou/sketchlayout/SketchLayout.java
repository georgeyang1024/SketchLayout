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

    int showIndex = 0;//登录页
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
    private List<LayoutTag> parseLayoutTags(StArtboards artboards,List<StLayer> orderEffectList) throws Exception {
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

        for (int i = 0; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer sourceLayer = orderEffectList.get(i);
            Log.d("test","curr:" + sourceLayer);
            StLayer leftLayer = null,rightLayer = null,topLayer = null,bottomLayer = null,outerLayer = null;
            double leftLayerDis = 0,rightLayerDis = 0,topLayerDis = 0,bottomLayerDis = 0;
            double tempDis;
//            Log.d("test","i:" + i);
            for (int j = i-1;j >= i-6 && j >= 0 && j < orderEffectList.size(); j--) {//往外层搜最多5个
                //循环内查找上下左右最近元素
//                Log.d("test","j:" + j);
//                if (j < 0) break;
//                if (j > orderEffectList.size()) continue;
                StLayer findLayer = orderEffectList.get(j);//参考目标
//                Log.d("test","findLayer:" + findLayer);
                tempDis = DisUtil.checkDisDirection(sourceLayer,findLayer,Gravity.LEFT);
                if (leftLayer == null) {
                    leftLayer = findLayer;
                    leftLayerDis = tempDis;
                } else if (Math.abs(tempDis)<Math.abs(leftLayerDis)) {//由于逐渐往外查找，两个里外不同，相同的距离，不要用等于取到最外层
                    leftLayer = findLayer;
                    leftLayerDis = tempDis;
                }

                tempDis = DisUtil.checkDisDirection(sourceLayer,findLayer,Gravity.RIGHT);
                if (rightLayer == null) {
                    rightLayer = findLayer;
                    rightLayerDis = tempDis;
                } else if (Math.abs(tempDis)<Math.abs(rightLayerDis)) {
                    rightLayer = findLayer;
                    rightLayerDis = tempDis;
                }

                tempDis = DisUtil.checkDisDirection(sourceLayer,findLayer,Gravity.TOP);
                if (topLayer == null) {
                    topLayer = findLayer;
                    topLayerDis = tempDis;
                } else if (Math.abs(tempDis)<Math.abs(topLayerDis)) {
                    topLayer = findLayer;
                    topLayerDis = tempDis;
                }

                tempDis = DisUtil.checkDisDirection(sourceLayer,findLayer,Gravity.BOTTOM);
                if (bottomLayer == null) {
                    bottomLayer = findLayer;
                    bottomLayerDis = tempDis;
                } else if (Math.abs(tempDis)<Math.abs(bottomLayerDis)) {
                    bottomLayer = findLayer;
                    bottomLayerDis = tempDis;
                }

                //只找一个外部
                if (outerLayer==null) {
                    if (DisUtil.isInner(sourceLayer,findLayer)) {
                        outerLayer = findLayer;
                    }
                }
            }

            //更名防止重复
            String rename = PinyinUtil.getName(sourceLayer.name);
            boolean finded = names.contains(rename);
            if (finded) {
                sourceLayer.name = PinyinUtil.getName(sourceLayer.name + sourceLayer.objectID.hashCode());
            } else {
                sourceLayer.name = rename;
            }
            names.add(sourceLayer.name);

            LayoutTag layoutTag = new LayoutTag();
            layoutTag.source = sourceLayer;
            layoutTag.leftLayer = leftLayer;
            layoutTag.leftDis = leftLayerDis;

            layoutTag.rightLayer = rightLayer;
            layoutTag.rightDis = rightLayerDis;

            layoutTag.topLayer = topLayer;
            layoutTag.topDis = topLayerDis;

            layoutTag.bottomLayer = bottomLayer;
            layoutTag.bottomDis = bottomLayerDis;

            layoutTag.outLayer = outerLayer;

            String xml = LayoutTagBuildUtil.generatedLayout(artboards,rootLayer,sourceLayer,layoutTag);
            xmlBuffer.append(xml+"\n");

            layoutTags.add(layoutTag);
        }
        Log.d("test",xmlBuffer.toString());
        return layoutTags;
    }




//    private void startLayout(JsonReader jsonReader) throws Exception {
//        List<StLayer> stLayer = null;
//
//        jsonReader.beginObject();
//        while (jsonReader.hasNext()) {
//            String keyName = jsonReader.nextName();
//            if (TextUtils.equals("notes",keyName)) {
//                jsonReader.skipValue();
//                continue;
//            } else if (TextUtils.equals("layers",keyName)) {
//                jsonReader.beginArray();
//                stLayer = JsonReaderUtil.paresArrayByTagClass(jsonReader, StLayer.class);
//                Log.d("test","layer:" + stLayer.size());
//                jsonReader.endArray();
//            } else {
//                String name = jsonReader.nextString();
//                Log.d("test",keyName + ">>" + name);
//            }
//        }
//        jsonReader.endObject();
//
//        //拿到所有
//
//        for (StLayer stLayerTag : stLayer) {
//            float minDis = Float.MAX_VALUE,maxDis = Float.MIN_VALUE;
//            for (StLayer stLayer2 : stLayer) {
//                if (stLayerTag == stLayer2) continue;
//
//            }
//        }
//
//    }
}
