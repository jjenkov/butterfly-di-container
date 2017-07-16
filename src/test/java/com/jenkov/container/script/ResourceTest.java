package com.jenkov.container.script;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.resources.Resources;
import junit.framework.TestCase;

import java.util.Locale;

/**

 */
public class ResourceTest extends TestCase {

    public static final ThreadLocal<Locale> threadLocal = new ThreadLocal<>();

    public static Locale getThreadLocale() {
        return threadLocal.get();
    }


    public void testLocalization() {
        IContainer container = new Container();
        ScriptFactoryBuilder builder = new ScriptFactoryBuilder(container);

        builder.addFactory("UK = java.util.Locale('en', 'gb'); ");
        builder.addFactory("DK = java.util.Locale('da', 'dk'); ");
        builder.addFactory("ES = java.util.Locale('es', 'es'); ");
        builder.addFactory("threadLocale = * com.jenkov.container.resources.Resources.getThreadLocale(); ");
        builder.addFactory("localize = * com.jenkov.container.resources.Resources.getString($1, $0, threadLocale, UK);");
        builder.addFactory("astring  = * localize($0, <DK : 'hej', UK : 'hello', ES: 'hola'> ); ");

        assertNull(container.instance("threadLocale"));


        String astring = (String) container.instance("astring");
        assertEquals("hello", astring);

        astring = (String) container.instance("astring", new Locale("da", "dk"));
        assertEquals("hej", astring);


        Resources.locale.set(new Locale("da", "dk"));
        assertNotNull(container.instance("threadLocale"));
        astring = (String) container.instance("astring");
        assertEquals("hej", astring);

        assertEquals("hola", container.instance("astring", new Locale("es", "es")));


//        Locale[] locales = Locale.getAvailableLocales();
//        for(Locale alocale : locales){
//            System.out.println("locale.getCountry() = " + alocale.getCountry() + " - " + alocale.getLanguage());
//        }

    }
}
