package com.ryel.zaja.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryel.zaja.utils.gson.DmsExclusionStrategy;
import com.ryel.zaja.utils.gson.DmsIncludeStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Type;

/**
 * Created by wangbin on 14-10-17.
 */
public class JsonUtil {

    protected final static Log logger = LogFactory.getLog(JsonUtil.class);

    public static GsonBuilder createGsonBuilder(){
        return new GsonBuilder().serializeNulls().
                setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * json字符串转换成对象
     * @param str  json字符串
     * @param type 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String str, Type type){
        Gson gson = createGsonBuilder().create();
        return gson.fromJson(str,type);
    }

    public static Gson getInstance() {
        ExclusionStrategy strategy = new DmsExclusionStrategy();
        Gson gson =createGsonBuilder()
                .setExclusionStrategies(strategy)
                .create();
        return gson;
    }


    /**
     * java对象转换成json字符串
     * @param obj  java对象
     * @param excludes 过滤字段
     * @return
     */
    public static String obj2Json(Object obj, String... excludes ){
        ExclusionStrategy strategy = new DmsExclusionStrategy(excludes);
        Gson gson =createGsonBuilder()
                .setExclusionStrategies(strategy)
                .create();
        return  gson.toJson(obj);
    }


    public static String toJsonInclude(Object obj, String... includeFields) {
        ExclusionStrategy strategy = new DmsIncludeStrategy(includeFields);
        Gson gson =createGsonBuilder()
                .setExclusionStrategies(strategy)
                .create();
        return  gson.toJson(obj);
    }





    /**
     * java对象转换成json字符串
     * @param obj
     * @param classes 类别数组
     * @param excludes  字符串数组
     * @return
     */
    public static String obj2Json(Object obj, Class[] classes, String... excludes){
        ExclusionStrategy strategy = new DmsExclusionStrategy(excludes,classes);
        Gson gson =createGsonBuilder()
                  .setExclusionStrategies(strategy)
                .create();
        return  gson.toJson(obj);
    }








}
