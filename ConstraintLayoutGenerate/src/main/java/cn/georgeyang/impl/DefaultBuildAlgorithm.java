package cn.georgeyang.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.georgeyang.bean.BoundDepend;
import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.BoundTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;
import cn.georgeyang.conf.Gravity;
import cn.georgeyang.intf.BuildAlgorithm;
import cn.georgeyang.intf.LayerFilter;
import cn.georgeyang.util.DisUtil;
import cn.georgeyang.util.PinyinUtil;

/**
 * 默认的边界构建算法实现
 * Created by george.yang on 18/1/21.
 */
public class DefaultBuildAlgorithm implements BuildAlgorithm {

    @Override
    public List<BoundResultTag> build(StArtboards artboards, LayerFilter layerFilter) {
        StRect effectBounds = layerFilter.effectBounds(artboards,acceptDeviation(artboards,null,Gravity.NONE));
        List<StLayer> orderEffectList = getEffectList(artboards,layerFilter,effectBounds);
        orderFindList(artboards,orderEffectList);
        List<StLayer> orderEffectList2 = filterDepend(artboards,layerFilter,orderEffectList);
        List<BoundTag> parseBoundTags = parseBoundTags(artboards,orderEffectList2,effectBounds,layerFilter);
        return parseBoundResultTags(artboards,parseBoundTags,layerFilter);
    }

    public List<StLayer> getEffectList(StArtboards artboards,LayerFilter layerFilter,StRect effectBound) {
        ArrayList<StLayer> layers = new ArrayList<>();
        for (StLayer layer:artboards.layers) {
            if (!layerFilter.filterLayer(artboards,layer,effectBound,acceptDeviation(artboards,null,Gravity.NONE))) {
                layers.add(layer);
            }
        }
        return layers;
    }

    public List<StLayer> filterDepend(StArtboards artboards,LayerFilter layerFilter,List<StLayer> orderEffectList) {
        List<StLayer> ret = new ArrayList<>();
        List<BoundDepend> list = new ArrayList<>();

        for (int i = 0; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer tagLayer = orderEffectList.get(i);

            BoundDepend boundDepend = new BoundDepend();
            boundDepend.source = tagLayer;
            boundDepend.outerList = new ArrayList<>();
            boundDepend.innerList = new ArrayList<>();

            list.add(boundDepend);
        }

        for (int i = 0; i < list.size(); i++) {//从外往里循环找位置
            BoundDepend tagDepend = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {//找inner
                BoundDepend findDepend = list.get(j);//离中心越近的元素
                if (DisUtil.isInner(findDepend.source,tagDepend.source)) {
                    tagDepend.innerList.add(findDepend.source);
                    findDepend.outerList.add(tagDepend.source);
                }
            }
        }

        for(BoundDepend boundDepend:list) {
            if (!layerFilter.filterDependent(artboards,boundDepend)) {
                ret.add(boundDepend.source);
            }
        }
        return ret;
    }

