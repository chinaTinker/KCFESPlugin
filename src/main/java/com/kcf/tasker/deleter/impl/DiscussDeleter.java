package com.kcf.tasker.deleter.impl;

import com.kcf.repo.DiscussRepo;
import com.kcf.tasker.deleter.Deleter;
import org.elasticsearch.client.Client;

import java.util.Collection;


/**
 * Author: 老牛
 * Date:   14-4-2
 */
public class DiscussDeleter extends Deleter{
    private DiscussRepo discussRepo = new DiscussRepo();

    public DiscussDeleter(String table, Client client) {
        super(table, client);
    }

    public DiscussDeleter(String table, long delay, Client client) {
        super(table, delay, client);
    }

    @Override
    protected Collection<Long> getAllIdFromDB() {
        return discussRepo.getAllIds();
    }

    @Override
    protected String getESType() {
        return "consult";
    }
}
