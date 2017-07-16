package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.TestProduct;
import junit.framework.TestCase;

/**

 */
public class FactoryFactoryTest extends TestCase {

    public void testContainerInjection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = \"a factory product\"; ");
        builder.addFactory("bean2 = com.jenkov.container.TestProduct((com.jenkov.container.IContainer) #bean1);");
        TestProduct bean2 = (TestProduct) container.instance("bean2");
        assertSame(container, bean2.container);

        builder.addFactory("bean3 = com.jenkov.container.TestProduct(); " +
                "  config{ $bean3.container = #bean; }");
        TestProduct bean3 = (TestProduct) container.instance("bean3");
        assertSame(container, bean3.container);

        builder.addFactory("bean4 = com.jenkov.container.TestProduct().setContainer(#bean);");
        TestProduct bean4 = (TestProduct) container.instance("bean4");
        assertSame(container, bean4.container);

        builder.addFactory("bean5 = com.jenkov.container.TestProduct.staticContainer = #bean;");
        IContainer containerStatic = (Container) container.instance("bean5");
        assertSame(containerStatic, container);

        TestProduct.staticContainer = null;
        builder.addFactory("bean6 = com.jenkov.container.TestProduct.setStaticContainer(#bean);");
        container.instance("bean6"); // returns null from void static method call.
        assertSame(TestProduct.staticContainer, container);

    }

    public void testFactoryInjection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = \"a factory product\"; ");
        builder.addFactory("bean2 = com.jenkov.container.TestProduct((com.jenkov.container.ICustomFactory) #bean);");
        TestProduct bean2 = (TestProduct) container.instance("bean2");
        assertEquals("a factory product", bean2.factory.bean());
        assertEquals("a factory product", bean2.factory.instance());
        assertEquals("a factory product", bean2.factory.beanInstance());

        builder.addFactory("bean3 = com.jenkov.container.TestProduct(); " +
                "  config{ $bean3.factory = #bean; }");
        TestProduct bean3 = (TestProduct) container.instance("bean3");
        assertEquals("a factory product", bean3.factory.bean());
        assertEquals("a factory product", bean3.factory.instance());
        assertEquals("a factory product", bean3.factory.beanInstance());

        builder.addFactory("bean4 = com.jenkov.container.TestProduct().setFactory(#bean);");
        TestProduct bean4 = (TestProduct) container.instance("bean4");
        assertEquals("a factory product", bean4.factory.bean());
        assertEquals("a factory product", bean4.factory.instance());
        assertEquals("a factory product", bean4.factory.beanInstance());


        TestProduct.staticFactory = null;
        builder.addFactory("bean5 = com.jenkov.container.TestProduct.staticFactory = #bean;");
        Object instance = container.instance("bean5");  //returns the factory... ?!?
        assertNotNull(TestProduct.staticFactory);
        assertEquals("a factory product", TestProduct.staticFactory.bean());
        assertEquals("a factory product", TestProduct.staticFactory.instance());
        assertEquals("a factory product", TestProduct.staticFactory.beanInstance());


        TestProduct.staticFactory = null;
        builder.addFactory("bean6 = com.jenkov.container.TestProduct.setStaticFactory(#bean);");
        container.instance("bean6"); // returns null from void static method call.
        assertNotNull(TestProduct.staticFactory);
        assertEquals("a factory product", TestProduct.staticFactory.bean());
        assertEquals("a factory product", TestProduct.staticFactory.instance());
        assertEquals("a factory product", TestProduct.staticFactory.beanInstance());
    }
}
