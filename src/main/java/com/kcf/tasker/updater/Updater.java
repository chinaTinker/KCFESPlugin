package com.kcf.tasker.updater;

import com.kcf.tasker.looker.Looker;

import java.lang.reflect.ParameterizedType;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午6:07
 */
public abstract class Updater<T> implements Observer {
    private String table = this.getEntityClassName();

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Looker){
            Looker looker = (Looker) observable;

            if(this.table.equals(looker.getTable())){
                this.dealData(o);
            }
        }
    }

    /** deal with the data*/
    protected abstract void dealData(Object obj);

    private String getEntityClassName() {
        ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
        Class<T> tClazz = (Class<T>) pt.getActualTypeArguments()[0];
        return tClazz.getSimpleName();
    }
}
