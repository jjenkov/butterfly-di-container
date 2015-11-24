package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.TestProduct;
import junit.framework.TestCase;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalNewInstanceTest extends TestCase {

    public void testInstance(){
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct();");
        TestProduct product1 = (TestProduct) container.instance("bean");
        assertNotNull(product1);
        assertNull(product1.getValue1());
        assertNull(product1.getValue2());
        TestProduct product1_1 = (TestProduct) container.instance("bean");
        assertNotSame(product1, product1_1);


        builder.addFactory(
                "bean2 = * com.jenkov.container.TestProduct();" +
                "    config{ $bean2.setValue1(\"value1\"); }");

        TestProduct product2 = (TestProduct) container.instance("bean2");
        assertEquals("value1", product2.getValue1());
        assertNull(product2.getValue2());
    }
}
