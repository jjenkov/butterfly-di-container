package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.TestProduct;
import junit.framework.TestCase;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalFlyweightTest extends TestCase {

    public void testInstance_using_string() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = 1F java.lang.String('test'); ");

        String test1 = (String) container.instance("bean", "1");
        String test2 = (String) container.instance("bean", "2");
        assertSame(test1, container.instance("bean", "1"));
        assertSame(test2, container.instance("bean", "2"));

        assertNotSame(test1, test2);
    }

    public void testInstance() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = 1F com.jenkov.container.TestProduct();");
        TestProduct product1 = (TestProduct) container.instance("bean", "1");
        assertSame(product1, container.instance("bean", "1"));
        assertNotSame(container.instance("bean", "1"), container.instance("bean", "2"));
        assertSame(container.instance("bean", "2"), container.instance("bean", "2"));
        assertNull(product1.getValue1());

        builder.addFactory(
                "bean2 = 1F com.jenkov.container.TestProduct();  " +
                        "     config{ $bean2.setValue1(\"value1\"); } ");

        TestProduct product2 = (TestProduct) container.instance("bean2", "1");
        assertSame(product2, container.instance("bean2", "1"));
        assertNotSame(container.instance("bean2", "1"), container.instance("bean2", "2"));
        assertSame(container.instance("bean2", "2"), container.instance("bean2", "2"));
        assertEquals("value1", product2.getValue1());
        assertEquals("value1", ((TestProduct) container.instance("bean2", "2")).getValue1());

        builder.addFactory(
                "bean3 = 1F com.jenkov.container.TestProduct();  " +
                        "     config { $bean3.setValue1(\"value1\"); } " +
                        "     myPhase{ $bean3.setValue2(\"value2\"); }"
        );

        TestProduct product3 = (TestProduct) container.instance("bean3", "1");
        assertSame(product3, container.instance("bean3", "1"));
        assertNotSame(container.instance("bean3", "1"), container.instance("bean3", "2"));
        assertSame(container.instance("bean3", "2"), container.instance("bean3", "2"));
        assertEquals("value1", product3.getValue1());
        assertEquals("value1", ((TestProduct) container.instance("bean3", "2")).getValue1());
        assertNull(product3.getValue2());

        container.execPhase("myPhase");
        assertEquals("value2", product3.getValue2());
        assertEquals("value2", ((TestProduct) container.instance("bean3", "2")).getValue2());

        builder.addFactory(
                "bean4 = 1F com.jenkov.container.TestProduct();  " +
                        "     config { $bean4.setValue1(\"value1\"); } " +
                        "     myPhase{ $bean4.setValue2(\"value2\"); } " +
                        "     dispose{ $bean4.setValue2(\"disposed\");}"
        );

        TestProduct product4 = (TestProduct) container.instance("bean4", "1");
        assertSame(product4, container.instance("bean4", "1"));
        assertNotSame(container.instance("bean4", "1"), container.instance("bean4", "2"));
        assertSame(container.instance("bean4", "2"), container.instance("bean4", "2"));
        assertEquals("value1", product4.getValue1());
        assertEquals("value1", ((TestProduct) container.instance("bean3", "2")).getValue1());
        assertNull(product4.getValue2());

        container.execPhase("myPhase");
        assertEquals("value2", product4.getValue2());
        assertEquals("value2", ((TestProduct) container.instance("bean4", "2")).getValue2());

        container.dispose();
        assertEquals("disposed", product4.getValue2());
        assertEquals("disposed", ((TestProduct) container.instance("bean4", "2")).getValue2());

    }

}
