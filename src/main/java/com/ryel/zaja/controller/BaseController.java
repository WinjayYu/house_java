package com.ryel.zaja.controller;

import com.ryel.zaja.controller.editor.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by burgl on 2016/8/20.
 */
public class BaseController {




    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MultipartFile.class, new CustomFileEditor());
        binder.registerCustomEditor(Double.class, new CustomDoubleEditor());
        binder.registerCustomEditor(Float.class, new CustomFloatEditor());
        binder.registerCustomEditor(Integer.class, new CustomIntegerEditor());
        binder.registerCustomEditor(Long.class, new CustomLongEditor());
        binder.registerCustomEditor(Date.class, new CustomDateEditor());
    }


    public Integer getPageNum(Integer start,Integer length){
        if(start==null){
            start = 0;
        }
        if(length == null){
            length = 10;
        }

        int pageNum = (start/length)+1;
        return pageNum;
    }


}
