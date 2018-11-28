package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.FeedDAO;
import edu.hubu.wdpt.model.Feed;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by Sugar  2018/11/21 23:15
 */
@Service
public class FeedService {

    @Resource
    FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId,List<Integer> userIds,int count){
        return feedDAO.selectUserFeeds(maxId,userIds,count);
    }

    public boolean addFeed(Feed feed){
         feedDAO.addFeed(feed);
         return feed.getId() >0;
    }

    public Feed getFeedById(int id) {
        return feedDAO.getFeedById(id);
    }
}
