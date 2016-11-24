package com.ryel.springBootJpa.utils;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbin on 14-10-21.
 */
public class DataTableFactory {


    public static Map<String,Object> fitting(Integer draw, Page<?> page){
        List<?> list = page.getContent();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",list);
        map.put("draw",draw);
        map.put("iTotalRecords",page.getTotalElements());
        map.put("iTotalDisplayRecords",page.getTotalElements());
        return  map;
    }

}
