package cn.georgeyang.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.georgeyang.bean.BoundResultTag;
import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.intf.BuildAlgorithm;
import cn.georgeyang.intf.LayerFilter;
import cn.georgeyang.intf.LayoutBuilder;
import cn.georgeyang.util.Color;
import cn.georgeyang.util.TextUtils;

/**
 * android布局构建
 * Created by george.yang on 18/1/21.
 */

public class AndroidLayoutBuilder extends LayoutBuilder {
    public AndroidLayoutBuilder() {
        super();
    }

    public AndroidLayoutBuilder(BuildAlgorithm buildAlgorithm, LayerFilter layerFilter) {
        super(buildAlgorithm, layerFilter);
    }

    private Random random = new Random(System.currentTimeMillis());
    @Override
    public String buildLayoutHeader(StArtboards artboards, List<BoundResultTag> tagList) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<android.support.constraint.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "   xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                "   xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "   android:layout_width=\"match_parent\"\n" +
                "   android:layout_height=\"match_parent\"\n" +
                "   android:background=\"@color/colorAccent\"\n" +
                "   >\n";
    }

    @Override
    public String buildLayoutFooter(StArtboards artboards, List<BoundResultTag> tagList) {
        return "</android.support.constraint.ConstraintLayout>";
    }

    @Override
    public String buildViewSize(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        String sizeStr = "android:layout_width=\"%s\"\n" +
                "android:layout_height=\"%s\"\n";
        return String.format(sizeStr, new Object[]{
                unitConversion(artboards,layer,tag, layer.rect.width),
                        unitConversion(artboards,layer,tag, layer.rect.height)
        });
    }

    @Override
    public String buildViewBound(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        List<String> list = new ArrayList<>();
        if (tag.leftToLeftLayer!=null) {
            list.add(String.format("app:layout_constraintLeft_toLeftOf=\"%s\"", new Object[]{tag.leftToLeftLayer}));
        }
        if (tag.leftToRightLayer!=null) {
            list.add(String.format("app:layout_constraintLeft_toRightOf=\"%s\"", new Object[]{tag.leftToRightLayer}));
        }
        if (tag.rightToRightLayer!=null) {
            list.add(String.format("app:layout_constraintRight_toRightOf=\"%s\"", new Object[]{tag.rightToRightLayer}));
        }
        if (tag.rightToLeftLayer!=null) {
            list.add(String.format("app:layout_constraintRight_toLeftOf=\"%s\"", new Object[]{tag.rightToLeftLayer}));
        }
        if (tag.topToTopLayer!=null) {
            list.add(String.format("app:layout_constraintTop_toTopOf=\"%s\"", new Object[]{tag.topToTopLayer}));
        }
        if (tag.topToBottomLayer!=null) {
            list.add(String.format("app:layout_constraintTop_toBottomOf=\"%s\"", new Object[]{tag.topToBottomLayer}));
        }
        if (tag.bottomToBottomLayer!=null) {
            list.add(String.format("app:layout_constraintBottom_toBottomOf=\"%s\"", new Object[]{tag.bottomToBottomLayer}));
        }
        if (tag.bottomToTopLayer!=null) {
            list.add(String.format("app:layout_constraintBottom_toTopOf=\"%s\"", new Object[]{tag.bottomToTopLayer}));
        }
        if (tag.marginLeft!=0) {
            list.add(String.format("android:layout_marginLeft=\"%s\"", new Object[]{unitConversion(artboards,layer,tag,tag.marginLeft)}));
        }
        if (tag.marginRight!=0) {
            list.add(String.format("android:layout_marginRight=\"%s\"", new Object[]{unitConversion(artboards,layer,tag,tag.marginRight)}));
        }
        if (tag.marginTop!=0) {
            list.add(String.format("android:layout_marginTop=\"%s\"", new Object[]{unitConversion(artboards,layer,tag,tag.marginTop)}));
        }
        if (tag.marginBottom!=0) {
            list.add(String.format("android:layout_marginBottom=\"%s\"", new Object[]{unitConversion(artboards,layer,tag,tag.marginBottom)}));
        }
        return TextUtils.join("\n",list) + "\n";
    }

    @Override
    public String buildViewBackground(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        String color;
        if (layer==null || layer.borders==null || layer.borders.isEmpty() || layer.borders.get(0).color==null || TextUtils.isEmpty(layer.borders.get(0).color.argb_hex)) {
            color = "#" + Integer.toHexString(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        } else {
            color = layer.borders.get(0).color.argb_hex;
        }
        return String.format( "android:background=\"%s\"\n",color);
    }


    @Override
    public String buildViewHeader(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        if (TextUtils.equals("text",layer.type)) {
            return "<TextView\n";
        }
        return "<ImageView\n";
    }

    @Override
    public String buildViewId(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        return String.format("android:id=\"@+id/%s\"\n" ,layer.getViewId());
    }

    @Override
    public String buildViewBody(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        if (TextUtils.equals("text",layer.type)) {
            return String.format("android:text=\"%s\"\n" +
                    "android:textColor=\"%s\"\n",new Object[]{layer.content,layer.color.argb_hex});
        }
        return "\n";
    }

    @Override
    public String buildViewFooter(StArtboards artboards, StLayer layer, BoundResultTag tag) {
        if (TextUtils.equals("text",layer.type)) {
            return ">\n</TextView>\n";
        }
        return ">\n</ImageView>\n";
    }


    @Override
    public String unitConversion(StArtboards artboards, StLayer layer, BoundResultTag tag, double px) {
//        return (int) px + "px";
        if (Math.abs(px) < 1) {
            return "0dp";
        }
        return String.format("@dimen/x%d",(int)px);
    }
}
