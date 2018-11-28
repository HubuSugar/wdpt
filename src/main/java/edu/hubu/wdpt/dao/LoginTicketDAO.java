package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * created by Sugar  2018/11/11 12:53
 */
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = "login_Ticket";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 向loginTicket表中添加一个loginTicket
     * @param ticket
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    /**
     * 通过ticket查询一个loginTicket,从而从loginTicket获取用户ID
     * @param ticket
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新用户登录信息
     * @param ticket
     * @param status
     */
    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);


}
