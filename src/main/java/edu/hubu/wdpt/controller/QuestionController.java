package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventProducer;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.*;
import edu.hubu.wdpt.service.*;
import edu.hubu.wdpt.utils.WdptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * created by Sugar  2018/11/16 18:22
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 用于添加问题
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(path={"/question/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                            @RequestParam("content") String content){

       try{
           Question question = new Question();
           question.setTitle(title);
           question.setContent(content);
           question.setUserId(hostHolder.getUser().getId());
           question.setCommentCount(0);
           question.setCreatedDate(new Date());
           if(hostHolder.getUser() == null){
               question.setUserId(WdptUtil.ANONYMOUS_USERID);
           } else {
               question.setUserId(hostHolder.getUser().getId());
           }
           if(questionService.addQuestion(question) > 0){

               eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                       .setActorId(question.getUserId()).setEntityId(question.getId())
                       .setExt("title",question.getTitle()).setExt("content",question.getContent()));

               return WdptUtil.getJSONString(0);
           }

       }catch (Exception e){
           logger.error("增加问题失败"+e.getMessage());
       }

    return WdptUtil.getJSONString(1,"失败");
    }


    /**
     * 用于查看问题详情
     */
    @RequestMapping(path = {"/question/{qid}"})
    public  String  showQuestionDetail(Model model, @PathVariable("qid") int qid){

        Question question = questionService.getQuestionById(qid);
        model.addAttribute("question",question);
        //首页的上显示的用户信息，或者换成hostHolder
      //  model.addAttribute("user",userService.getUser(question.getUserId()));
        //问题的所有评论
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<Map<String,Object>> comments = new ArrayList<>();
        for (Comment comment:commentList
             ) {
            Map<String,Object> map = new HashMap<>();
            map.put("comment",comment);

            //点赞状态
            if(hostHolder.getUser() == null){
                map.put("liked",0);
            }else{
                int likeStatus = likeService.getLikeStatus(hostHolder.getUser().getId(),comment.getId(),EntityType.ENTITY_COMMENT);
                map.put("liked",likeStatus);
            }
            long likeCount = likeService.getLikeCount(comment.getId(),EntityType.ENTITY_COMMENT);
            map.put("likeCount",likeCount);

            //评论这个问题的用户
            map.put("commentUser",userService.getUser(comment.getUserId()));
            comments.add(map);
        }
        model.addAttribute("comments",comments);

         //关注服务
         List<Map<String,Object>> followUsers = new ArrayList<>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            Map<String,Object> map = new HashMap<>();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            map.put("name", u.getName());
            map.put("headUrl", u.getHeadUrl());
            map.put("id", u.getId());
            followUsers.add(map);
        }
        model.addAttribute("followUsers", followUsers);

        if(hostHolder.getUser() != null){
            model.addAttribute("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,qid));
        }else{
            model.addAttribute("followed",false);
        }



        return "detail";
    }


}
