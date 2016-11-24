package com.ryel.springBootJpa.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by burgl on 2016/8/28.
 */
public class DmsIncludeStrategy implements ExclusionStrategy {


    private String[] excludeFields;
    private Class[] excludeClasses;


    public DmsIncludeStrategy() {

    }



    public DmsIncludeStrategy(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }


    public DmsIncludeStrategy(String[] excludeFields,
                                Class<?>[] excludeClasses) {
        this.excludeFields = excludeFields;
        this.excludeClasses = excludeClasses;
    }





    public boolean shouldSkipClass(Class<?> clazz) {
        if (this.excludeClasses == null) {
            return false;
        }

        for (Class<?> excludeClass : excludeClasses) {
            if (excludeClass.getName().equals(clazz.getName())) {
                return true;
            }
        }

        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        for (String field : this.excludeFields) {
            if (field.equals(f.getName())) {
                return false;
            }
        }
        return true;
    }

    public final String[] getExcludeFields() {
        return excludeFields;
    }

    public final Class<?>[] getExcludeClasses() {
        return excludeClasses;
    }
}
