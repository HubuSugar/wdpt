package edu.hubu.wdpt.service;

import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * created by Sugar  2018/11/20 16:11
 * 关注服务
 */
@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 用户关注了某个实体，可以关注问题，关注用户，关注评论等任何实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId,int entityType,int entityId){

        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //粉丝列表中保存的是粉丝的userId
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //关注对象列表中保存的是实体id
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx,jedis);

        return ret.size() == 2&& (long)ret.get(0) > 0&& (long)ret.get(1) > 0;
    }


    public boolean unfollow(int userId,int entityType,int entityId){

        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        //粉丝列表中保存的是粉丝的userId
        tx.zrem(followerKey,String.valueOf(userId));
        //关注对象列表中保存的是实体id
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2&& (long)ret.get(0) > 0&& (long)ret.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> list = new ArrayList<>();
        for (String s:idset
             ) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public List<Integer> getFollowers(int entityType,int entityId,int count){
        //获取粉丝列表
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }

    /**
     * 带翻页功能获取粉丝列表
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        //获取粉丝列表
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset, offset + count));
    }

    public List<Integer> getFollowees(int userId,int entityType,int count){
        //获取关注对象列表
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,0,count));

    }

    public List<Integer> getFollowees(int userId,int entityType,int offset,int count){
        //获取关注对象列表
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey,offset,offset + count));
    }

    //粉丝数
    public long getFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }

    //关注对象数
    public long getFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    //是否是这个实体的关注者
    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId)) != null;
    }

}
