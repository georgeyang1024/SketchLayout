package com.lvshou.sketchlayout;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    int showIndex = 3;//需要排版的界面index
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
                            List<LayoutTag> layoutTags = null;
                            if (stArtboards.height*1f/stArtboards.width>=16f/9) {
                                //大于6比9，是很长的图，不适合一屏展示，从上面开始布局

                            } else {
                                //一屏幕能展示完，边缘开始布局
                                layoutTags = parseLayoutTags(stArtboards);
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

    private List<LayoutTag> parseLayoutTags(StArtboards artboards) throws Exception {
        List<LayoutTag> layoutTags = new ArrayList<>();//已找到位置的Layer列表
        double minDis = Float.MAX_VALUE,maxDis = Float.MIN_VALUE;
        StLayer lastlayer = null;//最后操作的Layer
        ArrayList<StLayer> arrayList = null;
        if (artboards.layers instanceof ArrayList) {
            arrayList = (ArrayList) ((ArrayList)artboards.layers).clone();
        } else {
            arrayList = new ArrayList();
            arrayList.addAll(artboards.layers);
        }
        while (!arrayList.isEmpty()) {
            for (StLayer stLayer:arrayList) {
                double[] dis = checkZeroEndDis(artboards,stLayer);
                if (dis[0]<=minDis && dis[1] >= maxDis && stLayer!=lastlayer) {
                    layoutTags.add(parseLayoutTag(layoutTags,stLayer));

                    arrayList.remove(stLayer);
                    lastlayer = stLayer;
                    minDis = dis[0];
                    maxDis = dis[1];
                    break;
                }
            }
        }
        return layoutTags;
    }

    /**
     * 将找到的Layer转成位置类
     * @param findList
     * @param stLayer
     * @return
     */
    private LayoutTag parseLayoutTag(List<LayoutTag> findList,StLayer stLayer) {
        LayoutTag ret = new LayoutTag();
        //倒序查找，找最近的左右上下控件的不同方向的两个控件(如果垂直方向居中，找多一个上或下的最近控件)，定义位置
        double minDis = Float.MAX_VALUE,maxDis = Float.MIN_VALUE;
        for (int i = findList.size()-1; i <= 0; i--) {
            //放到对应位置
            
        }
        //找到最佳位置



        LayoutTagBuildUtil.buildLayoutTag(ret);
        return ret;
    }


    /**
     * 检测一个元素，距离坐标原点、屏幕最远点的距离
     * @return
     */
    private double[] checkZeroEndDis(StArtboards artboards,StLayer stLayer) {
        StRect rect = stLayer.rect;
        double[] ret = new double[2];
        ret[0] = Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2));
        ret[1] = Math.sqrt(Math.pow(artboards.width - rect.x,2) + Math.pow(artboards.height - rect.y,2));
        return ret;
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
