package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;

/**
    todo fix phase execution on local products
 */
public class GlobalSingletonFactory extends GlobalFactoryBase implements IGlobalFactory {

    public Object[] localProducts = null;

    public Class getReturnType() {
        return getLocalInstantiationFactory().getReturnType();
    }

    public synchronized Object instance(Object ... parameters) {
        if(this.localProducts == null){
            this.localProducts = new Object[getLocalProductCount()];
            this.localProducts[0] = getLocalInstantiationFactory().instance(parameters, localProducts);
            execPhase("config", parameters, localProducts);
        }
        return this.localProducts[0];
    }

    public Object[] execPhase(String phase, Object ... parameters) {
        return execPhase(phase, parameters, this.localProducts);
    }

    public String toString() {
        return "<GlobalSingletonFactory> --> "+ getLocalInstantiationFactory().toString();
    }


}
