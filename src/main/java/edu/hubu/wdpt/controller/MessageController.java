package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.model.Message;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.MessageService;
import edu.hubu.wdpt.service.UserService;
import edu.hubu.wdpt.utils.WdptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * created by Sugar  2018/11/16 11:59
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;


    /**
     * 页面上的弹框，所以返回Json
     * @param toName
     * @param content
     * @return
     */
    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName")  String  toName,
                             @RequestParam("content")  String content){
        try{

            if(hostHolder.getUser() == null){
                return WdptUtil.getJSONString(999,"未登录");
            }

            User user = userService.selectByName(toName);
            if(user == null){
                return WdptUtil.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());

            messageService.addMessage(message);
            return WdptUtil.getJSONString(0);

        }catch (Exception e){
            logger.error("发送消信失败"+e.getMessage());
            return WdptUtil.getJSONString(1,"发送失败");
        }
    }

        @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
        public String getConversationList(Model model){
        //当前用户为空的话，需要重新登录
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }
        //当前用户Id
        int localUserId = hostHolder.getUser().getId();

        List<Message> messageList = messageService.getConversationList(localUserId,0,10);
        List<Map<String,Object>> conversationList = new ArrayList<>();
        for(Message message:messageList){
            Map<String,Object> map = new HashMap<>();
            //查询每一条消息
            map.put("messageConversation",message);
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            map.put("targetUser",userService.getUser(targetId));
            //统计未读的消息
            int unreadCount =  messageService.getConversationUnreadCount(localUserId,message.getConversationId());
            map.put("unreadCount",unreadCount);

            conversationList.add(map);
         }
          model.addAttribute("conversationList",conversationList);
          return "letter";
       }

    /**
     * 获取详情
     * @param model
     * @param conversationId
     * @return
     */
       @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
       public String getConversationDetail(Model model,
                                           @RequestParam("conversationId")  String  conversationId){
            try{
                List<Message> conversationDetail = messageService.getConversationDetail(conversationId, 0, 10);
                List<Map<String,Object>>  messageList= new ArrayList<>();
                for (Message message:conversationDetail
                     ) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("messageDetail",message);
                    map.put("messageUser",userService.getUser(message.getFromId()));
                    messageList.add(map);
                }
                model.addAttribute("messageList",messageList);
            }catch (Exception e){
                logger.error("获取详情失败"+e.getMessage());
            }
           return "letterDetail";
       }




}
