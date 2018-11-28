package edu.hubu.wdpt.async;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Sugar  2018/11/20 10:14
 * 表示事件发生的现场
 */
public class EventModel {

    private EventType type;   //事件类型
    private int actorId;          //事件的触发者
    private int entityType;    //触发的载体
    private int entityId;    //触发的载体
    private int entityOwnerId; //事件的接受者
    private Map<String,String> exts = new HashMap<>();   //扩展字段

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    /**
     * 比如一个用户的点赞行为，事件类型：点赞LIKE,事件触发者:谁点了赞，触发的载体：给什么对象点了赞
     */



    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
            this.type = type;
            return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return  this;
    }


    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
