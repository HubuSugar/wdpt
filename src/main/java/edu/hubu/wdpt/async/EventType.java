package edu.hubu.wdpt.async;

/**
 * created by Sugar  2018/11/20 10:11
 * 表示事件的类型
 */
public enum EventType {
     //表示事件的类型
       LIKE(0),
      COMMENT(1),
       LOGIN(2),
      MAIL(3),
       FOLLOW(4),
       UNFOLLOW(5),
       ADD_QUESTION(6);

     private int value;
      EventType(int value){
          this.value = value;
      }
      public int getValue(){
          return value;
      }

}
