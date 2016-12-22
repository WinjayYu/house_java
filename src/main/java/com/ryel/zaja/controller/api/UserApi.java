package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.DefaultUploadFile;
import com.ryel.zaja.service.QiNiuService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.ryel.zaja.utils.bean.FileBo;;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


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

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private QiNiuService qiNiuService;

    private static int EXPIRES = 10 * 60; //超时时间10min
    private static int captchaW = 200;
    private static int captchaH = 60;

    /**
     * @api {post} /api/user/register 1.APP用户注册
     * @apiVersion 0.0.1
     * @apiName user.register
     * @apiGroup user
     * @apiDescription 用户注册
     * @apiParam {STRING} password 密码
     * @apiParam {String} mobile 手机
     * @apiSuccess {Result} Result 返回结果
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user, @RequestParam("verCode") String verCode) {
        ValueOperations<String, String> valueops = redisTemplate.opsForValue();
        String origVerCode = valueops.get(user.getMobile());
//        Object origVerCode = redisTemplate.opsForValue().get(user.getMobile());
        if (null == origVerCode) {
            return Result.error().msg(Error_code.ERROR_CODE_0010).data("").data(new HashMap<>());
        }
        if (!origVerCode.equals(verCode)) {
            return Result.error().msg(Error_code.ERROR_CODE_0009).data(new Object()).data(new HashMap<>());
        }
        try {
            user.setHead("");
            user.setNickname("");
            user.setUsername("");
            if (null == user.getSex()) {
                user.setSex("30");//未设置
            }
            if (null == user.getType()) {
                user.setType("10");//用户
            }
            userService.create(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
    public Result login(String mobile, String password) {
        User origUser = userService.login(mobile, password);
        if (origUser == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0004);//用户名或密码错误
        } else {
            origUser.setPassword("");
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
    public Result update(Integer userId, String nickname, String sex) {
        User user = new User();
        user.setId(userId);
        if(null != nickname) {
            user.setNickname(nickname);
        }else{
            user.setSex(sex);
        }
        userService.update(user);
        User origUser = userService.findById(user.getId());
        return Result.success().msg("").data(user2map(origUser));
    }


    /**
     * @api {post} /api/user/head 4.上传头像
     * @apiVersion 0.0.1
     * @apiName user.head
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
    //@RequestParam(value = "head", required = true) MultipartFile file
    @RequestMapping(value = "head")
    public Result headUpload(Integer userId,
                             @RequestParam(required = true) MultipartFile image) throws Exception {

        User user = userService.findById(userId);
        FileBo fileBo = defaultUploadFile.uploadFile(image.getOriginalFilename(), image.getInputStream());

        StringBuffer key = new StringBuffer();
//        key.append(qiNiuService.getDomain());
        key.append("user/" + userId + "/");
        key.append(qiNiuService.getFileName());

        String fileName = fileBo.getName();
        String path = fileBo.getFile().toString();

        String bodyString = null;
        try {
            bodyString = qiNiuService.upload(path, key.toString());
        }catch (Exception e){
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }

        StringBuffer remotePath = new StringBuffer();
        if (null != bodyString) {
            remotePath.append(qiNiuService.getDomain());
            remotePath.append(bodyString.substring(bodyString.indexOf("key") + 6, bodyString.length() - 2));
        }

        user.setHead(remotePath.toString());
        userService.update(user);
        Map<String ,String> dataMap = new HashMap<>();
        dataMap.put("remotePath",remotePath.toString());
        return Result.success().msg("").data(dataMap);
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
    public Result userInfo(Integer userId) throws Exception {
        User user = userService.findById(userId);
        return Result.success().data(user2map(user));
    }


    private Map<String, Object> user2map(User user) {
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
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
     * 账号不存在或者其他错误信息:
     * {
     * "status":1,
     * "data":{},
     * "msg":"error_14"
     * }
     * 找回密码短信发送成功:
     * {
     * "status":0,
     * "data":{},
     * "msg":""
     * }
     */
    @RequestMapping(value = "findpassword", method = RequestMethod.POST)
    public Result findPassword(String mobile, String password, String verCode) {
        try {
            User user = userService.findByMobile(mobile);

            if (null == user) {
                return Result.error().msg(Error_code.ERROR_CODE_0012);//手机号未注册用户
            }

            ValueOperations<String, String> valueops = redisTemplate.opsForValue();
            String origVerCode = valueops.get(mobile);

            if (null == origVerCode) {
                return Result.error().msg(Error_code.ERROR_CODE_0010).data("");
            }
            if (!origVerCode.equals(verCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0009).data(new Object());
            }

            user.setPassword(password);
            userService.update(user);

            return Result.success().msg("").data("");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 发送验证码
     * @param mobile
     * @param type 1=注册，2=修改密码
     * @return
     */
    @RequestMapping(value = "sendverifycode", method = RequestMethod.POST)
    public Result verifyCode(String mobile, String type) {
        String verCode = VerifyCodeUtil.getVerCode();
        ValueOperations<String, String> valueops = redisTemplate.opsForValue();
        valueops.set(mobile, verCode);
        //redisTemplate.opsForValue().set(mobile, verCode);
        redisTemplate.expire(mobile, 5, TimeUnit.MINUTES);

        String textEntity = VerifyCodeUtil.send(mobile,verCode,type);

        try {
            JSONObject jsonObj = new JSONObject(textEntity);
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if(error_code==0){
                System.out.println("Send message success.");
            }else{
                System.out.println("Send message failed,code is "+error_code+",msg is "+error_msg);
                return Result.error().msg(Error_code.ERROR_CODE_0008).data(new HashMap<>());
            }
        } catch (JSONException e) {
            logger.error(e.getMessage(),e);
            return Result.error().msg(Error_code.ERROR_CODE_0008).data(new HashMap<>());

        }

        return Result.success().msg("").data(new HashMap<>());
    }
}
