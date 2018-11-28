package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.LoginTicketDAO;
import edu.hubu.wdpt.dao.UserDAO;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.utils.WdptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * created by Sugar  2018/11/11 12:16
 * 功能：处理用户业务
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    UserDAO userDAO;

    @Resource
    LoginTicketDAO loginTicketDAO;

    @Autowired
    LoginTicketService loginTicketService;

    /**
     * 用户注册
     */
     public Map<String,Object> register(String username,String password){
         Map<String,Object> map = new HashMap<>();
         //注册时用户名为空
         if(StringUtils.isBlank(username)){
             map.put("msg","用户名不能为空");
             return map;
         }
         //密码为空
         if(StringUtils.isBlank(password)){
             map.put("msg","密码不能为空");
             return map;
         }
         User user = userDAO.selectByName(username);
         if(user != null){
            map.put("msg","用户名已被注册");
            return map;
         }

         //将用户添加到数据库
         user = new User();
         user.setName(username);
         //密码的盐值
         String salt = UUID.randomUUID().toString().substring(0,5);
         user.setSalt(salt);
         user.setPassword(WdptUtil.MD5(password+salt));
         Random random = new Random();
         user.setHeadUrl("../images/res/headUrl"+random.nextInt(10)+".png");
         userDAO.addUser(user);

         //注册成功直接跳转到首页
         //为该用户生成一个loginTicket
         String ticket =  loginTicketService.addLoginTicket(user.getId());
         map.put("ticket",ticket);
         return map;
     }

     /**
     * 处理用户登录业务
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WdptUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

         //登录成功
         //为该用户生成一个loginTicket
         String ticket =  loginTicketService.addLoginTicket(user.getId());
         map.put("ticket",ticket);
         map.put("userId",user.getId());
        return map;
    }

     /**
     * 根据一个用户的Id查询到一个用户
     * @param userId
     * @return
     */
    public User getUser(int userId){
        return userDAO.selectById(userId);
    }

     /**
     * 退出登录时将loginTicket的状态修改为1
     * @param ticket
     */
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    /**
     * 通过用户名查询用户
     * @param name
     * @return
     */
    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }
}
