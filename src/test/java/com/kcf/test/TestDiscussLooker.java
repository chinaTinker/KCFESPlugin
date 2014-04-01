package com.kcf.test;

import com.kcf.entity.Discuss;
import com.kcf.tasker.looker.Looker;
import com.kcf.tasker.looker.Lookers;
import com.kcf.util.DBHelper;
import com.kcf.util.RiverConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午1:54
 */

@RunWith(JUnit4.class)
public class TestDiscussLooker {
    @Before
    public void init(){
        DBHelper.init(
            RiverConfig.DEFAULT_DB_URL,
            RiverConfig.DEFAULT_DB_USER,
            RiverConfig.DEFAULT_DB_PWD
        );
    }

    @Test
    public void testDataQuery(){
        System.out.println("test discuss looker data query");

        Looker looker  = Lookers.getDiscussLooker(1000, null);
        try {
            Method method = Looker.class.getDeclaredMethod("lookData", null);
            method.setAccessible(true);
            List<Discuss> discusses = (List<Discuss>) method.invoke(looker);

            assert discusses != null;
            assert !discusses.isEmpty();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
