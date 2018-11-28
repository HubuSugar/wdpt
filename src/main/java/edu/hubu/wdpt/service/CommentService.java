package edu.hubu.wdpt.service;

import edu.hubu.wdpt.dao.CommentDAO;
import edu.hubu.wdpt.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by Sugar  2018/11/17 10:44
 * 评论服务，面向所有实体
 */
@Service
public class CommentService {

        private  static final Logger logger = LoggerFactory.getLogger(CommentService.class);

        @Resource
        CommentDAO commentDAO;

        @Autowired
        SensitiveService sensitiveService;

    /**
     * 查询某一实体下的所有用户的评论
     * @param entityId
     * @param entityType
     * @return
     */
        public List<Comment> getCommentsByEntity(int entityId,int entityType){
            return commentDAO.selectCommentByEntity(entityId,entityType);
        }


        /**
            *  添加一条评论
           * @param comment
           * @return
        */
        public int addComment(Comment comment){
            //添加一条评论，需要进行敏感词过滤
            comment.setContent(sensitiveService.filter(comment.getContent()));
            comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

            return commentDAO.addComment(comment) > 0 ?comment.getId() : 0;
        }

    /**
     * 获取某一实体下的评论总数
     */
        public int getCommentCount(int entityId,int entityType){
            return commentDAO.getCommentCount(entityId,entityType);
        }

     /**
     * 删除一条评论实际上是将其标志置为1
     */
     public boolean deleteComment(int commentId){
         return  commentDAO.updateStatus(commentId,1) > 0;
     }


     public Comment getCommentById(int commentId){
         return commentDAO.getCommentById(commentId);
     }

     public int getUserCommentCount(int userId){
         return commentDAO.getUserCommentCount(userId);
     }
}
