package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * created by Sugar  2018/11/17 10:31
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加一条评论
     * @param comment
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    /**
     * 通过Id查询一个评论
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Comment getCommentById(int id);

    /**
     * 查询某一个实体下所有用户的评论
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     * 查询某一个实体下所有的评论数
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select count(id)  from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

     /**
     * 更新评论状态
     * @param id
     * @param status
     * @return
     */
    @Update({"update comment set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);


    /**
     * 通过userId查询用户评论数
     * @param userId
     * @return
     */
    @Select({"select count(id)  from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);





}
