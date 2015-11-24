package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
   Extend this factory when implementing custom factories. Extending this class will reduce the risk of
   a faulty implementation. Since the class is abstract the compiler will help spotting wrong
   implementations.
 */
public abstract class LocalFactoryBase implements ILocalFactory {

    public abstract Class getReturnType();

    public abstract Object instance(Object[] parameters, Object[] localProducts);
}
