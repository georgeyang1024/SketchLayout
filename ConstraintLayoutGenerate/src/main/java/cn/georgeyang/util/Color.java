package cn.georgeyang.util;

/**
 * Created by georgeyang1024 on 2018/1/20.
 */

public class Color {
    public static final int BLACK = 0xff000000;

    public static final int argb(int a,int r,int g,int b) {
        a = (a<<24) & 0xFF000000;//
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
            g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
            b = b & 0x000000FF; //Mask out anything not blue.

            return a | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
