package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventProducer;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.model.Question;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.CommentService;
import edu.hubu.wdpt.service.FollowService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.service.UserService;
import edu.hubu.wdpt.utils.WdptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/20 17:28
 */
@Controller
public class FollowController {


    @Autowired
    FollowService followService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path={"/followUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
        .setEntityType(EntityType.ENTITY_USER)
        .setEntityId(userId));
        //返回关注的人数
        return WdptUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 取消关注用户
     * @param userId
     * @return
     */
    @RequestMapping(path={"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId));

        return WdptUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 关注问题
     * @param questionId
     * @return
     */
    @RequestMapping(path={"/followQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);

        if(question == null){
            return  WdptUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(question.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId));

        Map<String,Object> info = new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));

        return WdptUtil.getJSONString(ret ? 0:1,info);
    }

    @RequestMapping(path={"/unfollowQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WdptUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);

        if(question == null){
            return  WdptUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(question.getUserId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId));

        Map<String,Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WdptUtil.getJSONString(ret ? 0 : 1, info);

    }


    @RequestMapping(path={"/user/{uid}/followees"},method = {RequestMethod.GET})
    public String followees(@PathVariable("uid") int userId, Model model){

        List<Integer> followeeIds = followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);

        if(hostHolder.getUser() != null){
            model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followeeIds));
        }else{
            model.addAttribute("followees",getUserInfo(0,followeeIds));
        }

        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }


    @RequestMapping(path={"/user/{uid}/followers"},method = {RequestMethod.GET})
    public String followers(Model model,@PathVariable("uid") int userId){

        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser() != null){
             List<Map<String,Object>> followers = getUserInfo(hostHolder.getUser().getId(),followerIds);
            model.addAttribute("followers",followers);
        }else{
            model.addAttribute("followers",getUserInfo(0,followerIds));
        }

        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }


    private List<Map<String,Object>> getUserInfo(int localUserId,List<Integer>  userIds){
        List<Map<String,Object>> userInfos = new ArrayList<>();
        for(Integer uid:userIds){
                User user = userService.getUser(uid);
                if(user == null){
                    continue;
                }
                Map<String,Object> map = new HashMap<>();
                map.put("followUser",user);
                map.put("commentCount", commentService.getUserCommentCount(uid));
                map.put("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
                map.put("followeeCount",followService.getFolloweeCount(uid,EntityType.ENTITY_USER));

                if(localUserId != 0){
                    map.put("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
                }else{
                    map.put("followed",false);
                }
                userInfos.add(map);
        }

        return  userInfos;
    }
}
