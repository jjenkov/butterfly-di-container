package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.itf.factory.IGlobalFactory;
import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class MapFactoryTest extends TestCase {

    public void test() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("map = * <'key':'value'>; ");

        Map map = (Map) container.instance("map");
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }

    public void testFactoryAsValue() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("index  = 'index value';");

        String indexValue = (String) container.instance("index");
        assertEquals("index value", indexValue);


        builder.addFactory("uriMap = 1 < '/index3.html' : #index >;");

        Map map = (Map) container.instance("uriMap");
        assertEquals(1, map.size());
        assertTrue(map.get("/index3.html") instanceof IGlobalFactory);

        IGlobalFactory indexFactory = (IGlobalFactory) map.get("/index3.html");
        indexValue = (String) indexFactory.instance();
        assertEquals("index value", indexValue);
    }

    public void testNewInstanceAndSingleonMaps() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("index  = 'index value';");

        String indexValue = (String) container.instance("index");
        assertEquals("index value", indexValue);

        builder.addFactory("uriMap = * < '/index3.html' : #index >;");
        Map map1 = (Map) container.instance("uriMap");
        Map map2 = (Map) container.instance("uriMap");

    }
}
