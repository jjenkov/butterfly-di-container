package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class FlyweightKey {
    Object[] parameters = null;
    int hashCode = 0;

    public FlyweightKey(Object[] parameters) {
        this.parameters = parameters;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] == null) throw new FactoryException(
                    "FlyweightKey", "ERROR_FLYWEIGHT_KEY_PARAMETER",
                    "Flyweight parameter " + i + " was null. You cannot use null parameters with flyweight factories");
            this.hashCode *= parameters[i].hashCode();
        }
    }

    public Object[] getParameters() {
        return parameters;
    }

    public boolean equals(Object otherKeyObj) {
        FlyweightKey otherKey = (FlyweightKey) otherKeyObj;
        if (otherKey == null) return false;

        if (parameters.length != otherKey.getParameters().length) return false;

        for (int i = 0; i < parameters.length; i++) {
            if (!this.parameters[i].equals(otherKey.getParameters()[i])) return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCode;
    }
}
