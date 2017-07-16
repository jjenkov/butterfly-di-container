package com.jenkov.container;

import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.java.JavaFactory;
import com.jenkov.container.java.JavaFactoryBuilder;
import com.jenkov.container.script.ScriptFactoryBuilder;
import junit.framework.TestCase;

/**

 */
public class ContainerTest extends TestCase {


    public void testReplaceScriptFactory() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("test  = * com.jenkov.container.TestProduct().setValue1('value1'); ");
        builder.addFactory("test2 = * test.setValue2('other'); ");

        TestProduct testProduct = (TestProduct) container.instance("test");
        assertEquals("value1", testProduct.getValue1());

        TestProduct testProduct2 = (TestProduct) container.instance("test2");
        assertEquals("value1", testProduct2.getValue1());
        assertEquals("other", testProduct2.getValue2());

        builder.replaceFactory("test = * com.jenkov.container.TestProduct().setValue1('value2'); ");
        testProduct = (TestProduct) container.instance("test");
        assertEquals("value2", testProduct.getValue1());

        testProduct2 = (TestProduct) container.instance("test2");
        assertEquals("value2", testProduct2.getValue1());
        assertEquals("other", testProduct2.getValue2());

    }

    public void testOther() {
        IContainer container = new Container();
        JavaFactoryBuilder builder = new JavaFactoryBuilder(container);

        builder.addFactory("test", new TestFactory());

        builder.addFactory("test2", new StringFactory3());
    }

    public void testReplaceJavaFactory() {
        IContainer container = new Container();
        JavaFactoryBuilder builder = new JavaFactoryBuilder(container);

        builder.addFactory("test", new TestFactory());

        TestProduct product = (TestProduct) container.instance("test");
        assertEquals("value1", product.getValue1());

        builder.addFactory("test2", null, new TestFactory2());

        TestProduct product2 = (TestProduct) container.instance("test2");
        assertEquals("value1", product2.getValue1());
        assertEquals("other", product2.getValue2());


        builder.replaceFactory("test", new TestFactory3());

        product = (TestProduct) container.instance("test");
        assertEquals("value2", product.getValue1());

        product2 = (TestProduct) container.instance("test2");
        assertEquals("value2", product2.getValue1());
        assertEquals("other", product2.getValue2());
    }

    public void testReplaceNonExistingFactory() {
        IContainer container = new Container();
        JavaFactoryBuilder builder = new JavaFactoryBuilder(container);

        builder.replaceFactory("test", new TestFactory());
        TestProduct product = (TestProduct) container.instance("test");
        assertEquals("value1", product.getValue1());
    }

    public void testInit() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        String factoryDefinition =
                "singletonFactory = 1 com.jenkov.container.SingletonTestProduct(); " +
                        "   config { com.jenkov.container.SingletonTestProduct.doInit(); } ";

        builder.addFactory(factoryDefinition);

        container.init();

        //TestProduct product = (TestProduct) container.instance("singletonFactory");

    }

    public static class TestFactory extends JavaFactory {
        public TestProduct instance(Object... parameters) {
            TestProduct product = new TestProduct();
            product.setValue1("value1");
            return product;
        }
    }

    public static class TestFactory2 extends JavaFactory {
        public IGlobalFactory<TestProduct> test = null;

        public TestProduct instance(Object... parameters) {
            TestProduct product = test.instance(parameters);
            product.setValue2("other");
            return product;
        }
    }

    public static class TestFactory3 extends JavaFactory {
        public TestProduct instance(Object... parameters) {
            TestProduct product = new TestProduct();
            product.setValue1("value2");
            return product;
        }
    }

    public static class StringFactory3 extends JavaFactory {
        public IGlobalFactory test = null;

        public String instance(Object... parameters) {
            return "test2" + test.instance();
        }
    }
}
