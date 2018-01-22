package cn.georgeyang.impl;


import cn.georgeyang.bean.BoundDepend;
import cn.georgeyang.bean.BoundTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.intf.LayerFilter;
import cn.georgeyang.util.TextUtils;


public class DefaultLayerFilter implements LayerFilter {

    /**
     * 是否需要过滤？
     * @param artboards
     * @param layer
     * @return
     */
    @Override
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
        }
        return false;
    }

}
