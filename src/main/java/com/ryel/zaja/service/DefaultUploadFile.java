package com.ryel.zaja.service;

import com.ryel.zaja.utils.FileUtil;
import com.ryel.zaja.utils.bean.FileBo;
import com.ryel.zaja.utils.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.Calendar;

/**
 * 默认文件上传
 * Created by wangbin on 2016/6/27.
 */
@Service
public class DefaultUploadFile {


    @Value("${pro.upload.path}")
    private String path;

    @Value("${pro.upload.url}")
    private String url;

    /**
     * 上传文件
     * @param originalFileName
     * @param inputStream
     * @return
     */

    public FileBo uploadFile(String originalFileName, InputStream inputStream){
        try {
            FileBo fileBo = FileUtil.save(originalFileName,inputStream,path,customImageName(originalFileName));
            fileBo.setName(url + fileBo.getName());
            return fileBo;
        }catch (Exception e){
            throw new FileUploadException("文件上传失败!");
        }
    }

    /**
     * 删除文件
     * @param originalFileName
     */
    public boolean deleteFile(String originalFileName)
    {
       return FileUtil.deleteFile(path+originalFileName);
    }


    /**
     * 保存文件到本地
     * @param originalFileName
     * @param inputStream
     * @return
     */

    public FileBo saveFile(String originalFileName, InputStream inputStream)
    {
        try {
            FileBo fileBo = FileUtil.save(originalFileName,inputStream,path,"");
            fileBo.setName(url + fileBo.getName());
            return fileBo;
        }catch (Exception e){
            throw new FileUploadException("文件保存失败!");
        }
    }



    /**
     * 自定义文件名
     * @param fileName
     * @return
     */
    public String customImageName(String fileName) {
        Calendar now = Calendar.getInstance();
        int year=now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH)+1;
        StringBuffer sb = new StringBuffer();

        sb.append("img").append("/").append(year).append("/").append(month).append("/").append( System.nanoTime()+ "");
        return sb.toString();
    }

}
