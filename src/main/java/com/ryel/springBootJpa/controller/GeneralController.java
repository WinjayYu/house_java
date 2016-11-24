package com.ryel.springBootJpa.controller;

import com.ryel.springBootJpa.config.bean.Result;
import com.ryel.springBootJpa.service.DefaultUploadFile;
import com.ryel.springBootJpa.utils.bean.FileBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by burgl on 2016/8/28.
 */
@Controller
@RequestMapping(value = "gen")
public class GeneralController extends BaseController{

    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private DefaultUploadFile defaultUploadFile;


    @RequestMapping(value = "save/image")
    @ResponseBody
    public Result saveImage(@RequestParam(required = true) MultipartFile file)throws Exception{
        FileBo fileBo = defaultUploadFile.uploadFile(file.getOriginalFilename(),file.getInputStream());
        return Result.success().data(fileBo.getName());
    }



}
