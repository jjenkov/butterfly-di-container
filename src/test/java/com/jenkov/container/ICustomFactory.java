package com.jenkov.container;

/**
    This interface is used to test custom factory interface adaptation.
 */
public interface ICustomFactory {

    public String instance();
    public String instance(String parameter);

    public String bean();
    public String bean(String parameter);

    public String beanInstance();
    public String beanInstance(String parameter);

}
