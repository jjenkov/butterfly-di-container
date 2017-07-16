package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class MethodFactory extends LocalFactoryBase implements ILocalFactory {

    protected Method method = null;
    protected final Object methodInvocationTarget = null;    //todo remove this field, or change it to Class (methodOwnerClass).
    protected ILocalFactory methodInvocationTargetFactory = null;
    protected List<ILocalFactory> methodArgFactories = new ArrayList<>();


    public MethodFactory(Method method, ILocalFactory methodInvocationTargetFactory, List<ILocalFactory> methodArgFactories) {
        if (method == null) throw new IllegalArgumentException("Method cannot be null");
        this.method = method;
        this.methodInvocationTargetFactory = methodInvocationTargetFactory;
        this.methodArgFactories = methodArgFactories;
    }

    public MethodFactory(Method method, List<ILocalFactory> methodArgFactories) {
        this.method = method;
        this.methodArgFactories = methodArgFactories;
    }


    public Class getReturnType() {
        //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
        if (isVoidReturnType()) {
            if (methodInvocationTargetFactory != null) return methodInvocationTargetFactory.getReturnType();
            if (methodInvocationTarget != null) return methodInvocationTarget.getClass();
        }

        //if a method returns a parameterized product, not java.lang.Object should be returned, but the parameterized type.
        //Type returnType = this.method.getGenericReturnType();
        //if(returnType instanceof ParameterizedType){
        //    return (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
        //}
        //if(returnType instanceof TypeVariable){

        //}

        return this.method.getReturnType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object[] arguments = FactoryUtil.toArgumentArray(this.methodArgFactories, parameters, localProducts);
        try {
            if (this.methodInvocationTargetFactory != null) {
                //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
                if (isVoidReturnType()) {
                    Object target = this.methodInvocationTargetFactory.instance(parameters, localProducts);
                    if (target == null) {
                        throw new NullPointerException("The object call the method " + method.toString() + " on was null");
                    }
                    method.invoke(target, arguments);
                    return target;
                } else {
                    return method.invoke(this.methodInvocationTargetFactory.instance(parameters, localProducts), arguments);
                }
            }

            //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
            if (isVoidReturnType()) {
                method.invoke(this.methodInvocationTarget, arguments);
                return this.methodInvocationTarget;
            }

            return method.invoke(this.methodInvocationTarget, arguments);
        } catch (Throwable t) {
            throw new FactoryException(
                    "MethodFactory", "INSTANTIATION_ERROR",
                    "Error instantiating object from factory method " + this.method, t);
        } finally {
            for (int j = 0; j < arguments.length; j++) arguments[j] = null;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<MethodFactory: ");
        builder.append(method);
        builder.append("> --> ");
        if (this.methodInvocationTargetFactory != null) {
            builder.append(this.methodInvocationTargetFactory);
        } else {
            builder.append("<");
            builder.append(this.methodInvocationTarget);
            builder.append(">");
        }

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
