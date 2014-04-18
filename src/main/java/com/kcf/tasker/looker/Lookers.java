package com.kcf.tasker.looker;


import com.kcf.tasker.looker.impl.DiscussLooker;
import com.kcf.util.RiverConfig.Tables;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.EsExecutors;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-28
 * Time: 上午11:01
 *
 * Looker helper class, as a factory
 */
public class Lookers {
    private static final ESLogger logger = ESLoggerFactory.getLogger("Lookers");

    /**
     * looker factory method
     * @return
     *  the correct Looker instance
     *  null, if the table is not in the config
     */
    public static Looker getLooker(String table, Client client) {
        Tables theTable = Tables.valueOf(table);
        if(theTable != null){
            switch (theTable){
                case Discuss: return getDiscussLooker(client);
                default: return null;
            }
        }

        return null;
    }

    public static Class<? extends Looker> getLookerClazz(String name) {
        Tables theTable = Tables.valueOf(name);
        if(theTable != null){
            switch (theTable){
                case Discuss: return DiscussLooker.class;
                default: return null;
            }
        }

        return null;

    }

    public static DiscussLooker getDiscussLooker(Client client){

        return new DiscussLooker(client);
    }

}
