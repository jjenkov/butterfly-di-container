package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class InstanceMethodFactory extends LocalFactoryBase implements ILocalFactory {

    protected Method method = null;
    protected ILocalFactory methodInvocationTargetFactory = null;
    protected List<ILocalFactory> methodArgFactories = new ArrayList<>();


    public InstanceMethodFactory(Method method, ILocalFactory methodInvocationTargetFactory, List<ILocalFactory> methodArgFactories) {
        if (method == null) throw new IllegalArgumentException("Method cannot be null");
        if (methodInvocationTargetFactory == null)
            throw new IllegalArgumentException("Method invocation target cannot be null");
        this.method = method;
        this.methodInvocationTargetFactory = methodInvocationTargetFactory;
        this.methodArgFactories = methodArgFactories;
    }


    public Class getReturnType() {
        //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
        if (isVoidReturnType()) {
            return methodInvocationTargetFactory.getReturnType();
        }

        return this.method.getReturnType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object[] arguments = FactoryUtil.toArgumentArray(this.methodArgFactories, parameters, localProducts);
        try {
            Object target = this.methodInvocationTargetFactory.instance(parameters, localProducts);
            if (target == null) {
                throw new NullPointerException("The object call the method " + method.toString() + " on was null");
            }
            Object returnValue = method.invoke(target, arguments);

            //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
            if (isVoidReturnType()) {
                return target;
            } else {
                return returnValue;
            }

        } catch (Throwable t) {
            throw new FactoryException(
                    "InstanceMethodFactory", "INSTANTIATION_ERROR",
                    "Error instantiating object from instance method [" + this.method + "]", t);
        } finally {
            for (int j = 0; j < arguments.length; j++) arguments[j] = null;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<InstanceMethodFactory: ");
        builder.append(method);
        builder.append("> --> ");

        builder.append(this.methodInvocationTargetFactory);

        return builder.toString();
    }

    //this method is added only for testability. It is not part of the IFactory interface.
    public ILocalFactory getMethodInvocationTargetFactory() {
        return methodInvocationTargetFactory;
    }

    public Method getMethod() {
        return method;
    }

    private boolean isVoidReturnType() {
        return void.class.equals(this.method.getReturnType()) || this.method.getReturnType() == null;
    }

}
