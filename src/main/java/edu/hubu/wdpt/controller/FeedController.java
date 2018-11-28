package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.Feed;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.service.FeedService;
import edu.hubu.wdpt.service.FollowService;
import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Sugar  2018/11/22 13:49
 */
@Controller
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path={"/pullfeeds"},method = {RequestMethod.POST, RequestMethod.GET})
    public  String getPullFeeds(Model model){

        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);


        return "feeds";
    }

    @RequestMapping(path={"/pushfeeds"},method = {RequestMethod.POST, RequestMethod.GET})
    public  String getPushFeeds(Model model){

        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        String timelineKey = RedisKeyUtil.getTimelineKey(localUserId);

        List<String> feedIds = jedisAdapter.lrange(timelineKey,0,10);
        List<Feed> feeds = new ArrayList<>();

        for(String feedId:feedIds){
            Feed feed =  feedService.getFeedById(Integer.parseInt(feedId));
            if(feed == null){
                continue;
            }
            feeds.add(feed);
        }

        model.addAttribute("feeds",feeds);
        return "feeds";
    }


}
