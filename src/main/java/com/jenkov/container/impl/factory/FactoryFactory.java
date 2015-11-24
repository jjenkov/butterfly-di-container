package com.jenkov.container.impl.factory;

import com.jenkov.container.IContainer;
import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;
import com.jenkov.container.itf.factory.IGlobalFactory;

import java.lang.reflect.Proxy;

/**

 */
public class FactoryFactory extends LocalFactoryBase implements ILocalFactory {

    protected IContainer container              = null;
    protected Class      customFactoryInterface = null;
    protected String     defaultFactoryName     = null; //name of factory to inject / call, when interface method is named "instance()";

    public FactoryFactory(IContainer returnValue, String defaultFactoryName) {
        this.container          = returnValue;
        this.defaultFactoryName = defaultFactoryName;
    }

    public void setCustomFactoryInterface(Class customFactoryInterface) {
        if(!customFactoryInterface.isInterface()) {
            throw new FactoryException(
                    "FactoryFactory", "CONTAINER_INTERFACE_ADAPTATION",
                    "Can only adapt container to an interface. Method parameter " +
                    "to inject container into was : " + customFactoryInterface);
        }

        this.customFactoryInterface = customFactoryInterface;
    }

    public Class getReturnType() {
        if(this.customFactoryInterface != null) return this.customFactoryInterface;
        return IGlobalFactory.class;
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Class returnType = getReturnType();

        if(IGlobalFactory.class.equals(returnType) || FactoryUtil.isSubstitutableFor(returnType, IGlobalFactory.class)){
            return this.container.getFactory(this.defaultFactoryName);
        }

        if(FactoryUtil.isSubstitutableFor(returnType, IContainer.class)){
            return this.container;
        }

        return Proxy.newProxyInstance(returnType.getClassLoader(), new Class[]{returnType},
                new FactoryInterfaceAdapter(container, this.defaultFactoryName));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<FactoryFactory : ")
                .append(getReturnType())
                .append("> --> ")
                .append(this.container);

        return builder.toString();
    }

}
