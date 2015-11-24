package com.jenkov.container.java;

import com.jenkov.container.IContainer;
import com.jenkov.container.itf.factory.IGlobalFactory;

/**

 */
public class JavaFactory implements IGlobalFactory {

    protected Class      returnType = null;

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Object instance(Object... parameters) {
        return null;
    }

    public Object[] execPhase(String phase, Object... parameters) {
        return new Object[0];  
    }
}
