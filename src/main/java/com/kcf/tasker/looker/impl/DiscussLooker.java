package com.kcf.tasker.looker.impl;

import com.kcf.entity.Discuss;
import com.kcf.tasker.looker.Looker;
import com.kcf.tasker.updater.impl.DiscussUpdater;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午5:38
 */
public class DiscussLooker extends Looker<Discuss> {

    public DiscussLooker(long delay) {
        super(delay);
        super.addObserver(new DiscussUpdater());
    }

    @Override
    protected List<Discuss> lookData() {
        //TODO
        Discuss disc = new Discuss();
        disc.setCondId(415);
        disc.setContent("test the river");
        disc.setCreated(DateTime.now().getMillis());
        disc.setId(1);
        disc.setLastReplied(DateTime.now().getMillis());
        disc.setReplie(new ArrayList<String>());
        disc.setSubject("I test the river now");
        disc.setUpdated(DateTime.now().getMillis());

        return Lists.newArrayList(disc);
    }

    @Override
    protected long getLastUpdateTimeStamp(List<Discuss> data) {
        long tempStamp = 0l;

        for(Discuss disc : data){
            long crrUpdated = disc.getUpdated();

            tempStamp = crrUpdated > tempStamp? crrUpdated : tempStamp;
        }

        return tempStamp;
    }
}
