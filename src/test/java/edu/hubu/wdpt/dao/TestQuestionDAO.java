package edu.hubu.wdpt.dao;

import edu.hubu.wdpt.model.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * created by Sugar  2018/11/14 11:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuestionDAO {


    @Resource
    QuestionDAO questionDAO;

    /**
     * LIMIT 从第0条开始显示每次显示10
     */
    @Test
    public void testSelectLatest(){
       List<Question> questionList = questionDAO.selectLatestQuestions(13,0,10);
        for (Question question:
             questionList) {
            System.out.println("====================================");
            System.out.println(question);
        }
    }

    @Test
    public void testaddQuestion(){
        Question question = new Question();
         question.setUserId(14);
         question.setCommentCount(0);
         question.setContent("双十一淘宝销售额");
         question.setCreatedDate(new Date());
         question.setTitle("双十一");
        questionDAO.addQuestion(question);
    }

}
