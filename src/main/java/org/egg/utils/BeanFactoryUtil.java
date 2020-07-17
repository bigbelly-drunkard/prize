package org.egg.utils;

import java.lang.reflect.Constructor;

/**
 * @author dataochen
 * @Description
 * @date: 2020/3/6 14:17
 */
public class BeanFactoryUtil {

    public static Object generate(Class tClass) {
        Object o = null;
        try {
            Constructor constructor = tClass.getConstructor();
            o = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

}
