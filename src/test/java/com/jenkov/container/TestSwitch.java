package com.jenkov.container;

/**

 */
public class TestSwitch {

    public static void main(String[] args) {
        char aChar = '{';

        switch (aChar) {
            case '&':
            case '5':
                System.out.println("Yes!");
                break;
            case 'b':
            case '}':
                System.out.println("No!");
                break;
            default:
                System.out.println("default");
        }
    }
}
