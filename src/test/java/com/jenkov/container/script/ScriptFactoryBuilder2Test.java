package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.TestProduct;
import com.jenkov.container.impl.factory.*;
import com.jenkov.container.itf.factory.IGlobalFactory;
import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ScriptFactoryBuilder2Test extends TestCase {




    public void testUnknownFactories(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("decimal1 = com.jenkov.container.TestProduct().setDecimal(1.55); ");

        try{
            builder.addFactory("decimal2 = com.jenkov.container.TestProduct(decimal).setDecimal(1.55); ");
            fail("should fail because 'decimal' factory does not exist");
        }catch(ParserException e){
            //ignore, expected
        }

    }

    public void testDoubleValues(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("decimal = com.jenkov.container.TestProduct().setDecimal(1.55); ");
        TestProduct product = (TestProduct) container.instance("decimal");
        assertEquals(1.55, product.getDecimal());

    }

    public void testFactoryAssignment(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        assertEquals(-1, TestProduct.staticInt);
        builder.addFactory("bean = com.jenkov.container.TestProduct.staticInt = 5;");
        Object bean = container.instance("bean");
        assertEquals(5, TestProduct.staticInt);

        builder.addFactory("bean2 = com.jenkov.container.TestProduct(); " +
                           " config { $bean2.instanceInt = 8; }");
        TestProduct bean2 = (TestProduct) container.instance("bean2");
        assertEquals(8, bean2.instanceInt );

        TestProduct.staticInt = -1; //set back, because ScriptFactoryBuilderTest expects it as -1 as initial value.


    }


    public void testClassInjection(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct.class;");

        Object bean = container.instance("bean");
        assertEquals(TestProduct.class, bean);

    }

    public void testNullInjection() {
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = null;");

        Object value = container.instance("bean");
        assertNull(value);

        builder.addFactory("bean1 = com.jenkov.container.TestProduct().setValues(\"null\", \"null\"); ");

        TestProduct product1 = (TestProduct) container.instance("bean1");
        assertEquals("null", product1.getValue1());
        assertEquals("null", product1.getValue2());
    }

    public void testNullInjection2(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("bean1 = com.jenkov.container.TestProduct((com.jenkov.container.TestProduct) null).setValues(1, (java.lang.String) 2); " +
                             "    config { $bean1.setValues(null, (java.lang.String) null); } ");

        TestProduct product1 = (TestProduct) container.instance("bean1");
        assertNull(product1.getValue1());
        assertNull(product1.getValue2());

    }


    public void testConfigFiles() throws IOException {
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        FileInputStream fileInput = new FileInputStream("test-config.script");
        try{
            builder.addFactories(fileInput);
        }finally {
            fileInput.close();
        }

        TestProduct testProduct1 = (TestProduct) container.instance("bean1");
        TestProduct testProduct2 = (TestProduct) container.instance("bean1");
        assertNotNull(testProduct1);
        assertNotNull(testProduct2);
        assertNotSame(testProduct1, testProduct2);

        TestProduct testProduct3_1 = (TestProduct) container.instance("bean3");
        TestProduct testProduct3_2 = (TestProduct) container.instance("bean3");
        assertEquals("bean3Value", testProduct3_1.getValue1());
        assertEquals("bean3Value", testProduct3_2.getValue1());
        assertSame(testProduct3_1, testProduct3_2);




    }


    public void testCasting(){
        String script = "test = * com.jenkov.container.TestProduct();" +
                "config {$test.setValues(\"1\",(java.lang.String)\"2\"); }";

        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory(script);
    }

    public void testLocalProducts(){
        String script = "test = * com.jenkov.container.script.SomeFactoryProduct($0);" +
                             "    config{ $test.setArg1(\"override\"); } ";
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory(script);
        SomeFactoryProduct product = (SomeFactoryProduct) container.instance("test", "inputParameter");
        assertEquals("override", product.getArg1());
    }

    public void testBuildInputConsumingFactory(){
        String script = "test = * com.jenkov.container.script.SomeFactoryProduct($0);";
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory(script);
        GlobalFactoryProxy factory = (GlobalFactoryProxy) container.getFactory("test");
        assertTrue(factory.getDelegateFactory() instanceof GlobalNewInstanceFactory);
        GlobalNewInstanceFactory configuringFactory = (GlobalNewInstanceFactory) factory.getDelegateFactory();
        assertTrue(configuringFactory.getLocalInstantiationFactory() instanceof LocalProductProducerFactory);
        assertEquals(SomeFactoryProduct.class, factory.getReturnType());

        LocalProductProducerFactory localProductProducerFactory = (LocalProductProducerFactory) configuringFactory.getLocalInstantiationFactory();

        ConstructorFactory constructorFactory = (ConstructorFactory) localProductProducerFactory.getInstantiationFactory();
        assertEquals(1, constructorFactory.getConstructorArgFactories().size());
        assertEquals(String.class, constructorFactory.getConstructor().getParameterTypes()[0]);

        InputConsumerFactory inputFactory = (InputConsumerFactory) constructorFactory.getConstructorArgFactories().get(0);
        assertEquals(0, inputFactory.getInputParameterIndex());

        SomeFactoryProduct product = (SomeFactoryProduct) factory.instance("testValue");
        assertEquals("testValue", product.getArg1());

        //todo do more tests... the input consuming factory should ideally have its null return type changed
        //to match that of the constructor parameter (String.class) .

        script = "test2 = * test(\"aValue\");";
        builder.addFactory(script);

        product = (SomeFactoryProduct) container.instance("test2");
        assertEquals("aValue", product.getArg1());

        builder.addFactory("test3 = * test2.setArg1(\"overrideValue\");");
        product = (SomeFactoryProduct) container.instance("test3");
        assertEquals("overrideValue", product.getArg1());
    }

    public void testBuildConstructorFactory(){
        Container container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("test = * com.jenkov.container.TestProduct();");
        GlobalFactoryProxy factory = (GlobalFactoryProxy) container.getFactory("test");
        assertTrue(factory.getDelegateFactory() instanceof GlobalNewInstanceFactory);
        GlobalNewInstanceFactory configuringFactory = (GlobalNewInstanceFactory) factory.getDelegateFactory();
        assertTrue(configuringFactory.getLocalInstantiationFactory() instanceof LocalProductProducerFactory);
        assertEquals(TestProduct.class, factory.getReturnType());

        builder.addFactory("test2 = * com.jenkov.container.TestProduct(com.jenkov.container.TestProduct());");
        factory = (GlobalFactoryProxy) container.getFactory("test2");
        assertTrue(factory.getDelegateFactory() instanceof GlobalNewInstanceFactory);
        configuringFactory = (GlobalNewInstanceFactory) factory.getDelegateFactory();
        assertTrue(configuringFactory.getLocalInstantiationFactory() instanceof LocalProductProducerFactory);
        assertEquals(TestProduct.class, factory.getReturnType());
        TestProduct product = (TestProduct) container.instance("test2");
        assertNotNull(product);
        assertNotNull(product.getInternalProduct());

    }



}