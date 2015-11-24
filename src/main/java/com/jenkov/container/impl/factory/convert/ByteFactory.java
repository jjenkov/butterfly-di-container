package com.jenkov.container.impl.factory.convert;

import com.jenkov.container.impl.factory.LocalFactoryBase;
import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class ByteFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;

    public ByteFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return byte.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return Byte.parseByte(this.sourceFactory.instance(parameters, localProducts).toString());
    }
}
