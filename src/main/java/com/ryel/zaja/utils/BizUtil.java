package com.ryel.zaja.utils;


import com.ryel.zaja.entity.Community;
import com.ryel.zaja.service.CommunityService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BizUtil {

    public static String getOrderCode() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(new Date());
        int x = (int) (Math.random() * 900) + 100;
        return "DD" + dateString + x;
    }

    public static List<String> nearbyCommunity(double lon1, double lat1, String city, CommunityService communityService) {
        List<Community> communities = new ArrayList<Community>();
        List<Community> communityBycity = communityService.findByCity(city);
        List<String> uids = new ArrayList<>();
        for (Community community : communityBycity) {
            double lon2 = community.getLongitude().doubleValue();
            double lat2 = community.getLatitude().doubleValue();
            //计算两个点之间的距离
            double distance = GetDistanceUtil.GetDistance(lon1, lat1, lon2, lat2);
            if (distance <= 7000) {
                communities.add(community);
            }
        }
        for (Community community : communities) {
            uids.add(community.getUid());
        }
        return uids;
    }

    public static void main(String[] args) {
        System.out.println(getOrderCode());
    }
}

