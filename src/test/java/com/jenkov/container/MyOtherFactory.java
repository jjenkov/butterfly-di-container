package com.jenkov.container;

import com.jenkov.container.itf.factory.IGlobalFactory;
import com.jenkov.container.java.JavaFactory;

/**

 */
public class MyOtherFactory extends JavaFactory {

    public final IGlobalFactory test = null;

    public String instance(Object... parameters) {
        return "test2" + test.instance();
    }
}
