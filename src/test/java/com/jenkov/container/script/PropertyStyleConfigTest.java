package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import junit.framework.TestCase;

/**
 * Tests if the container can be used for property file style application configuration, for instance
 * <p>
 * noOfThreads = 10;
 * cacheSize   = 1024;
 * dbUrl       = "jdbc:tcp...";
 */
public class PropertyStyleConfigTest extends TestCase {

    public void test() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("noOfThreads = 10;");
        String noOfThreads = (String) container.instance("noOfThreads");
        assertEquals("10", noOfThreads);
        String noOfThreads2 = (String) container.instance("noOfThreads");
        assertSame(noOfThreads, noOfThreads2);


        builder.addFactory("dbUrl = 'jdbc:tcp';");
        String dbUrl = (String) container.instance("dbUrl");
        assertEquals("jdbc:tcp", dbUrl);

        builder.addFactory("defaultSize = 1;");
        String defaultSize = (String) container.instance("defaultSize");
        assertEquals("1", defaultSize);
        String defaultSize2 = (String) container.instance("defaultSize");
        assertSame(defaultSize, defaultSize2);

        builder.addFactory("aValue = 1 1;");
        String aValue = (String) container.instance("aValue");
        assertEquals("1", aValue);
        String aValue2 = (String) container.instance("aValue");
        assertSame(aValue, aValue2);


    }


}
