package com.jenkov.container.itf.factory;

/**
    A Local Factory is a factory that is used inside the container. Local factories are typically chained into
    factory chains. You cannot plug a local factory into a container. Local factories and chains are typically
    called from within a global factory. Local factories are mostly used in global factories when the global
    factories are created from a Butterfly Container Script. If you plugin custom IGlobalFactory implementations
    you will typically not need ILocalFactory instances inside it. You would just use plain Java.
 */
public interface ILocalFactory {

     public Class getReturnType();

    /**
     * This method is intended to be called *internally between* factories when creating
     * an instance of some class. It is not intended to be called directly by client code,
     * or from the Container. For that purpose use the instance(Object ... parameters)
     * method. If you do call this method directly, a null will suffice for the localProducts
     * array.
     *
     * NOTE: Only called on local factories. Never on global factories.
     *
     * <br/><br/>
     * If you develop a custom IFactory implementation you should
     * extend LocalFactoryBase and override this method, not the instance(Object ... parameters)
     * method. If your factory implementation calls any other factories it should pass on
     * both the parameters and localProducts object arrays!!
     * ... as in factory.instance(parameters, localProducts); .
     *  Do not just call the instance() or instance(parameters) method.
     *
     * @param parameters
     * @param localProducts
     * @return The created object.
     */
    public Object instance(Object[] parameters, Object[] localProducts);
}
