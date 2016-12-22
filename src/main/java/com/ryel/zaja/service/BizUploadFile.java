package com.ryel.zaja.service;

import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.bean.FileBo;
import com.ryel.zaja.utils.bean.QiniuResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BizUploadFile {
    @Autowired
    private QiNiuService qiNiuService;
    @Autowired
    private DefaultUploadFile defaultUploadFile;

    public String uploadHouseImageToQiniu(MultipartFile file,Integer houseId){
        String bizPath = "house/" + houseId + "/";
        return uploadToQiniu(file,bizPath);
    }

    private String uploadToQiniu(MultipartFile file, String bizPath) {
        try {
            FileBo fileBo = defaultUploadFile.uploadFile(file.getOriginalFilename(), file.getInputStream());
            String key = bizPath + qiNiuService.getFileName();
            String path = fileBo.getFile().toString();
            String bodyString = qiNiuService.upload(path, key);
            if (StringUtils.isNotBlank(bodyString)) {
                QiniuResponse qiniuResponse = JsonUtil.json2Obj(bodyString, QiniuResponse.class);
                return qiNiuService.getDomain() + qiniuResponse.getKey();
            }else {
                throw new BizException("上传七牛异常");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("上传失败",e);
        }
    }

}
