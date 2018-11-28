package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventProducer;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.Comment;
import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.service.CommentService;
import edu.hubu.wdpt.service.LikeService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.utils.WdptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by Sugar  2018/11/19 10:58
 * 点赞的业务
 */
@Controller
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    /**
     * 点赞业务
     * @param commentId
     * @return
     */
    @RequestMapping(path={"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);
        int questionId = comment.getEntityId();

        EventModel  eventModel =   new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setExt("questionId",String.valueOf(questionId))
                .setEntityOwnerId(comment.getUserId());
        eventProducer.fireEvent(eventModel);

        //点赞的时候返回点赞后的人数
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        //通过JSON返回点赞后的人数
        return WdptUtil.getJSONString(0,String.valueOf(likeCount));
    }

    /**
     * 踩的业务
     * @param commentId
     * @return
     */
    @RequestMapping(path={"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }
        //踩后返回总人数
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        //通过JSON返回点踩后的人数
        return WdptUtil.getJSONString(0,String.valueOf(likeCount));
    }


}
