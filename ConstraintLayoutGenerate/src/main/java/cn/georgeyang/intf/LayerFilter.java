package cn.georgeyang.intf;

import cn.georgeyang.bean.BoundDepend;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.bean.StRect;

/**
 * 布局过滤器
 */
public interface LayerFilter {
    /**
     * 第一步，获取有效区域
     * @param stArtboards
     * @return
     */
    StRect effectBounds(StArtboards stArtboards,double acceptDeviation);

    /**
     * 第二步
     * 根据有效距离,过滤无效的图层，单个元素的过滤
     * @param artboards
     * @param layer
     * @return
     */
    boolean filterLayer(StArtboards artboards, StLayer layer,StRect effectBound,double acceptDeviation);

    /**
     * 第三步
     * 位置信息确认好后，此时已有依赖，根据元素依赖再次过滤
     * @param artboards
     * @return
     */
    boolean filterDependent(StArtboards artboards, BoundDepend boundTag);

    /**
     * 第四步,处理的元素,和参考的元素，查看是否过滤
     * @param stArtboards
     * @param dealLayer
     * @param referenceLayer
     * @return
     */
    boolean filterReference(StArtboards stArtboards,StLayer dealLayer,StLayer referenceLayer);


}
