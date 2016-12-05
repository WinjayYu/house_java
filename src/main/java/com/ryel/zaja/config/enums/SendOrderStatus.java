package com.ryel.zaja.config.enums;

import java.util.LinkedHashMap;
import java.util.Map;


public enum SendOrderStatus {

    SAVED(10L, "已保存"),

    ENABLED(20L, "已启用"),

    UNABLE(30L, "已停用"),;

    SendOrderStatus(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Long code;
    private String desc;

    public Long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Long, String> getEnumMap() {
        Map<Long, String> map = new LinkedHashMap<>();
        SendOrderStatus[] values = values();
        for (SendOrderStatus item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static SendOrderStatus getByCode(Long code) {
        SendOrderStatus[] values = values();
        for (SendOrderStatus item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Long code) {
        SendOrderStatus[] values = values();
        for (SendOrderStatus item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}