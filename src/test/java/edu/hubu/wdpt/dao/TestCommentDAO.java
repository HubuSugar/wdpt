package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.Comment;
import edu.hubu.wdpt.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * created by Sugar  2018/11/11 12:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCommentDAO {


    @Autowired
    CommentDAO  commentDAO;

    @Test
    public void testaddComment(){
        Comment comment = new Comment();
        comment.setContent("评论内容1");
        comment.setEntityId(1);
        comment.setEntityType(2);
        comment.setUserId(1);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        int k = commentDAO.addComment(comment);
        System.out.println(k + "=============================");
    }

    @Test
    public void testgetCommentById(){
        Comment comment = commentDAO.getCommentById(1);
        System.out.println(comment);
    }

    @Test
    public void testselectCommentByEntity(){
       List<Comment>  comment = commentDAO.selectCommentByEntity(1,2);
        System.out.println(comment);
    }

    @Test
    public void testgetCommentCount(){
        int commentCount = commentDAO.getCommentCount(1, 2);
        System.out.println(commentCount + "======================");
    }

    @Test
    public void testupdateStatus(){
        int i = commentDAO.updateStatus(1, 1);
        System.out.println(i + "======================");
    }


}
