package cn.georgeyang.impl;


import cn.georgeyang.bean.BoundDepend;
import cn.georgeyang.bean.BoundTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StBorders;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;
import cn.georgeyang.intf.LayerFilter;
import cn.georgeyang.util.DisUtil;
import cn.georgeyang.util.TextUtils;


public class DefaultLayerFilter implements LayerFilter {

    @Override
    public StRect effectBounds(StArtboards stArtboards,double acceptDeviation) {
        StRect rect =  new StRect(stArtboards);
        for(StLayer layer:stArtboards.layers)
        {
            if (TextUtils.equals(layer.name,"VIRGIN")) {
                if (TextUtils.equals(layer.type,"text")) {
                    //sim卡运营商标记
                    rect.y = Math.max(rect.y,128);
                }
            }
            //减少软键盘布局 高度
//            if (layer.name.startsWith("Keyboard")) {
//                if (TextUtils.equals(layer.type,"shape")) {
//                    rect.height = Math.min(rect.height,stArtboards.height-432);
//                }
//            }
        }
        return rect;
    }

    /**
     * 是否需要过滤？
     * @param artboards
     * @param layer
     * @return
     */
    @Override
    public boolean filterLayer(StArtboards artboards, StLayer layer,StRect effectBound,double acceptDeviation) {
        if (layer==null || layer.rect == null) return true;
        boolean ret = !DisUtil.isInner(layer.rect,effectBound,acceptDeviation);
//        System.out.println("filterLayer?" + layer.name + ">>" + ret);
//        if (layer.rect.x < effectBound.x) {
//            return true;
//        }
//        if (layer.rect.x + layer.rect.width>effectBound.width) {//超出画布
//            return true;
//        }
//        if (layer.rect.y + layer.rect.height>effectBound.height) {//超出画布
//            return true;
//        }
//        if (layer.rect.y + layer.rect.height <= 128) {//标题部分
//            return true;
//        }
//        return false;
        return ret;
    }

    /**
     * 过滤参考view
     * @param stArtboards 画板
     * @param referenceLayer 参考元素
     * @param dealLayer 处理的元素
     * @return
     */
    @Override
    public boolean filterReference(StArtboards stArtboards,StLayer dealLayer,StLayer referenceLayer) {
        if (referenceLayer==null || dealLayer==null) {
            return true;
        }
        if (TextUtils.equals(referenceLayer.type,"text")) {
            return true;
        }
        return false;
    }

    /**
     * 带依赖的过滤
     * @param artboards
     * @param boundTag
     * @return
     */
    @Override
    public boolean filterDependent(StArtboards artboards, BoundDepend boundTag) {
        if (boundTag==null || boundTag.source==null) return true;
        StLayer layer = boundTag.source;

        if (!(boundTag.innerList==null || boundTag.innerList.isEmpty())) {
            StLayer innerFirst = boundTag.innerList.get(0);
            if (TextUtils.equals("text",innerFirst.type) && !TextUtils.isEmpty(innerFirst.content)) {
                char c = innerFirst.content.charAt(0);
                if (c >= 'a' && c <= 'z' || c >='A' && c <= 'Z') {
                    if (boundTag.innerList.size()==1) {
                        return true;
                    }
                }
            }
        }
        if (!(boundTag.outerList==null || boundTag.outerList.isEmpty())) {
            if (TextUtils.equals("text",layer.type) && !TextUtils.isEmpty(layer.content)) {
                char c = layer.content.charAt(0);
                if (c >= 'a' && c <= 'z' || c >='A' && c <= 'Z') {
                    StLayer outterFirst = boundTag.outerList.get(0);
                    if (outterFirst.rect.height == 84 && outterFirst.rect.width == 63) {
                        return true;
                    }
                }
            }
            //如果外布局是键盘布局，排除键盘布局内部的内容
            for (StLayer stLayer:boundTag.outerList) {
                if (stLayer.name.startsWith("Keyboard")) {
                    if (TextUtils.equals(stLayer.type, "shape")) {
                        return true;
                    }
                }
            }


        }

        return false;
    }

}
