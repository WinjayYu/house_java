package com.ryel.zaja.entity;

/**
 * Author: koabs
 * 9/18/16.
 * app 更新版本号
 */
public class AppVersion {

    /**
     * app 版本号
     */
    private Integer version = 1;

    /**
     * app下载地址
     */
    private String url = "http://14.17.81.101:8080/files/app/IVF_V1.0_Beta_0913.apk";


    private String name = "1.0";


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
