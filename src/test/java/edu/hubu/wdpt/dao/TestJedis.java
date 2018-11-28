package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.User;
import edu.hubu.wdpt.service.LikeService;
import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

/**
 * created by Sugar  2018/11/11 12:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJedis {

    @Autowired
    LikeService likeService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testJedisPool(){
        jedisAdapter.sadd("userxx","123");
        System.out.println(jedisAdapter.sismember("userxx","123") + "=====================");
    }


    @Test
    public void testlikeCount(){

        //点赞
        long likeCount = likeService.like(25, 20, 1);
        System.out.println(likeCount+"==========================");
        likeCount = likeService.like(26, 20, 1);
        System.out.println(likeCount+"==========================");
        long likeCount1 = likeService.getLikeCount(20, 1);
        System.out.println(likeCount1+"==========================");
        int likeStatus = likeService.getLikeStatus(24, 20, 1);
        System.out.println(likeStatus+"=========================");


    }



}
