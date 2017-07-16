package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.Locale;
import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalizedResourceFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory resourceMapFactory = null;
    protected IGlobalFactory localeFactory = null;

    public LocalizedResourceFactory(ILocalFactory resourceMapFactory, IGlobalFactory localeFactory) {
        this.resourceMapFactory = resourceMapFactory;
        this.localeFactory = localeFactory;
    }

    public Class getReturnType() {
        return Object.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Map<Object, ILocalFactory> resourceMap = (Map) this.resourceMapFactory.instance(parameters, localProducts);
        Locale locale = (Locale) this.localeFactory.instance(parameters, localProducts);
        if (locale == null) {
            throw new NullPointerException("The 'locale' factory returned null. It must always return a valid Locale instance.");
        }
//        return resourceMap.get(locale);
        return resourceMap.get(locale).instance(parameters, localProducts);
    }

}
