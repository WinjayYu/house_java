package com.ryel.zaja.utils;

import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.CommunityService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billyu on 2016/12/9.
 */
public class GetDistanceUtil {
    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)


    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     * @param lon1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 根据经纬度查询附近的小区信息
     * @param lon1 经度
     * @param lat1 纬度
     * @return 返回的附近所有的小区信息
     * */
    public static List<String> nearbyCommunity(double lon1, double lat1, List<Community> communityBycity) {
        List<Community> communities5 = new ArrayList<Community>();
        List<Community> communities10 = new ArrayList<Community>();
        List<Community> communities20 = new ArrayList<Community>();

        List<String> uids = new ArrayList<>();
        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 5000) {
                communities5.add(community);
            }else if (distance > 5000 && distance <= 10000)
            {
                communities10.add(community);
            }else
            {
                communities20.add(community);
            }
            //if(6 == houses.size()) break;
        }
        for(Community community : communities5){
            uids.add(community.getUid());
        }
        return uids;
    }

}