    @Override
    public double acceptDeviation(StArtboards artboards, StLayer layer, int gravity) {
        return artboards.width / 100d;//误差范围 100分之1
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


    private List<BoundResultTag> parseBoundResultTags(StArtboards artboards,List<BoundTag> tagList,LayerFilter layerFilter) {
        List<BoundResultTag> list = new ArrayList<>();

        for (BoundTag tag:tagList) {
            BoundResultTag resultTag = new BoundResultTag();
            resultTag.source = tag;

            if (tag.leftRightEq) {
                if (tag.leftToLeftLayer != tag.rightToRightLayer) {
                    //测试完成
                    resultTag.leftToLeftLayer = tag.leftToLeftLayer;
                    resultTag.marginLeft = tag.leftToLeftDis;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;
                    resultTag.marginRight = tag.rightToRightDis;
                } else if (tag.leftToLeftLayer == tag.rightToRightLayer) {
                    //测试完成
                    resultTag.leftToLeftLayer = tag.leftToLeftLayer;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;
                } else if (tag.rightToLeftLayer != tag.leftToRightLayer) {
                    resultTag.rightToLeftLayer = tag.leftToLeftLayer;
                    resultTag.marginRight = tag.leftToLeftDis;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;
                    resultTag.marginLeft = tag.rightToRightDis;
                } else if (tag.rightToLeftLayer == tag.leftToRightLayer) {
                    resultTag.rightToLeftLayer = tag.leftToLeftLayer;
                    resultTag.leftToRightLayer = tag.rightToRightLayer;
                }
            } else if (tag.leftMinThanRight) {
                if (Math.abs(tag.leftToLeftDis) < Math.abs(tag.leftToRightDis)) {
                    resultTag.leftToLeftLayer = tag.useLeftLayer;
                    resultTag.marginLeft = tag.leftToLeftDis;
                } else {
                    resultTag.leftToRightLayer = tag.useLeftLayer;
                    resultTag.marginLeft = tag.leftToRightDis;
                }
            } else if (tag.rightMinThanLeft) {
                if (Math.abs(tag.rightToRightDis) < Math.abs(tag.rightToLeftDis)) {
                    resultTag.rightToRightLayer = tag.useRightLayer;
                    resultTag.marginRight = tag.rightToRightDis;
                } else {
                    resultTag.rightToLeftLayer = tag.useRightLayer;
                    resultTag.marginRight = tag.rightToLeftDis;
                }
            }

            if (tag.topBottomEq) {
                if (tag.topToTopLayer != tag.bottomToBottomLayer) {
                    resultTag.topToTopLayer = tag.topToTopLayer;
                    resultTag.marginTop = tag.topToTopDis;
                    resultTag.bottomToBottomLayer = tag.bottomToBottomLayer;
                    resultTag.marginBottom = tag.bottomToBottomDis;
                } else if (tag.topToTopLayer == tag.bottomToBottomLayer) {
                    resultTag.topToTopLayer = tag.topToTopLayer;
                    resultTag.bottomToBottomLayer = tag.bottomToBottomLayer;
                } else if (tag.bottomToTopLayer != tag.topToBottomLayer) {
                    resultTag.bottomToTopLayer = tag.topToTopLayer;
                    resultTag.marginBottom = tag.topToTopDis;
                    resultTag.bottomToBottomLayer = tag.bottomToBottomLayer;
                    resultTag.marginTop = tag.topToTopDis;
                } else if (tag.bottomToTopLayer == tag.topToBottomLayer) {
                    resultTag.bottomToTopLayer = tag.topToTopLayer;
                    resultTag.topToBottomLayer = tag.bottomToBottomLayer;
                }
            } else if (tag.topMinThanBottom) {
                if (Math.abs(tag.topToTopDis) < Math.abs(tag.topToBottomDis)) {
                    resultTag.topToTopLayer = tag.useTopLayer;
                    resultTag.marginTop = tag.topToTopDis;
                } else {
                    resultTag.topToBottomLayer = tag.useTopLayer;
                    resultTag.marginTop = tag.topToBottomDis;
                }
            } else if (tag.bottomMinThanTop) {
                if (Math.abs(tag.bottomToBottomDis) < Math.abs(tag.bottomToTopDis)) {
                    resultTag.bottomToBottomLayer = tag.useBottomLayer;
                    resultTag.marginBottom = tag.bottomToBottomDis;
                } else {
                    resultTag.bottomToTopLayer = tag.useBottomLayer;
                    resultTag.marginBottom = tag.bottomToTopDis;
                }
            }


            //对比元素最优边界与它对应的外围布局
            if (tag.outLayer!=null) {
                double left = DisUtil.checkRealDisDirection(tag.source,Gravity.LEFT,tag.outLayer,Gravity.LEFT);
                double right = DisUtil.checkRealDisDirection(tag.source,Gravity.RIGHT,tag.outLayer,Gravity.RIGHT);
                double top = DisUtil.checkRealDisDirection(tag.source,Gravity.TOP,tag.outLayer,Gravity.TOP);
                double bottom = DisUtil.checkRealDisDirection(tag.source,Gravity.BOTTOM,tag.outLayer,Gravity.BOTTOM);
                if ((int)left >> 31 == (int)right >> 31) {//判断是否都是正数或者都是负数
                    if (Math.abs(Math.abs(left) - Math.abs(right)) <= acceptDeviation(artboards,tag.outLayer,Gravity.HORIZONTAL)) {
                        resultTag.clearBound(Gravity.NONE);
                        resultTag.leftToLeftLayer = tag.outLayer;
                        resultTag.rightToRightLayer = tag.outLayer;
                    }
                }
                if ((int)top >> 31 == (int)bottom >> 31) {//判断是否都是正数或者都是负数
                    if (Math.abs(Math.abs(top) - Math.abs(bottom)) <= acceptDeviation(artboards,tag.outLayer,Gravity.VERTICAL)) {
                        resultTag.clearBound(Gravity.NONE);
                        resultTag.topToTopLayer = tag.outLayer;
                        resultTag.bottomToBottomLayer = tag.outLayer;
                    }
                }
                //如果布局的某一反向被清空了，补充一个最短距离
                if (resultTag.leftToLeftLayer == null && resultTag.rightToRightLayer==null && resultTag.leftToRightLayer==null && resultTag.rightToLeftLayer==null) {
                    if (Math.abs(left) < Math.abs(right)) {
                        resultTag.leftToLeftLayer = tag.outLayer;
                        resultTag.marginLeft = left;
                    } else {
                        resultTag.rightToRightLayer = tag.outLayer;
                        resultTag.marginRight = right;
                    }
                }
                if (resultTag.topToTopLayer==null && resultTag.bottomToBottomLayer==null && resultTag.topToBottomLayer==null && resultTag.bottomToTopLayer==null) {
                    if (Math.abs(top) < Math.abs(bottom)) {
                        resultTag.topToTopLayer = tag.outLayer;
                        resultTag.marginTop = top;
                    } else {
                        resultTag.bottomToBottomLayer = tag.outLayer;
                        resultTag.marginBottom = bottom;
                    }
                }
            }

            list.add(resultTag);
        }

        return list;
    }
    /**
     * 定义各方向最短位置
     * @param artboards
     * @param orderEffectList
     * @return
     * @throws Exception
     */
    private List<BoundTag> parseBoundTags(StArtboards artboards, List<StLayer> orderEffectList,StRect effectBounds,LayerFilter layerFilter) {
        List<BoundTag> boundTags = new ArrayList<>();//已找到位置的Layer列表
        List<String> names = new ArrayList<>();

        StLayer rootLayer = new StLayer();
        rootLayer.name = "parent";
        rootLayer.objectID = "parent";
        rootLayer.rect = new StRect();
        rootLayer.rect = effectBounds;

        orderEffectList.add(0,rootLayer);

        for (int i = 1; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer tagLayer = orderEffectList.get(i);

            StLayer outerLayer = null;
            StLayer leftToLeftLayer = null,leftToRightLayer = null,rightToRightLayer = null,rightToLeftLayer = null;
            StLayer topToTopLayer = null,topToBottomLayer = null,bottomToBottomLayer = null,bottomToTopLayer = null;
            double[] leftToLeftDis = null,leftToRightDis =null ,rightToRightDis =null,rightToLeftDis = null;
            double[] topToTopDis = null,topToBottomDis = null,bottomToBottomDis = null,bottomToTopDis = null;
            double[] tempDis;
//            int findMinIndex = i - 100;//默认找前6个元素，如果中间有N个过滤元素，继续往前找N个，保证找到6个有效元素
            int findMinIndex = 0;//往回找全部已知元素
            for (int j = i-1;j >= findMinIndex && j >= 0 && j < orderEffectList.size();j--) {//往外层搜最多5个
                StLayer findLayer = orderEffectList.get(j);//参考目标

                //跳过无效的参考目标
                if (layerFilter.filterReference(artboards,tagLayer,findLayer)) {
//                    findMinIndex--;
                    continue;
                }

                if (outerLayer==null) {
                    //只找一个最近的外部元素
                    if (DisUtil.isInner(tagLayer,findLayer)) {
                        outerLayer = findLayer;
                    }
                }


                //左居左
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer, Gravity.LEFT,findLayer,Gravity.LEFT);
                if (leftToLeftLayer==null) {
                    leftToLeftLayer = findLayer;
                    leftToLeftDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(leftToLeftDis[0])) {
                    leftToLeftLayer = findLayer;
                    leftToLeftDis = tempDis;
                }

                //左居某右
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.LEFT,findLayer,Gravity.RIGHT);
                if (leftToRightLayer==null) {
                    leftToRightLayer = findLayer;
                    leftToRightDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(leftToRightDis[0])) {
                    leftToRightLayer = findLayer;
                    leftToRightDis = tempDis;
                }

                //右居某右
                tempDis =  DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.RIGHT,findLayer,Gravity.RIGHT);
                if (rightToRightLayer==null) {
                    rightToRightLayer = findLayer;
                    rightToRightDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(rightToRightDis[0])) {
                    rightToRightLayer = findLayer;
                    rightToRightDis = tempDis;
                }

                //右居某左
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.RIGHT,findLayer,Gravity.LEFT);
                if (rightToLeftLayer==null) {
                    rightToLeftLayer = findLayer;
                    rightToLeftDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(rightToLeftDis[0])) {
                    rightToLeftLayer = findLayer;
                    rightToLeftDis = tempDis;
                }

