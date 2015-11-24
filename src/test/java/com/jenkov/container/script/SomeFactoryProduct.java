package com.jenkov.container.script;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class SomeFactoryProduct {

    protected String arg1 = null;
    protected String arg2 = null;

    public SomeFactoryProduct(String arg1){
        this.arg1 = arg1;
    }

    public SomeFactoryProduct(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public void dispose(){
        
    }
}
