package com.jenkov.container.java;

import com.jenkov.container.IContainer;
import com.jenkov.container.impl.factory.FactoryUtil;
import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.itf.factory.FactoryException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Method;

/**
   A JavaFactoryBuilder is capable of adding JavaFactory's to an IContainer instance.
 */
public class JavaFactoryBuilder {

    protected IContainer container = null;

    /**
     * Creates a new JavaFactoryBuilder which inserts its factories into the given container.
     * @param container The container to insert Java factories into.
     */
    public JavaFactoryBuilder(IContainer container) {
        this.container = container;
    }

    /**
     * Adds a Java factory to the container.
     * The return type of the Java factory is determined by looking at the return type of
     * the instance() method in the Java factory added (it doesn't have to be Object).
     *
     * @param name The name to identify this factory by in the container.
     * @param newFactory The Java factory to add to the container.
     */
    public void addFactory(String name, JavaFactory newFactory){
        addFactory(name, null, newFactory);
    }

    /**
     * Adds a Java factory to the container.
     * @param name The name to identify this factory by in the container.
     * @param returnType The type of component the added factory produces.
     * @param newFactory The Java factory to add to the container.
     */
    public void addFactory(String name, Class returnType, JavaFactory newFactory){
        if(returnType == null) {
            setReturnType(newFactory);
        } else {
            newFactory.setReturnType(returnType);
        }
        injectFactories(name, newFactory);
        container.addFactory(name, newFactory);
    }

    /**
     * Adds a Java factory to the container.
     * The return type of the Java factory is determined by looking at the return type of
     * the instance() method in the Java factory added (it doesn't have to be Object).
     *
     * @param name The name to identify this factory by in the container.
     * @param newFactory The Java factory to add to the container.
     */
    public void replaceFactory(String name, JavaFactory newFactory){
        replaceFactory(name, null, newFactory);
    }

    /**
     * Adds a Java factory to the container.
     * @param name The name to identify this factory by in the container.
     * @param returnType The type of component the added factory produces.
     * @param newFactory The Java factory to add to the container.
     */
    public void replaceFactory(String name, Class returnType, JavaFactory newFactory){
        if(returnType == null) {
            setReturnType(newFactory);
        } else {
            newFactory.setReturnType(returnType);
        }
        injectFactories(name, newFactory);
        container.replaceFactory(name, newFactory);
    }

    private void setReturnType(JavaFactory newFactory) {
        try {
            Method method = newFactory.getClass().getMethod("instance", new Class[]{Object[].class});
            newFactory.setReturnType(method.getReturnType());
        } catch (NoSuchMethodException e) {
            throw new FactoryException(
                    "JavaFactoryBuilder", "INSTANCE_METHOD_NOT_FOUND",
                    "instance method not found for factory", e);
        }
    }

    private void injectFactories(String name, JavaFactory newFactory) {
        Class factoryClass = newFactory.getClass();

        for(Field field : factoryClass.getFields()){
            Class rawType       = field.getType();

            if(isFactory(rawType)){
                String factoryName = field.getName();
                Factory factoryAnnotation = field.getAnnotation(Factory.class);
                if(factoryAnnotation != null){
                    factoryName = factoryAnnotation.value();
                }
                IGlobalFactory factory = container.getFactory(factoryName);
                if(factory == null) {
                    throw new FactoryException(
                            "JavaFactoryBuilder", "INJECT_FACTORIES",
                            "Factory field/annotation name '" + factoryName + "' does not match a factory name in the container");
                }
                Class factoryReturnType = factory.getReturnType();

                Type type = field.getGenericType();
                if(type instanceof ParameterizedType){
                    Class genericType = null;
                    ParameterizedType pType = (ParameterizedType) type;
                    genericType = (Class) pType.getActualTypeArguments()[0];

                    if(!FactoryUtil.isSubstitutableFor(factoryReturnType, genericType)){
                        throw new FactoryException(
                                "JavaFactoryBuilder", "MIS_MATCHING_RETURN_TYPE",
                                "Mismatching return type in factory named '" + name + "' for factory field '" + field.getName() + "'. "
                                + "Factory " + field.getName() + " returns " + factory.getReturnType() + ". "
                                + "Factory field " + field.getName() + " is parameterized to " + genericType
                        );
                    }
                }
                try {
                    field.set(newFactory, factory);
                } catch (IllegalAccessException e) {
                    throw new FactoryException(
                            "JavaFactoryBuilder", "INSTANCE_METHOD_NOT_ACCESSIBLE",                    
                            "Error setting factory field " + field.getName(), e);
                }
            }
        }
    }

    private boolean isFactory(Class rawType) {
        return rawType.equals(IGlobalFactory.class);
    }
}
