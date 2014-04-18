package org.elasticsearch.river;

import com.kcf.tasker.deleter.Deleter;
import com.kcf.tasker.deleter.Deleters;
import com.kcf.tasker.looker.Looker;
import com.kcf.tasker.looker.Lookers;
import com.kcf.util.DBHelper;
import com.kcf.util.QuartzContext;
import com.kcf.util.RiverConfig;
import com.kcf.util.RiverConfig.Tables;
import com.kcf.util.ThreadContext;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.support.XContentMapValues;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 上午11:13
 *
 * KCF indices update river
 * The logical engine fired here
 */
public class KCFIndexUpdateRiver extends AbstractRiverComponent implements River{
    private SettingInfo settingInfo = null;

    private Client client;

    @Inject
    public KCFIndexUpdateRiver(RiverName riverName, RiverSettings settings,
                               @RiverIndexName String riverIndexName, Client client) {
        super(riverName, settings);

        this.client = client;
        this.settingInfo = this.wrapSetting(this.getSourceSettings(settings));

        logger.info("get the custom config info: {}", this.settingInfo);

        DBHelper.init(this.settingInfo.url, this.settingInfo.user, this.settingInfo.pwd);
        QuartzContext.init(client, this.settingInfo.delay);
    }

    @Override
    public void start() {
        logger.info("start the kcf-index-update river");

        for(Tables table : Tables.values()){
            String name = table.name();
            QuartzContext.fire(Lookers.getLookerClazz(name));
            QuartzContext.fire(Deleters.getDeleterClazz(name));
        }
    }

    @Override
    public void close() {
        logger.info("close the kcf-index-update river");

        QuartzContext.shutdown();
    }

    private Map<String, Object> getSourceSettings(RiverSettings settings){
        Map<String, Object> sourceSettings = Maps.newHashMap();

        if(settings.settings().containsKey(RiverConfig.RIVER_TYPE)){
            sourceSettings = (Map<String, Object>)(
                    settings.settings().get(RiverConfig.RIVER_TYPE));
        }

        return sourceSettings;
    }

    private SettingInfo wrapSetting(Map<String, Object> settings) {
        String url  = XContentMapValues.nodeStringValue(
                settings.get("url"), RiverConfig.DEFAULT_DB_URL);
        String user = XContentMapValues.nodeStringValue(
                settings.get("user"), RiverConfig.DEFAULT_DB_USER);
        String pwd  = XContentMapValues.nodeStringValue(
                settings.get("password"), RiverConfig.DEFAULT_DB_PWD);
        long delay  = XContentMapValues.nodeLongValue(
                settings.get("delay"), RiverConfig.DEFAULT_DELAY);

        return new SettingInfo(url, user, pwd, delay);
    }

    /** custom settings */
    private class SettingInfo{
        private String url;
        private String user;
        private String pwd;
        private long   delay;

        private SettingInfo(String url, String user, String pwd, long delay) {
            this.url = url;
            this.user = user;
            this.pwd = pwd;
            this.delay = delay;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=====================\n");
            sb.append("url:   ").append(this.url).append("\n");
            sb.append("user:  ").append(this.user).append("\n");
            sb.append("pwd:   ").append(this.pwd).append("\n");
            sb.append("delay: ").append(this.delay).append(" ms");
            sb.append("\n=====================\n");
            return sb.toString();
        }
    }
}