                //上居某上
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.TOP,findLayer,Gravity.TOP);
                if (topToTopLayer==null) {
                    topToTopLayer = findLayer;
                    topToTopDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(topToTopDis[0])) {
                    topToTopLayer = findLayer;
                    topToTopDis = tempDis;
                }

                //上居某下
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.TOP,findLayer,Gravity.BOTTOM);
                if (topToBottomLayer==null) {
                    topToBottomLayer = findLayer;
                    topToBottomDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(topToBottomDis[0])) {
                    topToBottomLayer = findLayer;
                    topToBottomDis = tempDis;
                }

                //下居某下
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.BOTTOM,findLayer,Gravity.BOTTOM);
                if (bottomToBottomLayer==null) {
                    bottomToBottomLayer = findLayer;
                    bottomToBottomDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(bottomToBottomDis[0])) {
                    bottomToBottomLayer = findLayer;
                    bottomToBottomDis = tempDis;
                }

                //下居某上
                tempDis = DisUtil.checkDisDirection(rootLayer,tagLayer,Gravity.BOTTOM,findLayer,Gravity.TOP);
                if (bottomToTopLayer==null) {
                    bottomToTopLayer = findLayer;
                    bottomToTopDis = tempDis;
                } else if (Math.abs(tempDis[0]) < Math.abs(bottomToTopDis[0])) {
                    bottomToTopLayer = findLayer;
                    bottomToTopDis = tempDis;
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


            BoundTag boundTag = new BoundTag();
            boundTag.source = tagLayer;
            boundTag.outLayer = outerLayer;

            boundTag.leftToLeftLayer = leftToLeftLayer;
            boundTag.leftToLeftDis = leftToLeftDis[1];

            boundTag.leftToRightLayer = leftToRightLayer;
            boundTag.leftToRightDis = leftToRightDis[1];

            boundTag.rightToRightLayer = rightToRightLayer;
            boundTag.rightToRightDis = rightToRightDis[1];

            boundTag.rightToLeftLayer = rightToLeftLayer;
            boundTag.rightToLeftDis = rightToLeftDis[1];

            boundTag.topToTopLayer = topToTopLayer;
            boundTag.topToTopDis = topToTopDis[1];

            boundTag.topToBottomLayer = topToBottomLayer;
            boundTag.topToBottomDis = topToBottomDis[1];

            boundTag.bottomToBottomLayer = bottomToBottomLayer;
            boundTag.bottomToBottomDis = bottomToBottomDis[1];

            boundTag.bottomToTopLayer = bottomToTopLayer;
            boundTag.bottomToTopDis = bottomToTopDis[1];

            //左右最小距离的差值
            double[] leftRightDis = new double[2];
            leftRightDis[0] = Math.min(Math.abs(leftToLeftDis[0]),Math.abs(leftToRightDis[0]))-Math.min(Math.abs(rightToRightDis[0]),Math.abs(rightToLeftDis[0]));
            leftRightDis[1] = Math.min(Math.abs(leftToLeftDis[1]),Math.abs(leftToRightDis[1]))-Math.min(Math.abs(rightToRightDis[1]),Math.abs(rightToLeftDis[1]));
            if (Math.abs(leftRightDis[1])<=acceptDeviation(artboards,tagLayer,Gravity.HORIZONTAL)) {
                boundTag.leftRightEq = true;
            } else if (leftRightDis[0]>0) {
                boundTag.rightMinThanLeft = true;
            } else {
                boundTag.leftMinThanRight = true;
            }

            double[] topBottomDis = new double[2];
            topBottomDis[0] = Math.min(Math.abs(topToTopDis[0]),Math.abs(topToBottomDis[0]))-Math.min(Math.abs(bottomToTopDis[0]),Math.abs(bottomToBottomDis[0]));
            topBottomDis[1] = Math.min(Math.abs(topToTopDis[1]),Math.abs(topToBottomDis[1]))-Math.min(Math.abs(bottomToTopDis[1]),Math.abs(bottomToBottomDis[1]));
            if (Math.abs(topBottomDis[1])<=acceptDeviation(artboards,tagLayer,Gravity.VERTICAL)) {
                boundTag.topBottomEq = true;
            } else if (topBottomDis[0]>0) {
                boundTag.bottomMinThanTop = true;
            } else {
                boundTag.topMinThanBottom = true;
            }

            //最后一步定义真正需要的依赖元素
            if (boundTag.leftRightEq) {
                if (Math.abs(leftToLeftDis[0])<=Math.abs(leftToRightDis[0])) {
                    boundTag.useLeftLayer = leftToLeftLayer;
                } else {
                    boundTag.useLeftLayer = rightToLeftLayer;
                }
                if (Math.abs(rightToRightDis[0])<=Math.abs(rightToLeftDis[0])) {
                    boundTag.useRightLayer = rightToRightLayer;
                } else {
                    boundTag.useRightLayer = rightToLeftLayer;
                }
            } else if (boundTag.leftMinThanRight) {
                //通过测试
                if (Math.abs(leftToLeftDis[0])<=Math.abs(leftToRightDis[0])) {
                    boundTag.useLeftLayer = leftToLeftLayer;
                } else {
                    boundTag.useLeftLayer = leftToRightLayer;
                }
            } else if (boundTag.rightMinThanLeft) {
                if (Math.abs(rightToRightDis[0])<=Math.abs(rightToLeftDis[0])) {
                    boundTag.useRightLayer = rightToRightLayer;
                } else {
                    boundTag.useRightLayer = rightToLeftLayer;
                }
            }

            if (boundTag.topBottomEq) {
                if (Math.abs(topToTopDis[0])<=Math.abs(topToBottomDis[0])) {
                    boundTag.useTopLayer = topToTopLayer;
                } else {
                    boundTag.useTopLayer = topToBottomLayer;
                }
                if (Math.abs(bottomToBottomDis[0])<=Math.abs(bottomToTopDis[0])) {
                    boundTag.useBottomLayer = bottomToBottomLayer;
                } else {
                    boundTag.useBottomLayer = bottomToTopLayer;
                }
            } else if (boundTag.topMinThanBottom) {
                if (Math.abs(topToTopDis[0])<=Math.abs(topToBottomDis[0])) {
                    boundTag.useTopLayer = topToTopLayer;
                } else {
                    boundTag.useTopLayer = topToBottomLayer;
                }
            } else {
                if (Math.abs(bottomToBottomDis[0])<=Math.abs(bottomToTopDis[0])) {
                    boundTag.useBottomLayer = bottomToBottomLayer;
                } else {
                    boundTag.useBottomLayer = bottomToTopLayer;
                }
            }

            boundTags.add(boundTag);
        }

        return boundTags;
    }

}
