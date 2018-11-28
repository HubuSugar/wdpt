package edu.hubu.wdpt.controller;

import edu.hubu.wdpt.model.EntityType;
import edu.hubu.wdpt.model.Question;
import edu.hubu.wdpt.service.FollowService;
import edu.hubu.wdpt.service.QuestionService;
import edu.hubu.wdpt.service.SearchService;
import edu.hubu.wdpt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/25 20:35
 */
@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private static final String HLPRE = "<font color = \'red\'>";
    private static final String HLPOS = "</font>";

    @Autowired
    SearchService searchService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @RequestMapping(path={"/search"},method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset",defaultValue = "0")  int offset,
                         @RequestParam(value ="count",defaultValue = "10") int count){
        try{
            List<Question> questionList = searchService.searchQuestion(keyword,offset,count,HLPRE,HLPOS);

            List<Map<String,Object>> questions = new ArrayList<>();
            for(Question question:questionList){

                Question q = questionService.getQuestionById(question.getId());

                Map<String,Object> map = new HashMap<>();

                if(question.getContent() != null){
                    q.setContent(question.getContent());
                }

                if(question.getTitle() != null){
                    q.setTitle(question.getTitle());
                }

                map.put("question",q);
                map.put("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                map.put("user", userService.getUser(q.getUserId()));
                questions.add(map);
            }
            model.addAttribute("questions", questions);
            model.addAttribute("keyword", keyword);
        }catch (Exception e){
            logger.error("搜索出现异常"+e.getMessage());
        }

        return "result";
    }



}
