package com.ryel.zaja.utils;

import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂奕恒 on 2014-12-15.
 */
public class APIFactory {

    /**
     * json转换公共类
     *
     * @param page 分页数据
     * @return
     */
    public static Map<String, Object> fitting(Page<?> page) {
        List<?> list = page.getContent();

        // data中的page
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("totalNum", page.getTotalElements());
        pageMap.put("totalPage", page.getTotalPages());
        pageMap.put("currentPage", page.getNumber() + 1);

              //数据过滤
        List newList = new ArrayList();
        for (Object ob : list) {
            if(ob instanceof House)
            {
                newList.add(filterHouse((House)ob));
            }
        }

        // data中的list
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("list", newList.size() != 0 ? newList : list);
        dataMap.put("page", pageMap);
        return dataMap;
    }

    /**
     * json转换公共类
     *
     * @param page 分页数据
     * @return
     */
    public static Map<String, Object> fitting(Page<?> page, List<?> list) {
        // data中的page
        Map<String, Object> pageMap = new HashMap<String, Object>();
        if (null == list || list.size() == 0) {
            pageMap.put("totalNum", 0);
            pageMap.put("totalPage", 0);
        } else {
            pageMap.put("totalNum", page.getTotalElements());
            pageMap.put("totalPage", page.getTotalPages());
        }
        pageMap.put("currentPage", page.getNumber() + 1);

        // data中的list
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("list", list);
        dataMap.put("page", pageMap);
        return dataMap;
    }

    /**
     * json转换公共类
     * <p/>
     * 合并特殊分页数据
     *
     * @return
     */
    public static Map<String, Object> fittingPlus(Page<?> page1, Page<?> page2, Integer errorCount, List<?> list) {
        // data中的page
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("totalNum", page1.getTotalElements() + page2.getTotalElements() - errorCount);
        pageMap.put("totalPage", page1.getTotalPages() + page2.getTotalPages());
        pageMap.put("currentPage", page1.getNumber() + 1);

        // data中的list
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("list", list);
        dataMap.put("page", pageMap);
        return dataMap;
    }

    public static Map<String, Object> fitting(List list) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("list", list);
        return dataMap;
    }

    /**
     * User过滤字段
     * <p/>
     *
     * @return
     */
    public static Map<String,Object> filterUser(User user)
    {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("id",user.getId());
        userMap.put("nickname",user.getNickname());
        userMap.put("username",user.getUsername());
        userMap.put("sex",user.getSex());
        userMap.put("mobile",user.getMobile());
        userMap.put("type",user.getType());
        userMap.put("head",user.getHead());
        return  userMap;
    }

    /**
     * House过滤字段
     * <p/>
     *
     * @return
     */
    public static Map<String,Object> filterHouse(House house)
    {
        Map<String, Object> houseMap = new HashMap<String, Object>();
        houseMap.put("id",house.getId());
        houseMap.put("agent",filterUser(house.getAgent()));
        houseMap.put("layout",house.getLayout());
        houseMap.put("area",house.getArea());
        houseMap.put("price",house.getPrice());
        houseMap.put("status",house.getStatus());
        houseMap.put("type",house.getType());
        houseMap.put("tags",house.getTags());
        houseMap.put("year",house.getYear());
        houseMap.put("feature",house.getFeature());
        houseMap.put("imgs",house.getImgs());
        houseMap.put("renovation",house.getRenovation());
        houseMap.put("commission",house.getCommission());
        houseMap.put("orientation",house.getOrientation());
        houseMap.put("city",house.getCity());
        houseMap.put("purpose",house.getPurpose());
        houseMap.put("floor",house.getFloor());
        houseMap.put("community",house.getCommunity());
        houseMap.put("title",house.getTitle());
        houseMap.put("cover",house.getCover());

        return  houseMap;
    }




}
