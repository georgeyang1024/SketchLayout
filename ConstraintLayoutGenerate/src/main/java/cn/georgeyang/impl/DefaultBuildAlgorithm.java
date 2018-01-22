package cn.georgeyang.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.BoundTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;
import cn.georgeyang.conf.Gravity;
import cn.georgeyang.intf.BuildAlgorithm;
import cn.georgeyang.util.DisUtil;
import cn.georgeyang.util.PinyinUtil;
import cn.georgeyang.util.TextUtils;

/**
 * 默认的边界构建算法实现
 * Created by george.yang on 18/1/21.
 */
public class DefaultBuildAlgorithm implements BuildAlgorithm {
    @Override
    public List<BoundResultTag> buildBoundTag(StArtboards artboards) {
        List<StLayer> orderEffectList = filterLayer(artboards);
        orderFindList(artboards,orderEffectList);
        List<BoundTag> parseBoundTags = parseBoundTags(artboards,orderEffectList);
        return parseBoundTags(parseBoundTags);
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

    private ArrayList<StLayer> filterLayer(StArtboards artboards) {
        ArrayList<StLayer> layers = new ArrayList<>();
        for (StLayer layer:artboards.layers) {
            if (!filterLayer(artboards,layer)) {
                layers.add(layer);
            }
        }
        return layers;
    }


    /**
     * 是否需要过滤？
     * @param artboards
     * @param layer
     * @return
     */
    public boolean filterLayer(StArtboards artboards, StLayer layer) {
        if (layer==null || layer.rect == null) return true;
        if (layer.rect.x + layer.rect.width>artboards.width) {//超出画布
            return true;
        }
        if (layer.rect.y + layer.rect.height>artboards.height) {//超出画布
            return true;
        }
        if (layer.rect.y + layer.rect.height <= 128) {//标题部分
            return true;
        }
        return false;
    }

    /**
     * 过滤参考view
     * @param stArtboards 画板
     * @param referenceLayer 参考元素
     * @param dealLayer 处理的元素
     * @return
     */
    public boolean filterReference(StArtboards stArtboards,StLayer referenceLayer,StLayer dealLayer) {
        if (referenceLayer==null || dealLayer==null) {
            return true;
        }
        if (TextUtils.equals(referenceLayer.type,"text")) {
            return true;
        }
        return false;
    }


    private List<BoundResultTag> parseBoundTags(List<BoundTag> tagList) {
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

            list.add(resultTag);
        }

        return list;
    }
    /**
     * 定义位置
     * @param artboards
     * @param orderEffectList
     * @return
     * @throws Exception
     */
    private List<BoundTag> parseBoundTags(StArtboards artboards, List<StLayer> orderEffectList) {
        List<BoundTag> boundTags = new ArrayList<>();//已找到位置的Layer列表
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
            int findMinIndex = i - 6;//默认找前6个元素，如果中间有N个过滤元素，继续往前找N个，保证找到6个有效元素
            for (int j = i-1;j >= findMinIndex && j >= 0 && j < orderEffectList.size();j--) {//往外层搜最多5个
                StLayer findLayer = orderEffectList.get(j);//参考目标

                //跳过无效的参考目标
                if (filterReference(artboards,findLayer,tagLayer)) {
                    findMinIndex--;
                    continue;
                }


                //左居左
                tempDis = DisUtil.checkDisDirection(tagLayer, Gravity.LEFT,findLayer,Gravity.LEFT);
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

            BoundTag boundTag = new BoundTag();
            boundTag.source = tagLayer;
            boundTag.leftToLeftLayer = leftToLeftLayer;
            boundTag.leftToLeftDis = leftToLeftDis;

            boundTag.leftToRightLayer = leftToRightLayer;
            boundTag.leftToRightDis = leftToRightDis;

            boundTag.rightToRightLayer = rightToRightLayer;
            boundTag.rightToRightDis = rightToRightDis;

            boundTag.rightToLeftLayer = rightToLeftLayer;
            boundTag.rightToLeftDis = rightToLeftDis;

            boundTag.topToTopLayer = topToTopLayer;
            boundTag.topToTopDis = topToTopDis;

            boundTag.topToBottomLayer = topToBottomLayer;
            boundTag.topToBottomDis = topToBottomDis;

            boundTag.bottomToBottomLayer = bottomToBottomLayer;
            boundTag.bottomToBottomDis = bottomToBottomDis;

            boundTag.bottomToTopLayer = bottomToTopLayer;
            boundTag.bottomToTopDis = bottomToTopDis;

            boundTag.outLayer = outerLayer;

            //左右最小距离的差值
            double leftRightDis = Math.min(Math.abs(leftToLeftDis),Math.abs(leftToRightDis))-Math.min(Math.abs(rightToRightDis),Math.abs(rightToLeftDis));
            if (Math.abs(leftRightDis)<=2) {
                boundTag.leftRightEq = true;
            } else if (leftRightDis>0) {
                boundTag.rightMinThanLeft = true;
            } else {
                boundTag.leftMinThanRight = true;
            }

            double topBottomDis = Math.min(Math.abs(topToTopDis),Math.abs(topToBottomDis))-Math.min(Math.abs(bottomToTopDis),Math.abs(bottomToBottomDis));
            if (Math.abs(topBottomDis)<=2) {
                boundTag.topBottomEq = true;
            } else if (topBottomDis>0) {
                boundTag.bottomMinThanTop = true;
            } else {
                boundTag.topMinThanBottom = true;
            }

            //最后一步定义真正需要的依赖元素
            if (boundTag.leftRightEq) {
                if (Math.abs(leftToLeftDis)<=Math.abs(leftToRightDis)) {
                    boundTag.useLeftLayer = leftToLeftLayer;
                } else {
                    boundTag.useLeftLayer = rightToLeftLayer;
                }
                if (Math.abs(rightToRightDis)<=Math.abs(rightToLeftDis)) {
                    boundTag.useRightLayer = rightToRightLayer;
                } else {
                    boundTag.useRightLayer = rightToLeftLayer;
                }
            } else if (boundTag.leftMinThanRight) {
                //通过测试
                if (Math.abs(leftToLeftDis)<=Math.abs(leftToRightDis)) {
                    boundTag.useLeftLayer = leftToLeftLayer;
                } else {
                    boundTag.useLeftLayer = leftToRightLayer;
                }
            } else if (boundTag.rightMinThanLeft) {
                if (Math.abs(rightToRightDis)<=Math.abs(rightToLeftDis)) {
                    boundTag.useRightLayer = rightToRightLayer;
                } else {
                    boundTag.useRightLayer = rightToLeftLayer;
                }
            }

            if (boundTag.topBottomEq) {
                if (Math.abs(topToTopDis)<=Math.abs(topToBottomDis)) {
                    boundTag.useTopLayer = topToTopLayer;
                } else {
                    boundTag.useTopLayer = topToBottomLayer;
                }
                if (Math.abs(bottomToBottomDis)<=Math.abs(bottomToTopDis)) {
                    boundTag.useBottomLayer = bottomToBottomLayer;
                } else {
                    boundTag.useBottomLayer = bottomToTopLayer;
                }
            } else if (boundTag.topMinThanBottom) {
                if (Math.abs(topToTopDis)<=Math.abs(topToBottomDis)) {
                    boundTag.useTopLayer = topToTopLayer;
                } else {
                    boundTag.useTopLayer = topToBottomLayer;
                }
            } else {
                if (Math.abs(bottomToBottomDis)<=Math.abs(bottomToTopDis)) {
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
