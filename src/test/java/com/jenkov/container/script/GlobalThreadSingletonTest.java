package com.jenkov.container.script;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;
import com.jenkov.container.TestProduct;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalThreadSingletonTest extends TestCase {

    public void testThreadSingleton_bug_by_marcus_rechtien() throws InterruptedException {
        Container container = new Container();
        com.jenkov.container.script.ScriptFactoryBuilder builder = new com.jenkov.container.script.ScriptFactoryBuilder(container);
        builder.addFactory("test1 = 1T java.lang.String('test1'); ");

        String test1 = (String) container.instance("test1");
        assertEquals("test1", test1);

        TestThread thread1 = new TestThread(container){
            public void run(){
                this.string1 = (String) container.instance("test1");
                this.string2 = (String) container.instance("test1");
            }
        };
        thread1.start();
        thread1.join();
        assertSame(thread1.getString1(), thread1.getString2());

        TestThread thread2 = new TestThread(container){
            public void run(){
                this.string1 = (String) container.instance("test1");
                this.string2 = (String) container.instance("test1");
            }
        };
        thread2.start();
        thread2.join();
        assertSame(thread2.getString1(), thread2.getString2());

        assertNotSame(thread1.getString1(), thread2.getString1());
    }


    public void testInstance() throws InterruptedException {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = 1T com.jenkov.container.TestProduct();");
        TestProduct product1 = (TestProduct) container.instance("bean");
        assertNotNull(product1);
        assertNull(product1.getValue1());
        assertNull(product1.getValue2());
        TestProduct product1_1 = (TestProduct) container.instance("bean");
        assertSame(product1, product1_1);


        builder.addFactory(
                "bean2 = 1T com.jenkov.container.TestProduct();" +
                "    config{ $bean2.setValue1(\"value1\"); }");


        TestThread thread = new TestThread(container){
            public void run(){
                this.instance1 = container.instance("bean2");
                this.instance2 = container.instance("bean2");
            }
        };
        thread.start();
        thread.join();

        TestProduct product2 = (TestProduct) container.instance("bean2");
        assertEquals("value1", product2.getValue1());
        assertNull(product2.getValue2());

        assertEquals("value1", ((TestProduct)thread.getInstance1()).getValue1());
        assertNull(((TestProduct)thread.getInstance1()).getValue2());

        assertNotSame(product2, thread.getInstance1());
        assertSame(thread.getInstance2(), thread.getInstance1());

        builder.addFactory(
                "bean3 = 1T com.jenkov.container.TestProduct();" +
                "    config { $bean3.setValue1(\"value1\"); } " +
                "    myPhase{ $bean3.setValue2(\"value2\"); } " +
                "    dispose{ $bean3.setValue2(\"disposed\"); }");

        thread = new TestThread(container){
            public void run(){
                this.instance1 = container.instance("bean3");
                this.instance2 = container.instance("bean3");
            }
        };
        thread.start();
        thread.join();

        TestProduct product3 = (TestProduct) container.instance("bean3");
        assertEquals("value1", product2.getValue1());
        assertNull(product3.getValue2());

        TestProduct product3_1 = (TestProduct) container.instance("bean3");
        assertSame(product3, product3_1);
        assertNotSame(product3, thread.getInstance1());
        assertNotSame(product3, thread.getInstance2());

        assertEquals("value1", ((TestProduct) thread.getInstance1()).getValue1());
        assertEquals("value1", ((TestProduct) thread.getInstance2()).getValue1());
        assertSame(thread.getInstance1(), thread.getInstance2());

        container.execPhase("myPhase");
        assertEquals("value2", product3.getValue2());
        assertEquals("value2", ((TestProduct) thread.getInstance1()).getValue2());
        assertEquals("value2", ((TestProduct) thread.getInstance2()).getValue2());

        container.dispose();
        assertEquals("disposed", product3.getValue2());
        assertEquals("disposed", ((TestProduct) thread.getInstance1()).getValue2());
        assertEquals("disposed", ((TestProduct) thread.getInstance2()).getValue2());
    }

}
