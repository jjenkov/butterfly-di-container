package com.jenkov.container;

/**
 * This interface is used to test custom factory interface adaptation.
 */
public interface ICustomFactory {

    String instance();

    String instance(String parameter);

    String bean();

    String bean(String parameter);

    String beanInstance();

    String beanInstance(String parameter);

}
