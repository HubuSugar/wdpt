package edu.hubu.wdpt.service;

import edu.hubu.wdpt.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/25 14:18
 */
@Service
public class SearchService {


     private static final String SOLR_URL = "http://127.0.0.1:8983/solr/wdpt";
     private HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(SOLR_URL).build();
     private  static final String QUESTION_TITLE_FIELD = "question_title";
     private  static final  String QUESTION_CONTENT_FIELD = "question_content";


    /**
     * 搜索服务
     * @param keyWord 关键字
     * @param offset   偏移位置
     * @param count   便宜数
     * @param hlPre   前缀标签
     * @param hlPos   后缀标签
     * @return  查询到的问题列表
     */
    public List<Question> searchQuestion(String keyWord,int offset,int count,String hlPre,String hlPos)
            throws IOException, SolrServerException {
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyWord);
        query.setRows(count);
        query.setStart(offset);
        //开启高亮,设置前缀，后缀
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        //返回到客户端的页面
        QueryResponse queryResponse = httpSolrClient.query(query);


        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();


        for (Map.Entry<String, Map<String, List<String>>> entry : queryResponse.getHighlighting().entrySet()) {
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));

            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    question.setContent(contentList.get(0));
                }
            }


            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                    if (titleList.size() > 0) {
                        question.setTitle(titleList.get(0));
                    }
            }
            questionList.add(question);
    }
           return questionList;
    }


    public boolean indexQuestion(int qid,String title,String content) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id",qid);
        doc.setField(QUESTION_TITLE_FIELD,title);
        doc.setField(QUESTION_CONTENT_FIELD,title);
        UpdateResponse response =  httpSolrClient.add(doc,5000);
        return response != null && response.getStatus() == 0;
    }

}
