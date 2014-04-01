package com.kcf.tasker.updater.impl;

import com.kcf.entity.Book;
import com.kcf.entity.Discuss;
import com.kcf.entity.User;
import com.kcf.repo.BookRepo;
import com.kcf.repo.DiscussRepo;
import com.kcf.repo.HospitalRepo;
import com.kcf.repo.UserRepo;
import com.kcf.tasker.looker.impl.DiscussLooker;
import com.kcf.tasker.updater.Updater;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午6:21
 */
public class DiscussUpdater extends Updater<Discuss> {
    private static final ESLogger logger = ESLoggerFactory.getLogger("DiscussUpdater");

    private HospitalRepo hospitalRepo = new HospitalRepo();
    private BookRepo     bookRepo = new BookRepo();
    private UserRepo     userRepo = new UserRepo();
    private DiscussRepo  discussRepo = new DiscussRepo();

    public DiscussUpdater(Client client){
        super(client);
    }

    @Override
    protected String builderJson(Discuss disc){
        String json = null;
        try {
            Book book = bookRepo.getBookById(disc.getBookId());
            User user = userRepo.getUserById(disc.getUserId());

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("conditions",    disc.getConditionName());
            builder.field("crrHospital",   hospitalRepo.getHospitals(disc.getBookId()));
            builder.field("datetime",      disc.getCreated().toString("yyyy-MM-dd HH:mm:ss"));
            builder.field("description",   disc.getContent());
            builder.field("discussId",     disc.getId());
            builder.field("helps",         book != null? book.getQuestion() : null);
            builder.field("id",            disc.getId());
            builder.field("patientId",     user.getId());
            builder.field("patientAge",    user.getAge());
            builder.field("patientGender", user.getGender());
            builder.field("patientName",   user.getName());
            builder.field("bookSlug",      book != null? book.getSlug() : null);
            builder.field("replies",       discussRepo.getReplies(disc.getId()));
            builder.field("replyCount",    disc.getReplyCount());
            builder.field("lastReplyTime", disc.getLastReplied().toString("yyyy-MM-dd Hh:mm:ss"));
            builder.field("title",         disc.getSubject());
            builder.field("viewCount",     disc.getViewCount());
            builder.field("topped",        disc.isTopped()? 1 : 0);

            builder.endObject();

            json = builder.string();
        } catch (IOException e) {
            logger.error("create {} json string failed", e, super.table);
        }

        return  json;
    }

    @Override
    protected String getESType() {
        return "consult";
    }

    @Override
    protected String getCurrentId(Discuss discuss) {
        return String.valueOf(discuss.getId());
    }


}
