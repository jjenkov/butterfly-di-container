package com.jenkov.container;

import com.jenkov.container.java.JavaFactory;
import com.jenkov.container.itf.factory.IGlobalFactory;

/**

 */
public class MyOtherFactory extends JavaFactory {

    public IGlobalFactory test = null;
    public String instance(Object ... parameters) {
        return "test2" + test.instance();
    }
}
