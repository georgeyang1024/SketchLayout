package cn.georgeyang.intf;

import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;

/**
 * 布局过滤器
 */
public interface LayerFilter {
    boolean filterLayer(StArtboards artboards, StLayer layer);
    boolean filterReference(StArtboards stArtboards,StLayer dealLayer,StLayer referenceLayer);
}
