package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import junit.framework.TestCase;

/**

 */
public class MathMaxTest extends TestCase {

    public void test() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("max = java.lang.Math.max((int)$0,(int)$1);");

        int max = (Integer) container.instance("max", 2, 4);
        assertEquals(4, max);


    }

}
