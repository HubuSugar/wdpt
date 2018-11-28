package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Sugar  2018/11/11 12:45
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     *登录业务
     */
     @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "next",required = false) String next,
                                 @RequestParam(value = "remember",defaultValue = "false") boolean remember,
                                HttpServletResponse  httpServletResponse
                                 ){
         Map<String,Object> map = new HashMap<>();
         try{
             map = userService.login(username,password);
             if(map.containsKey("ticket")){
                 //登陆成功
                 Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                 cookie.setPath("/");
                 if(remember){
                     cookie.setMaxAge(3600 * 24 * 7);
                 }
                 //将cookie返回到客户端
                 httpServletResponse.addCookie(cookie);
               if(StringUtils.isNotBlank(next)){
                   return "redirect:"+next;
               }
                return "redirect:/";
             }else{
                 model.addAttribute("msg",map.get("msg"));
                 return "login";
             }
         }catch(Exception e){
             logger.error("登陆异常" + e.getMessage());
             return "login";
         }
     }

    /**
     * 注册业务访问路径
     */
     @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})
      public String register(Model model,
                                        @RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        @RequestParam(value = "next") String next,
                                        @RequestParam(value = "remember",defaultValue = "false",required = false) boolean remember,
                                        HttpServletResponse  httpServletResponse
                                        ){
        Map<String,Object> map = new HashMap<>();
        try{
            map = userService.register(username,password);
            if(map.containsKey("ticket")){
                //登陆成功
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember){
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                //将cookie返回到客户端
                httpServletResponse.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch(Exception e){
            logger.error("注册异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
      }


      @RequestMapping(path = {"/reglogin"},method = {RequestMethod.GET})
     public String regLoginPage(Model model,
                                @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
      }


    /**
     * 退出页面访问路径,通过CookieValue注解取得浏览器上cookie中的ticket值
     * @return
     */
     @RequestMapping(path={"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
      public String logout(@CookieValue("ticket") String ticket){
            userService.logout(ticket);
            return "redirect:/";
      }


}
