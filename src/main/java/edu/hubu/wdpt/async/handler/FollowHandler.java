package edu.hubu.wdpt.async.handler;

import edu.hubu.wdpt.async.EventHandler;
import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.Message;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.MessageService;
import edu.hubu.wdpt.service.UserService;
import edu.hubu.wdpt.utils.WdptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * created by Sugar  2018/11/20 20:19
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        //产生一个站内信
        Message message = new Message();
        message.setFromId(WdptUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
       return Arrays.asList(EventType.FOLLOW);
    }
}
