package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;

/**

 */
public class Bugs extends TestCase {

    public void testExtraParanthesesErrorMessage() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        String script = "test1 = * com.jenkov.container.TestProduct()); \n" +
                "test2 = * com.jenkov.container.TestProduct();  ";

        ByteArrayInputStream input = new ByteArrayInputStream(script.getBytes());

        try {
            builder.addFactories(input);
            fail("should throw exception because of extra parantheses in script");
        } catch (ParserException e) {
            assertTrue(e.getMessage().contains("Expected token ; but found )"));
        }


    }

    public void testFactoryTailErrorMessage() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        String script = "test1 = * com.jenkov.container.TestProduct().setValue1('A value')); ";

        ByteArrayInputStream input = new ByteArrayInputStream(script.getBytes());

        try {
            builder.addFactories(input);
            fail("should throw exception because of extra parantheses in script");
        } catch (ParserException e) {
            assertTrue(e.getMessage().contains("Expected token ; but found )"));
        }


    }
}
