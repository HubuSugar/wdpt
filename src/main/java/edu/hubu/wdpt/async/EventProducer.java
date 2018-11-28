package edu.hubu.wdpt.async;

import com.alibaba.fastjson.JSONObject;
import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Sugar  2018/11/20 10:33
 * 用Redis来实现异步队列
 */
@Service
public class EventProducer {

        @Autowired
      JedisAdapter jedisAdapter;

    /**
     * 将事件推进redis的阻塞队列
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel){

        try{
            //将事件的现场序列化
            String json = JSONObject.toJSONString(eventModel);
          //  System.out.println(json);
            //获取redis中保存这些事件的队列
            String eventKey = RedisKeyUtil.getEventqueue();
            jedisAdapter.lpush(eventKey,json);
            return true;
        }catch (Exception e){
            return false;
        }

    }

}
