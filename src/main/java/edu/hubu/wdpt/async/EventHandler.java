package edu.hubu.wdpt.async;

import java.util.List;

/**
 * created by Sugar  2018/11/20 10:50
 */
public interface EventHandler {

    //处理哪些事件
    void doHandle(EventModel model);

    //自己关注哪些事件,比如LIKE,LOGIN
    List<EventType>  getSupportEventTypes();


}
