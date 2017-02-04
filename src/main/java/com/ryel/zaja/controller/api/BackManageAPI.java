package com.ryel.zaja.controller.api;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.config.enums.HouseStatus;
import com.ryel.zaja.controller.HouseController;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.pingan.ZJJZ_API_GW;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.APIFactory;
import com.ryel.zaja.utils.DataTableFactory;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.VerifyCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nathan on 2017/1/6.
 */
@RestController()
@RequestMapping(value = "/back/", produces = "application/json; charset=UTF-8")
public class BackManageAPI {
    protected final static Logger logger = LoggerFactory.getLogger(BackManageAPI.class);
    @Value("${pro.upload.path}")
    private String path;
    @Value("${pro.upload.url}")
    private String url;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentMaterialService agentService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private QiNiuService qiNiuService;
    @Autowired
    private BizUploadFile bizUploadFile;
    @Autowired
    private DefaultUploadFile defaultUploadFile;
    @Autowired
    private UserWalletAccountService userWalletAccountService;


    private final String remoteUrlHead = "http://img.zaja.xin/";

    /**
     * 获取未审核经纪人列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "uncheckagents")
    public Result uncheckagents(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<User> page = userService.unCheckAgent(pageNum, pageSize);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            for (User user : page.getContent()) {
                Map agents = new HashMap<>();
                agents.put("id", user.getId());
                agents.put("mobile", user.getMobile());

                AgentMaterial material = agentService.findByAgentId(user.getId());
                agents.put("idcard", material.getIdcard());
                agents.put("companyName", material.getCompanyName());
                agents.put("companyCode", material.getCompanyCode());
                agents.put("positive", material.getPositive());
                agents.put("negative", material.getNegative());
                agents.put("companyPic", material.getCompanyPic());

                list.add(agents);
            }

            Map<String, Object> dataMap = APIFactory.fitting(page, list);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 0 审核通过 1审核拒绝
     *
     * @param userId
     * @param type
     * @return
     */
    @RequestMapping(value = "reviewoperation")
    public Result reviewoperation(Integer userId, Integer type, String username, String sex, String refusemsg) {
        try {

            User agent = userService.findById(userId);
            if (null == agent) {
                return Result.error().msg(Error_code.ERROR_CODE_0004).data(new HashMap<>());
            }

            if (type == 0) {
                agent.setAgentStatus(AgentRegisterStatus.APPROVE_PASS.getCode());
                agent.setUsername(username);
                agent.setSex(sex);
                //经纪人审核失败发送消息
                VerifyCodeUtil.sendTip(agent.getMobile(),refusemsg,"0");

                try {
                    //创建见证宝子账户
                    HashMap parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
                    HashMap retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
                    parmaKeyDict.put("TranFunc", "6000"); // 交易码，此处以【6000】接口为例子
                    parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
                    parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
                    parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
                    parmaKeyDict.put("FuncFlag", "1"); // 功能标志1：开户 3销户
                    parmaKeyDict.put("ThirdCustId", userId +""); // 交易网会员代码
                    parmaKeyDict.put("CustProperty", "00"); // 会员属性
                    parmaKeyDict.put("NickName", username); // 会员昵称
                    parmaKeyDict.put("MobilePhone", agent.getMobile()); // 手机号码
                    parmaKeyDict.put("Email", ""); // 邮箱
                    parmaKeyDict.put("Reserve", "会员开户"); // 保留域

                    ZJJZ_API_GW msg = new ZJJZ_API_GW();
                    String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

                    msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
                    String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

                    retKeyDict = msg.parsingTranMessageString(recvMessage);
                    System.out.println("返回报文:=" + retKeyDict);
                    /**
                     * 第三部分：解析银行返回的报文的实例
                     */
                    retKeyDict = msg.parsingTranMessageString(recvMessage);
                    String custAcctId = (String) retKeyDict.get("CustAcctId");
                    String rspCode = (String) retKeyDict.get("RspCode");
                    if ("000000".equals(rspCode) && StringUtils.isNotEmpty(custAcctId)) {
                        // 创建成功，写入数据库
                        UserWalletAccount userWalletAccount = new UserWalletAccount();
                        userWalletAccount.setUserId(userId);
                        userWalletAccount.setThirdCustId(userId.toString());
                        userWalletAccount.setCustAcctId(custAcctId);
                        userWalletAccount.setMobilePhone(agent.getMobile());
                        userWalletAccount.setNickName(username);
                        userWalletAccountService.create(userWalletAccount);
                    } else
                    {
                        logger.error("创建见证宝",retKeyDict.get("RspMsg"));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            } else {
                agent.setAgentStatus(AgentRegisterStatus.APPROVE_REJECT.getCode());
                //经纪人审核失败发送消息
                VerifyCodeUtil.sendTip(agent.getMobile(),refusemsg,"1");

                //删除经纪人提交的资料
                defaultUploadFile.deleteFile("agent/"+agent.getId());
                //数据库删除信息
                AgentMaterial material = agentService.findByAgentId(userId);
                agentService.deleteById(material.getId());
            }
            userService.update(agent);
            return Result.success().data(new HashMap<>());
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 获取经纪人/用户列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "userlist")
    public Result userlist(Integer pageNum, Integer pageSize, String type) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<User> page = userService.userList(pageNum, pageSize, type);
            //用户直接返回
            if (type.equals("10")) {
                Map<String, Object> dataMap = APIFactory.fitting(page);
                return Result.success().data(dataMap);
            }

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (User user : page.getContent()) {
                if (AgentRegisterStatus.APPROVE_PASS.getCode().equals(user.getAgentStatus())) {
                    Map agents = new HashMap<>();
                    agents.put("id", user.getId());
                    agents.put("mobile", user.getMobile());
                    agents.put("username", user.getUsername());
                    agents.put("sex", user.getSex());
                    agents.put("head", user.getHead());

                    AgentMaterial material = agentService.findByAgentId(user.getId());
                    agents.put("idcard", material.getIdcard());
                    agents.put("companyName", material.getCompanyName());
                    agents.put("companyCode", material.getCompanyCode());
                    agents.put("positive", material.getPositive());
                    agents.put("negative", material.getNegative());
                    agents.put("companyPic", material.getCompanyPic());

                    list.add(agents);
                }
            }

            Map<String, Object> dataMap = APIFactory.fitting(page, list);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());

        }
    }

