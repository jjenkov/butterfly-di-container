package com.jenkov.container;

import com.jenkov.container.IContainer;

import java.util.List;
import java.util.Set;
import java.net.URL;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class TestProduct {

    public static final String FIELD_VALUE = "fieldValue";

    protected TestProduct internalProduct = null;
    protected String      value1          = null;
    protected String      value2          = null;
    protected double      decimal         = 0;

    protected String[]    stringArray     = null;
    protected List        list            = null;
    protected Set         set             = null;
    protected URL[]       urlArray        = null;
    protected List<URL>   urlList         = null;
    protected List<Integer> integerList   = null;

    protected String[]      array1 = null;
    protected Integer[]     array2 = null;
    protected TestProduct[] array3 = null;

    protected Class       classValue      = null;

    public        int instanceInt = -1;
    public static int staticInt   = -1;

    public IContainer     container = null;
    public ICustomFactory factory   = null;

    public static IContainer     staticContainer = null;
    public static ICustomFactory staticFactory   = null;

    public List<Integer> genericListInt = null;
    public List<URL>     genericListUrl = null;
    public List          normalList     = null;


    public static TestProduct createProduct(){
        return new TestProduct();
    }

    public static TestProduct createProduct(TestProduct product){
        return new TestProduct(product);
    }

    public TestProduct() {
    }

    public TestProduct(TestProduct product){
        this.internalProduct = product;
    }

    public TestProduct(ICustomFactory factory) {
        this.factory = factory;
    }

    public TestProduct(IContainer container) {
        this.container = container;
    }

    public TestProduct getInternalProduct() {
        return internalProduct;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setValues(String value1, Object value2){
        this.value1 = value1;
        this.value2 = value2.toString();
    }

    public void setValues(String value1, String value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public void setIntValue(int lengthOfString){
        //System.out.println("length: " + lengthOfString);
    }

    public double getDecimal() {
        return decimal;
    }

    public void setDecimal(double decimal) {
        this.decimal = decimal;
    }

    public Class getClassValue() {
        return classValue;
    }

    public void setClassValue(Class classValue) {
        this.classValue = classValue;
    }

    public void setFactory(ICustomFactory factory) {
        this.factory = factory;
    }

    public void setContainer(IContainer container) {
        this.container = container;
    }

    public static void setStaticContainer(IContainer staticContainer) {
        TestProduct.staticContainer = staticContainer;
    }

    public static void setStaticFactory(ICustomFactory staticFactory) {
        TestProduct.staticFactory = staticFactory;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public List getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public URL[] getUrlArray() {
        return urlArray;
    }

    public void setUrlArray(URL[] urlArray) {
        this.urlArray = urlArray;
    }

    public List<URL> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<URL> urlList) {
        this.urlList = urlList;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public String[] getArray1() {
        return array1;
    }

    public void setArray(String[] array1) {
        this.array1 = array1;
    }

    public Integer[] getArray2() {
        return array2;
    }

    public void setArray(Integer[] array2) {
        this.array2 = array2;
    }

    public TestProduct[] getArray3() {
        return array3;
    }

    public void setArray(TestProduct[] array3) {
        this.array3 = array3;
    }
    
}
