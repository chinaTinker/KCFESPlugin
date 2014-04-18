package com.kcf.tasker.updater;

import com.kcf.tasker.updater.impl.DiscussUpdater;
import com.kcf.util.RiverConfig;
import org.elasticsearch.client.Client;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-18
 *
 * updater support class
 */
public class Updaters {

    public static Updater getUpdater(String table, Client client) {
        RiverConfig.Tables theTable = RiverConfig.Tables.valueOf(table);
        if(theTable != null){
            switch (theTable){
                case Discuss: return new DiscussUpdater(client);
                default: return null;
            }
        }

        return null;
    }
}
