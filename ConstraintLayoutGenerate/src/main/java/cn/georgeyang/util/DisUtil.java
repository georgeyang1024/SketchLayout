package cn.georgeyang.util;

import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;
import cn.georgeyang.conf.Gravity;

/**
 * 距离计算工具
 * Created by georgeyang1024 on 2017/12/25.
 */

public class DisUtil {
    /**
     * 检测一个元素，离坐标原点、屏幕最远点的总距离
     * @return
     * done
     */
    public static final double checkZeroEndDis(StArtboards artboards, StLayer stLayer) {
        if (artboards==null || stLayer==null) {
            return Double.MAX_VALUE;
        }
        StRect rect = stLayer.rect;
        double zeroDis = Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2));
        double endDis = Math.sqrt(Math.pow( rect.x + rect.width - artboards.width,2) + Math.pow(rect.y + rect.height - artboards.height,2));
        double totalDis = zeroDis + endDis;
        return totalDis;
    }

    /**
     * 两个元素的距离
     * @param tagLayer
     * @param tagGravity
     * @param sourceLayer
     * @param sourceGravity
     * @return [0]布局距离 [1]真实距离
     */
    public static final double[] checkDisDirection(StLayer parent,StLayer tagLayer,int tagGravity,StLayer sourceLayer,int sourceGravity) {
        double[] ret = new double[] {Double.MAX_VALUE,Double.MAX_VALUE};
        if (sourceLayer == null || tagLayer == null) return ret;
        double dis = checkDisDirection(tagLayer.rect,tagGravity,sourceLayer.rect,sourceGravity);
        double aDis = dis;
//        if (parent == sourceLayer && tagGravity==Gravity.BOTTOM && sourceGravity==Gravity.BOTTOM) {
//            //如果元素参考画布底部，底部距离*4倍(因为手机端，宽度和长度会变)
//            aDis = dis * 4;
//        }
        ret[0] = aDis;
        ret[1] = dis;
        return ret;
    }

    /**
     * 真实的距离
     * @param tagLayer
     * @param tagGravity
     * @param sourceLayer
     * @param sourceGravity
     * @return
     */
    public static final double checkRealDisDirection(StLayer tagLayer,int tagGravity,StLayer sourceLayer,int sourceGravity) {
        if (sourceLayer == null || tagLayer == null) return Double.MAX_VALUE;
        return checkDisDirection(tagLayer.rect,tagGravity,sourceLayer.rect,sourceGravity);
    }

    /**
     * 检测两矩形在某一方向对应边的距离
     * @param tagRect 布局的目标
     * @param sourceRect 布局的参考资源
     * @param sourceGravity
     * @param tagGravity
     * @return
     */
    public static final double checkDisDirection(StRect tagRect,int tagGravity,StRect sourceRect,int sourceGravity) {
        double ret = Double.MAX_VALUE;
        if (tagGravity == Gravity.LEFT && sourceGravity == Gravity.LEFT) {
            ret = tagRect.x - sourceRect.x;
        }
        if (tagGravity == Gravity.LEFT && sourceGravity == Gravity.RIGHT) {
            ret = tagRect.x - sourceRect.x - sourceRect.width;
        }
        if (tagGravity==Gravity.RIGHT && sourceGravity == Gravity.RIGHT) {
            //反方向
            ret = sourceRect.x + sourceRect.width - (tagRect.x + tagRect.width);
        }
        if (tagGravity==Gravity.RIGHT && sourceGravity==Gravity.LEFT) {
            //反方向
            ret = sourceRect.x - (tagRect.x + tagRect.width);
        }
        if (tagGravity==Gravity.TOP && sourceGravity==Gravity.TOP) {
            ret = tagRect.y - sourceRect.y;
        }
        if (tagGravity==Gravity.TOP && sourceGravity==Gravity.BOTTOM) {
            ret = tagRect.y - sourceRect.y - sourceRect.height;
        }
        if (tagGravity==Gravity.BOTTOM && sourceGravity==Gravity.BOTTOM) {
            //反方向
            ret = sourceRect.y + sourceRect.height - (tagRect.y + tagRect.height);
        }
        if (tagGravity==Gravity.BOTTOM && sourceGravity==Gravity.TOP) {
            //反方向
            ret = sourceRect.y - (tagRect.y + tagRect.height);
        }
        //不允许出现负值
        if (ret < 0) {
            ret = Double.MAX_VALUE;
        }
        return ret;
    }

    public static final boolean isInner(StLayer sourceLayer,StLayer outerLayer) {
        return isInner(sourceLayer.rect,outerLayer.rect,0);
    }

    public static final boolean isInner(StLayer sourceLayer,StLayer outerLayer,double acceptDeviation) {
        return isInner(sourceLayer.rect,outerLayer.rect,acceptDeviation);
    }

    public static final boolean isInner(StRect sourceRect,StRect outerRect) {
        return isInner(sourceRect,outerRect,0);
    }
    public static final boolean isInner(StRect sourceRect,StRect outerRect,double acceptDeviation) {
        return outerRect.x - acceptDeviation / 2 <= sourceRect.x
                && outerRect.x + outerRect.width + acceptDeviation / 2 >= sourceRect.x + sourceRect.width
                && outerRect.y - acceptDeviation / 2 <= sourceRect.y
                && outerRect.y + outerRect.height + acceptDeviation / 2 >= sourceRect.y + sourceRect.height;
    }

}
