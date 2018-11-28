package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.User;
import org.apache.ibatis.annotations.*;

/**
 * created by Sugar  2018/11/11 11:53
 */
@Mapper
public interface UserDAO {

    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, "+INSET_FIELDS;

    /**
     * 添加一个用户
     * @param user
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    /**
     * 通过id查询用户信息
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    /**
     * 通过姓名查询一个用户，用于用户注册检测用户名是否重复
     * @param name
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    /**
     * 更新用户的密码
     * @param user
     */
    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    /**
     * 删除用户
     * @param id
     */
    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);



}
