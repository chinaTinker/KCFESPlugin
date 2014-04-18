package com.kcf.tasker.deleter;

import com.kcf.tasker.deleter.impl.DiscussDeleter;
import com.kcf.util.RiverConfig;
import org.elasticsearch.client.Client;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-2
 */
public class Deleters {

    public static Deleter getDeleter(String table, Client client){
        RiverConfig.Tables theTable = RiverConfig.Tables.valueOf(table);
        if(theTable != null){
            switch (theTable){
                case Discuss: return new DiscussDeleter(client);
                default: return null;
            }
        }

        return null;
    }

    public static Class<? extends Deleter> getDeleterClazz(String name) {
        RiverConfig.Tables theTable = RiverConfig.Tables.valueOf(name);
        if(theTable != null){
            switch (theTable){
                case Discuss: return DiscussDeleter.class;
                default: return null;
            }
        }

        return null;
    }
}
