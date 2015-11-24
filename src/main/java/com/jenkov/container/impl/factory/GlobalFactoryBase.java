package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**

 */
public abstract class GlobalFactoryBase implements IGlobalFactory {
    protected ILocalFactory  localInstantiationFactory  = null;
//    protected IGlobalFactory globalInstantiationFactory = null;
    protected Map<String, List<ILocalFactory>> phases = new ConcurrentHashMap();

    protected int localProductCount = 0;

    public void setLocalProductCount(int localProductCount) {
        this.localProductCount = localProductCount;
    }

    public int getLocalProductCount() {
        return localProductCount;
    }

    public ILocalFactory getLocalInstantiationFactory() {
        return localInstantiationFactory;
    }

    public void setLocalInstantiationFactory(ILocalFactory localInstantiationFactory) {
        this.localInstantiationFactory = localInstantiationFactory;
    }

//    public IGlobalFactory getGlobalInstantiationFactory() {
//        return globalInstantiationFactory;
//    }

//    public void setGlobalInstantiationFactory(IGlobalFactory globalInstantiationFactory) {
//        this.globalInstantiationFactory = globalInstantiationFactory;
//    }

    public void setPhase(String phase, List<ILocalFactory> factories){
        this.phases.put(phase, factories);
    }
    

    /**
     * This method is called by the container when executing a phase in a factory that supports life cycle phases.
     * The container knows nothing about local products, therefore this method is called. Only the concrete
     * factory knows about cached local products (if any).
     *
     * <br/><br/>
     * This is the method a global factory will override when implementing life cycle phase behaviour, e.g. for
     * cached objects.
     *
     * @param phase       The name of the phase to execute. For instance, "config" or "dispose".
     * @param parameters  The parameters passed to the container when the phase begins. For instance to
     *                    an instance() method call, or an execPhase(phase, factory, parameters) call.
     * @return            Null, or the local products the phase ends up being executed on. If executed
     *                    for several local product arrays (e.g. in pools or flyweights), null will be returned, since it does not
     *                    make sense to return anything. Returning anything would only make sense for
     *                    the "create" phase, but currently this phase does not use the execPhase() method
     *                    to carry out its work. It uses the factory.instance() methods instead.
     */
    public Object[] execPhase(String phase, Object ... parameters){
        return null;
    }
    /*
    {
        Object[] localProducts = this.localProductCount > 0? new Object[this.localProductCount] : null;
        return execPhase(phase, parameters, localProducts);
    }*/

    /**
     * Executes a life cycle phase on the given local products, using the given input parameters.
     * This method is a utility method that global factories can use to implement their phase execution.
     * @param phase          The name of the phase to execute.
     * @param parameters     Any input parameters to pass to the phase factory chain.
     * @param localProducts  Any local products (typically cached products) to pass to the phase factory chain.

     * @return            Null, or the local products the phase ends up being executed on. If executed
     *                    for several local product arrays (e.g. in pools or flyweights), null will be returned, since it does not
     *                    make sense to return anything. Returning anything would only make sense for
     *                    the "create" phase, but currently this phase does not use the execPhase() method
     *                    to carry out its work. It uses the factory.instance() methods instead.
     */
    protected Object[] execPhase(String phase, Object[] parameters, Object[] localProducts){
        List<ILocalFactory> phaseFactories = this.phases.get(phase);
        if(phaseFactories != null){
            for(ILocalFactory factory : phaseFactories){
                factory.instance(parameters, localProducts);
            }
        }
        return localProducts;
    }

}
