package com.ryel.zaja.controller;


import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.AgentRegisterStatus;
import com.ryel.zaja.entity.AgentMaterial;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.service.AgentMaterialService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.utils.DataTableFactory;
import com.ryel.zaja.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/mgt/user")
public class UserController  extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private AgentMaterialService agentMaterialService;


    @RequestMapping("/userIndex")
    public String index(){
        return "userList";
    }

    @RequestMapping("/agentIndex")
    public String agentIndex(){
        return "agentList";
    }

    @RequestMapping("/agentList")
    @ResponseBody
    public Map list(Integer draw, Integer start, Integer length, String name) {
        int pageNum = getPageNum(start, length);
        Page<User> page = userService.mgtPageAgent(pageNum, length, name);
        Map<String, Object> result = DataTableFactory.fitting(draw, page);
        return result;
    }

    @RequestMapping("/agentDetail")
    public ModelAndView agentDetail(Integer agentId) {
        ModelAndView modelAndView = new ModelAndView("agentDetail");
        User user = userService.findById(agentId);
        AgentMaterial agentMaterial = agentMaterialService.findByAgentId(agentId);
        modelAndView.addObject("user",user);
        modelAndView.addObject("agentMaterial",agentMaterial);
        return modelAndView;
    }

    @RequestMapping("/userList")
    @ResponseBody
    public Map userList(Integer draw, Integer start, Integer length, String name) {
        int pageNum = getPageNum(start, length);
        Page<User> page = userService.findByPage(pageNum, length, name);
        Map<String, Object> result = DataTableFactory.fitting(draw, page);
        return result;
    }

    @RequestMapping("/userDetail")
    public ModelAndView userDetail(Integer userId) {
        ModelAndView modelAndView = new ModelAndView("userDetail");
        User user = userService.findById(userId);
        AgentMaterial agentMaterial = agentMaterialService.findByAgentId(userId);
        modelAndView.addObject("user",user);
        modelAndView.addObject("agentMaterial",agentMaterial);
        return modelAndView;
    }

    @RequestMapping("/agentApprove")
    @ResponseBody
    public Result agentApprove(Integer id,String type) {
        User agent = userService.findById(id);
        if("0".equals(type)){
            agent.setAgentStatus(AgentRegisterStatus.APPROVE_PASS.getCode());
        }else {
            agent.setAgentStatus(AgentRegisterStatus.APPROVE_REJECT.getCode());
        }
        userService.update(agent);
        return Result.success().msg("操作成功");
    }

    /**
     * 删除管理员帐号
     * @param request
     * @param response
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(HttpServletRequest request,
                         HttpServletResponse response,
                         String ids){
        int[] arrayId = JsonUtil.json2Obj(ids, int[].class);
        userService.deleteByIds(arrayId);
        return Result.success().msg("用户删除成功!");
    }



    @RequestMapping("/save")
    @ResponseBody
    public Result save(HttpServletRequest request,
                       HttpServletResponse response,
                       User user){
        if(user.getId()==null){
            userService.create(user);
        }else{
            userService.update(user);
        }
        return Result.success().msg("用户操作成功!");
    }









}
