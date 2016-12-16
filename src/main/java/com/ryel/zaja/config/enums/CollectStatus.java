//package com.ryel.zaja.config.enums;
//
///**
// * Created by billyu on 2016/12/14.
// */
//public enum CollectStatus {
//    COLLECT("10", "收藏"), CANCEL_COLLECT("20", "取消收藏");
//
//    private String code;
//    private String desc;
//
//    private CollectStatus(String code, String desc) {
//        this.code = code;
//        this.desc = desc;
//    }
//
//    public static String getCode(String desc) {
//        for (CollectStatus c : values()) {
//            if (c.getDesc().equals(desc)) {
//                String str = c.getCode();
//                return c.getCode();
//            }
//        }
//        return null;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//
//    public String getDesc() {
//        return desc;
//    }
//
//
//}
