package cn.georgeyang.bean;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */

public class StRect {
    public StRect () {}
    public StRect (StArtboards stArtboards) {
        this.x = 0;
        this.y = 0;
        this.width = stArtboards.width;
        this.height = stArtboards.height;
    }

    public float x,y,width,height;
}
