package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**

 */
public class MapFactory extends LocalFactoryBase implements ILocalFactory {


    protected List<ILocalFactory> keyFactories = null;
    protected List<ILocalFactory> valueFactories = null;

    /**
     * If true, produces a map of the product factories instead of the products.
     */
    protected boolean isFactoryMap = false;

    public MapFactory(List<ILocalFactory> keyFactories, List<ILocalFactory> valueFactories) {
        this.keyFactories = keyFactories;
        this.valueFactories = valueFactories;
    }

    public Class getReturnType() {
        return Map.class;
    }

    public synchronized boolean isFactoryMap() {
        return isFactoryMap;
    }

    public synchronized void setFactoryMap(boolean factoryMap) {
        isFactoryMap = factoryMap;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Map map = new HashMap();

        Iterator<ILocalFactory> keyFactoryIterator = keyFactories.iterator();
        Iterator<ILocalFactory> valueFactoryIterator = valueFactories.iterator();
        if (!isFactoryMap()) {
            while (keyFactoryIterator.hasNext()) {
                ILocalFactory keyFactory = keyFactoryIterator.next();
                ILocalFactory valueFactory = valueFactoryIterator.next();
                map.put(keyFactory.instance(parameters, localProducts),
                        valueFactory.instance(parameters, localProducts));
            }
        } else {
            while (keyFactoryIterator.hasNext()) {
                ILocalFactory keyFactory = keyFactoryIterator.next();
                ILocalFactory valueFactory = valueFactoryIterator.next();
                map.put(keyFactory.instance(parameters, localProducts), valueFactory);
            }
        }
        return map;
    }
}