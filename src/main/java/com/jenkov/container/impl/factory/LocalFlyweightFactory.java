package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalFlyweightFactory extends LocalFactoryBase implements ILocalFactory {

    public ILocalFactory sourceFactory = null;
    public final Map<FlyweightKey, Object> instances = new HashMap<>();


    public LocalFlyweightFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public ILocalFactory getSourceFactory() {
        return sourceFactory;
    }

    public Class getReturnType() {
        return this.sourceFactory.getReturnType();
    }

    public synchronized Object instance(Object[] parameters, Object[] localProducts) {
        FlyweightKey key = new FlyweightKey(parameters);
        return this.instances.computeIfAbsent(key, k -> this.sourceFactory.instance(parameters, localProducts));
    }

    public String toString() {
        return "<LocalFlyweightFactory> --> " + this.sourceFactory.toString();
    }

}
