package com.jenkov.container.script;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;
import com.jenkov.container.TestProductCasting;

import java.net.URL;
import java.net.MalformedURLException;

/**

 */
public class ParameterCastingTest extends TestCase {

    public void test() throws MalformedURLException {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("bean = * com.jenkov.container.TestProductCasting().setValue((int) 2);");
        TestProductCasting product = (TestProductCasting) container.instance("bean");

        assertNull(product.getValue1());
        assertNull(product.getValue2());
        assertEquals(2, product.getValue3());

        
        builder.addFactory("bean2 = * com.jenkov.container.TestProductCasting().setValue((String) 2);");
        product = (TestProductCasting) container.instance("bean2");

        assertEquals("2", product.getValue1());
        assertNull(product.getValue2());
        assertEquals(-1, product.getValue3());


        builder.addFactory("bean3 = * com.jenkov.container.TestProductCasting().setValue((URL) 'http://jenkov.com');");
        product = (TestProductCasting) container.instance("bean3");

        assertNull(product.getValue1());
        assertEquals(new URL("http://jenkov.com"), product.getValue2());
        assertEquals(-1, product.getValue3());



    }
}
