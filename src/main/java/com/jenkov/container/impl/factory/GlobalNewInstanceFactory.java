package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;

/**
 *
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalNewInstanceFactory extends GlobalFactoryBase implements IGlobalFactory {

    public Class getReturnType() {
        return getLocalInstantiationFactory().getReturnType();
    }

    public Object instance(Object ... parameters) {
        Object[] localProducts = getLocalProductCount() > 0 ? new Object[getLocalProductCount()] : null;
        return instance(parameters, localProducts);
    }

    /* todo remove this method, because it will only be called from the instance() method above */
    public Object instance(Object[] parameters, Object[] localProducts) {
        Object instance = getLocalInstantiationFactory().instance(parameters, localProducts);
        if(localProducts != null) localProducts[0] = instance;

        execPhase("config", parameters, localProducts);

        return instance;
    }

    public Object[] execPhase(String phase, Object ... parameters) {
        return null;
    }

}
