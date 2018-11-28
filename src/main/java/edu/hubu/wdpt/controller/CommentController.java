package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventProducer;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.Comment;
import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.service.CommentService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.utils.WdptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * created by Sugar  2018/11/17 13:42
 * 评论的接口
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public  String addComment(@RequestParam("questionId") int questionId,
                              @RequestParam("content") String content){
        try{

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setStatus(0);
            comment.setCreatedDate(new Date());

            if(hostHolder.getUser() == null){
                comment.setUserId(WdptUtil.ANONYMOUS_USERID);
                //return  "redirect:/reglogin";
            }else {
                comment.setUserId(hostHolder.getUser().getId());
            }

            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);

            //增加一条评论之后,获取新的评论数
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId));


        }catch(Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
         return  "redirect:/question/"+questionId;

    }


}
