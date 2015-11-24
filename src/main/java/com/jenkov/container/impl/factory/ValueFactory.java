package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.ILocalFactory;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class ValueFactory extends LocalFactoryBase implements ILocalFactory {

    public Object value      = null;

    public ValueFactory(Object value) {
        this.value = value;
    }

    public Class getReturnType() {
        if(this.value == null) return null;
        return this.value.getClass();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        return this.value;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder .append("<ValueFactory : ")
                .append(getReturnType())
                .append("> --> <")
                .append(this.value)
                .append(">");

        return builder.toString();
    }
}
