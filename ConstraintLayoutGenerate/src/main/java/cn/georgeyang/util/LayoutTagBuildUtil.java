package cn.georgeyang.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.georgeyang.bean.LayoutTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;

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
            "        android:text=\"%s\"\n" +
            "        android:textColor=\"%s\"\n";

    private static String buildViewBackground(StArtboards artboards, StLayer layer, LayoutTag tag) {
        String color;
        if (layer==null || layer.borders==null || layer.borders.isEmpty() || layer.borders.get(0).color==null || TextUtils.isEmpty(layer.borders.get(0).color.argb_hex)) {
            color = "#" + Integer.toHexString(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        } else {
            color = layer.borders.get(0).color.argb_hex;
        }
        return String.format( "android:background=\"%s\"\n",color);
    }

//    private static String buildViewBound(StArtboards artboards,StLayer layer,LayoutTag tag) {
//        List list = new ArrayList<String>();
//        if (tag.betterLeftLayer!=null) {
//            if (tag.betterLeftDis==0) {
//                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.betterLeftLayer}));
//            } else if (tag.betterLeftDis<=0) {
//                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.betterLeftLayer}));
//                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.betterLeftDis + "px"}));
//            } else {
//                //1.4更正，未测试
//                list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.betterLeftLayer}));
//                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.betterLeftDis + "px"}));
//            }
//        }
//
//        if (tag.betterRightLayer!=null) {
//            if (tag.betterRightDis==0) {
//                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.betterRightLayer}));
//            } else if (tag.betterRightDis>=0) {
//                //1.4更正
//                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.betterRightLayer}));
//                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.betterRightDis + "px"}));
//            } else {
//                //1.4更正
//                list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.betterRightLayer}));
//                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.betterRightDis + "px"}));
//            }
//        }
//
//        if (tag.betterTopLayer!=null) {
//            if (tag.betterTopDis==0) {
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.betterTopLayer}));
//            } else if (tag.betterTopDis>=0) {
//                list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"", new Object[]{tag.betterTopLayer}));
//                list.add(String.format("android:layout_marginBottom=\"%s\"", new Object[]{(int) tag.betterTopDis + "px"}));
//            } else {
//                //1.4日更正
//                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.betterTopLayer}));
//                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) -tag.betterTopDis + "px"}));
//            }
//        }
//
//        if (tag.betterBottomLayer!=null) {
//            if (tag.betterBottomDis==0) {
//                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
//            } else if (tag.betterBottomDis>=0) {
//                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
//                list.add(String.format("android:layout_marginBottom=\"%s\"", new Object[]{(int) tag.betterBottomDis + "px"}));
//            } else {
//                list.add(String.format("app:layout_constraintBottom_toTopOf=\"%s\"", new Object[]{tag.betterBottomLayer}));
//                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.betterBottomDis + "px"}));
//            }
//        }


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
//        return TextUtils.join("\n",list.toArray(new String[list.size()]));
//    }

    private static String buildTextViewHeader(StArtboards artboards,StLayer layer,LayoutTag tag) {
        String id =  PinyinUtil.getPinyinName(layer.name);
        String color;
        if (layer==null || layer.color==null || TextUtils.isEmpty(layer.color.argb_hex)) {
            color = "#" + Integer.toHexString(Color.BLACK);
        } else {
            color = layer.color.argb_hex;
        }
        Object[] params = new Object[]{
                layer.getViewId(),
                (int)(layer.rect.width) + "px",
                (int)(layer.rect.height) + "px",
                TextUtils.isEmpty(layer.content)?id:layer.content,
                color
        };
//        if ("denglu".equalsIgnoreCase(layer.getViewId())) {
//            Log.d("test","c:" + color);
//        }
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


    private static String buildViewBound (StArtboards artboards,StLayer layer,LayoutTag tag) {
        List list = new ArrayList<String>();
        if (tag.leftRightEq) {
            if (tag.leftToLeftLayer!=tag.rightToRightLayer) {
                //测试完成
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToLeftDis + "px"}));
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));
            } else if (tag.leftToLeftLayer==tag.rightToRightLayer) {
                //测试完成
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
            } else if (tag.rightToLeftLayer != tag.leftToRightLayer) {
                //未测试
                list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.leftToRightDis + "px"}));
                //未测试
                list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));
            } else if (tag.rightToLeftLayer == tag.leftToRightLayer) {
                list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
                list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
            }
        } else if (tag.leftMinThanRight) {
            if (Math.abs(tag.leftToLeftDis) < Math.abs(tag.leftToRightDis)) {
                list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.useLeftLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToLeftDis + "px"}));
            } else {
                //已完成
                list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.useLeftLayer}));
                list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{(int) tag.leftToRightDis + "px"}));
            }
        } else if (tag.rightMinThanLeft) {
            //成功
            if (Math.abs(tag.rightToRightDis) < Math.abs(tag.rightToLeftDis)) {
                list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.useRightLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToRightDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.useRightLayer}));
                list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{(int) tag.rightToLeftDis + "px"}));
            }
        }

        if (tag.topBottomEq) {
            list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.topToTopLayer}));
            list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.bottomToBottomLayer}));
        } else if (tag.topMinThanBottom) {
            if (Math.abs(tag.topToTopDis) < Math.abs(tag.topToBottomDis)) {
                list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.useTopLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.topToTopDis + "px"}));
            } else {
                list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"", new Object[]{tag.useTopLayer}));
                list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{(int) tag.topToBottomDis + "px"}));
            }
        } else if (tag.bottomMinThanTop) {
            if (Math.abs(tag.bottomToBottomDis) < Math.abs(tag.bottomToTopDis)) {
                list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"",new Object[]{tag.useBottomLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)(tag.bottomToBottomDis) + "px"}));
            } else {
                list.add(String.format("app:layout_constraintBottom_toTopOf=\"%s\"",new Object[]{tag.useBottomLayer}));
                list.add(String.format("android:layout_marginBottom=\"%s\"\n",new Object[]{(int)(tag.bottomToTopDis) + "px"}));
            }
        }

        return TextUtils.join("\n",list);
    }


