package com.ryel.springBootJpa.utils;



import com.ryel.springBootJpa.annotations.MustConvert;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by wangbin on 14-11-23.
 */
public class ClassUtil {


    public static <T> Boolean objInArray(T obj, List<T> list) {
        if (obj == null || list == null || list.size() == 0) {
            return false;
        }
        try {
            Class objClazz = obj.getClass();
            Field objField = objClazz.getDeclaredField("id");
            objField.setAccessible(true);
            for (T item : list) {
                Class itemClazz = item.getClass();
                Field itemIdField = itemClazz.getDeclaredField("id");
                itemIdField.setAccessible(true);
                if (objField.get(obj).equals(itemIdField.get(item))) {
                    return true;
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static <E> E copyProperties(E dest,E orig ) {

        try {
            Field[] fields = orig.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object value = parGetVal(fieldName, orig);
                if (field.isAnnotationPresent(MustConvert.class)) {
                    parSetVal(fieldName, dest, value);
                }else{
                    if(value!=null){
                        parSetVal(fieldName, dest, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dest;
    }


    /**
     * 通过get方法获取属性值
     */
    public static Object parGetVal(String fieldName, Object obj) {

        Object fieldVal = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
            Method method = pd.getReadMethod();
            fieldVal = method.invoke(obj);
        } catch (Exception e) {
            return null;
        }
        return fieldVal;
    }

    /**
     * 通过Set方法赋属性值
     */
    public static void parSetVal(String fieldName, Object obj, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
            Method method = pd.getWriteMethod();
            method.invoke(obj, value);
        } catch (Exception e) {

        }

    }


    public static String getFirstLowerStr(String entityName) {

        char[] chars = new char[1];

        chars[0] = entityName.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            return entityName.replaceFirst(temp, temp.toLowerCase());
        }

        return entityName;
    }

}
