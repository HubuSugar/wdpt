package edu.hubu.wdpt.service;

import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Sugar  2018/11/19 10:58
 *
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId,int entityId,int entityType){

        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1:0;
    }


    public long like(int userId,int  entityType,int entityId){

             //点赞业务的时候先将userId加到likeKey中
              String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
              jedisAdapter.sadd(likeKey,String.valueOf(userId));

             //点踩业务的时候， 将userId从disLikeKey中移除
              String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
              jedisAdapter.srem(disLikeKey,String.valueOf(userId));

              return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId,int  entityType,int entityId){

        //点赞业务的时候先将userId加到likeKey中
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }


}
