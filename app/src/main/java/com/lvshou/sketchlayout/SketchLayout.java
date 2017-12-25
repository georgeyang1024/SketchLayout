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
                        if (index==showIndex) {
//                            startLayout(reader);
                            reader.beginObject();
                            StArtboards stArtboards = JsonReaderUtil.paresObject(reader, StArtboards.class);
                            Log.d("test","name:" + stArtboards.name);
                            if (stArtboards.height*1f/stArtboards.width<=16f/9) {
                                //大于6比9，是很长的图，不适合一屏展示，从上面开始布局

                            } else {
                                //一屏幕能展示完，边缘开始布局
                                doLater(stArtboards);
                            }
                            reader.endObject();
                        }
                        index++;
                    }
                    reader.endArray();
                }
                reader.skipValue();
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
    private void orderFindList(final  StArtboards artboards,List<StLayer> effectList) {
        Collections.sort(effectList, new Comparator<StLayer>() {
            @Override
            public int compare(StLayer layer, StLayer t1) {
                double disL = checkZeroEndDis(artboards,layer);
                double disR = checkZeroEndDis(artboards,t1);
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
        for (int i = 0; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer sourceLayer = orderEffectList.get(i);
            Log.d("test","curr:" + sourceLayer);
            StLayer leftLayer = null,rightLayer = null,topLayer = null,bottomLayer = null,outerLayer = null;
            double leftLayerDis = 0,rightLayerDis = 0,topLayerDis = 0,bottomLayerDis = 0;
            double tempDis;
            Log.d("test","i:" + i);
            for (int j = i-1;j >= i-6 && j >= 0 && j < orderEffectList.size(); j--) {//往外层搜最多5个
                //循环内查找上下左右最近元素
//                Log.d("test","j:" + j);
//                if (j < 0) break;
//                if (j > orderEffectList.size()) continue;
                StLayer findLayer = orderEffectList.get(j);//参考目标
                Log.d("test","findLayer:" + findLayer);
                tempDis = checkDis(sourceLayer,findLayer,Gravity.LEFT);
                if (leftLayer == null) {
                    leftLayer = findLayer;
                    leftLayerDis = tempDis;
                } else if (tempDis>leftLayerDis) {//由于逐渐往外查找，两个里外不同，相同的距离，不要用等于取到最外层
                    leftLayer = findLayer;
                    leftLayerDis = tempDis;
                }

                tempDis = checkDis(sourceLayer,findLayer,Gravity.RIGHT);
                if (rightLayer == null) {
                    rightLayer = findLayer;
                    rightLayerDis = tempDis;
                } else if (tempDis>rightLayerDis) {
                    rightLayer = findLayer;
                    rightLayerDis = tempDis;
                }

                tempDis = checkDis(sourceLayer,findLayer,Gravity.TOP);
                if (topLayer == null) {
                    topLayer = findLayer;
                    topLayerDis = tempDis;
                } else if (tempDis>topLayerDis) {
                    topLayer = findLayer;
                    topLayerDis = tempDis;
                }

                tempDis = checkDis(sourceLayer,findLayer,Gravity.BOTTOM);
                if (bottomLayer == null) {
                    bottomLayer = findLayer;
                    bottomLayerDis = tempDis;
                } else if (tempDis>bottomLayerDis) {
                    bottomLayer = findLayer;
                    bottomLayerDis = tempDis;
                }

                //只找一个外部
                if (outerLayer==null) {
                    if (isInner(sourceLayer,findLayer)) {
                        outerLayer = findLayer;
                    }
                }
            }

            //元素上下左右外环最近的元素都已找到，筛选两个最优元素
            //查看是否有居中,局左边居右边距离相差2px，当成是居中
            if (leftLayer == rightLayer && Math.abs(leftLayerDis-rightLayerDis)<=2) {
                if (checkDis(sourceLayer,topLayer,Gravity.TOP)>checkDis(sourceLayer,bottomLayer,Gravity.BOTTOM)) {
                    topLayer = null;
                } else {
                    bottomLayer = null;
                }
            } else if (topLayer == bottomLayer && Math.abs(topLayerDis-bottomLayerDis)<=2) {
                if (checkDis(sourceLayer,leftLayer,Gravity.LEFT)>checkDis(sourceLayer,rightLayer,Gravity.RIGHT)) {
                    leftLayer = null;
                } else {
                    rightLayer = null;
                }
            } else {//没有居中的时候，找两个不同方向的元素
                if (topLayerDis>bottomLayerDis) {
                    topLayer = null;
                } else {
                    bottomLayer = null;
                }
                if (leftLayerDis>rightLayerDis) {
                    leftLayer = null;
                } else {
                    rightLayer = null;
                }
            }


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

            LayoutTagBuildUtil.generatedLayout(artboards,sourceLayer,layoutTag);

            layoutTags.add(layoutTag);
        }
        return layoutTags;
    }


    /**
     * 检测一个元素，离坐标原点、屏幕最远点的总距离
     * @return
     * done
     */
    private double checkZeroEndDis(StArtboards artboards,StLayer stLayer) {
        if (artboards==null || stLayer==null) {
            return Double.MAX_VALUE;
        }
//        Log.d("test","checkZeroEndDis!!");
//        Log.d("test","stLayer:"+ stLayer);
        StRect rect = stLayer.rect;
        double zeroDis = Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2));
        double endDis = Math.sqrt(Math.pow( rect.x + rect.width - artboards.width,2) + Math.pow(rect.y + rect.height - artboards.height,2));
//        Log.d("test","zeroDis:"+ zeroDis);
//        Log.d("test","endDis:"+ endDis);
        double totalDis = zeroDis + endDis;
//        Log.d("test","totalDis:"+ totalDis);
//        stLayer.totalDis = totalDis;
        return totalDis;
    }

    /**
     * 计算两个元素在某个方向的距离
     * @param sourceLayer
     * @param tagLayer
     * @param gravity
     * @return
     */
    private double checkDis(StLayer sourceLayer,StLayer tagLayer,int gravity) {
        if (sourceLayer == null || tagLayer == null) return Double.MAX_VALUE;
        if (gravity==Gravity.LEFT) {
            return Math.abs(tagLayer.rect.x - sourceLayer.rect.x);
        }
        if (gravity==Gravity.LEFT) {
            return Math.abs(tagLayer.rect.x + tagLayer.rect.width - sourceLayer.rect.x - sourceLayer.rect.width);
        }
        if (gravity==Gravity.TOP) {
            return Math.abs(tagLayer.rect.y - sourceLayer.rect.y);
        }
        if (gravity==Gravity.BOTTOM) {
            return Math.abs(tagLayer.rect.y + tagLayer.rect.height - sourceLayer.rect.y - sourceLayer.rect.height);
        }
        return Double.MAX_VALUE;
    }

    private boolean isInner(StLayer sourceLayer,StLayer tagLayer) {
        return tagLayer.rect.x < sourceLayer.rect.x
                && tagLayer.rect.x + tagLayer.rect.width > sourceLayer.rect.x + sourceLayer.rect.width
                && tagLayer.rect.y < sourceLayer.rect.y
                && tagLayer.rect.y + tagLayer.rect.height > sourceLayer.rect.y + sourceLayer.rect.height;
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
