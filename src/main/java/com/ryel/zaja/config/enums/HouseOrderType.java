package com.ryel.zaja.config.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum HouseOrderType {
    FROM_HOUSE         ("10", "来自房源信息"),
    FROM_CUSTOM        ("20", "来自自定义信息"),
    ;

    HouseOrderType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<String, String> getEnumMap() {
        Map<String, String> map = new LinkedHashMap<>();
        HouseOrderType[] values = values();
        for (HouseOrderType item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static HouseOrderType getByCode(Long code) {
        HouseOrderType[] values = values();
        for (HouseOrderType item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Long code) {
        HouseOrderType[] values = values();
        for (HouseOrderType item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}