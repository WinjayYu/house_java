package com.ryel.zaja.config.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum HouseStatus {
    SAVED         ("10", "待审核"),
    ENABLED       ("20", "审核驳回"),
    PUTAWAY_YET   ("30", "已上架"),
    SOLD_OUT_YET  ("40", "已下架"),
    IN_CONNECT    ("50", "交接中"),
    CLOSED        ("60", "已关闭"),
    OTHER         ("-1", "其他"),
    ;

    HouseStatus(String code, String desc) {
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

    public static List<String> getAgentCanSeeStatus() {
        List<String> status = new ArrayList<String>();
        status.add(HouseStatus.PUTAWAY_YET.getCode());
        status.add(HouseStatus.IN_CONNECT.getCode());
        return status;
    }


    public static Map<String, String> getEnumMap() {
        Map<String, String> map = new LinkedHashMap<>();
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static HouseStatus getByCode(Long code) {
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Long code) {
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}