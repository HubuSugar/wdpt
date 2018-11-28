package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * created by Sugar  2018/11/14 10:50
 */
@Mapper
public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     *发布一个问题
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    /**
     * 支持分页查询最近发布的问题，需要在配置文件中自定义SQl语句QuestionDAO.xml
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    /**
     *通过id查询一个问题
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question getById(int id);

    /**
     *更新每一个问题的评论数
     */
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
