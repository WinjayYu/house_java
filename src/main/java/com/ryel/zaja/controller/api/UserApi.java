package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.UserType;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.House;
import com.ryel.zaja.entity.ThirdUser;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.ryel.zaja.utils.bean.FileBo;;
import com.sun.org.apache.xml.internal.utils.SerializableLocatorImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Author: koabs
 * 8/22/16.
 * app 客户端用户登入,注册等功能.
 */
@RestController()
@RequestMapping(value = "/api/user/", produces = "application/json; charset=UTF-8")
public class UserApi {
    protected final static Logger logger = LoggerFactory.getLogger(UserApi.class);


    @Value("${pro.upload.url}")
    private String uploadUrl;


    @Autowired
    private UserService userService;
    @Autowired
    private DefaultUploadFile defaultUploadFile;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private ThirdUserService thirdUserService;

    @Autowired
    private HouseService houseService;

    @Resource
    private BizUploadFile bizUploadFile;




//    private static int EXPIRES = 10 * 60; //超时时间10min
//    private static int captchaW = 200;
//    private static int captchaH = 60;

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
        ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
        String origVerCode = valueops.get(user.getMobile());
//        Object origVerCode = redisTemplate.opsForValue().get(user.getMobile());
        if (null == origVerCode) {
            return Result.error().msg(Error_code.ERROR_CODE_0010).data(new HashMap<>());
        }
        if (!origVerCode.equals(verCode)) {
            return Result.error().msg(Error_code.ERROR_CODE_0009).data(new HashMap<>());
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
        } catch (BizException be) {
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0006).data(new HashMap<>());//手机号被占用
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
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
    public String login(String mobile, String password) {
        User origUser = userService.login(mobile, password);
        if(null == origUser) {
            Result result = Result.error().msg(Error_code.ERROR_CODE_0004);//用户名或密码错误
            return JsonUtil.obj2ApiJson(result);
        }
        if("".equals(origUser.getPassword())){
            Result result = Result.error().msg(Error_code.ERROR_CODE_0030);//未设置密码
            return JsonUtil.obj2ApiJson(result);
        }
            origUser.setPassword("");
            Result result = Result.success().msg("").data(user2map(origUser));
            return JsonUtil.obj2ApiJson(result);
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
        if (null != nickname) {
            user.setNickname(nickname);
        } else {
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

        try {

            User user = userService.findById(userId);
            if(null != user.getHead()){
                String head = user.getHead();
                String filename = head.substring(head.indexOf("user"));
                qiNiuService.deleteOneFile(filename);
            }

            String path = bizUploadFile.uploadUserImageToQiniu(image, userId);
            user.setId(userId);
            user.setHead(path);
            userService.update(user);
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("remotePath", path);
            return Result.success().msg("").data(dataMap);
        }catch (BizException be){
            logger.error(be.getMessage(), be);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0025).data(new HashMap<>());
        }
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
                return Result.error().msg(Error_code.ERROR_CODE_0012).data(new HashMap<>());//手机号未注册用户
            }

            ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
            String origVerCode = valueops.get(mobile);

            if (null == origVerCode) {
                return Result.error().msg(Error_code.ERROR_CODE_0010).data(new HashMap<>());
            }
            if (!origVerCode.equals(verCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0009).data(new HashMap<>());
            }

            user.setPassword(password);
            userService.update(user);

            return Result.success().msg("").data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    /**
     * 发送验证码
     *
     * @param mobile
     * @param type   1=注册，2=修改密码
     * @return
     */
    @RequestMapping(value = "sendverifycode", method = RequestMethod.POST)
    public Result verifyCode(String mobile, String type) {
        String verCode = VerifyCodeUtil.getVerCode();
        ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
        valueops.set(mobile, verCode);
        //redisTemplate.opsForValue().set(mobile, verCode);
        stringRedisTemplate.expire(mobile, 5, TimeUnit.MINUTES);

        String textEntity = VerifyCodeUtil.send(mobile, verCode, type);

        try {
            JSONObject jsonObj = new JSONObject(textEntity);
            int error_code = jsonObj.getInt("error");
            String error_msg = jsonObj.getString("msg");
            if (error_code == 0) {
                System.out.println("Send message success.");
            } else {
                System.out.println("Send message failed,code is " + error_code + ",msg is " + error_msg);
                return Result.error().msg(Error_code.ERROR_CODE_0008).data(new HashMap<>());
            }
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0008).data(new HashMap<>());

        }

