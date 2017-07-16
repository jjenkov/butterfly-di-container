package com.jenkov.container.script;

/**
 * @author Jakob Jenkov - Copyright 2004-2006 Jenkov Development
 */
public class ParserError implements Comparable {

    int lineNo = 0;
    int charNo = 0;

    String errorText = null;

    public ParserError(int lineNo, int charNo, String errorText) {
        this.lineNo = lineNo;
        this.charNo = charNo;
        this.errorText = errorText;
    }

    public int getLineNo() {
        return lineNo;
    }

    public int getCharNo() {
        return charNo;
    }

    public String getErrorText() {
        return errorText;
    }

    public String toString() {
        return "(" + lineNo + ":" + charNo + ") " + errorText;
    }

    public int compareTo(Object o) {
        if (o == null) throw new NullPointerException("Cannot compare a ParserError instance to null!");
        if (!(o instanceof ParserError)) {
            throw new IllegalArgumentException("Cannot compare ParserError instances to " + o.getClass().getName());
        }

        ParserError other = (ParserError) o;
        if (this.lineNo > other.getLineNo()) return 1;
        if (this.lineNo < other.getLineNo()) return -1;
        return Integer.compare(this.charNo, other.getCharNo());

    }


}
