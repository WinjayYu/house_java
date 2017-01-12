package com.ryel.zaja.service;

import com.ryel.zaja.entity.Apk;

/**
 * Created by billyu on 2017/1/12.
 */
public interface ApkService extends ICommonService<Apk>{

    Apk check(String version, String type);

    Apk findLatestVersion(String type);

}
