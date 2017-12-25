package com.lvshou.sketchlayout;

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
    public static boolean filter(StArtboards artboards,StLayer layer) {
        if (layer.rect.x + layer.rect.width>artboards.width) {//超出画布
            return true;
        }
        if (layer.rect.y + layer.rect.height>artboards.height) {//超出画布
            return true;
        }
        return false;
    }
}
