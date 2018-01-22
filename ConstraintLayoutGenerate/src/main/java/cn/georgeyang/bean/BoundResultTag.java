package cn.georgeyang.bean;

/**
 * 筛选后的最佳边界
 * Created by george.yang on 18/1/21.
 */

public class BoundResultTag {
    public BoundTag source;//来源

    public StLayer leftToRightLayer;
    public StLayer leftToLeftLayer;
    public StLayer rightToLeftLayer;
    public StLayer rightToRightLayer;

    public StLayer topToTopLayer;
    public StLayer topToBottomLayer;
    public StLayer bottomToTopLayer;
    public StLayer bottomToBottomLayer;

    public double marginLeft = 0,marginRight = 0,marginTop = 0,marginBottom = 0;


    public void clearBound () {
        leftToLeftLayer = null;
        leftToRightLayer = null;
        rightToRightLayer = null;
        rightToLeftLayer = null;
        topToTopLayer = null;
        topToBottomLayer = null;
        bottomToBottomLayer = null;
        bottomToTopLayer = null;
        marginLeft = 0;
        marginRight = 0;
        marginTop = 0;
        marginBottom = 0;
    }
}
