package com.jenkov.container;

import com.jenkov.container.impl.factory.*;
import com.jenkov.container.itf.factory.IGlobalFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class Container implements IContainer {

    protected Map<String, IGlobalFactory> factories = null;

    public Container() {
        this.factories = new ConcurrentHashMap<String, IGlobalFactory>();
    }

    public Container(Map<String, IGlobalFactory> factories) {
        this.factories = factories;
    }


    public void addFactory(String name, IGlobalFactory factory) {
        if(this.factories.containsKey(name)) throw
                new ContainerException(
                        "Container", "FACTORY_ALREADY_EXISTS",
                        "Container already contains a factory with this name: " + name);
        this.factories.put(name, new GlobalFactoryProxy(factory));
    }

    public void addValueFactory(String id, Object value){
        GlobalFactoryBase factory = new GlobalNewInstanceFactory();
        factory.setLocalInstantiationFactory(new ValueFactory(value));
        this.factories.put(id, new GlobalFactoryProxy(factory));
    }

    public IGlobalFactory replaceFactory(String name, IGlobalFactory newFactory){
        GlobalFactoryProxy factoryProxy = (GlobalFactoryProxy) this.factories.get(name);
        if(factoryProxy == null) {
            addFactory(name, newFactory);
            return null;
        } else {
            return factoryProxy.setDelegateFactory(newFactory);
        }
    }

    public void removeFactory(String id) {
        this.factories.remove(id);
    }

    public IGlobalFactory getFactory(String id) {
        IGlobalFactory factory = this.factories.get(id);
        //if(factory == null) throw new ContainerException("Unknown Factory: " + id);
        return factory;
    }

    public Map<String, IGlobalFactory> getFactories() {
        return this.factories;
    }

    public Object instance(String id, Object ... parameters){
        IGlobalFactory factory = this.factories.get(id);
        if(factory == null) throw new ContainerException(
                "Container", "UNKNOWN_FACTORY",                    
                "Unknown Factory: " + id);
        return factory.instance(parameters);
    }

    public void init(){
        for(String key : this.factories.keySet()){
            Object factory = this.factories.get(key);

            if(factory instanceof GlobalFactoryProxy){
                factory = ((GlobalFactoryProxy) factory).getDelegateFactory();
                if(factory instanceof GlobalSingletonFactory){
                    ((GlobalSingletonFactory) factory).instance();
                }
            }
        }
    }

    public void dispose(){
        execPhase("dispose");
    }

    public void execPhase(String phase) {
        for(String key : this.factories.keySet()){
            execPhase(phase, key);
        }
    }

    public void execPhase(String phase, String factoryName) {
        Object factory  = this.factories.get(factoryName);
        if(factory instanceof GlobalFactoryProxy){
            ((GlobalFactoryProxy) factory).execPhase(phase);
        }
    }


}
