package com.kcf.entity;

import org.elasticsearch.common.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午3:31
 */
public class Book {
    private Long id;
    private String slug;
    private String question;
    private String outline;
    private String story;
    private DateTime created;
    private DateTime updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
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

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public void setUpdated(Timestamp updated){
        this.updated = new DateTime(updated.getTime());
    }
}
