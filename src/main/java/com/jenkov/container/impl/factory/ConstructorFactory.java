package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class ConstructorFactory extends LocalFactoryBase implements ILocalFactory {

    protected Constructor       constructor             = null;
    protected List<ILocalFactory>    constructorArgFactories = new ArrayList<ILocalFactory>();

    public ConstructorFactory(Constructor constructor){
        this.constructor = constructor;
    }

    public ConstructorFactory(Constructor contructor, List<ILocalFactory> contructorArgFactories) {
        this.constructor             = contructor;
        this.constructorArgFactories = contructorArgFactories;
    }

    public ConstructorFactory(Constructor constructor, ILocalFactory[] constructorArgFactories){
        this.constructor = constructor;
        for(ILocalFactory factory : constructorArgFactories){
            this.constructorArgFactories.add(factory);            
        }
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public List<ILocalFactory> getConstructorArgFactories() {
        return constructorArgFactories;
    }

    public Class getReturnType() {
        return this.constructor.getDeclaringClass();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object[] arguments = FactoryUtil.toArgumentArray(this.constructorArgFactories, parameters, localProducts);

        Object returnValue = null;
        try {
            returnValue = this.constructor.newInstance(arguments);
        } catch (Throwable t){
            throw new FactoryException(
                    "ConstructorFactory", "CONSTRUCTOR_EXCEPTION",
                    "Error instantiating object from constructor " + this.constructor, t);
        } finally{
            for(int j=0; j<arguments.length; j++)arguments[j] = null;
        }

        return returnValue;
    }

    public String toString() {
        return "<ConstructorFactory : " + getReturnType() + ">";
    }


}
