package com.jenkov.container.java;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;
import com.jenkov.container.TestProduct;
import com.jenkov.container.MyOtherFactory;
import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.FactoryException;

/**

 */
public class JavaFactoryBuilderTest extends TestCase {

     public void testConfiguration(){
        IContainer container = new Container();
        JavaFactoryBuilder builder = new JavaFactoryBuilder(container);
        IGlobalFactory factory = null;

        builder.addFactory("test", String.class, new JavaFactory(){
            public Object instance(Object... parameters) {
                return "test";
            }
        });
        String string = (String) container.instance("test");
        assertEquals("test", string);
        factory = container.getFactory("test");
        assertEquals(String.class, factory.getReturnType());

        builder.addFactory("test2", null, new JavaFactory(){
            public IGlobalFactory test = null;
            public String instance(Object... parameters) {
                return "test2" + test.instance();
            }
        });
        string = (String) container.instance("test2");
        assertEquals("test2test", string);
        factory = container.getFactory("test2");
        assertEquals(String.class, factory.getReturnType());


        builder.addFactory("test3", String.class, new JavaFactory(){
            public IGlobalFactory<String> test = null;
            public Object instance(Object... parameters) {
                return "test3" + test.instance();
            }
        });
        string = (String) container.instance("test3");
        assertEquals("test3test", string);
        factory = container.getFactory("test3");
        assertEquals(String.class, factory.getReturnType());

        builder.addFactory("test4", null, new JavaFactory(){
            public IGlobalFactory<String> test2 = null;
            public String instance(Object... parameters) {
                return "test4" + test2.instance();
            }
        });
        string = (String) container.instance("test4");
        assertEquals("test4test2test", string);
        factory = container.getFactory("test4");
        assertEquals(String.class, factory.getReturnType());

        try {
            builder.addFactory("test4", null, new JavaFactory(){
                public IGlobalFactory<Integer> test2 = null;
                public String instance(Object... parameters) {
                    return "test4" + test2.instance();
                }
            });
            fail("exception should be thrown because test2 factory is parameterized to Integer");
        } catch (Exception e) {
            //do nothing, exception expected
        }


        try {
            builder.addFactory("test5", null, new JavaFactory(){
                public IGlobalFactory<String> test5 = null;
                public String instance(Object... parameters) {
                    return "test5" + test5.instance();
                }
            });
            fail("exception should be thrown because test5 factory does not exist");
        } catch (FactoryException e) {
            //do nothing, exception expected
        }

        builder.addFactory("test6", new JavaFactory(){
            public @Factory("test2") IGlobalFactory<String> testXYZ = null;
            public String instance(Object... parameters) {
                return "test6" + testXYZ.instance();
            }
        });
        string = (String) container.instance("test6");
        assertEquals("test6test2test", string);
        factory = container.getFactory("test6");
        assertEquals(String.class, factory.getReturnType());

    }
}
