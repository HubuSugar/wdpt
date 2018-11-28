package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.model.Question;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.CommentService;
import edu.hubu.wdpt.service.FollowService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/14 10:37
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    /**
     *首页展示
     */
    @RequestMapping(path = {"/index","/"},method = {RequestMethod.GET})
    public String home(Model model){
        List<Map<String,Object>> vos = getQuestions(0,0,10);
        model.addAttribute("vos",vos);
        return "index";
    }


    @RequestMapping(path = {"/user/{userId}"},method = RequestMethod.GET)
    public String userIndex(Model model, @PathVariable("userId") int userId){
        List<Map<String,Object>> vos = getQuestions(userId,0,10);
        model.addAttribute("vos",vos);


        User user = userService.getUser(userId);

        Map<String,Object> map = new HashMap<String,Object>();

        map.put("userInfo",user);
        int commentCount = commentService.getUserCommentCount(userId);
        map.put("commentCount",commentCount);
        long followerCount = followService.getFollowerCount(EntityType.ENTITY_USER,userId);
        map.put("followerCount",followerCount);
        long followeeCount = followService.getFolloweeCount(userId,EntityType.ENTITY_USER);
        map.put("followeeCount",followeeCount);

        if (hostHolder.getUser() != null) {
            map.put("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            map.put("followed", false);
        }

        model.addAttribute("profileUser",map);

        return "profile";
    }


    /**
     * 返回公共的vos
     * 读取关于当前用户的10个问题
     */
    public List<Map<String,Object>> getQuestions(int userId,int offset,int limit){
        List<Question> questionList = questionService.getLatestQuestions(userId,offset,limit);
        List<Map<String,Object>> vos = new ArrayList<>();
        for (Question question:questionList) {
            Map<String,Object> obj = new HashMap<>();
            obj.put("question",question);
            obj.put("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            obj.put("user",userService.getUser(question.getUserId()));
            vos.add(obj);
        }
        return vos;
    }


}
