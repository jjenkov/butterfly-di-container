package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.TestProduct;
import com.jenkov.container.impl.factory.*;
import junit.framework.TestCase;

/**

 */
public class MethodFactoryTest extends TestCase {

    public void testMethodModifiers() {
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        try {
            builder.addFactory("test = * com.jenkov.container.TestProduct().createProduct();");
            fail("The createProduct() method is static and trying to call it on an instance should throw exception");
        } catch (ParserException e) {
            if (!"FactoryBuilder".equals(e.getInfoItems().get(0).errorContext)) {
                fail("Wrong exception caught");
            }
            if (!"INSTANCE_METHOD_FACTORY_ERROR".equals(e.getInfoItems().get(0).errorCode)) {
                fail("Wrong exception caught");
            }
        }


        try {
            builder.addFactory("test = * com.jenkov.container.TestProduct.getValue1();");
            fail("The createProduct() method is static and trying to call it on an instance should throw exception");
        } catch (ParserException e) {
            if (!"FactoryBuilder".equals(e.getInfoItems().get(0).errorContext)) {
                fail("Wrong exception caught");
            }
            if (!"STATIC_METHOD_FACTORY_ERROR".equals(e.getInfoItems().get(0).errorCode)) {
                fail("Wrong exception caught");
            }
        }


    }


    public void testBuildMethodFactory() {
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("test = * com.jenkov.container.TestProduct.createProduct();");
        GlobalFactoryProxy factory = (GlobalFactoryProxy) container.getFactory("test");
        assertTrue(factory.getDelegateFactory() instanceof GlobalNewInstanceFactory);
        GlobalNewInstanceFactory globalNewInstanceFactory = (GlobalNewInstanceFactory) factory.getDelegateFactory();
        assertTrue(globalNewInstanceFactory.getLocalInstantiationFactory() instanceof LocalProductProducerFactory);
        assertEquals(TestProduct.class, factory.getReturnType());
        assertNotNull(container.instance("test"));
        assertNotSame(container.instance("test"), container.instance("test"));

        builder.addFactory("test2 = * com.jenkov.container.TestProduct.createProduct(test);");
        TestProduct product = (TestProduct) container.instance("test2");
        assertNotNull(product);
        assertNotSame(product, product.getInternalProduct());
        assertNotSame(container.instance("test2"), container.instance("test2"));

        builder.addFactory("test3 = * com.jenkov.container.TestProduct.createProduct(test).getInternalProduct();");
        product = (TestProduct) container.instance("test3");
        assertNotNull(product);
        assertNull(product.getInternalProduct());
        assertNotSame(container.instance("test3"), container.instance("test3"));
        factory = (GlobalFactoryProxy) container.getFactory("test3");
        assertTrue(factory.getDelegateFactory() instanceof GlobalNewInstanceFactory);
        globalNewInstanceFactory = (GlobalNewInstanceFactory) factory.getDelegateFactory();
        assertTrue(globalNewInstanceFactory.getLocalInstantiationFactory() instanceof LocalProductProducerFactory);
        assertTrue(((LocalProductProducerFactory) globalNewInstanceFactory.getLocalInstantiationFactory()).getInstantiationFactory() instanceof InstanceMethodFactory);


        InstanceMethodFactory instanceMethodFactory = (InstanceMethodFactory) ((LocalProductProducerFactory) globalNewInstanceFactory.getLocalInstantiationFactory()).getInstantiationFactory();
        assertEquals("getInternalProduct", instanceMethodFactory.getMethod().getName());
        StaticMethodFactory staticMethodFactory = (StaticMethodFactory) instanceMethodFactory.getMethodInvocationTargetFactory();
        assertEquals("createProduct", staticMethodFactory.getMethod().getName());

        builder.addFactory("test4 = * test3.setValue1(\"lala\").setValue2(\"lala2\");");
        product = (TestProduct) container.instance("test4");
        assertEquals("lala", product.getValue1());
        assertEquals("lala2", product.getValue2());
    }
}
