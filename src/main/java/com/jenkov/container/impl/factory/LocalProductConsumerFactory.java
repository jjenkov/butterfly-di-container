package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalProductConsumerFactory extends LocalFactoryBase implements ILocalFactory {

    protected int index = 0;
    protected Class returnType = null;

    public LocalProductConsumerFactory(Class returnType, int index) {
        this.returnType = returnType;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return localProducts[this.index];
    }


}
