package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalThreadSingletonFactory extends LocalFactoryBase implements ILocalFactory {
    public ILocalFactory sourceFactory = null;
    public final Map<Thread, Object> instances = new HashMap<>();


    public LocalThreadSingletonFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public ILocalFactory getSourceFactory() {
        return sourceFactory;
    }

    public Class getReturnType() {
        return this.sourceFactory.getReturnType();
    }

    public synchronized Object instance(Object[] parameters, Object[] localProducts) {
        Thread callingThread = Thread.currentThread();
        return this.instances.computeIfAbsent(callingThread, k -> this.sourceFactory.instance(parameters, localProducts));
    }

    public String toString() {
        return "<LocalThreadSingletonFactory> --> " + this.sourceFactory.toString();
    }

}
