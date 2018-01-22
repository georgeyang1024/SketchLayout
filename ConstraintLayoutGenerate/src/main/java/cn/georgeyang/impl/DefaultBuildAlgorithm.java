package cn.georgeyang.impl;

import java.util.ArrayList;
import java.util.List;

import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.BoundTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;
import cn.georgeyang.conf.Gravity;
import cn.georgeyang.intf.BuildAlgorithm;
import cn.georgeyang.util.DisUtil;
import cn.georgeyang.util.LayerFilterUtil;
import cn.georgeyang.util.PinyinUtil;

/**
 * 默认的边界构建算法实现
 * Created by george.yang on 18/1/21.
 */
public class DefaultBuildAlgorithm implements BuildAlgorithm {
    @Override
    public List<BoundResultTag> buildBoundTag(StArtboards artboards, List<StLayer> orderEffectList) {
        List<BoundTag> parseBoundTags = parseBoundTags(artboards,orderEffectList);
        return parseBoundResultTags(artboards,parseBoundTags);
    }

    @Override
    public double acceptDeviation(StArtboards artboards, StLayer layer,int gravity) {
        return 2;
    }

    private List<BoundResultTag> parseBoundResultTags(StArtboards artboards,List<BoundTag> tagList) {
        List<BoundResultTag> list = new ArrayList<>();

        for (BoundTag tag:tagList) {
            BoundResultTag resultTag = new BoundResultTag();
            resultTag.source = tag;

            if (tag.leftRightEq) {
                if (tag.leftToLeftLayer != tag.rightToRightLayer) {
                    //测试完成

//                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToLeftDis + "px"}));
//                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));

                    resultTag.leftToLeftLayer = tag.leftToLeftLayer;
                    resultTag.marginLeft = tag.leftToLeftDis;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;
                    resultTag.marginRight = tag.rightToRightDis;

                } else if (tag.leftToLeftLayer == tag.rightToRightLayer) {
                    //测试完成
//                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
//                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));

                    resultTag.leftToLeftLayer = tag.leftToLeftLayer;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;

                } else if (tag.rightToLeftLayer != tag.leftToRightLayer) {
//                    //未测试
//                    list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.leftToRightDis + "px"}));
//                    //未测试
//                    list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));

                    resultTag.rightToLeftLayer = tag.leftToLeftLayer;
                    resultTag.marginRight = tag.leftToLeftDis;
                    resultTag.rightToRightLayer = tag.rightToRightLayer;
                    resultTag.marginLeft = tag.rightToRightDis;

                } else if (tag.rightToLeftLayer == tag.leftToRightLayer) {
//                    list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
//                    list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
                    resultTag.rightToLeftLayer = tag.leftToLeftLayer;
                    resultTag.leftToRightLayer = tag.rightToRightLayer;
                }
            } else if (tag.leftMinThanRight) {
                if (Math.abs(tag.leftToLeftDis) < Math.abs(tag.leftToRightDis)) {
//                    list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.useLeftLayer}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToLeftDis + "px"}));
                    resultTag.leftToLeftLayer = tag.useLeftLayer;
                    resultTag.marginLeft = tag.leftToLeftDis;
                } else {
                    //已完成
//                    list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.useLeftLayer}));
//                    list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToRightDis + "px"}));
                    resultTag.leftToRightLayer = tag.useLeftLayer;
                    resultTag.marginLeft = tag.leftToRightDis;
                }
            } else if (tag.rightMinThanLeft) {
                //成功
                if (Math.abs(tag.rightToRightDis) < Math.abs(tag.rightToLeftDis)) {
//                    list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.useRightLayer}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));

                    resultTag.rightToRightLayer = tag.useRightLayer;
                    resultTag.marginRight = tag.rightToRightDis;
                } else {
//                    list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.useRightLayer}));
//                    list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToLeftDis + "px"}));
                    resultTag.rightToLeftLayer = tag.useRightLayer;
                    resultTag.marginRight = tag.rightToLeftDis;
                }
            }

            if (tag.topBottomEq) {
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.topToTopLayer}));
//                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.bottomToBottomLayer}));
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
//                    list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.useTopLayer}));
//                    list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.topToTopDis + "px"}));
                    resultTag.topToTopLayer = tag.useTopLayer;
                    resultTag.marginTop = tag.topToTopDis;
                } else {
//                    list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"", new Object[]{tag.useTopLayer}));
//                    list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.topToBottomDis + "px"}));
                    resultTag.topToBottomLayer = tag.useTopLayer;
                    resultTag.marginTop = tag.topToBottomDis;
                }
            } else if (tag.bottomMinThanTop) {
                if (Math.abs(tag.bottomToBottomDis) < Math.abs(tag.bottomToTopDis)) {
//                    list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.useBottomLayer}));
//                    list.add(String.format("android:layout_marginBottom=\"%s\"\n", new Object[]{(int) (tag.bottomToBottomDis) + "px"}));
                    resultTag.bottomToBottomLayer = tag.useBottomLayer;
                    resultTag.marginBottom = tag.bottomToBottomDis;
                } else {
//                    list.add(String.format("app:layout_constraintBottom_toTopOf=\"%s\"", new Object[]{tag.useBottomLayer}));
//                    list.add(String.format("android:layout_marginBottom=\"%s\"\n", new Object[]{(int) (tag.bottomToTopDis) + "px"}));
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
                if (!tag.leftRightEq && (int)left >> 31 == (int)right >> 31) {//判断是否都是正数或者都是负数
                    if (Math.abs(Math.abs(left) - Math.abs(right)) <= acceptDeviation(artboards,tag.outLayer,Gravity.HORIZONTAL)) {
                        resultTag.clearBound();
                        resultTag.leftToLeftLayer = tag.outLayer;
                        resultTag.rightToRightLayer = tag.outLayer;
                    }
                }
                if (!tag.topBottomEq && (int)top >> 31 == (int)bottom >> 31) {//判断是否都是正数或者都是负数
                    if (Math.abs(Math.abs(top) - Math.abs(bottom)) <= acceptDeviation(artboards,tag.outLayer,Gravity.VERTICAL)) {
                        resultTag.clearBound();
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
    private List<BoundTag> parseBoundTags(StArtboards artboards, List<StLayer> orderEffectList) {
        List<BoundTag> boundTags = new ArrayList<>();//已找到位置的Layer列表
        List<String> names = new ArrayList<>();

        StLayer rootLayer = new StLayer();
        rootLayer.name = "parent";
        rootLayer.objectID = "parent";
        rootLayer.rect = new StRect();
        rootLayer.rect.x = 0;
        rootLayer.rect.y = 0;
        rootLayer.rect.width = artboards.width;
        rootLayer.rect.height = artboards.height;

        orderEffectList.add(0,rootLayer);

//        //找到外部元素
//        for (int i = 1; i < orderEffectList.size(); i++) {
//            StLayer tagLayer = orderEffectList.get(i);
//
//            BoundTag boundTag = new BoundTag();
//            boundTag.source = tagLayer;
//            boundTags.add(boundTag);
//
//            for (int j = i-1; j >= 0 && j < orderEffectList.size();j--) {
//                StLayer findLayer = orderEffectList.get(j);//参考目标
//                //跳过无效的参考目标
//                if (LayerFilterUtil.filterReference(artboards,findLayer,tagLayer)) {
//                    continue;
//                }
//
//                if (DisUtil.isInner(tagLayer,findLayer)) {
//                    boundTag.outLayer = findLayer;
//                    break;
//                }
//            }
//        }


        for (int i = 1; i < orderEffectList.size(); i++) {//从外往里循环找位置
            StLayer tagLayer = orderEffectList.get(i);
            StLayer leftToLeftLayer = null,leftToRightLayer = null,rightToRightLayer = null,rightToLeftLayer = null;
            StLayer topToTopLayer = null,topToBottomLayer = null,bottomToBottomLayer = null,bottomToTopLayer = null;
            StLayer outerLayer = null;
            double[] leftToLeftDis = null,leftToRightDis =null ,rightToRightDis =null,rightToLeftDis = null;
            double[] topToTopDis = null,topToBottomDis = null,bottomToBottomDis = null,bottomToTopDis = null;
            double[] tempDis;
//            int findMinIndex = i - 100;//默认找前6个元素，如果中间有N个过滤元素，继续往前找N个，保证找到6个有效元素
            int findMinIndex = 0;//往回找全部已知元素
            for (int j = i-1;j >= findMinIndex && j >= 0 && j < orderEffectList.size();j--) {//往外层搜最多5个
                StLayer findLayer = orderEffectList.get(j);//参考目标

                //跳过无效的参考目标
                if (LayerFilterUtil.filterReference(artboards,findLayer,tagLayer)) {
                    findMinIndex--;
                    continue;
                }

                //只找一个最近的外部元素
                if (outerLayer==null) {
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
