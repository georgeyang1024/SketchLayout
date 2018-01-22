package cn.georgeyang.intf;

import java.util.ArrayList;
import java.util.List;

import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;

/**
 * 边界构建算法
 * Created by george.yang on 18/1/21.
 */
public interface BuildAlgorithm {
    List<BoundResultTag> buildBoundTag(StArtboards artboards);
}
