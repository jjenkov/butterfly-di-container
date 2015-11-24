package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class StaticMethodFactory extends LocalFactoryBase implements ILocalFactory {

    protected Method              method             = null;
    protected List<ILocalFactory> methodArgFactories = new ArrayList<ILocalFactory>();


    public StaticMethodFactory(Method method, List<ILocalFactory> methodArgFactories) {
        if(method == null) throw new IllegalArgumentException("Method cannot be null");
        this.method             = method;
        this.methodArgFactories = methodArgFactories;
    }


    public Class getReturnType() {
        //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
        if(isVoidReturnType()){
            return Class.class;
        }

        return this.method.getReturnType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        Object[] arguments = FactoryUtil.toArgumentArray(this.methodArgFactories, parameters, localProducts);
        try {
            //if a method returns void, it should return the invocation target instead, enabling method chaining on methods returning void.
            Object returnValue = method.invoke(null, arguments);
            if(isVoidReturnType()){
                return null;
            }

            return returnValue;
        } catch(NullPointerException e){
            throw new FactoryException(
                    "StaticMethodFactory", "INSTANTIATION_ERROR",
                    "Error instantiating object from static method [" + this.method +
                            "]. Are you sure the method is declared static?"
                    , e);
        } catch (Throwable t){
            throw new FactoryException(
                    "StaticMethodFactory", "INSTANTIATION_ERROR",
                    "Error instantiating object from static method [" + this.method + "]", t);
        }finally{
            //for(int j=0; j<arguments.length; j++)arguments[j] = null;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<StaticMethodFactory: ");
        builder.append(method);
        builder.append("> --> ");

        return builder.toString();
    }


    public Method getMethod() {
        return method;
    }

    private boolean isVoidReturnType() {
        return void.class.equals(this.method.getReturnType()) || this.method.getReturnType() == null;
    }

}