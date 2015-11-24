package com.jenkov.container.script;

import junit.framework.TestCase;
import com.jenkov.container.IContainer;
import com.jenkov.container.Container;

import java.util.Locale;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalizedResourceFactoryTest extends TestCase {

    public static Locale getLocale(){
        return new Locale("da", "dk");
    }

    public void test(){
        IContainer           container = new Container();
        ScriptFactoryBuilder builder   = new ScriptFactoryBuilder(container);

        builder.addFactory("UK     = 1 java.util.Locale.UK;");
        builder.addFactory("DK     = 1 java.util.Locale('da', 'dk');");
        builder.addFactory("locale = * com.jenkov.container.script.LocalizedResourceFactoryTest.getLocale(); ");
        builder.addFactory("aText  = L <UK:'hello', DK:'hej'>;");

        String aText = (String) container.instance("aText");
        assertEquals("hej", aText);
        
    }
}
