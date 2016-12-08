package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.dao.UserDao;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.exception.UserException;
import com.ryel.zaja.service.DefaultUploadFile;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.bean.FileBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: koabs
 * 8/22/16.
 * app 客户端用户登入,注册等功能.
 */
@RestController()
@RequestMapping("/api/user/")
public class UserApi {
    protected final static Logger logger = LoggerFactory.getLogger(UserApi.class);


    @Value("${pro.upload.url}")
    private String uploadUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private DefaultUploadFile defaultUploadFile;

    @Autowired
    private UserDao userDao;

    /**
     * @api {post} /api/user/register 1.APP用户注册
     * @apiVersion 0.0.1
     * @apiName user.register
     * @apiGroup user
     * @apiDescription 用户注册
     * @apiParam {STRING} password 密码
     * @apiParam {String} mobile 手机
     * @apiSuccess {Result} Result 返回结果
     *
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user) {
        try {
            user.setHead("");
            user.setNickname("");
            user.setUsername("");
            user.setType(user.getType());
            if(null == user.getSex()) {
                user.setSex("30");//未设置
                }
            userService.create(user);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0006).data("");//手机号被占用
        }

        return Result.success().msg("").data(user2map(user));
    }


    /**
     * @api {post} /api/user/login 2.APP用户登陆
     * @apiVersion 0.0.1
     * @apiName user.login
     * @apiGroup user
     * @apiDescription 用户登录
     * @apiParam {STRING} mobile 手机
     * @apiParam {STRING} password 密码
     * @apiSuccess {Result} Result 返回结果
     * @apiUse UserInfo
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(User user) {
        User origUser = userService.login(user.getMobile(), user.getPassword());
        if (origUser == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0004);//用户名或密码错误
        } else {
            return Result.success().msg("").data(user2map(origUser));
        }

    }

    /**
     * @api {post} /api/user/update 3.修改用户信息
     * @apiVersion 0.0.1
     * @apiName user.update
     * @apiGroup user
     * @apiDescription 修改用户信息
     * @apiParam {STRING} password 密码
     * @apiParam {STRING} name 名称
     * @apiParam {STRING} mobile 手机
     * @apiSuccess {Result} Result 返回结果
     * @apiUse UserInfo
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(User user) {
        userService.update(user);
        User origUser = userDao.findOne(user.getId());
        return Result.success().msg("").data(user2map(origUser));
    }


    /**
     * @api {post} /api/user/uploadhead 4.上传头像
     * @apiVersion 0.0.1
     * @apiName user.uploadhead
     * @apiGroup user
     * @apiDescription 上传头像
     * @apiParam {STRING} userId 用户ID
     * @apiParam {FILE} file 图片文件
     * @apiSuccess {Result} Result 返回结果
     * @apiSuccessExample {json} Success-Response:
     * {
     * "status": 200,
     * "msg": "",
     * "data": "http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg"
     * }
     */
    @RequestMapping(value = "head/upload")
    public Result userHeadUpload(Integer userId,
                                @RequestParam(required = true) MultipartFile file)throws Exception  {
        User user = userService.findById(userId);
        FileBo fileBo = defaultUploadFile.uploadFile(file.getOriginalFilename(),file.getInputStream());
        userService.update(user);
        return Result.success().msg("");
    }

    /**
     * @api {post} /api/user/info 5.查询用户信息
     * @apiVersion 0.0.1
     * @apiName user.info
     * @apiGroup user
     * @apiDescription 修改用户信息
     * @apiParam {STRING} userId 用户ID
     * @apiSuccess {Result} Result 返回结果
     * @apiUse UserInfo
     */
    @RequestMapping(value = "info")
    public Result userInfo(Integer userId)throws Exception  {
        User user = userService.findById(userId);
        return Result.success().data(user2map(user));
    }


    private Map<String,Object> user2map(User user){
        Map<String,Object> result = new HashMap<>();
        result.put("user",user);
        return result;
    }


    /**
     * @api {post} /api/user/findPassword 13.找回密码
     * @apiVersion 0.0.1
     * @apiName user.findPassword
     * @apiNote /api/user/findPassword
     * @apiParam {String} mobile 用户邮箱
     * @apiGroup user
     * @apiDescription findPassword
     * @apiSuccess {Result} Result 返回结果
     * @apiSuccessExample {json} Success-Response:
        账号不存在或者其他错误信息:
        {
        "status":1,
        "data":{},
        "msg":"error_14"
        }
        找回密码短信发送成功:
        {
        "status":0,
        "data":{},
        "msg":""
        }
     */
    @RequestMapping(value = "findpassword", method = RequestMethod.POST)
    public Result findPassword(User user) {
        if (null == user || null == userDao.findByMobile(user.getMobile())) {
            return Result.error().msg(Error_code.ERROR_CODE_0012);//手机号未注册用户
        }
        userService.update(user);
        return Result.success().msg("").data("");
    }

    @RequestMapping(value = "sendverifycode" ,method = RequestMethod.POST)
    public String verifyCode(){
        return null;
    }
}
