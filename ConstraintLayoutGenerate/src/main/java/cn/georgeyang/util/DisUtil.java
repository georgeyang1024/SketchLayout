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


    public static final double checkDisDirection(StLayer tagLayer,int tagGravity,StLayer sourceLayer,int sourceGravity) {
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

    public static final boolean isInner(StLayer sourceLayer,StLayer tagLayer) {
        return tagLayer.rect.x < sourceLayer.rect.x
                && tagLayer.rect.x + tagLayer.rect.width > sourceLayer.rect.x + sourceLayer.rect.width
                && tagLayer.rect.y < sourceLayer.rect.y
                && tagLayer.rect.y + tagLayer.rect.height > sourceLayer.rect.y + sourceLayer.rect.height;
    }

}
