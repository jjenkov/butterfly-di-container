package com.jenkov.container.script;

import com.jenkov.container.TestProduct;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**

 */
public class FieldGenericTypeTest extends TestCase {

    public void testGenericType() throws NoSuchFieldException {

        Field listField = TestProduct.class.getField("normalList");
//        System.out.println(listField.getType());

        Field genericListField = TestProduct.class.getField("genericListInt");
//        System.out.println(genericListField.getType());

        ParameterizedType type = (ParameterizedType) genericListField.getGenericType();
//        System.out.println(type.getClass());
//        System.out.println(type.getRawType());

        Type[] typeArguments = type.getActualTypeArguments();
        for (Type typeArgument : typeArguments) {
            Class typeArgumentClass = (Class) typeArgument;
//            System.out.println(typeArgumentClass);
        }

    }
}
