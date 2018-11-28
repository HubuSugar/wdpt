package edu.hubu.wdpt.async.handler;

import edu.hubu.wdpt.async.EventHandler;
import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventType;
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
 * created by Sugar  2018/11/20 12:00
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WdptUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());   //事件的触发者
        String qid = model.getExt("questionId");
        message.setContent("用户"+user.getName()+"赞了你评论，http://127.0.0.1:8080/question/"+model.getExt("questionId"));
         messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
