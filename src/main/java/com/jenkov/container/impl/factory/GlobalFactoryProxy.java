package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;

/**

 */
public class GlobalFactoryProxy implements IGlobalFactory {

    protected IGlobalFactory delegateFactory = null;

    public GlobalFactoryProxy(IGlobalFactory delegateFactory) {
        this.delegateFactory = delegateFactory;
    }

    public synchronized IGlobalFactory getDelegateFactory() {
        return delegateFactory;
    }

    public synchronized IGlobalFactory setDelegateFactory(IGlobalFactory delegateFactory) {
        IGlobalFactory oldFactory = this.delegateFactory;
        this.delegateFactory = delegateFactory;
        return oldFactory;
    }

    public synchronized Class getReturnType() {
        return this.delegateFactory.getReturnType();
    }

    public Object instance(Object ... parameters) {
        IGlobalFactory localDelegateFactory = null;
        synchronized(this){
            localDelegateFactory = this.delegateFactory;
        }
        return localDelegateFactory.instance(parameters);
    }

    public Object[] execPhase(String phase, Object ... parameters) {
        IGlobalFactory localDelegateFactory = null;
        synchronized(this){
            localDelegateFactory = this.delegateFactory;
        }
        return localDelegateFactory.execPhase(phase, parameters);
    }
}
