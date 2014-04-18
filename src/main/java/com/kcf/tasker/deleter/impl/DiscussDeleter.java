package com.kcf.tasker.deleter.impl;

import com.kcf.entity.Discuss;
import com.kcf.repo.DiscussRepo;
import com.kcf.tasker.deleter.Deleter;
import org.elasticsearch.client.Client;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Collection;


/**
 * Author: 老牛
 * Date:   14-4-2
 */
public class DiscussDeleter extends Deleter<Discuss>{
    private static final DiscussRepo discussRepo = new DiscussRepo();

    public DiscussDeleter() {
        super();
    }

    public DiscussDeleter(Client client) {
        super(client);
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
