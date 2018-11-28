package edu.hubu.wdpt.async.handler;

import com.alibaba.fastjson.JSONObject;
import edu.hubu.wdpt.async.EventHandler;
import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.Feed;
import edu.hubu.wdpt.model.Question;
import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.FeedService;
import edu.hubu.wdpt.service.FollowService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.service.UserService;
import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * created by Sugar  2018/11/21 23:29
 */
@Component
public class FeedHandler implements EventHandler {


    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());

        if(actor == null){
            return  null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        if(model.getType() == EventType.COMMENT || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION))
        {
            Question question = questionService.getQuestionById(model.getEntityId());

            if(question ==null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);

        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {

        // 为了测试，把model的userId随机一下
        //Random r = new Random();
        //model.setActorId(1+r.nextInt(10));

        //产生一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));

        if(feed.getData() == null){
            return;
        }
        feedService.addFeed(feed);

        //给事件的粉丝推
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        followers.add(0);

        for(int follower:followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
