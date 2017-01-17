package com.ryel.zaja.utils;

import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.AgentLocation;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.SellHouse;
import com.ryel.zaja.entity.User;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 涂奕恒 on 2014-12-15.
 */
public class APIFactory{

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
            if(ob instanceof SellHouse){
                newList.add(filterSellHouse((SellHouse)ob));
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

    /**
     * SellHouse过滤字段
     * <p/>
     *
     * @return
     */
    private static Map<String,Object> filterSellHouse(SellHouse sellHouse) {
        Map<String, Object> sellHouseMap = new HashMap<String, Object>();
        sellHouseMap.put("id",sellHouse.getId());
        sellHouseMap.put("status",sellHouse.getStatus());
        sellHouseMap.put("addTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sellHouse.getAddTime()));
        sellHouseMap.put("community",sellHouse.getCommunity());
        sellHouseMap.put("layout",sellHouse.getLayout());
        sellHouseMap.put("price",sellHouse.getPrice());
        sellHouseMap.put("renovation",sellHouse.getRenovation());
        sellHouseMap.put("area",sellHouse.getArea());
        sellHouseMap.put("houseNum",sellHouse.getHouseNum());
        sellHouseMap.put("num",sellHouse.getNum()*3 + new Random().nextInt(3));
        sellHouseMap.put("user",filterUser(sellHouse.getUser()));

        return  sellHouseMap;
    }

    /**
     * House列表过滤
     * @param houses
     * @return
     */
    public static List<Map<String,Object>> listFilterHouse(List<House> houses){
        List<Map<String,Object>> list = new ArrayList<>();
        for(House house : houses){
            list.add(filterHouse(house));
        }
        return list;
    }


    public static Map<String,Object> filterAgentLocation(AgentLocation agentLocation){
        Map<String, Object> houseMap = new HashMap<String, Object>();
        houseMap.put("agent", filterUser(agentLocation.getAgent()));
        houseMap.put("longitude", agentLocation.getLongitude());
        houseMap.put("latitude", agentLocation.getLatitude());

        return houseMap;
    }


    /**
     * 当page的size为0的时候，使用此方法，返回前台需要的格式
     * @return
     */
    public static Map<String, Object> emptyMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list",new ArrayList<>());
        return map;
    }
}
