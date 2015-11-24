package com.jenkov.container.script;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;
import com.jenkov.container.TestProduct;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalSingletonTest extends TestCase {

    public void testInstance(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = 1 com.jenkov.container.TestProduct();");
        TestProduct product1 = (TestProduct) container.instance("bean");
        assertNotNull(product1);
        assertNull(product1.getValue1());
        assertNull(product1.getValue2());
        TestProduct product1_1 = (TestProduct) container.instance("bean");
        assertSame(product1, product1_1);


        builder.addFactory(
                "bean2 = 1 com.jenkov.container.TestProduct();" +
                "    config{ $bean2.setValue1(\"value1\"); }");

        TestProduct product2 = (TestProduct) container.instance("bean2");
        assertEquals("value1", product2.getValue1());
        assertNull(product2.getValue2());
        TestProduct product2_1 = (TestProduct) container.instance("bean2");
        assertSame(product2, product2_1);

        builder.addFactory(
                "bean3 = 1 com.jenkov.container.TestProduct();" +
                "    config { $bean3.setValue1(\"value1\"); } " +
                "    myPhase{ $bean3.setValue2(\"value2\"); } " +
                "    dispose{ $bean3.setValue2(\"disposed\"); }");

        TestProduct product3 = (TestProduct) container.instance("bean3");
        assertEquals("value1", product2.getValue1());
        assertNull(product3.getValue2());
        TestProduct product3_1 = (TestProduct) container.instance("bean3");
        assertSame(product3, product3_1);

        container.execPhase("myPhase");
        assertEquals("value2", product3.getValue2());

        container.dispose();
        assertEquals("disposed", product3.getValue2());
    }

}
