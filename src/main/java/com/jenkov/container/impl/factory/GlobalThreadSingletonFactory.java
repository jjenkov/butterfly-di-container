package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * todo fix phase execution on local products
 *
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalThreadSingletonFactory extends GlobalFactoryBase implements IGlobalFactory {

    public final Map<Thread, Object[]> localProductMap = new HashMap<>();

    public Class getReturnType() {
        return getLocalInstantiationFactory().getReturnType();
    }

    public synchronized Object instance(Object... parameters) {
        Thread callingThread = Thread.currentThread();
        Object[] threadLocalProducts = this.localProductMap.get(callingThread);
        if (threadLocalProducts != null) return threadLocalProducts[0];

        threadLocalProducts = new Object[getLocalProductCount()];
        threadLocalProducts[0] = getLocalInstantiationFactory().instance(parameters, threadLocalProducts);
        execPhase("config", parameters, threadLocalProducts);
        this.localProductMap.put(callingThread, threadLocalProducts);

        return threadLocalProducts[0];
    }


    public Object[] execPhase(String phase, Object... parameters) {
        for (Thread thread : this.localProductMap.keySet()) {
            Object[] threadLocalProducts = this.localProductMap.get(thread);
            execPhase(phase, parameters, threadLocalProducts);
        }
        return null;
    }

    public String toString() {
        return "<GlobalThreadSingletonFactory> --> " + getLocalInstantiationFactory().toString();
    }

}
