package com.kcf.entity;

import org.elasticsearch.common.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午5:34
 *
 * discuss entity
 */
public class Discuss {
    private Long id;
    private String subject;
    private String content;
    private String conditionName;
    private Long bookId;
    private Long userId;
    private int viewCount;
    private int replyCount;
    private DateTime lastReplied;
    private DateTime created;
    private DateTime updated;
    private List<String> replie;
    private boolean topped;

    public DateTime getLastReplied() {
        return lastReplied;
    }

    public void setLastReplied(DateTime lastReplied) {
        this.lastReplied = lastReplied;
    }
    public void setLastReplied(Timestamp lastReplied) {

        this.lastReplied = new DateTime(lastReplied.getTime());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }
    public void setCreated(Timestamp created){
        this.created = new DateTime(created.getTime());
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = new DateTime(updated.getTime());
    }
    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public List<String> getReplie() {
        return replie;
    }

    public void setReplie(List<String> replie) {
        this.replie = replie;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isTopped() {
        return topped;
    }

    public void setTopped(boolean topped) {
        this.topped = topped;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:      ").append(this.id).append("\n");
        sb.append("bookId:  ").append(this.bookId).append("\n");
        sb.append("subject: ").append(this.subject).append("\n");
        sb.append("content: ").append(this.content).append("\n");
        sb.append("conditionName: ").append(this.conditionName).append("\n");

        return sb.toString();
    }
}
