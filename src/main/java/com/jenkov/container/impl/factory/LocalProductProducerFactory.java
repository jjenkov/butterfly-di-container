package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalProductProducerFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory instantiationFactory = null;
    protected int index = 0;

    public LocalProductProducerFactory(ILocalFactory localProductFactory, int index) {
        if (localProductFactory == null) {
            throw new IllegalArgumentException("Local product factory cannot be null");
        }
        this.instantiationFactory = localProductFactory;
        this.index = index;
    }

    public ILocalFactory getInstantiationFactory() {
        return instantiationFactory;
    }

    public int getIndex() {
        return index;
    }

    public Class getReturnType() {
        return this.instantiationFactory.getReturnType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object product = this.instantiationFactory.instance(parameters, localProducts);
        localProducts[this.index] = product;
        return product;
    }
}