//    /**
//     * 二次定位，所有情况
//     * 通用定位
//     * @param artboards
//     * @param layer
//     * @param tag
//     */
//    private static void alignmentLayout(StArtboards artboards,StLayer rootLayer,StLayer layer,LayoutTag tag) {
//        //左右定位
//        if (tag.leftLayer==null && tag.rightLayer==null) {
//            Log.d("test","##0");
//            //左右都没找到最近元素，使用父类
//            double tempParentLeftDis = DisUtil.checkDisDirection(tag.source, rootLayer, Gravity.LEFT);
//            double tempParentRightDis = DisUtil.checkDisDirection(layer, rootLayer, Gravity.RIGHT);
//            if (Math.abs(Math.abs(tempParentLeftDis)- Math.abs(tempParentRightDis)) <= 2) {
//                //1.4更正
//                //左右距离一样，且，都是同一个元素，即是居中
//                tag.betterLeftLayer = rootLayer;
//                tag.betterRightLayer = rootLayer;
//            } else if (Math.abs(tempParentLeftDis) < Math.abs(tempParentRightDis)) {
//                tag.betterLeftLayer = rootLayer;
//                tag.betterLeftDis = tempParentLeftDis;
//            } else {
//                tag.betterRightLayer = rootLayer;
//                tag.betterRightDis = tempParentRightDis;
//            }
//        } else if (tag.leftLayer == tag.rightLayer && Math.abs(Math.abs(tag.leftDis)-Math.abs(tag.rightDis))<=2) {
//            Log.d("test","##1");
//            //左右距离一样，且，都是同一个元素，即是居中
//            tag.betterLeftLayer = tag.leftLayer;
//            tag.betterRightLayer = tag.rightLayer;
//        } else if (tag.leftLayer == tag.rightLayer) {
//            Log.d("test","##2");
//            //左右元素一样，但是不居中,选一个最佳
//            if (tag.outLayer==null) {
//                Log.d("test","##21");
//                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.leftLayer, Gravity.LEFT);
//                double tempRightDis = DisUtil.checkDisDirection(layer,tag.rightLayer, Gravity.RIGHT);
//                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
//                    tag.betterLeftLayer = tag.leftLayer;
//                    tag.betterLeftDis = tempLeftDis;
//                } else {
//                    tag.betterRightLayer = tag.rightLayer;
//                    tag.betterRightDis = tempRightDis;
//                }
//            } else {
//                Log.d("test","##22");
//                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
//                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
//                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
//                    tag.betterLeftLayer = tag.outLayer;
//                    tag.betterLeftDis = tempLeftDis;
//                } else {
//                    tag.betterRightLayer = tag.outLayer;
//                    tag.betterRightDis = tempRightDis;
//                }
//            }
//        } else if (tag.leftLayer != null && tag.rightLayer==null) {
//            Log.d("test","##3");
//            //左右只有一个左元素
//            if (tag.outLayer==null) {
//                double tempLeftDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.LEFT);
//                tag.betterLeftLayer = tag.outLayer;
//                tag.betterLeftDis = tempLeftDis;
//            } else {
//                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
//                if (Math.abs(tag.leftDis)<=Math.abs(tempLeftDis)) {
//                    tag.betterLeftLayer = tag.leftLayer;
//                    tag.betterLeftDis = tag.leftDis;
//                } else {
//                    tag.betterLeftLayer = tag.outLayer;
//                    tag.betterLeftDis = tempLeftDis;
//                }
//            }
//        } else if (tag.rightLayer != null && tag.leftLayer==null) {
//            Log.d("test","##4");
//            //左右只有一个右元素
//            if (tag.outLayer==null) {
//                double tempRightDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.RIGHT);
//                tag.betterRightLayer = tag.outLayer;
//                tag.betterRightDis = tempRightDis;
//            } else {
//                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
//                if (Math.abs(tag.rightDis)<=Math.abs(tempRightDis)) {
//                    tag.betterRightLayer = tag.rightLayer;
//                    tag.betterRightDis = tag.rightDis;
//                } else {
//                    tag.betterRightLayer = tag.outLayer;
//                    tag.betterRightDis = tempRightDis;
//                }
//            }
//        } else if (tag.leftLayer!=tag.rightLayer){
//            Log.d("test","##5");
//            //最后一种情况，左右均不为空，且不相同
//            //此时，如果有外围元素，参考外围元素最近的点，没有的话，取找到的最近的点
//            if (tag.outLayer==null) {
//                if (Math.abs(tag.leftDis)<=Math.abs(tag.rightDis)) {
//                    tag.betterLeftLayer = tag.leftLayer;
//                    tag.betterLeftDis = tag.leftDis;
//                } else {
//                    tag.betterRightLayer = tag.rightLayer;
//                    tag.betterRightDis = tag.rightDis;
//                }
//            } else {
//                double tempLeftDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.LEFT);
//                double tempRightDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.RIGHT);
//                if (Math.abs(tempLeftDis)<=Math.abs(tempRightDis)) {
//                    tag.betterLeftLayer = tag.outLayer;
//                    tag.betterLeftDis = tempLeftDis;
//                } else {
//                    tag.betterRightLayer = tag.outLayer;
//                    tag.betterRightDis = tempRightDis;
//                }
//            }
//        } else {
//            Log.d("test","error");
//        }
//
//
//        //上下定位
//        if (tag.topLayer==null && tag.bottomLayer==null) {
//            //左右都没找到最近元素，使用父类
//            double tempParentTopDis = DisUtil.checkDisDirection(tag.source, rootLayer, Gravity.TOP);
//            double tempParentBottomDis = DisUtil.checkDisDirection(layer, rootLayer, Gravity.BOTTOM);
//            if (Math.abs(tempParentTopDis) <= Math.abs(tempParentBottomDis)) {
//                tag.betterTopLayer = rootLayer;
//                tag.betterTopDis = tempParentTopDis;
//            } else {
//                tag.betterBottomLayer = rootLayer;
//                tag.betterBottomDis = tempParentBottomDis;
//            }
//        } else if (tag.topLayer == tag.bottomLayer && Math.abs(tag.topDis-tag.bottomDis)<=2) {
//            //上下距离一样，且，都是同一个元素，即是居中
//            tag.betterTopLayer = tag.topLayer;
//            tag.betterBottomLayer = tag.bottomLayer;
//        } else if (tag.topLayer == tag.bottomLayer) {
//            //左右元素一样，但是不居中,选一个最佳
//            if (tag.outLayer==null) {
//                double tempTopDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.TOP);
//                double tempBottomDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.BOTTOM);
//                if (Math.abs(tempTopDis) <= Math.abs(tempBottomDis)) {
//                    tag.betterTopLayer = rootLayer;
//                    tag.betterTopDis = tempTopDis;
//                } else {
//                    tag.betterBottomLayer = rootLayer;
//                    tag.betterBottomDis = tempBottomDis;
//                }
//            } else {
//                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
//                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
//                if (Math.abs(tempTopDis)<=Math.abs(tempBottomDis)) {
//                    tag.betterTopLayer = tag.outLayer;
//                    tag.betterTopDis = tempTopDis;
//                } else {
//                    tag.betterBottomLayer = tag.outLayer;
//                    tag.betterBottomDis = tempBottomDis;
//                }
//            }
//        } else if (tag.topLayer != null && tag.bottomLayer==null) {
//            //左右只有一个左元素
//            if (tag.outLayer==null) {
//                double tempTopDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.TOP);
//                tag.betterTopLayer = rootLayer;
//                tag.betterTopDis = tempTopDis;
//            } else {
//                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
//                if (Math.abs(tag.topDis)<=Math.abs(tempTopDis)) {
//                    tag.betterTopLayer = tag.topLayer;
//                    tag.betterTopDis = tag.topDis;
//                } else {
//                    tag.betterTopLayer = tag.outLayer;
//                    tag.betterTopDis = tempTopDis;
//                }
//            }
//        } else if (tag.bottomLayer != null && tag.topLayer==null) {
//            //左右只有一个右元素
//            if (tag.outLayer==null) {
//                double tempBottomDis = DisUtil.checkDisDirection(layer,rootLayer, Gravity.BOTTOM);
//                tag.betterBottomLayer = tag.outLayer;
//                tag.betterBottomDis = tempBottomDis;
//            } else {
//                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
//                if (Math.abs(tag.bottomDis)<=Math.abs(tempBottomDis)) {
//                    tag.betterBottomLayer = tag.bottomLayer;
//                    tag.betterBottomDis = tag.bottomDis;
//                } else {
//                    tag.betterBottomLayer = tag.outLayer;
//                    tag.betterBottomDis = tempBottomDis;
//                }
//            }
//        } else if (tag.topLayer!=tag.bottomLayer){
//            //最后一种情况，左右均不为空，且不相同
//            //此时，如果有外围元素，参考外围元素最近的点，没有的话，取找到的最近的点
//            if (tag.outLayer==null) {
//                if (Math.abs(tag.topDis)<=Math.abs(tag.bottomDis)) {
//                    tag.betterTopLayer = tag.topLayer;
//                    tag.betterTopDis = tag.topDis;
//                } else {
//                    tag.betterBottomLayer = tag.bottomLayer;
//                    tag.betterBottomDis = tag.bottomDis;
//                }
//            } else {
//                double tempTopDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.TOP);
//                double tempBottomDis = DisUtil.checkDisDirection(layer,tag.outLayer, Gravity.BOTTOM);
//                if (Math.abs(tempTopDis)<=Math.abs(tempBottomDis)) {
//                    tag.betterTopLayer = tag.outLayer;
//                    tag.betterTopDis = tempTopDis;
//                } else {
//                    tag.betterBottomLayer = tag.outLayer;
//                    tag.betterBottomDis = tempBottomDis;
//                }
//            }
//        } else {
//            Log.d("test","error");
//        }
//    }

    /**
     * 一次定位好LayoutTag位置后，调用此方法生成背景，写入文字之类的操作
     */
    public static String generatedLayout(StArtboards artboards,StLayer rootLayer,StLayer layer,LayoutTag tag) {
        System.out.println("====================generatedLayout====================");
        System.out.println("layer:" + layer);
        System.out.println("tag:" + tag);
//        alignmentLayout(artboards,rootLayer,layer,tag);
//        Log.d("test","alignmentLayout:"  + tag);
        String xml = buildView(artboards,layer,tag);
        System.out.println(xml);


        return xml;
    }
}
