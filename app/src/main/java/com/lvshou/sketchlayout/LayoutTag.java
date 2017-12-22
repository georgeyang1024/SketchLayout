package com.lvshou.sketchlayout;

/**
 * Created by george.yang on 17/12/22.
 */

public class LayoutTag {
    public String id;//生成规则 layers名+index
    public String viewName;//布局的类

    public StLayer leftLayer;//layout_constraintLeft_toLeftOf对应的id,如果0，则表示父类
    public StLayer rightLayer,topLayer,bottomLayer;

    public StLayer outLayer;//如果是嵌套在里面的话，它有个outer
    public StLayer source;//自身资源
}
