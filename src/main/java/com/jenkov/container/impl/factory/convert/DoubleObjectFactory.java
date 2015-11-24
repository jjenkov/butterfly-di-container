package com.jenkov.container.impl.factory.convert;

import com.jenkov.container.impl.factory.LocalFactoryBase;
import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class DoubleObjectFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public DoubleObjectFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return Double.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return new Double(this.sourceFactory.instance(parameters, localProducts).toString());
    }


}
