package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.LoginTicketDAO;
import edu.hubu.wdpt.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * created by Sugar  2018/11/11 20:04
 */
@Service
public class LoginTicketService {

     @Autowired
    LoginTicketDAO loginTicketDAO;

    /**
     * 添加一个loginTicket,同时将生成的ticket返回，用于到时候通过map集合返回给前台
     */
    public String addLoginTicket(int userId){
         LoginTicket ticket = new LoginTicket();
         ticket.setUserId(userId);
         Date date = new Date();
         //当前时间加上30天
         date.setTime(date.getTime() + 1000*3600*24);
         ticket.setExpired(date);
         ticket.setStatus(0);   //状态0表示正常，1表示无效
         ticket.setTicket(UUID.randomUUID().toString().replace("-",""));
         loginTicketDAO.addTicket(ticket);
         return ticket.getTicket();
     }


}
