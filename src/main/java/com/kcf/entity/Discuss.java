package com.kcf.entity;

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
    private long id;
    private String subject;
    private String content;
    private long condId;
    private long lastReplied;
    private long created;
    private long updated;
    private List<String> replie;

    public long getLastReplied() {
        return lastReplied;
    }

    public void setLastReplied(long lastReplied) {
        this.lastReplied = lastReplied;
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

    public long getCondId() {
        return condId;
    }

    public void setCondId(long condId) {
        this.condId = condId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public List<String> getReplie() {
        return replie;
    }

    public void setReplie(List<String> replie) {
        this.replie = replie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:      ").append(this.id).append("\n");
        sb.append("subject: ").append(this.subject).append("\n");
        sb.append("content: ").append(this.content).append("\n");

        return sb.toString();
    }
}
