package com.ryel.zaja.config.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//订单状态：10=未接单、20=待支付、30=经济人待确认、40=用户待确认、50=待评价、60=已完成
public enum HouseOrderStatus {
    NO_ORDER         ("10", "未接单"),
    WAIT_PAYMENT      ("20", "待支付"),
    WAIT_AGENT_COMFIRM  ("30", "经济人待确认"),
    APPLY_REBATE("31","用户申请退款"),
    AGENT_COMFIRM_REBATE("32","经纪人确认退款"),
    MANAGER_COMFIRM_REBATE("33","后台管理员确认退款"),
    WAIT_USER_COMFIRM  ("40", "用户待确认"),
    WAIT_COMMENT   ("50", "待评价"),
    FINISHED       ("60", "已完成"),
    OTHER         ("-1", "其他"),
    ;

    HouseOrderStatus(String code, String desc) {
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

    public static List<String> getPayedList() {
        List<String> status = new ArrayList<String>();
        status.add(HouseOrderStatus.WAIT_AGENT_COMFIRM.getCode());
        status.add(HouseOrderStatus.WAIT_USER_COMFIRM.getCode());
        status.add(HouseOrderStatus.WAIT_COMMENT.getCode());
        status.add(HouseOrderStatus.FINISHED.getCode());
        return status;
    }

    public static Map<String, String> getEnumMap() {
        Map<String, String> map = new LinkedHashMap<>();
        HouseOrderStatus[] values = values();
        for (HouseOrderStatus item : values) {
            map.put(item.code, item.desc);
        }
        return map;
    }

    public static HouseOrderStatus getByCode(Long code) {
        HouseOrderStatus[] values = values();
        for (HouseOrderStatus item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Long code) {
        HouseOrderStatus[] values = values();
        for (HouseOrderStatus item : values) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return "";
    }
}