package com.jenkov.container;

import java.net.URL;

/**

 */
public class TestProductCasting {

    protected String value1 = null;
    protected URL    value2 = null;
    protected int    value3 = -1;


    public void setValue(String value){
        this.value1 = value;
    }


    public void setValue(URL url){
        this.value2 = url;
    }

    public void setValue(int value){
        this.value3 = value;
    }

    public String getValue1() {
        return value1;
    }

    public URL getValue2() {
        return value2;
    }

    public int getValue3() {
        return value3;
    }
}
