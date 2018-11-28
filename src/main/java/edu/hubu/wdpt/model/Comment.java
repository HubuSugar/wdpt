package edu.hubu.wdpt.model;

import java.util.Date;

/**
 * created by Sugar  2018/11/17 10:28
 * 评论实体
 */
public class Comment {

    private int id;
    private int userId;
    private int entityId;
    private int entityType;
    private String Content;
    private Date createdDate;
    private int status;    //评论状态，区分评论是否正常

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", entityId=" + entityId +
                ", entityType=" + entityType +
                ", Content='" + Content + '\'' +
                ", createdDate=" + createdDate +
                ", status=" + status +
                '}';
    }
}
