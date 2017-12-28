package com.lvshou.sketchlayout;

import android.view.Gravity;

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
    public static final double checkZeroEndDis(StArtboards artboards,StLayer stLayer) {
        if (artboards==null || stLayer==null) {
            return Double.MAX_VALUE;
        }
//        Log.d("test","checkZeroEndDis!!");
//        Log.d("test","stLayer:"+ stLayer);
        StRect rect = stLayer.rect;
        double zeroDis = Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2));
        double endDis = Math.sqrt(Math.pow( rect.x + rect.width - artboards.width,2) + Math.pow(rect.y + rect.height - artboards.height,2));
//        Log.d("test","zeroDis:"+ zeroDis);
//        Log.d("test","endDis:"+ endDis);
        double totalDis = zeroDis + endDis;
//        Log.d("test","totalDis:"+ totalDis);
//        stLayer.totalDis = totalDis;
        return totalDis;
    }

    /**
     * 计算两个元素在某个方向的距离
     * @param sourceLayer
     * @param tagLayer
     * @param gravity
     * @return
     */
    public static final double checkDisDirection(StLayer sourceLayer,StLayer tagLayer,int gravity) {
        if (sourceLayer == null || tagLayer == null) return Double.MAX_VALUE;
        return checkDisDirection(sourceLayer.rect,tagLayer.rect,gravity);
    }

    /**
     * 计算两点距离，带方向
     * @param sourceRect
     * @param tagRect
     * @param gravity
     * @return
     */
    public static final double checkDisDirection(StRect sourceRect,StRect tagRect,int gravity) {
        if (sourceRect == null || tagRect == null) return Double.MAX_VALUE;
        if (gravity== Gravity.LEFT) {
            return tagRect.x - sourceRect.x;
        }
        if (gravity==Gravity.RIGHT) {
            return tagRect.x + tagRect.width - sourceRect.x - sourceRect.width;
        }
        if (gravity==Gravity.TOP) {
            return tagRect.y - sourceRect.y;
        }
        if (gravity==Gravity.BOTTOM) {
            return tagRect.y + tagRect.height - sourceRect.y - sourceRect.height;
        }
        return Double.MAX_VALUE;
    }

    public static final boolean isInner(StLayer sourceLayer,StLayer tagLayer) {
        return tagLayer.rect.x < sourceLayer.rect.x
                && tagLayer.rect.x + tagLayer.rect.width > sourceLayer.rect.x + sourceLayer.rect.width
                && tagLayer.rect.y < sourceLayer.rect.y
                && tagLayer.rect.y + tagLayer.rect.height > sourceLayer.rect.y + sourceLayer.rect.height;
    }

}
