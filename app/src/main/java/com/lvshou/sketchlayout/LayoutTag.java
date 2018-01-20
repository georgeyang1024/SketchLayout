package com.lvshou.sketchlayout;


/**
 * Created by george.yang on 17/12/22.
 */

public class LayoutTag {
    public StLayer source;//自身资源
    public StLayer outLayer;//外围参考的元素


    public StLayer leftToRightLayer;//当前矩形，最近那个矩形的右边
    public StLayer leftToLeftLayer;
    public StLayer rightToLeftLayer;
    public StLayer rightToRightLayer;

    public StLayer topToTopLayer;
    public StLayer topToBottomLayer;
    public StLayer bottomToTopLayer;
    public StLayer bottomToBottomLayer;

    public double leftToLeftDis = 0,leftToRightDis = 0,rightToRightDis = 0,rightToLeftDis = 0;
    public double topToTopDis = 0,topToBottomDis = 0,bottomToBottomDis = 0,bottomToTopDis = 0;

    public boolean leftMinThanRight;
    public boolean leftRightEq;
    public boolean rightMinThanLeft;

    public boolean topMinThanBottom;
    public boolean topBottomEq;
    public boolean bottomMinThanTop;

    public StLayer useLeftLayer,useRightLayer,useTopLayer,useBottomLayer;

    @Override
    public String toString() {
        return "LayoutTag{" +
                "source=" + source +
                ", outLayer=" + outLayer +
                ", leftToLeftLayer=" + leftToLeftLayer +
                ", leftToRightLayer=" + leftToRightLayer +
                ", rightToRightLayer=" + rightToRightLayer +
                ", rightToLeftLayer=" + rightToLeftLayer +
                ", topToTopLayer=" + topToTopLayer +
                ", topToBottomLayer=" + topToBottomLayer +
                ", bottomToBottomLayer=" + bottomToBottomLayer +
                ", bottomToTopLayer=" + bottomToTopLayer +
                ", leftToLeftDis=" + leftToLeftDis +
                ", leftToRightDis=" + leftToRightDis +
                ", rightToRightDis=" + rightToRightDis +
                ", rightToLeftDis=" + rightToLeftDis +
                ", topToTopDis=" + topToTopDis +
                ", topToBottomDis=" + topToBottomDis +
                ", bottomToBottomDis=" + bottomToBottomDis +
                ", bottomToTopDis=" + bottomToTopDis +
                ", leftMinThanRight=" + leftMinThanRight +
                ", leftRightEq=" + leftRightEq +
                ", rightMinThanLeft=" + rightMinThanLeft +
                ", topMinThanBottom=" + topMinThanBottom +
                ", topBottomEq=" + topBottomEq +
                ", bottomMinThanTop=" + bottomMinThanTop +
                ", useLeftLayer=" + useLeftLayer +
                ", useRightLayer=" + useRightLayer +
                ", useTopLayer=" + useTopLayer +
                ", useBottomLayer=" + useBottomLayer +
                '}';
    }
}
