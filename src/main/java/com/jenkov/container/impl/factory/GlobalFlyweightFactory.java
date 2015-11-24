package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * todo fix phase execution for local products
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class GlobalFlyweightFactory extends GlobalFactoryBase implements IGlobalFactory {

    public Map<FlyweightKey, Object[]> localProducts = new HashMap<FlyweightKey, Object[]>();

    public Class getReturnType() {
        return getLocalInstantiationFactory().getReturnType();
    }

    public Object instance(Object ... parameters) {
        FlyweightKey key = new FlyweightKey(parameters);
        Object[] keyLocalProducts = this.localProducts.get(key);

        if(keyLocalProducts != null) return keyLocalProducts[0];
        keyLocalProducts = new Object[getLocalProductCount()];
        return instance(parameters, keyLocalProducts);
    }

    public synchronized Object instance(Object[] parameters, Object[] localProducts) {
        FlyweightKey key = new FlyweightKey(parameters);
        Object[] keyLocalProducts = this.localProducts.get(key);
        if(keyLocalProducts == null){
            keyLocalProducts = localProducts;
            keyLocalProducts[0] = getLocalInstantiationFactory().instance(parameters, localProducts);
            this.localProducts.put(key, keyLocalProducts);
            execPhase("config", parameters, keyLocalProducts);
        }
        return keyLocalProducts[0];
    }

    public Object[] execPhase(String phase, Object ... parameters) {
        for(FlyweightKey key : localProducts.keySet()){
            Object[] keyLocalProducts = localProducts.get(key);
            execPhase(phase, parameters, keyLocalProducts);
        }
        return null;
    }

    public String toString() {
        return "<GlobalFlyweightFactory> --> "+ getLocalInstantiationFactory().toString();
    }

}
