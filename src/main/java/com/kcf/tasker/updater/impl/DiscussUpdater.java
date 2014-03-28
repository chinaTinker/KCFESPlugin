package com.kcf.tasker.updater.impl;

import com.kcf.entity.Discuss;
import com.kcf.tasker.updater.Updater;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午6:21
 */
public class DiscussUpdater extends Updater<Discuss> {
    private static final ESLogger logger = ESLoggerFactory.getLogger("DiscussUpdater");

    @Override
    protected void dealData(Object obj) {
        if(obj instanceof List){
            List<Discuss> discusses = (List<Discuss>) obj;

            logger.info("There commes {} records: \n{}\n", discusses.size(), discusses);

            //TODO
        }
    }
}
