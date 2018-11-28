package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.MessageDAO;
import edu.hubu.wdpt.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by Sugar  2018/11/17 21:17
 */
@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    SensitiveService  sensitiveService;

    @Resource
    MessageDAO messageDAO;

    /**
     * 增加站内信
     */
    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    /**
     * 查询站内信
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    /**
     * 当前用户发给别人或者别人发给当前用户的消息分组的列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationList(int userId,int offset,int limit){
      return   messageDAO.getConversationList(userId,offset,limit);
    }

    /**
     * 查询当前用户某一个会话未读的消息的总记录
     * @param userId
     * @param conversationId
     * @return
     */
    public int getConversationUnreadCount(int userId,String conversationId){
       return  messageDAO.getConversationUnreadCount(userId,conversationId);
    }

}
