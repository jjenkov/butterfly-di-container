package com.jenkov.container.script;

import com.jenkov.container.IContainer;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class TestThread extends Thread{

    protected Object instance1 = null;
    protected Object instance2 = null;

    protected String string1   = null;
    protected String string2   = null;

    protected IContainer container = null;

    public TestThread(IContainer container) {
        this.container = container;
    }

    public Object getInstance1() {
        return instance1;
    }

    public Object getInstance2() {
        return instance2;
    }

    public String getString1() {
        return string1;
    }

    public String getString2() {
        return string2;
    }
}