        return Result.success().msg("").data(new HashMap<>());
    }

    @RequestMapping(value = "thiredlogin", method = RequestMethod.POST)
    public String thirdLogin(String openid, String type, String nickname,
                             String headUrl) {
        try {
            ThirdUser thirdUser = thirdUserService.findByOpenid(openid);
            if (null != thirdUser) {
                if (null != thirdUser.getUser()) {
                    String mobile = thirdUser.getUser().getMobile();
                    User user = userService.findByMobile(mobile);
                    Result result = Result.success().msg("").data(user);
                    return JsonUtil.obj2ApiJson(result);
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("status", "0");//"0"代表没有绑定手机号
                    Result result = Result.success().msg("").data(map);
                    return JsonUtil.obj2ApiJson(result);
                }

            } else {
                ThirdUser thirdUser1 = thirdUserService.create(type, openid, headUrl, nickname);
                Result result = Result.success().msg("").data(thirdUser1);
                return JsonUtil.obj2ApiJson(result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Result result = Result.error().msg(Error_code.ERROR_CODE_0025);
            return JsonUtil.obj2ApiJson(result);
        }
    }

    /**
     * 第三方登陆绑定手机号
     * @param mobile
     * @param openid
     * @param verCode
     * @return
     */
    @RequestMapping(value = "bindmobile", method = RequestMethod.POST)
    public String bindMobile(String mobile, String openid, String verCode) {
        try {
            ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
            String origVerCode = valueops.get(mobile);

            if (null == origVerCode) {
                Result result = Result.error().msg(Error_code.ERROR_CODE_0010);
                return JsonUtil.obj2ApiJson(result);
            }
            if (!origVerCode.equals(verCode)) {
                Result result = Result.error().msg(Error_code.ERROR_CODE_0009);
                return JsonUtil.obj2ApiJson(result);
            }



            ThirdUser thirdUser = thirdUserService.findByOpenid(openid);
            if (null == thirdUser) {
                Result result = Result.error().msg(Error_code.ERROR_CODE_0029);
                return JsonUtil.obj2ApiJson(result);
            }


            if(null != userService.findByMobile(mobile)){
                thirdUser.setUser(userService.findByMobile(mobile));
                ThirdUser thirdUser1 = thirdUserService.update(thirdUser);

                Result result = Result.success().msg("").data(thirdUser1);
                return JsonUtil.obj2ApiJson(result);
            }

            User user = new User();
            user.setHead(thirdUser.getHead());
            user.setPassword("");
            user.setNickname(thirdUser.getNickname());
            user.setType(UserType.USER.getCode());
            user.setSex("30");//"30"表示未设置性别
            user.setMobile(mobile);

            userService.create(user);

            //将userId存到第三方表中
            User origUser = userService.findByMobile(mobile);
            thirdUser.setUser(origUser);
            ThirdUser thirdUser2 = thirdUserService.update(thirdUser);

            Result result = Result.success().msg("").data(thirdUser2);
            return JsonUtil.obj2ApiJson(result);
        } catch (BizException be){
            logger.error(be.getMessage(), be);
            Result result =  Result.error().msg(Error_code.ERROR_CODE_0006);
            return JsonUtil.obj2ApiJson(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Result result = Result.error().msg(Error_code.ERROR_CODE_0025);
            return JsonUtil.obj2ApiJson(result);
        }
    }

    /**
     * 获取经纪人发布的房源列表
     * @param userId
     */
    @RequestMapping(value = "queryagentpublishlist", method = RequestMethod.POST)
    public Result querymypublishlist(Integer userId, Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<House> houses = houseService.pageByAgentId2(userId,
                    new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id"));

            Map<String, Object> dataMap = APIFactory.fitting(houses);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 申请成为经纪人
     */
    @RequestMapping(value = "apply", method = RequestMethod.POST)
    public Result register(User user, AgentMaterial agentMaterial, String verifycode,
                           @RequestParam(required = false) MultipartFile positiveFile,
                           @RequestParam(required = false) MultipartFile negativeFile,
                           @RequestParam(required = false) MultipartFile companyPicFile) {
        try {
            // 校验验证码
            Object origVerCode = stringRedisTemplate.opsForValue().get(user.getMobile());
            if (null == origVerCode) {
                return Result.error().msg(Error_code.ERROR_CODE_0010).data(new HashMap<>());
            }
            if (!origVerCode.equals(verifycode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0009).data(new HashMap<>());
            }

            userService.agentRegister(user,agentMaterial,verifycode,positiveFile,negativeFile,companyPicFile);
            return Result.success().msg("").data(new HashMap<>());
        } catch (BizException e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(e.getCode()).data(new HashMap<>());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }
}
