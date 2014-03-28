package org.elasticsearch.module;

import org.elasticsearch.common.inject.Binder;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.river.KCFIndexUpdateRiver;
import org.elasticsearch.river.River;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 上午11:04
 */
public class KCFIndexUpdateRiverModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(River.class).to(KCFIndexUpdateRiver.class).asEagerSingleton();
    }
}
