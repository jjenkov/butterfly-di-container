package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalThreadSingletonFactory extends LocalFactoryBase implements ILocalFactory {
    public ILocalFactory            sourceFactory = null;
    public Map<Thread, Object> instances     = new HashMap<Thread, Object>();


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
        Object instance = this.instances.get(callingThread);
        if(instance == null){
            instance = this.sourceFactory.instance(parameters, localProducts);
            this.instances.put(callingThread, instance);
        }
        return instance;
    }

    public String toString() {
        return "<LocalThreadSingletonFactory> --> "+ this.sourceFactory.toString();
    }

}
