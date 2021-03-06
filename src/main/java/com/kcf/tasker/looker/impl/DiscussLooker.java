package com.kcf.tasker.looker.impl;

import com.kcf.entity.Discuss;
import com.kcf.tasker.looker.Looker;
import com.kcf.tasker.updater.impl.DiscussUpdater;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午5:38
 */
public class DiscussLooker extends Looker<Discuss> {
    private static ESLogger logger = ESLoggerFactory.getLogger("DiscussLooker");

    public DiscussLooker() {
        super();
    }

    public DiscussLooker(Client client) {
        super(client);
    }

    @Override
    protected DateTime getLastUpdateTimeStamp(List<Discuss> data) {
        DateTime tempStamp = super.lastUpdateTimeStamp;

        for(Discuss disc : data){
            DateTime crrUpdated = disc.getUpdated();

            tempStamp = crrUpdated.isAfter(tempStamp)? crrUpdated : tempStamp;
        }

        return tempStamp;
    }

    @Override
    protected List<Discuss> parseRow(ResultSet rs){
        List<Discuss> data = Lists.newArrayList();
        try {
            while (rs.next()) {
                Discuss disc = new Discuss();
                disc.setId(rs.getLong("id"));
                disc.setSubject(rs.getString("subject"));
                disc.setContent(rs.getString("content"));
                disc.setBookId(rs.getLong("bookId"));
                disc.setCreated(rs.getTimestamp("created"));
                disc.setUpdated(rs.getTimestamp("updated"));
                disc.setLastReplied(rs.getTimestamp("lastReplied"));
                disc.setConditionName(rs.getString("name"));
                disc.setViewCount(rs.getInt("viewCount"));
                disc.setReplyCount(rs.getInt("commentCount"));
                disc.setTopped(rs.getBoolean("topped"));
                disc.setUserId(rs.getLong("userBaseId"));

                data.add(disc);
            }
        }catch (SQLException e) {
            logger.error("parse result set failed", e);
        }

        return data;
    }

    @Override
    protected String getSql() {
        return  " select dt.id, dt.subject, dt.content, dt.bookId, " +
                " dt.updated, dt.created, dt.lastReplied, c.name, " +
                " dt.viewCount, dt.commentCount, dt.topped, dt.userBaseId " +
                " from DiscussTopic dt, `Condition` c" +
                " where dt.conditionId = c.id and dt.updated > ?";
    }
}
