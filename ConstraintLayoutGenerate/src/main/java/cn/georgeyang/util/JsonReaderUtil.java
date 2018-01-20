package cn.georgeyang.util;


import com.google.gson.stream.JsonReader;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeyang1024 on 2017/12/22.
 */

public class JsonReaderUtil  {
    public static <T> T paresObject (JsonReader reader, Class<T> clazz) {

        try {
            T ret = clazz.newInstance();
            while (reader.hasNext()) {
                String name = reader.nextName();
                name = name == null?"" : name.replace("-","_");
//                Log.d("test","name:" + name);
                Field[] fields = clazz.getDeclaredFields();
                boolean find = false;
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (TextUtils.equals(name, field.getName())) {
                        Class fClazz = field.getType();
//                        Log.d("test", "fClazz:" + fClazz);
                        find = true;
                        if (fClazz.equals(Integer.class) || fClazz.equals(int.class)) {
                            field.set(ret, reader.nextInt());
                        } else if (fClazz.equals(Double.class) || fClazz.equals(double.class)) {
                            field.set(ret, reader.nextDouble());
                        } else if (fClazz.equals(Boolean.class) || fClazz.equals(boolean.class)) {
                            field.set(ret, reader.nextBoolean());
                        } else if (fClazz.equals(Long.class) || fClazz.equals(long.class)) {
                            field.set(ret, reader.nextLong());
                        } else if (fClazz.equals(Float.class) || fClazz.equals(float.class)) {
                            field.set(ret, (float) reader.nextDouble());
                        } else if (fClazz.equals(String.class)) {
                            field.set(ret, reader.nextString());
                        } else if (fClazz.isAssignableFrom(List.class)) {
                            reader.beginArray();
                            field.set(ret, paresArray(reader, field));
                            reader.endArray();
                        } else {
                            reader.beginObject();
                            field.set(ret, paresObject(reader, fClazz));
                            reader.endObject();
                        }
                    }
                }
                if (!find) {
                    reader.skipValue();
                }
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List paresArray (JsonReader reader, Field f) {
        // 获取f字段的通用类型
        Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型
        // 如果不为空并且是泛型参数的类型
        if (fc != null && fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            Type[] types = pt.getActualTypeArguments();
            if (types != null && types.length > 0) {
                return paresArrayByTagClass(reader,(Class) types[0]);
            }
        }
        return null;
    }

    public static <T> List<T>  paresArrayByTagClass (JsonReader reader,List list, Class<T> tagClass) {
        try {
            while (reader.hasNext()) {
                if (tagClass.equals(String.class)) {
                    list.add(reader.nextString());
                } else {
                    reader.beginObject();
                    list.add(paresObject(reader,tagClass));
                    reader.endObject();
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> paresArrayByTagClass (JsonReader reader, Class<T> clazz) {
        return paresArrayByTagClass(reader,new ArrayList(),clazz);
    }
}
