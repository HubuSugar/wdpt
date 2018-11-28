package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.QuestionDAO;
import edu.hubu.wdpt.model.HostHolder;
import edu.hubu.wdpt.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;


import java.util.List;

/**
 * created by Sugar  2018/11/14 17:15
 */
@Service
public class QuestionService {


     @Autowired
    QuestionDAO questionDAO;

     @Autowired
    HostHolder hostHolder;

     @Autowired
     SensitiveService sensitiveService;

    /**
     *首页展示发布的问题
     */
     public List<Question> getLatestQuestions(int userId,int offset,int limit){
         return  questionDAO.selectLatestQuestions(userId,offset,limit);
     }

     /**
     * 添加一个问题
     */
     public  int addQuestion(Question question){

         question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
         question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

         return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    /**
     * 通过id查询一个问题
     * @param questionId
     * @return
     */
    public Question getQuestionById(int questionId){
           return questionDAO.getById(questionId);
    }

    /**
     * 通过问题id更新问题的评论数
     * @param id
     * @param count
     * @return
     */
    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }


}
