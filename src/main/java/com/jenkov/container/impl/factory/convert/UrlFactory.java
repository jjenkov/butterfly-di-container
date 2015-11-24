package com.jenkov.container.impl.factory.convert;

import com.jenkov.container.impl.factory.LocalFactoryBase;
import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class UrlFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public UrlFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return URL.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object urlSource = null;
        try {
            urlSource = this.sourceFactory.instance(parameters, localProducts);
            return new URL(urlSource.toString());
        } catch (MalformedURLException e) {
            throw new FactoryException(
                    "UrlFactory", "INVALID_URL",                    
                    "Error creating URL from object: " + urlSource, e);
        }
    }
}
