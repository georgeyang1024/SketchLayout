package cn.georgeyang.intf;

import java.util.List;

import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.impl.DefaultBuildAlgorithm;
import cn.georgeyang.impl.DefaultLayerFilter;

/**
 * 通用布局构建接口
 * Created by george.yang on 18/1/21.
 */
public abstract class LayoutBuilder {
    private BuildAlgorithm buildAlgorithm;
    private LayerFilter layerFilter;

    public LayoutBuilder () {
        this(new DefaultBuildAlgorithm(),new DefaultLayerFilter());
    }

    /**
     * 构建方法，设置算法和过滤器
     * @param buildAlgorithm
     */
    public LayoutBuilder (BuildAlgorithm buildAlgorithm,LayerFilter layerFilter) {
        this.buildAlgorithm = buildAlgorithm;
        this.layerFilter = layerFilter;
    }


    public String buildLayout(StringBuffer stringBuffer, StArtboards artboards) {
        List<BoundResultTag> resultTagList = buildAlgorithm.build(artboards,layerFilter);
        stringBuffer.append(buildLayoutHeader(artboards,resultTagList));
        stringBuffer.append(buildLayoutBody(artboards,resultTagList));
        stringBuffer.append(buildLayoutFooter(artboards,resultTagList));
        return stringBuffer.toString();
    }

    public String buildLayout(StArtboards artboards) {
        return buildLayout(new StringBuffer(),artboards);
    }

    public abstract String buildLayoutHeader(StArtboards artboards, List<BoundResultTag> tagList);

    public abstract String buildLayoutFooter(StArtboards artboards, List<BoundResultTag> tagList);

    public String buildLayoutBody(StArtboards artboards, List<BoundResultTag> tagList) {
        if (tagList==null) return "";
        StringBuffer stringBuffer = new StringBuffer();
        for (BoundResultTag tag:tagList) {
            stringBuffer.append(buildViewLayout(artboards,tag.source.source,tag));
        }
        return stringBuffer.toString();
    }


    public String buildViewLayout(StringBuffer stringBuffer,StArtboards artboards,StLayer layer,BoundResultTag tag) {
        if (stringBuffer==null || tag == null) return "";
        stringBuffer.append(buildViewHeader(artboards,layer,tag));
        stringBuffer.append(buildViewId(artboards,layer,tag));
        stringBuffer.append(buildViewBackground(artboards,layer,tag));
        stringBuffer.append(buildViewSize(artboards,layer,tag));
        stringBuffer.append(buildViewBound(artboards,layer,tag));
        stringBuffer.append(buildViewBody(artboards,layer,tag));
        stringBuffer.append(buildViewFooter(artboards,layer,tag));
        return stringBuffer.toString();
    }


    public String buildViewLayout(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        return buildViewLayout(new StringBuffer(),artboards,layer,tag);
    }

    /**
     * 构建view的尺寸
     * @param artboards
     * @param layer
     * @param tag
     * @return
     */
    public abstract String buildViewSize(StArtboards artboards, StLayer layer, BoundResultTag tag);

    /**
     * 构建view的头部
     * @param artboards
     * @param layer
     * @param tag
     * @return
     */
    public abstract String buildViewHeader(StArtboards artboards,StLayer layer,BoundResultTag tag);

    public abstract String buildViewId(StArtboards artboards,StLayer layer,BoundResultTag tag);

    public abstract String buildViewBody(StArtboards artboards,StLayer layer,BoundResultTag tag);

    public abstract String buildViewBackground(StArtboards artboards,StLayer layer,BoundResultTag tag);


    /**
     * 构建view的尾部
     * @param artboards
     * @param layer
     * @param tag
     * @return
     */
    public abstract String buildViewFooter(StArtboards artboards, StLayer layer, BoundResultTag tag);


    /**
     * 构建view的边界
     * @param artboards
     * @param layer
     * @param tag
     * @return
     */
    public abstract String buildViewBound(StArtboards artboards,StLayer layer,BoundResultTag tag);


    /**
     * 统一的单位转化
     * @param artboards
     * @param layer
     * @param tag
     * @param px
     * @return
     */
    public abstract String unitConversion(StArtboards artboards, StLayer layer, BoundResultTag tag, double px);
}
