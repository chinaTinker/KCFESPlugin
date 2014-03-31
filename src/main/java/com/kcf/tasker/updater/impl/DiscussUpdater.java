package com.kcf.tasker.updater.impl;

import com.kcf.entity.Discuss;
import com.kcf.tasker.updater.Updater;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午6:21
 */
public class DiscussUpdater extends Updater<Discuss> {
    private static final ESLogger logger = ESLoggerFactory.getLogger("DiscussUpdater");

    public DiscussUpdater(Client client){
        super(client);
    }

    @Override
    protected String builderJson(Discuss disc){
        //TODO
        return  null;
    }

    @Override
    protected String getESType() {
        return "Consult";
    }

    @Override
    protected String getCurrentId(Discuss discuss) {
        return String.valueOf(discuss.getId());
    }
}
