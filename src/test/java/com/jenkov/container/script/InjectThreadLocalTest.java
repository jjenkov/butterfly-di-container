package com.jenkov.container.script;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;

/**

 */
public class InjectThreadLocalTest extends TestCase {

    public static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    protected String local3 = null;

    public void testInjectThreadLocal() throws InterruptedException {
        final IContainer           container = new Container();
        ScriptFactoryBuilder builder   = new ScriptFactoryBuilder(container);

        builder.addFactory("local = * com.jenkov.container.script.InjectThreadLocalTest.threadLocal.get(); ");

        String local1 = (String) container.instance("local");
        assertNull(local1);
        threadLocal.set("value");

        String local2 = (String) container.instance("local");
        assertEquals("value", local2);

        Thread thread = new Thread(){
            public void run(){
                threadLocal.set("value3");

                local3 = (String) container.instance("local");
            }
        };
        thread.start();

        thread.join();

//        System.out.println(local3);
        assertEquals("value3", local3);


    }
}
