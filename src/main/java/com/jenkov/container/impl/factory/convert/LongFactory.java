package com.jenkov.container.impl.factory.convert;

import com.jenkov.container.impl.factory.LocalFactoryBase;
import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class LongFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public LongFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return long.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return Long.parseLong(this.sourceFactory.instance(parameters, localProducts).toString());
    }
}
