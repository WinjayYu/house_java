package com.ryel.zaja.config.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum HouseStatus {
    SAVED         ("10", "待审核"),
    REJECT        ("20", "审核驳回"),
    PUTAWAY_YET   ("30", "已上架"),
    SOLD_OUT_YET  ("40", "已下架"),
    IN_CONNECT    ("50", "交接中"),
    CLOSED        ("60", "已关闭"),
    DELETE         ("99", "删除"),
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


    public static List<String> getUserCanSeeStatus() {
        List<String> status = new ArrayList<String>();
        status.add(HouseStatus.PUTAWAY_YET.getCode());
        return status;
    }

    //我的房源信息可以看到的状态
    public static List<String> getManagerCanSeeStatus() {
        List<String> status = new ArrayList<String>();
        status.add(HouseStatus.SAVED.getCode());
        status.add(HouseStatus.REJECT.getCode());
        status.add(HouseStatus.PUTAWAY_YET.getCode());
        status.add(HouseStatus.SOLD_OUT_YET.getCode());
        status.add(HouseStatus.IN_CONNECT.getCode());
        status.add(HouseStatus.CLOSED.getCode());

        return status;
    }

    public static String getAllStatusOptionHtml(Boolean hasAll) {
        HouseStatus[] values = values();
        String html = "";
        if(hasAll){
            html += "<option value=\"\">全部</option>";
        }
        for (HouseStatus item : values) {
            html += "<option value=\"" + item.code + "\">" + item.desc + "</option>";
        }
        return html;
    }

    public static Map<String, String> getEnumMap() {
        Map<String, String> map = new LinkedHashMap<>();
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static HouseStatus getByCode(String code) {
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        HouseStatus[] values = values();
        for (HouseStatus item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}