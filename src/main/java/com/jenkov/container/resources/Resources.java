package com.jenkov.container.resources;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.script.ScriptFactoryBuilder;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Resources {

    public static final ThreadLocal<Locale> locale = new ThreadLocal<Locale>();


    public static Locale getThreadLocale(){
        return locale.get();
    }

    public static String getString(Map<Locale, String> texts, Locale paramLocale, Locale threadLocale, Locale defaultLocale){
        if(texts == null) throw new NullPointerException("texts parameter (Map) was null");
        Locale actualLocale = paramLocale;
        if(actualLocale == null) actualLocale = threadLocale;
        if(actualLocale == null) actualLocale = defaultLocale;

        return texts.get(actualLocale);
    }


    public static void main(String[] args) throws NoSuchMethodException {

        IContainer           container = new Container();
        ScriptFactoryBuilder builder   = new ScriptFactoryBuilder(container);

        builder.addFactory("UK = java.util.Locale('en', 'gb'); ");
        builder.addFactory("DK = java.util.Locale('da', 'dk'); ");
        builder.addFactory("threadLocale = com.jenkov.container.resources.Resources.getThreadLocale(); ");
        builder.addFactory("localize = * com.jenkov.container.resources.Resources.getString($1, $0, threadLocale, UK);");
        builder.addFactory("astring  = * localize($0, <DK : 'hej', UK : 'hello'> } "
        );

        String astring = (String) container.instance("astring");

        System.out.println("astring = " + astring);

        

    }




}
