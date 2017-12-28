package com.lvshou.sketchlayout;


/**
 * Created by george.yang on 17/12/22.
 */

public class LayoutTag {
    public StLayer source;//自身资源
    public StLayer outLayer;//外围参考的元素

    //第一次寻找到最近的元素
    public StLayer leftLayer;//layout_constraintLeft_toLeftOf对应的id,如果0，则表示父类
    public StLayer rightLayer,topLayer,bottomLayer;
    public double leftDis,rightDis,topDis,bottomDis;

    //转成布局字符串前，对比父类外围元素，找到各个位置的最佳参考元素
    public StLayer betterLeftLayer,betterRightLayer,betterTopLayer,betterBottomLayer;
    public double betterLeftDis,betterRightDis,betterTopDis,betterBottomDis;

    @Override
    public String toString() {
        return "LayoutTag{" +
                "source=" + source +
                ", outLayer=" + outLayer +
                ", leftLayer=" + leftLayer +
                ", rightLayer=" + rightLayer +
                ", topLayer=" + topLayer +
                ", bottomLayer=" + bottomLayer +
                ", leftDis=" + leftDis +
                ", rightDis=" + rightDis +
                ", topDis=" + topDis +
                ", bottomDis=" + bottomDis +
                ", betterLeftLayer=" + betterLeftLayer +
                ", betterRightLayer=" + betterRightLayer +
                ", betterTopLayer=" + betterTopLayer +
                ", betterBottomLayer=" + betterBottomLayer +
                ", betterLeftDis=" + betterLeftDis +
                ", betterRightDis=" + betterRightDis +
                ", betterTopDis=" + betterTopDis +
                ", betterBottomDis=" + betterBottomDis +
                '}';
    }
}
