package com.ryel.zaja.config.enums;

/**
 * Created by billyu on 2016/12/18.
 */
public enum UserType {
    MANAGER("10"),USER("20"),AGENT("30");

    private String type;

    UserType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
