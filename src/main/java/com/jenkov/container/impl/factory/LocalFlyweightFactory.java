package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalFlyweightFactory extends LocalFactoryBase implements ILocalFactory {

    public ILocalFactory sourceFactory = null;
    public Map<FlyweightKey, Object> instances = new HashMap<FlyweightKey, Object>();


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
        Object instance = this.instances.get(key);
        if(instance == null){
            instance = this.sourceFactory.instance(parameters, localProducts);
            this.instances.put(key, instance);
        }
        return instance;
    }

    public String toString() {
        return "<LocalFlyweightFactory> --> "+ this.sourceFactory.toString();
    }

}
