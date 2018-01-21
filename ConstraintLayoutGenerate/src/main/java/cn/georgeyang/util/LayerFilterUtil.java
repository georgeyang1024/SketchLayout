package cn.georgeyang.util;

import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;

/**
 * 过滤没用的Layer
 * Created by georgeyang1024 on 2017/12/25.
 */
public class LayerFilterUtil {
    /**
     * 是否需要过滤？
     * @param artboards
     * @param layer
     * @return
     */
    public static boolean filter(StArtboards artboards, StLayer layer) {
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
    public static boolean filterReference(StArtboards stArtboards,StLayer referenceLayer,StLayer dealLayer) {
        if (referenceLayer==null || dealLayer==null) {
            return true;
        }
        if (TextUtils.equals(referenceLayer.type,"text")) {
            return true;
        }
        return false;
    }
}
