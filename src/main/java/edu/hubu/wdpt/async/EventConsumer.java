package edu.hubu.wdpt.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.hubu.wdpt.utils.JedisAdapter;
import edu.hubu.wdpt.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/20 10:52
 * 实现bean生命周期中的InitializingBean接口，项目启动之后就是执行afterPropertiesSet()方法将事件注册到容器中
 */
@Service
public class EventConsumer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
     private Map<EventType, List<EventHandler>>  config = new HashMap<>();

     private ApplicationContext applicationContext;

     @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {

        //找出spring上下文中所有的applicationContext的是实现类
       Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

       if(beans != null){
           for(Map.Entry<String,EventHandler> entry: beans.entrySet()){
               //找到了EventHandler关注的事件
               List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

               for(EventType type:eventTypes){
                   if(!config.containsKey(type)){
                       //如果config中不包含这个类型的事件，那么将这个事件加入config
                       config.put(type,new ArrayList<EventHandler>());

                   }
                   config.get(type).add(entry.getValue());
               }
           }
       }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventqueue();
                    List<String> events = jedisAdapter.brpop(0, key);

                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
    }

}
