package com.jenkov.container;

import com.jenkov.container.itf.factory.IGlobalFactory;

import java.util.Map;

/**
 * The factory manager can keep track of the factories in the application.
 * You can register factories, unregister factories and other stuff.
 *
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public interface IContainer {

    /**
     * Adds a factory to the container using the given name.
     * @param name A name to identify the factory by.
     * @param factory A factory producing some component.
     */
    public void addFactory(String name, IGlobalFactory factory);

    /**
     * Adds a value factory to the container using the given name.
     * A value factory just returns the value passed in the value parameter.
     * Thus the value object becomes a singleton.
     * Value factories can be used to add constants or configuration parameters to the container,
     * though these can also be added in scripts.
     *
     * @param name The name to identify the factory by.
     * @param value The value the value factory is to return (as a singleton).
     */
    public void addValueFactory(String name, Object value);

    /**
     * Replaces the existing factory with the given name, with the new factory passed as parameter.
     * All factories referencing the old factory will hereafter reference the new factory.
     * @param name       The name of the factory to replace.
     * @param newFactory The new factory that is to replace the old.
     * @return           The old factory - the one that was replaced.
     */
    public IGlobalFactory replaceFactory(String name, IGlobalFactory newFactory);

    /**
     * Removes the factory identified by the given name from the container.
     * @param name The name identifying the factory to remove.
     */
    public void removeFactory(String name);

    /**
     * Returns the factory identified by the given name.
     * @param name The name identifying the factory to return.
     * @return The factory identified by the given name.
     */
    public IGlobalFactory getFactory(String name);

    /**
     * Returns a Map containing all the factories in this container.
     * @return A Map containing all the factories in this container.
     */
    public Map<String, IGlobalFactory> getFactories();

    /**
     * Returns instance of whatever component the factory identified by the given
     * name produces.
     * @param name    The name of the factory to obtain an instance from. 
     * @param parameters Any parameters needed by the factory to produce the component instance.
     * @return An instance of the component the factory identified by the given name produces.
     */
    public Object instance(String name, Object ... parameters);

    /**
     * Initializes the container. Currently this means creating all singletons and other cached instances.
     */
    public void init();

    /**
     * Executes the given life cycle phase on all factories in the container.
     * @param phase The name of the life cycle phase to execute ("config", "dipose" etc.)
     */
    public void execPhase(String phase);

    /**
     * Executes the given life cycle phase on the factory identified by the given name.
     * @param phase The name of the life cycle phase to execute ("config", "dispose" etc.)
     * @param name The name of the factory to execute the life cycle phase on.
     */
    public void execPhase(String phase, String name);

    /**
     * Executes the "dispose" life cycle phase on all factories in the container.
     */
    public void dispose();

}
