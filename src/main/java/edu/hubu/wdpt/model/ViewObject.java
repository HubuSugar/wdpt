package edu.hubu.wdpt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Sugar  2018/11/14 17:23
 * 暂时没用到直接在后台用了一个map集合
 */
public class ViewObject {

    private Map<String,Object> map = new HashMap<>();

    public void set(String key,Object value){
        map.put(key,value);
    }

    public Object get(String key){
        return map.get(key);
    }

}
