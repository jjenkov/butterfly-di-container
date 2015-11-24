package com.jenkov.container.impl.factory;

import com.jenkov.container.IContainer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**

 */
public class FactoryInterfaceAdapter implements InvocationHandler {

    IContainer          container                    = null;
    Map<Method, String> instanceMethodFactoryNameMap = new HashMap<Method, String>();
    String              defaultFactoryName           = null;

    public FactoryInterfaceAdapter(IContainer container, String defaultFactoryName) {
        this.container = container;
        this.defaultFactoryName = defaultFactoryName;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String factoryName = null;
        synchronized(this.instanceMethodFactoryNameMap){
            factoryName = this.instanceMethodFactoryNameMap.get(method);
            if(factoryName == null){
                String methodName = method.getName();
                if(methodName.endsWith("Instance") || methodName.endsWith("instance")){
                    factoryName = methodName.substring(0, methodName.length() - "Instance".length());
                } else {
                    factoryName = methodName;
                }
                if(factoryName.length() == 0) factoryName = defaultFactoryName;
                this.instanceMethodFactoryNameMap.put(method, factoryName);
            }
        }
        return this.container.instance(factoryName, args);
    }
}
