package edu.hubu.wdpt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * created by Sugar  2018/11/19 10:42
 * 操作redis
 */
@Service
public class JedisAdapter{

     private static  final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

     private static JedisPool jedisPool;


         //连接实例的最大连接数
         private static int MAX_ACTIVE = 1024;

         //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
         private static int MAX_IDLE = 200;

        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
        private static int MAX_WAIT = 10000;

        //连接超时的时间　　
        private static int TIMEOUT = 10000;

        //连接的主机
        private static final  String  HOST = "localhost";

        //连接的端口
        private static final int   PORT =  6379;

        //连接的密码
        private static final  String AUTH = "123456";

        //表示使用第九个数据库
        private static final  int DATABASE = 9;

        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        private static boolean TEST_ON_BORROW = true;

      static {

                try {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(MAX_ACTIVE);
                    config.setMaxIdle(MAX_IDLE);
                    config.setMaxWaitMillis(MAX_WAIT);
                    config.setTestOnBorrow(TEST_ON_BORROW);
                    jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, AUTH,DATABASE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

    }

        //使用set集合实现点赞点踩的功能
        //get set
        public long  sadd(String key,String value){
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return  jedis.sadd(key,value);
            }catch (Exception e){
                logger.error("发生异常"+e.getMessage());
            }finally {
                if(jedis != null){
                    jedis.close();
                }
            }
            return 0;
        }

        public long  srem(String key,String value){
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return  jedis.srem(key,value);
            }catch (Exception e){
                logger.error("发生异常"+e.getMessage());
            }finally {
                if(jedis != null){
                    jedis.close();
                }
            }
            return 0;
        }

        //集合中元素的个数
        public long  scard(String key){
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return  jedis.scard(key);
            }catch (Exception e){
                logger.error("发生异常"+e.getMessage());
            }finally {
                if(jedis != null){
                    jedis.close();
                }
            }
            return 0;
        }

        public boolean  sismember(String key,String value){
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return  jedis.sismember(key,value);
            }catch (Exception e){
                logger.error("发生异常"+e.getMessage());
            }finally {
                if(jedis != null){
                    jedis.close();
                }
            }
            return false;
        }

       //链表
        public long lpush(String key,String value){
             Jedis jedis= null;
              try{
                  jedis = jedisPool.getResource();
                  return jedis.lpush(key,value);
              }catch (Exception e){
                  logger.error("发生异常" + e.getMessage());
               }finally {
                  if(jedis != null){
                      jedis.close();
                  }
              }
              return 0;
        }

    //链表
    public List<String> lrange(String key,int start,int end){
        Jedis jedis= null;
        try{
            jedis = jedisPool.getResource();
            return jedis.lrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }


    public List<String> brpop(int timeout, String key){
        Jedis jedis= null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis(){
          return jedisPool.getResource();
    }

    public Transaction multi(Jedis jedis){
          try{
              return jedis.multi();
          }catch(Exception e){
              logger.error("发生异常"+e.getMessage());
          }
          return null;
    }


    public  List<Object> exec(Transaction tx,Jedis jedis){
          try{
              return tx.exec();
          }catch (Exception e){
              logger.error("发生异常"+e.getMessage());
          }finally {
                if(tx != null){
                    try{
                        tx.close();
                    }catch(IOException ioe){
                        logger.error("发生异常"+ioe.getMessage());
                    }
                }
                if(jedis != null){
                    jedis.close();
                }
          }
          return null;
    }

    public long zadd(String key, double score,String value){
          Jedis jedis = null;
          try{
              jedis = jedisPool.getResource();
              return  jedis.zadd(key,score,value);
          }catch (Exception e){
              logger.error("发生异常"+e.getMessage());
          }finally {
              if(jedis != null){
                  jedis.close();
              }
          }
          return 0;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return  jedis.zrevrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return  jedis.zcard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key,String member){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return  jedis.zscore(key,member);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }
}
