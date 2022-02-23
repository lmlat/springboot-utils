package com.github.akor.model;

import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-10-24 22:05
 * @Description : 牛客面试题
 */
public class NowCoderInterviewQuestion {
    private String id;//ID
    private String specialColumn;//专栏
    private String type;//试题类型，简答题等
    private String problem;//提出的问题
    private String answer;//参考答案
    private List<String> commentIds;//评论Ids
    private String createTime;//创建时间
    private String updateTime;//更新时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialColumn() {
        return specialColumn;
    }

    public void setSpecialColumn(String specialColumn) {
        this.specialColumn = specialColumn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }
}