    /**
     * 获取房屋信息列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "houselist")
    public Result houselist(Integer pageNum, Integer pageSize, String status) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 1;
            }
            Page<House> page = houseService.backPageHouse(pageNum, pageSize,status);

            Map<String, Object> dataMap = APIFactory.fitting(page);
            return Result.success().data(dataMap);
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());

        }
    }

    /**
     * 审核房源 0代表通过 1代表拒绝
     * @param houseId
     * @param type
     * @return
     */
    @RequestMapping("reviewhouse")
    public Result reviewhouse(Integer houseId, String type,String refusemsg) {
        House house = houseService.findById(houseId);
        if ("0".equals(type)) {
            // 设置为上架状态
            house.setStatus(HouseStatus.PUTAWAY_YET.getCode());

            // 本地图片上传到七牛并更新imgs字段
            String imgs = house.getImgs();
            if (StringUtils.isNotBlank(imgs)) {
                List<String> imageList = JsonUtil.json2Obj(imgs, List.class);

                //删除七牛图片
                try {
                        qiNiuService.deleteMultipleFile(remoteUrlHead + house.getId());
                    }catch (Exception e){
                        logger.error("上传文件异常");
                        e.printStackTrace();
                }

                List<String> newList = new ArrayList<String>();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (String imageUrl : imageList) {
                        String newUrl = localUrlToQiniuUrl(imageUrl, house.getId().toString());
                        newList.add(newUrl);
                    }
                }
                String newImgs = JsonUtil.obj2Json(newList);
                house.setImgs(newImgs);
                house.setCover(newList.get(0));

                //删除本地图片
                defaultUploadFile.deleteFile("house/"+house.getId());

            }
        }else {
            house.setStatus(HouseStatus.REJECT.getCode());
            //房屋审核失败发送消息
            VerifyCodeUtil.sendTip(house.getAgent().getMobile(),refusemsg,"2");
        }
        houseService.update(house);
        return Result.success();
    }

    private String localUrlToQiniuUrl(String localUrl, String houseId) {
        String newUrl = localUrl;
        if (StringUtils.isNotBlank(localUrl)) {
            try {
                String imagePath = localUrl.replace(url, path);
                File file = new File(imagePath);
                if (file.exists()) {
                    newUrl = bizUploadFile.uploadHouseImageToQiniu(file, houseId);
                } else {
                    logger.error("文件不存在：" + file.getPath());
                }
            } catch (Exception e) {
                logger.error("上传文件异常");
                e.printStackTrace();
            }
        }
        return newUrl;
    }
}
