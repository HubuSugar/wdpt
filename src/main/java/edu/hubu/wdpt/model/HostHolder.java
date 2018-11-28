package edu.hubu.wdpt.model;

import org.springframework.stereotype.Component;

/**
 * created by Sugar  2018/11/12 15:37
 * 利用Ioc思想存放用户信息
 */
@Component
public class HostHolder {

     ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

     public User getUser(){
         return userThreadLocal.get();
     }

     public void setUser(User user){
         userThreadLocal.set(user);
     }

     public void clear(){
         userThreadLocal.remove();
     }


}
