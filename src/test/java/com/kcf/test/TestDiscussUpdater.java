package com.kcf.test;

import com.kcf.entity.Discuss;
import com.kcf.tasker.updater.Updater;
import com.kcf.tasker.updater.impl.DiscussUpdater;
import com.kcf.util.DBHelper;
import com.kcf.util.RiverConfig;
import org.elasticsearch.common.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午7:57
 */
@RunWith(JUnit4.class)
public class TestDiscussUpdater {
    private Discuss disc = new Discuss();

    @Before
    public void init(){
        DBHelper.init(
            RiverConfig.DEFAULT_DB_URL,
            RiverConfig.DEFAULT_DB_USER,
            RiverConfig.DEFAULT_DB_PWD
        );

        disc.setCreated(DateTime.now());
        disc.setUserId(1l);
        disc.setSubject("test build json");
        disc.setTopped(true);
        disc.setReplyCount(11);
        disc.setBookId(11l);
        disc.setConditionName("肺癌");
        disc.setContent("Test build Discuss Updater json");
        disc.setId(11);
        disc.setViewCount(1111);
        disc.setLastReplied(DateTime.now());

    }

    @Test
    public void testBuildJson() {
        System.out.println("test discuss updater -- build json");

        try {
            Updater updater = new DiscussUpdater(null);
            Method method = DiscussUpdater.class.getDeclaredMethod("builderJson", Discuss.class);
            method.setAccessible(true);

            Object rest = method.invoke(updater, this.disc);

            System.out.println(rest);

            assert rest != null;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            assert false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            assert false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assert false;
        }

        assert true;
    }
}
