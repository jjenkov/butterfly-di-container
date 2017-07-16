package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.TestProduct;
import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

/**

 */
public class CollectionFactoryTest extends TestCase {

    public static int[] asIntArray(Object... numbers) {
        int[] intArray = new int[numbers.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = (Integer) numbers[i];
        }
        return intArray;
    }

    public static int add(int... numbers) {
        int result = 0;
        for (int number : numbers) result += number;
        return result;
    }

    public void testPrimitiveArray() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);
        builder.addFactory("test = * com.jenkov.container.script.CollectionFactoryTest.add([$0, $1]); ");

        int result = (Integer) container.instance("test", 10, 20);
        assertEquals(result, 30);

        builder.addFactory("test2 = * com.jenkov.container.script.CollectionFactoryTest.add($0); ");

        //todo Fix this piece here which has a bug in Java 8 (not earlier versions - strange)
        //builder.addFactory("test3 = * test2((int[]) [21, 31]); ");
        //result = (Integer) container.instance("test3");
        //assertEquals(52, result);

    }

    public void testCollectionCasting() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        assertEquals(TestProduct[].class, Array.newInstance(TestProduct.class, 0).getClass());

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setArray((java.lang.Integer[])[0,1,2,3]); ");
        TestProduct product = (TestProduct) container.instance("bean");

        assertEquals(new Integer(0), product.getArray2()[0]);
        assertEquals(new Integer(1), product.getArray2()[1]);
        assertEquals(new Integer(2), product.getArray2()[2]);
        assertEquals(new Integer(3), product.getArray2()[3]);

        assertNull(product.getArray1());
        assertNull(product.getArray3());


        builder.addFactory("bean2 = * com.jenkov.container.TestProduct().setArray((java.lang.String[])['0','1','2','3']); ");
        TestProduct product2 = (TestProduct) container.instance("bean2");

        assertEquals("0", product2.getArray1()[0]);
        assertEquals("1", product2.getArray1()[1]);
        assertEquals("2", product2.getArray1()[2]);
        assertEquals("3", product2.getArray1()[3]);

        assertNull(product2.getArray2());
        assertNull(product2.getArray3());

        builder.addFactory("bean3 = * com.jenkov.container.TestProduct().setArray((com.jenkov.container.TestProduct[])" +
                "[com.jenkov.container.TestProduct().setValue1('1'),com.jenkov.container.TestProduct().setValue1('2')]); ");
        TestProduct product3 = (TestProduct) container.instance("bean3");

        assertEquals("1", product3.getArray3()[0].getValue1());
        assertEquals("2", product3.getArray3()[1].getValue1());

        assertNull(product3.getArray1());
        assertNull(product3.getArray2());
    }


    public void testIntegerCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setIntegerList([0,1,2,3]); ");

        TestProduct product = (TestProduct) container.instance("bean");
        assertEquals(4, product.getIntegerList().size());

        assertEquals(new Integer(0), product.getIntegerList().get(0));
        assertEquals(new Integer(1), product.getIntegerList().get(1));
        assertEquals(new Integer(2), product.getIntegerList().get(2));
        assertEquals(new Integer(3), product.getIntegerList().get(3));

    }

    public void testUrlCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setUrlArray(" +
                "   ['http://butterfly.jenkov.com', 'http://tutorials.jenkov.com']); ");

        TestProduct product = (TestProduct) container.instance("bean");
        assertEquals("http://butterfly.jenkov.com", product.getUrlArray()[0].toString());
        assertEquals("http://tutorials.jenkov.com", product.getUrlArray()[1].toString());


        builder.addFactory("bean2 = * com.jenkov.container.TestProduct().setUrlList(" +
                "   ['http://butterfly.jenkov.com', 'http://tutorials.jenkov.com']); ");

        TestProduct product2 = (TestProduct) container.instance("bean2");
        assertEquals("http://butterfly.jenkov.com", product2.getUrlList().get(0).toString());
        assertEquals("http://tutorials.jenkov.com", product2.getUrlList().get(1).toString());

    }

    public void testSetCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setSet(" +
                "   ['value1', 'value2']); ");

        TestProduct product = (TestProduct) container.instance("bean");
        Iterator iterator = product.getSet().iterator();
        assertEquals("value2", iterator.next());
        assertEquals("value1", iterator.next());
    }

    public void testArrayCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setStringArray(" +
                "  ['value1', 'value2']); ");

        TestProduct product = (TestProduct) container.instance("bean");
        assertEquals("value1", product.getStringArray()[0]);
        assertEquals("value2", product.getStringArray()[1]);

        container.instance("bean");
    }

    public void testInjectedListCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProduct().setList(['value1', 'value2']).setValue1('2'); ");

        TestProduct product = (TestProduct) container.instance("bean");
        assertEquals("value1", product.getList().get(0));
        assertEquals("value2", product.getList().get(1));
        assertEquals("2", product.getValue1());
    }


    public void testRawListCollection() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * [com.jenkov.container.TestProduct().setValue1(\"value1\"), com.jenkov.container.TestProduct().setValue1(\"value2\")]; ");

        Object object = container.instance("bean");
        List list = (List) object;
        assertEquals(2, list.size());

        TestProduct product1 = (TestProduct) list.get(0);
        assertEquals("value1", product1.getValue1());

        TestProduct product2 = (TestProduct) list.get(1);
        assertEquals("value2", product2.getValue1());
    }
}
