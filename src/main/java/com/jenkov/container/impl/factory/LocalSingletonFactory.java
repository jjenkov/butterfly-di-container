package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * This class is a local singleton factory.
 * <p>
 * Local singletons do not have their own life cycle phases. The products managed by a local singleton
 * will share life cycle phases with the global factory they are part of. If you need to manage the
 * life cycle of a local singleton, make it a named local factory, and reference the named local product
 * (the singleton instance) from the global factory's life cycle phases.
 *
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class LocalSingletonFactory extends LocalFactoryBase implements ILocalFactory {

    protected ILocalFactory sourceFactory = null;
    protected Object instance = null;

    public LocalSingletonFactory(ILocalFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    public Class getReturnType() {
        return this.sourceFactory.getReturnType();
    }

    public synchronized Object instance(Object[] parameters, Object[] localProducts) {
        if (this.instance == null) {
            this.instance = this.sourceFactory.instance(parameters, localProducts);
        }
        return this.instance;
    }


}
