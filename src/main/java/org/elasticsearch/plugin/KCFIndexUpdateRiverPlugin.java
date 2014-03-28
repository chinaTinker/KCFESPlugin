package org.elasticsearch.plugin;

import com.kcf.util.RiverConfig;
import org.elasticsearch.module.KCFIndexUpdateRiverModule;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.river.RiversModule;

/**
 * User: xuyifeng
 * Date: 13-12-4
 * Time: 上午12:44
 */
public class KCFIndexUpdateRiverPlugin extends AbstractPlugin{
    private static final ESLogger logger = ESLoggerFactory.getLogger("kcfPlugin");

    @Override
    public String name() {
        return RiverConfig.RIVER_NAME;
    }

    @Override
    public String description() {
        return RiverConfig.RIVER_DESC;
    }

    /**
     * This hook will invoked by the ES with injection
     * Register the my river module to river module
     *
     * @param module
     */
    public void onModule(RiversModule module) {
        module.registerRiver(RiverConfig.RIVER_TYPE, KCFIndexUpdateRiverModule.class);
    }

}
