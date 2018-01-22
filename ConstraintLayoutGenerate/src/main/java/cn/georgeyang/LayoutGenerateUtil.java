package cn.georgeyang;

import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.georgeyang.bean.StArtboards;
import cn.georgeyang.bean.StLayer;
import cn.georgeyang.impl.AndroidLayoutBuilder;
import cn.georgeyang.impl.DefaultBuildAlgorithm;
import cn.georgeyang.impl.DefaultLayerFilter;
import cn.georgeyang.util.DisUtil;
import cn.georgeyang.util.JsonReaderUtil;
import cn.georgeyang.util.LayerFilterUtil;
import cn.georgeyang.util.TextUtils;

/**
 *
 */
public class LayoutGenerateUtil {

    public static void main(String[] args) {
        int showIndex = 4;//登录页
        generate(showIndex);
    }

    public static void generate(int pageIndex) {
        try {
            //3.0设计/登录注册/index.html#artboard9
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream("test.json"),"utf-8");
            JsonReader reader = new JsonReader(inputReader);
            reader.beginObject();
            while(reader.hasNext()){
                String keyName = reader.nextName();
                System.out.println(keyName);
                if (TextUtils.equals(keyName,"artboards")) {
                    reader.beginArray();
                    int index = 0;
                    while (reader.hasNext()) {
                        reader.beginObject();
                        if (index==pageIndex) {
                            StArtboards stArtboards = JsonReaderUtil.paresObject(reader, StArtboards.class);
                            System.out.println("name:" + stArtboards.name);
                            if (stArtboards.height*1f/stArtboards.width<=16f/9) {
                                //大于6比9，是很长的图，不适合一屏展示，从上面开始布局

                            } else {
                                //一屏幕能展示完，边缘开始布局
                                generate(stArtboards);
                            }
                        }
                        while (reader.hasNext()) {
                            reader.nextName();
                            reader.skipValue();
                        }
                        reader.endObject();
                        index++;
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generate(StArtboards artboards) throws Exception {
        String string = new AndroidLayoutBuilder(new DefaultBuildAlgorithm(),new DefaultLayerFilter()).buildLayout(artboards);
        FileWriter writer=new FileWriter("result.xml");
        writer.write(string);
        writer.close();

    }




}
