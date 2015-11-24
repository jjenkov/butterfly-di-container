package com.jenkov.container.impl.factory;

import com.jenkov.container.itf.factory.FactoryException;
import com.jenkov.container.itf.factory.ILocalFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class FieldFactory extends LocalFactoryBase implements ILocalFactory {
    protected Field    field             = null;
    protected Object   fieldOwner        = null;
    protected ILocalFactory fieldOwnerFactory = null;


    public FieldFactory(Field method) {
        this.field = method;
    }

    public FieldFactory(Field field, Object fieldOwner) {
        this.field = field;
        this.fieldOwner = fieldOwner;
    }

    public FieldFactory(Field field, ILocalFactory fieldOwnerFactory) {
        this.field = field;
        this.fieldOwnerFactory = fieldOwnerFactory;
    }


    public Class getReturnType() {
        return this.field.getType();
    }

    public Object instance(Object[] parameters, Object[] localProducts) {
        try {
            if(this.fieldOwnerFactory != null){
                return this.field.get(this.fieldOwnerFactory.instance(parameters, localProducts));
            }
            return this.field.get(this.fieldOwner);
        } catch (IllegalAccessException e) {
            throw new FactoryException(
                    "FieldFactory", "ERROR_ACCESSING_FIELD",
                    "Error accessing field " + field, e);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("field: ");
        builder.append(field);

        return builder.toString();
    }

}
