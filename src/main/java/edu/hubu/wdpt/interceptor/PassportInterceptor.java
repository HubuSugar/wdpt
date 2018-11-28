package edu.hubu.wdpt.interceptor;

import edu.hubu.wdpt.dao.LoginTicketDAO;
import edu.hubu.wdpt.dao.UserDAO;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.model.LoginTicket;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.LoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * created by Sugar  2018/11/16 17:34
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Resource
    LoginTicketDAO loginTicketDAO;

    @Resource
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String ticket = null;
        if(request.getCookies() != null){
            for (Cookie cookie:request.getCookies()
                 ) {
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //验证用户的身份
            if(loginTicket == null||loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                   return true;
            }
            //通过loginTicket获取当前登录的用户身份
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostHolder.getUser() != null){
            modelAndView.addObject("user",hostHolder.getUser());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        hostHolder.clear();
    }
}
