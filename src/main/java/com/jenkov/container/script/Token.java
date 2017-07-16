package com.jenkov.container.script;

import java.util.Arrays;

/**

 */
public class Token {

    /* todo decide if these constants really belong inside this class.
        the Token class doesn't really know what tokens it represents.
        The ScriptTokenizer knows what tokens exists in its input,
        since it is after all the ScriptTokenizer that chops the input up into Tokens.
     */
    public static final Token NULL = new Token("null");

    /* todo change mode in FactoryDefinition from String to Token, to save String instances created! */
    public static final Token NEW_INSTANCE = new Token('*');
    public static final Token SINGLETON = new Token('1');
    public static final Token THREAD_SINGLETON = new Token("1T");
    public static final Token FLYWEIGHT = new Token("1F");
    public static final Token LOCALIZED = new Token("L");

    public static final Token CURLY_LEFT = new Token('{');
    public static final Token CURLY_RIGHT = new Token('}');
    public static final Token PARENTHESIS_LEFT = new Token('(');
    public static final Token PARENTHESIS_RIGHT = new Token(')');
    public static final Token SQUARE_LEFT = new Token('[');
    public static final Token SQUARE_RIGHT = new Token(']');
    public static final Token LESS_THAN = new Token('<');
    public static final Token GREATER_THAN = new Token('>');
    public static final Token COMMA = new Token(',');
    public static final Token COLON = new Token(':');
    public static final Token SEMI_COLON = new Token(';');
    public static final Token DOT = new Token('.');
    public static final Token QUOTE = new Token('"');
    public static final Token QUOTE_SINGLE = new Token('\'');
    public static final Token EQUALS = new Token('=');
    public static final Token HASH = new Token('#');
    public static final Token DOLLAR = new Token('$');
    private int hash;
    /* location in source file */
    protected int lineNo = 0;
    protected int charNoBefore = 0;
    protected int charNoAfter = 0;
    /* location in input buffer */
    protected int from = 0;
    protected int length = 0;
    protected char[] buffer = null;

    protected Token() {
    }

    protected Token(String tokenValue) {
        this.buffer = tokenValue.toCharArray();
        this.from = 0;
        this.length = this.buffer.length;
        this.hash = _hashCode();
    }

    protected Token(char... tokenChars) {
        this.buffer = tokenChars;
        this.from = 0;
        this.length = this.buffer.length;
        this.hash = _hashCode();
    }


    public static Token delimiterToken(char delimiter) {
        switch (delimiter) {
            case '{':
                return Token.CURLY_LEFT;
            case '}':
                return Token.CURLY_RIGHT;
            case '(':
                return Token.PARENTHESIS_LEFT;
            case ')':
                return Token.PARENTHESIS_RIGHT;
            case '[':
                return Token.SQUARE_LEFT;
            case ']':
                return Token.SQUARE_RIGHT;
            case '<':
                return Token.LESS_THAN;
            case '>':
                return Token.GREATER_THAN;
            case ',':
                return Token.COMMA;
            case ';':
                return Token.SEMI_COLON;
            case '.':
                return Token.DOT;
        }
        return null;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getCharNoBefore() {
        return charNoBefore;
    }

    public void setCharNoBefore(int charNoBefore) {
        this.charNoBefore = charNoBefore;
    }

    public int getCharNoAfter() {
        return charNoAfter;
    }

    public void setCharNoAfter(int charNoAfter) {
        this.charNoAfter = charNoAfter;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public char[] getBuffer() {
        return buffer;
    }

    public void setBuffer(char[] buffer) {
        this.buffer = buffer;
    }

    public void append(char aChar) {
        //do nothing... just here to avoid compiler errors. remove later.
        this.length++;
        this.charNoAfter = this.charNoBefore + this.length;

    }

    public void setLength(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }

    public int hashCode() {
        if (this.hash == 0)
            return this.hash = _hashCode();
        return hash;
    }

    // todo check if there is any gain to be found by caching hash codes.
    public int _hashCode() {

        int hashCode = Arrays.hashCode(buffer);
//        for (char aBuffer : this.buffer) {
//            hashCode *= (int) aBuffer;
//        }
        if (hashCode == 0)
            hashCode = 1;
        return hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Token)) return false;

        Token other = (Token) obj;
        if (this.length != other.length) return false;

        for (int i = 0; i < this.length; i++) {
            if (this.buffer[getFrom() + i] != other.buffer[other.getFrom() + i]) return false;
        }
        return true;
    }

    public char charAt(int index) {
        return this.buffer[getFrom() + index];
    }

    public String toString() {
        return new String(this.buffer, this.from, this.length);
    }

    public void right(int offset) {
        this.from += offset;
        this.length -= offset;
    }

    public boolean startsWith(Token other) {
        if (other.length > this.length) return false;
        for (int i = 0; i < other.length; i++) {
            if (this.buffer[getFrom() + i] != other.buffer[other.getFrom() + i]) return false;
        }
        return true;
    }

    public boolean endsWith(Token other) {
        if (other.length > this.length) return false;

        for (int i = other.length - 1; i >= 0; i--) {
            if (this.buffer[getFrom() + i] != other.buffer[other.getFrom() + i]) return false;
        }

        return true;
    }

    /* todo this method can be optimized. Calls to startsWith and endsWith can be optimized by comparing directly into the buffer instead */
    public boolean isString() {
        return startsWith(Token.QUOTE) && endsWith(Token.QUOTE) || startsWith(Token.QUOTE_SINGLE) && endsWith(Token.QUOTE_SINGLE);
    }

    public boolean isInteger() {
        for (int i = 0; i < this.length; i++) {
            switch (this.buffer[getFrom() + i]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    continue;
                default:
                    return false;
            }
        }

        return true;
    }

    public boolean isDecimal() {
        for (int i = 0; i < this.length; i++) {
            switch (this.buffer[getFrom() + i]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                    continue;
                default:
                    return false;
            }
        }

        return true;
    }

    public boolean isNumber() {
        return isInteger() || isDecimal();
    }

    public void reset() {
        this.charNoAfter = 0;
        this.charNoBefore = 0;
        this.lineNo = 0;
        this.from = 0;
        this.length = 0;
        this.hash = 0;
    }
}
