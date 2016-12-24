package com.ryel.zaja.config.enums;

import java.util.LinkedHashMap;
import java.util.Map;
public enum AgentRegisterStatus {
    APPROVE_APPLY    ("10", "申请审核"),
    APPROVE_PASS     ("20", "审核通过"),
    APPROVE_REJECT   ("30", "审核驳回"),
    OTHER            ("-1", "其他"),
    ;

    AgentRegisterStatus(String code, String desc) {
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
        AgentRegisterStatus[] values = values();
        for (AgentRegisterStatus item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static AgentRegisterStatus getByCode(Long code) {
        AgentRegisterStatus[] values = values();
        for (AgentRegisterStatus item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Long code) {
        AgentRegisterStatus[] values = values();
        for (AgentRegisterStatus item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}