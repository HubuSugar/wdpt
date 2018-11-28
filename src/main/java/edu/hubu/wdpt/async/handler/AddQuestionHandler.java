package edu.hubu.wdpt.async.handler;

import edu.hubu.wdpt.async.EventHandler;
import edu.hubu.wdpt.async.EventModel;
import edu.hubu.wdpt.async.EventType;
import edu.hubu.wdpt.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * created by Sugar  2018/11/26 14:57
 */
@Component
public class AddQuestionHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {

        try{
            searchService.indexQuestion(model.getEntityId(),model.getExt("title"),model.getExt("content"));
        }catch (Exception e){
            logger.error("增加题目索引失败"+e.getMessage());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
