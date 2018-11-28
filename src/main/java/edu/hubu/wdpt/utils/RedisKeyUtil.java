package edu.hubu.wdpt.utils;

/**
 * created by Sugar  2018/11/19 11:11
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    //关注列表（粉丝），哪些人关注了你
    private static String BIZ_FOLLOWER= "FOLLOWER";
    //关注对象，关注了哪些人
    private static String BIZ_FOLLOWEE= "FOLLOWEE";

    private static String BIZ_TIMELINE= "TIMELINE";

    //生成喜欢的redis键
    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    //生成不喜欢的redis键
    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    //redis实现赞踩
    public static  String getEventqueue(){
        return BIZ_EVENTQUEUE ;
    }

    //生成follower的键
    public static  String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER+ SPLIT + String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    //生成了followee的键,某一个用户关注的某一类问题的列表
    public static  String getFolloweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE+ SPLIT + String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }


    public static  String getTimelineKey(int userId){
        return  BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

}
